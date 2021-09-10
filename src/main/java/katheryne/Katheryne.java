package katheryne;

// import task classes
import katheryne.task.Deadline;
import katheryne.task.Event;
import katheryne.task.Task;
import katheryne.task.Todo;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.PROPERTY;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@JsonTypeInfo(use = NAME, include = PROPERTY)

/**
 * Chat bot katheryne.Katheryne, used for simple todo lists
 */
public class Katheryne {
    
//    public Katheryne() {
//        
//    }
    
    public static void main(String[] args) {
        // initialise variables
        Storage storage = new Storage();
        Ui ui = new Ui();
        TaskList lst = new TaskList();
        
        // initialise Katheryne
        try {
            storage.loadTasks(lst, "tasks.json");
        } catch (KatheryneException e) {
            ui.showErrorMessage(e);
        }
        ui.greet(lst);
        
        Scanner in = new Scanner(System.in);
        while (true) {
            try {
                String userInput = in.nextLine();
                String[] keywordInput = userInput.split(" ", 2);
                if (userInput.equalsIgnoreCase("BYE")) {
                    break;
                } else if (keywordInput[0].equalsIgnoreCase("done") || keywordInput[0].equalsIgnoreCase("complete")) {
                    if (keywordInput.length == 1) {
                        throw new KatheryneException("Traveller, please specify by index which task you completed.");
                    }
                    int i = Integer.parseInt(keywordInput[1]);
                    if (i <= lst.getSize() && i > 0) {
                        lst.doTask(i - 1);
                        ui.say("Task done: " + lst.getTask(i - 1));
                    } else {
                        throw new KatheryneException("That's not a valid index...");
                    }
                } else if (keywordInput[0].equalsIgnoreCase("delete")) {
                    if (keywordInput.length == 1) {
                        throw new KatheryneException("Traveller, please specify by index what I should delete.");
                    }
                    int i = Integer.parseInt(keywordInput[1]);
                    if (i <= lst.getSize() && i > 0) {
                        System.out.println("Okay, I'll delete the following item:");
                        System.out.println(lst.deleteTask(i - 1));
                    } else {
                        throw new KatheryneException("That's not a valid index...");
                    }
                } else if (userInput.equalsIgnoreCase("list")) {
                    // return the list of items stored
                    lst.printList();
//                    System.out.println("Here's the list I've stored for you:");
//                    for (int i = 1; i <= lst.size(); i++) {
//                        System.out.println(i + ") " + lst.get(i - 1));
//                    }
                } else if (keywordInput[0].equalsIgnoreCase("deadline")) {
                    try {
                        String[] parsedDeadline = keywordInput[1].split("/by");
                        if (parsedDeadline[0].isEmpty()) {
                            throw new KatheryneException(
                                    "A deadline needs a description and a /by time in the format 2007-12-03. The " 
                                            + "description is missing~");
                        }
                        if (parsedDeadline.length == 2) {
                            lst.add(new Deadline(parsedDeadline[0].trim(), LocalDate.parse(parsedDeadline[1].trim())));
                        } else {
                            throw new KatheryneException(
                                    "A deadline needs a description and a /by time in the format 2007-12-03.");
                        }
                        System.out.println(
                                "'" + parsedDeadline[0] + "' added to your list, do by " + parsedDeadline[1]);
                    } catch (DateTimeParseException e) {
                        throw new KatheryneException(
                                "The by time is in the wrong format. It must be in the format YYYY-MM-DD");
                    }
                } else if (keywordInput[0].equalsIgnoreCase("event")) {
                    try {
                        String[] parsedEvent = keywordInput[1].split(" /at ");
                        if (parsedEvent[0].isEmpty()) {
                            throw new KatheryneException(
                                    "An event needs a description and an /at time when it occurs in the format " 
                                            + "2007-12-03. The description is missing");
                        }
                        if (parsedEvent.length == 2) {
                            lst.add(new Event(parsedEvent[0].trim(), LocalDate.parse(parsedEvent[1].trim())));
                            System.out.println(
                                    "'" + parsedEvent[0] + "' added to your list, scheduled for " + parsedEvent[1]);
                        } else {
                            throw new KatheryneException(
                                    "An event needs a description and an /at time when it occurs in the format " 
                                            + "2007-12-03.");
                        }
                    } catch (DateTimeParseException e) {
                        throw new KatheryneException(
                                "The at time is in the wrong format. It must be in the format YYYY-MM-DD");
                    }
                } else if (keywordInput[0].equalsIgnoreCase("todo")) {
                    if (keywordInput.length == 1) {
                        throw new KatheryneException("A todo needs a description ");
                    } else {
                        lst.add(new Todo(keywordInput[1].trim()));
                        System.out.println("Todo item '" + keywordInput[1] + "' added to your list");
                    }
                } else {
                    throw new UnknownCommandException();
                }
                System.out.println("There are currently " + lst.getSize() + " items in your list.");
            } catch (UnknownCommandException e) {
                System.out.println(e.getMessage());
            } catch (KatheryneException e) {
                System.out.println(e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("You need to specify a number in the correct format. ERROR: "
                        + e.getMessage());
            }
        }

        // try to save the file
        try {
            storage.saveTasks(lst,"tasks.json");
        } catch (KatheryneException e) {
            System.out.println(e.getMessage());
        }
    }
}