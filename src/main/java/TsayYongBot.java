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
        printBlock("Hello! I'm the Tsay Yong Bot", "What can I do for you?");

        Scanner sc = new Scanner(System.in);
        while (true) {
            if (!sc.hasNextLine()) {
                break;
            }
            String input = sc.nextLine();
            if (input.equals("bye")) {
                printBlock("Bye. Hope to see you again soon!");
                break;
            }
            printBlock(input);
        }
        sc.close();
    }
}