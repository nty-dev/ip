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
                               "  " + t.toString());
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
                               "  " + t.toString());
                }
                continue;
            }

            if (input.startsWith("todo")) {
                String desc = input.length() > 4 ? input.substring(4).trim() : "";
                if (desc.isEmpty()) {
                    printBlock("OOPS!!! The description of a todo cannot be empty.");
                } else {
                    Task t = new Todo(desc);
                    tasks.add(t);
                    printBlock("Got it. I've added this task:",
                               "  " + t.toString(),
                               String.format("Now you have %d tasks in the list.", tasks.size()));
                }
                continue;
            }

            if (input.startsWith("deadline")) {
                String rest = input.length() > 8 ? input.substring(8).trim() : "";
                int byPos = rest.indexOf(" /by ");
                if (byPos == -1) {
                    printBlock("OOPS!!! For deadlines, use: deadline <desc> /by <when>");
                } else {
                    String desc = rest.substring(0, byPos).trim();
                    String by = rest.substring(byPos + 5).trim();
                    if (desc.isEmpty() || by.isEmpty()) {
                        printBlock("OOPS!!! The description and /by must not be empty.");
                    } else {
                        Task t = new Deadline(desc, by);
                        tasks.add(t);
                        printBlock("Got it. I've added this task:",
                                   "  " + t.toString(),
                                   String.format("Now you have %d tasks in the list.", tasks.size()));
                    }
                }
                continue;
            }

            if (input.startsWith("event")) {
                String rest = input.length() > 5 ? input.substring(5).trim() : "";
                int fromPos = rest.indexOf(" /from ");
                int toPos = rest.indexOf(" /to ");
                if (fromPos == -1 || toPos == -1 || toPos <= fromPos) {
                    printBlock("OOPS!!! For events, use: event <desc> /from <start> /to <end>");
                } else {
                    String desc = rest.substring(0, fromPos).trim();
                    String from = rest.substring(fromPos + 7, toPos).trim();
                    String to = rest.substring(toPos + 5).trim();
                    if (desc.isEmpty() || from.isEmpty() || to.isEmpty()) {
                        printBlock("OOPS!!! The description, /from, and /to must not be empty.");
                    } else {
                        Task t = new Event(desc, from, to);
                        tasks.add(t);
                        printBlock("Got it. I've added this task:",
                                   "  " + t.toString(),
                                   String.format("Now you have %d tasks in the list.", tasks.size()));
                    }
                }
                continue;
            }

            printBlock("OOPS!!! I'm sorry, but I don't know what that means :-(");
        }
        sc.close();
    }
}