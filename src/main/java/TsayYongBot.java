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

    public static void main(String[] args) {
        String[] tasks = new String[100];
        int taskCount = 0;

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
                if (taskCount == 0) {
                    printBlock("(no tasks yet)");
                } else {
                    String[] lines = new String[taskCount];
                    for (int i = 0; i < taskCount; i++) {
                        lines[i] = (i + 1) + ". " + tasks[i];
                    }
                    printBlock(lines);
                }
                continue;
            }

            if (taskCount >= 100) {
                printBlock("Sorry, I can't store more than 100 tasks.");
            } else {
                tasks[taskCount++] = input;
                printBlock("added: " + input);
            }
        }
        sc.close();
    }
}