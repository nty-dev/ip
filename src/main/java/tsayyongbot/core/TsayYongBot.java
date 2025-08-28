package tsayyongbot.core;

import tsayyongbot.ui.Ui;
import tsayyongbot.parser.Parser;
import tsayyongbot.io.Storage;
import tsayyongbot.task.*;

import java.nio.file.Paths;

/**
 * Entry point and main event loop for TsayYongBot.
 *
 * Coordinates the UI, parser, storage and the in-memory task list.
 */
public class TsayYongBot {

    private static void persist(Storage storage, TaskList tasks) {
        try {
            storage.save(tasks.asList());
        } catch (Exception e) {
            System.err.println("Save failed: " + e.getMessage());
        }
    }

    /**
     * Runs the interactive loop until the user issues {@code bye} or EOF is
     * reached.
     */
    public void run() {
        Ui ui = new Ui();
        Storage storage = new Storage(Paths.get("data", "tsayyongbot.jsonl"));
        TaskList tasks;
        try {
            tasks = new TaskList(storage.load());
        } catch (Exception e) {
            System.err.println("Load failed, starting empty: " + e.getMessage());
            tasks = new TaskList();
        }

        ui.showWelcome();
        boolean isExit = false;
        while (!isExit) {
            try {
                String full = ui.readCommand();
                if (full == null) {
                    break;
                }
                Parser.Parsed p = Parser.parse(full);

                switch (p.type) {
                    case BYE -> {
                        ui.showGoodbye();
                        isExit = true;
                    }
                    case LIST ->
                        ui.showList(tasks.asList());
                    case TODO -> {
                        Task t = tasks.addTodo(p.desc);
                        ui.showAdded(t, tasks.size());
                        persist(storage, tasks);
                    }
                    case DEADLINE -> {
                        Task t = tasks.addDeadline(p.desc, p.by);
                        ui.showAdded(t, tasks.size());
                        persist(storage, tasks);
                    }
                    case EVENT -> {
                        Task t = tasks.addEvent(p.desc, p.from, p.to);
                        ui.showAdded(t, tasks.size());
                        persist(storage, tasks);
                    }
                    case MARK -> {
                        Task t = tasks.mark(p.index);
                        ui.showMarked(t);
                        persist(storage, tasks);
                    }
                    case UNMARK -> {
                        Task t = tasks.unmark(p.index);
                        ui.showUnmarked(t);
                        persist(storage, tasks);
                    }
                    case DELETE -> {
                        Task removed = tasks.delete(p.index);
                        ui.showRemoved(removed, tasks.size());
                        persist(storage, tasks);
                    }
                    case FIND -> {
                        ui.showFindResults(tasks.find(p.desc));
                    }
                    default ->
                        throw new TsayYongBotException("Unknown command.");
                }
            } catch (TsayYongBotException e) {
                ui.showError(e.getMessage());
            }
        }
    }

    /**
     * Program entry point.
     *
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        new TsayYongBot().run();
    }
}
