import java.util.Scanner;

/**
 * Entry point of The program
 */
public class Duke {
    private Storage storage;
    private TaskList<Task> tasks;
    private Ui ui;

    public Duke(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList<>(storage.load());
        } catch (DukeException e) {
            System.out.println(e);
        }
    }

    public void run() {
        Scanner sc = new Scanner(System.in);
        ui.welcome();
        boolean exitProgram = false;
        while (!exitProgram && sc.hasNextLine()) {
            try {
                String fullCommand = sc.nextLine();
                Command c = Parser.parse(fullCommand);
                c.execute(tasks, ui, storage);
                storage.writeToFile(tasks);
                exitProgram = c.isExitProgram();
            } catch (DukeException e) {
                System.out.println(e.getMessage());
            }
        }
        sc.close();
    }


    public static void main(String[] args) {
        new Duke("data/duke.txt").run();
    }
}
