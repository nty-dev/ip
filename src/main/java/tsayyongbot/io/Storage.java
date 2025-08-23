package tsayyongbot.io;
import tsayyongbot.task.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Storage {
    private final Path file;

    public Storage(Path file) { this.file = file; }

    public List<Task> load() throws IOException {
        List<Task> tasks = new ArrayList<>();
        ensureFileReady();

        for (String line : Files.readAllLines(file, StandardCharsets.UTF_8)) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("//")) continue;

            try {
                String type = getStr(line, "type");
                boolean done = getBool(line, "done");
                String desc = unb64(getStr(line, "desc_b64"));

                Task t;
                if ("T".equals(type)) {
                    t = new Todo(desc);
                } else if ("D".equals(type)) {
                    String by = unb64(nvl(getStr(line, "by_b64")));
                    t = new Deadline(desc, by);
                } else if ("E".equals(type)) {
                    String from = unb64(nvl(getStr(line, "from_b64")));
                    String to   = unb64(nvl(getStr(line, "to_b64")));
                    t = new Event(desc, from, to);
                } else {
                    continue;
                }
                if (done) t.markAsDone();
                tasks.add(t);
            } catch (Exception ignore) {
            }
        }
        return tasks;
    }

    public void save(List<Task> tasks) throws IOException {
        ensureDirReady();
        List<String> lines = new ArrayList<>();
        for (Task t : tasks) lines.add(serialize(t));
        Files.write(file, lines, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    private void ensureFileReady() throws IOException {
        ensureDirReady();
        if (!Files.exists(file)) Files.createFile(file);
    }

    private void ensureDirReady() throws IOException {
        Path parent = file.getParent();
        if (parent != null && !Files.exists(parent)) Files.createDirectories(parent);
    }

    private static String serialize(Task t) {
        StringBuilder sb = new StringBuilder("{");
        sb.append("\"type\":\"").append(typeOf(t)).append("\",");
        sb.append("\"done\":").append(t.isDone()).append(",");
        sb.append("\"desc_b64\":\"").append(b64(t.getDescription())).append("\"");
        if (t instanceof Deadline d) {
            sb.append(",\"by_b64\":\"").append(b64(d.getBy())).append("\"");
        } else if (t instanceof Event e) {
            sb.append(",\"from_b64\":\"").append(b64(e.getFrom())).append("\"");
            sb.append(",\"to_b64\":\"").append(b64(e.getTo())).append("\"");
        }
        sb.append("}");
        return sb.toString();
    }

    private static String typeOf(Task t) {
        if (t instanceof Todo) return "T";
        if (t instanceof Deadline) return "D";
        if (t instanceof Event) return "E";
        return "?";
    }

    private static final Pattern STR_FIELD = Pattern.compile("\"([^\"]+)\"\\s*:\\s*\"([^\"]*)\"");
    private static final Pattern BOOL_FIELD = Pattern.compile("\"([^\"]+)\"\\s*:\\s*(true|false)");

    private static String getStr(String json, String key) {
        Matcher m = STR_FIELD.matcher(json);
        while (m.find()) {
            if (m.group(1).equals(key)) return m.group(2);
        }
        return null;
    }

    private static boolean getBool(String json, String key) {
        Matcher m = BOOL_FIELD.matcher(json);
        while (m.find()) {
            if (m.group(1).equals(key)) return Boolean.parseBoolean(m.group(2));
        }
        return false;
    }

    private static String b64(String s) {
        return Base64.getEncoder().encodeToString(s.getBytes(StandardCharsets.UTF_8));
    }

    private static String unb64(String s) {
        return new String(Base64.getDecoder().decode(s), StandardCharsets.UTF_8);
    }

    private static String nvl(String s) { return s == null ? "" : s; }
}