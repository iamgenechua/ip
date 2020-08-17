import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

/**
 * Encapsulates the chatBot and its behavior.
 */
class ChatBot {
    private String logo;
    private String user;
    private String botName;
    private List<Task> toDoList;

    /**
     * Instantiates a chatBot with a name.
     * @param botName the name of the chatBot
     */
    ChatBot(String botName) {
        logo = logo = "#    #   ##   # ###### ###### #    #\n"
                + "#   #   #  #  #     #  #      ##   #\n"
                + "####   #    # #    #   #####  # #  #\n"
                + "#  #   ###### #   #    #      #  # #\n"
                + "#   #  #    # #  #     #      #   ##\n"
                + "#    # #    # # ###### ###### #    #\n";
        user = "You: ";
        this.botName = botName;
        toDoList = new ArrayList<>();
    }

    /**
     * Prints a welcome message for the user.
     */
    void welcome() {
         String welcomeMessage = "Konichiwa! Welcome to Kaizen\n"
                 + "I am " + this.botName + " what can I do for you today?\n";

        System.out.println(this.logo
                + "\n"
                + welcomeMessage);
    }

    /**
     * Interacts with the user based on his input.
     */
    void getInput() {

        Scanner sc = new Scanner(System.in);
        boolean exitProgram = false;

        while (!exitProgram && sc.hasNextLine()) {
            try {
                String input = sc.nextLine();
                String[] inputArray = input.split(" ", 2); // separates the first word from the rest
                String command = inputArray[0].toLowerCase();
                switch(command) {
                    case "bye":
                        bye();
                        exitProgram = true;
                        break;
                    case "list":
                        showList();
                        break;
                    case "done":
                        makeTaskDone(inputArray);
                        break;
                    case "todo":
                        makeTodo(inputArray);
                        break;
                    case "deadline":
                        makeDeadline(inputArray);
                        break;
                    case "event":
                        makeEvent(inputArray);
                        break;
                    default:
                        System.out.println("NANI??! Please say something that I can understand!");
                }
            } catch (DukeException e) {
                System.out.println(e.getMessage());
            }
        }
        sc.close();
    }

    /**
     * Presents the user with the list of tasks
     */
    void showList() {
        // guard clause
        if (this.toDoList.isEmpty()) {
            System.out.println("No tasks at the moment! Good job!\n");
            return;
        }

        System.out.println(this.botName + ": "
                + "Here are your tasks!");
        for (int i = 0; i < toDoList.size(); i++) {
            System.out.println((i+1) + ". "
                    + toDoList.get(i));
        }
        System.out.println();
    }

    /**
     * Produce a done Task
     * @param taskStringArray the position of the task in the arraylist
     * @throws DukeException when there is an error with task
     */
    void makeTaskDone(String[] taskStringArray) throws DukeException {
        try {
            // if length is not 2, nothing was passed in after 'done'
            if (taskStringArray.length != 2) {
                throw new DukeException("Which task are you done with? Please key in the task number!\n");
            }
            // check if taskNumber is a number
            int taskNumber = Integer.parseInt(taskStringArray[1]);
            // check if taskNumber is valid
            if (taskNumber <= 0 || taskNumber > this.toDoList.size()) {
                throw new DukeException("Sorry, no such task!\n");
            }

            Task currentTask = this.toDoList.get(taskNumber - 1);
            Task newTask = currentTask.markAsDone();
            this.toDoList.set(taskNumber - 1, newTask);
            System.out.println(this.botName + ": " + "Sugoi! This task is done!");
            System.out.println(newTask + "\n");

        } catch (NumberFormatException e) {
            throw new DukeException(taskStringArray[1] + " is not an integer!\n");
        }
    }

    void makeTodo(String[] inputArray) throws DukeException {
        // if length is not 2, nothing was passed in after 'todo'
        if (inputArray.length != 2) {
            throw new DukeException("NANI??! Enter a description for your todo!\n");
        }

        String description = inputArray[1];
        Task taskToAdd = new ToDo(description);
        addTask(taskToAdd);
    }

    void makeDeadline(String[] inputArray) throws DukeException {
        // if length is not 2, nothing was passed in after 'deadline'
        if (inputArray.length != 2) {
            throw new DukeException("NANI??! Enter a description for your deadline!\n");
        }

        // if description is lacking a /by keyword
        String description = inputArray[1];
        if (description.indexOf("/by") < 0) {
            throw new DukeException("Please enter a valid deadline!\n");
        }

        String[] descriptionArray = description.split("/by", 2);
        if (descriptionArray.length != 2) {
            throw new DukeException("NANI??! Enter your deadline name & an end-time for your deadline!\n");
        }

        String deadlineName = descriptionArray[0];
        String deadlineEndTime = descriptionArray[1];

        Task taskToAdd = new Deadline(deadlineName, deadlineEndTime);
        addTask(taskToAdd);
    }

    void makeEvent(String[] inputArray) throws DukeException {
        // if length is not 2, nothing was passed in after 'makeEvent'
        if (inputArray.length != 2) {
            throw new DukeException("NANI??! Enter a description for your event!\n");
        }

        // if event is lacking a /by keyword
        String description = inputArray[1];
        if (description.indexOf("/at") < 0) {
            throw new DukeException("Please enter a valid event!\n");
        }

        String[] descriptionArray = description.split("/at", 2);
        if (descriptionArray.length != 2) {
            throw new DukeException("NANI??! Enter your event name & an event timing!\n");
        }

        String eventName = descriptionArray[0];
        String eventTiming = descriptionArray[1];

        Task taskToAdd = new Event(eventName, eventTiming);
        addTask(taskToAdd);
    }

    void addTask(Task taskToAdd) {
        this.toDoList.add(taskToAdd);
        System.out.println(this.botName + ": "
                + "Hai! I have added this task to your list:\n"
                + taskToAdd);
        System.out.println("You now have "
                + this.toDoList.size()
                + " tasks in your list. Gambatte!\n");
    }

    /**
     * Says 'bye' to the user.
     */
    void bye() {
        System.out.println(this.botName + ": "
                + "Sayonara! See you again my friend!");
    }
}
