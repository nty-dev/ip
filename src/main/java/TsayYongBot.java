import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TsayYongBot {
    private static final String LINE =
            "____________________________________________________________";

    private static void printBlock(String... lines) {
        System.out.println(LINE);
        for (String s : lines) {
            System.out.println(" " + s);
        }
        System.out.println(LINE);
    }

    private static void printList(List<Task> tasks) {
        if (tasks.isEmpty()) {
            printBlock("(no tasks yet)");
            return;
        }
        String[] lines = new String[tasks.size() + 1];
        lines[0] = "Here are the tasks in your list:";
        for (int i = 0; i < tasks.size(); i++) {
            lines[i + 1] = String.format("%d.%s", i + 1, tasks.get(i));
        }
        printBlock(lines);
    }

    private static Integer parseIndex(String cmd, String input) {
        String[] parts = input.trim().split("\\s+");
        if (parts.length != 2) return null;
        if (!parts[0].equals(cmd)) return null;
        try {
            return Integer.valueOf(parts[1]);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static void main(String[] args) {
        List<Task> tasks = new ArrayList<>();

        printBlock("Hello! I'm the Tsay Yong Bot", "What can I do for you?");

        Scanner sc = new Scanner(System.in);
        while (true) {
            if (!sc.hasNextLine()) break;
            String input = sc.nextLine().trim();

            if (input.equals("bye")) {
                printBlock("Bye. Hope to see you again soon!");
                break;
            }

            if (input.equals("list")) {
                printList(tasks);
                continue;
            }

            Integer idx = parseIndex("mark", input);
            if (idx != null) {
                if (idx < 1 || idx > tasks.size()) {
                    printBlock("OOPS!!! That task number is out of range.");
                } else {
                    Task t = tasks.get(idx - 1);
                    t.markAsDone();
                    printBlock("Nice! I've marked this task as done:",
                               "  " + t.pretty());
                }
                continue;
            }

            idx = parseIndex("unmark", input);
            if (idx != null) {
                if (idx < 1 || idx > tasks.size()) {
                    printBlock("OOPS!!! That task number is out of range.");
                } else {
                    Task t = tasks.get(idx - 1);
                    t.markAsNotDone();
                    printBlock("OK, I've marked this task as not done yet:",
                               "  " + t.pretty());
                }
                continue;
            }

            tasks.add(new Task(input));
            printBlock("added: " + input);
        }
        sc.close();
    }
}