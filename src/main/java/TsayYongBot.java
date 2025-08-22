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

    private static void ensure(boolean condition, String message) throws TsayYongBotException {
        if (!condition) throw new TsayYongBotException(message);
    }

    private static int parseIndexStrict(String cmd, String input, int max)
            throws TsayYongBotException {
        String[] parts = input.trim().split("\\s+");
        ensure(parts.length == 2 && parts[0].equals(cmd),
               "Usage: " + cmd + " <task-number>");
        int idx;
        try {
            idx = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            throw new TsayYongBotException("The task number for '" + cmd + "' must be an integer.");
        }
        ensure(idx >= 1 && idx <= max, "That task number is out of range.");
        return idx;
    }

    public static void main(String[] args) {
        List<Task> tasks = new ArrayList<>();
        printBlock("Hello! I'm the Tsay Yong Bot", "What can I do for you?");

        Scanner sc = new Scanner(System.in);
        while (true) {
            if (!sc.hasNextLine()) break;
            String input = sc.nextLine().trim();

            try {
                if (input.equals("bye")) {
                    printBlock("Bye. Hope to see you again soon!");
                    break;
                }

                if (input.equals("list")) {
                    printList(tasks);
                    continue;
                }

                if (input.startsWith("mark")) {
                    int idx = parseIndexStrict("mark", input, tasks.size());
                    Task t = tasks.get(idx - 1);
                    t.markAsDone();
                    printBlock("Nice! I've marked this task as done:",
                               "  " + t.toString());
                    continue;
                }

                if (input.startsWith("unmark")) {
                    int idx = parseIndexStrict("unmark", input, tasks.size());
                    Task t = tasks.get(idx - 1);
                    t.markAsNotDone();
                    printBlock("OK, I've marked this task as not done yet:",
                               "  " + t.toString());
                    continue;
                }

                if (input.startsWith("todo")) {
                    String desc = input.length() > 4 ? input.substring(4).trim() : "";
                    ensure(!desc.isEmpty(), "The description of a todo cannot be empty.");
                    Task t = new Todo(desc);
                    tasks.add(t);
                    printBlock("Got it. I've added this task:",
                               "  " + t.toString(),
                               String.format("Now you have %d tasks in the list.", tasks.size()));
                    continue;
                }

                if (input.startsWith("deadline")) {
                    String rest = input.length() > 8 ? input.substring(8).trim() : "";
                    int byPos = rest.indexOf(" /by ");
                    ensure(byPos != -1, "For deadlines, use: deadline <desc> /by <when>");
                    String desc = rest.substring(0, byPos).trim();
                    String by = rest.substring(byPos + 5).trim();
                    Task t = new Deadline(desc, by);
                    tasks.add(t);
                    printBlock("Got it. I've added this task:",
                               "  " + t.toString(),
                               String.format("Now you have %d tasks in the list.", tasks.size()));
                    continue;
                }

                if (input.startsWith("event")) {
                    String rest = input.length() > 5 ? input.substring(5).trim() : "";
                    int fromPos = rest.indexOf(" /from ");
                    int toPos = rest.indexOf(" /to ");
                    ensure(fromPos != -1 && toPos != -1 && toPos > fromPos,
                           "For events, use: event <desc> /from <start> /to <end>");
                    String desc = rest.substring(0, fromPos).trim();
                    String from = rest.substring(fromPos + 7, toPos).trim();
                    String to = rest.substring(toPos + 5).trim();
                    Task t = new Event(desc, from, to);
                    tasks.add(t);
                    printBlock("Got it. I've added this task:",
                               "  " + t.toString(),
                               String.format("Now you have %d tasks in the list.", tasks.size()));
                    continue;
                }

                throw new TsayYongBotException("I'm sorry, but I don't know what that means :-(");

            } catch (TsayYongBotException e) {
                printBlock("OOPS!!! " + e.getMessage());
            }
        }
        sc.close();
    }
}