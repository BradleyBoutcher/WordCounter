package com.bboutcher;

import static com.ea.async.Async.await;
import static java.util.concurrent.CompletableFuture.completedFuture;

import com.ea.async.Async;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

/**
 * Class for creating, storing, and accessing multiple
 * instances of WordCounter objects
 */

final class CounterManager {

    enum ManagementStages {
        INPUT,
        NEW,
        PRINT,
        OPEN,
        SAVE,
        DELETE,
        EXIT,
    }

    // All saved wordCounters
    private static HashMap<String, WordCounter> wordCounters = new HashMap<>();
    // Store the word counter being acted upon
    private static WordCounter temporary = null;
    private static Logger log = Logger.getLogger("Word Counter Management Service");
    // Obtaining user input
    private static Scanner input;
    // Current state or stage
    private static ManagementStages currentStage;

    // Avoid instantiation, effectively a static class
    private CounterManager() {}

    static void start() throws Exception {
        Async.init();
        start(ManagementStages.INPUT);
    }

    /**
     * The state or stage management method of our management class
     * Allows us to continue to provide and act upon user input until user
     * is finished
     *
     * @param stage Current step or stage of the countermanager
     * @throws Exception
     */
    private static CompletableFuture<Void> start(ManagementStages stage) throws Exception {
        switch (stage) {
            case INPUT: {
                currentStage = await(handleInput());
                return start(currentStage);
            }
            case NEW: {
                currentStage = await(createWordCounter());
                return start(currentStage);
            }
            case OPEN : {
                currentStage = await(accessWordCounter());
                return start(currentStage);
            }
            case PRINT: {
                System.out.println("The aggregated word count for the current Word Counter is...");
                return start(currentStage);
            }
            case EXIT: {
                input = null;
                temporary = null;
                wordCounters.clear();
                System.out.println("Thank you for using Word Counter!");
                break;
            }
        }
        return completedFuture(null);
    }

    /**
     * Method consumed by the INPUT stage
     * Initial state where the user decides what actions to take
     *
     * @throws Exception
     */
    private static CompletableFuture<ManagementStages> handleInput() throws Exception {

        int answer = getIntegerInput(
                "Please choose a command: " +
                        "\n 1: Create a new Word Reader " +
                        "\n 2: Print a saved Word Reader" +
                        "\n 3: Save current Word Reader" +
                        "\n 4: Open a saved Word Reader" +
                        "\n 5: Delete a saved Word Reader" +
                        "\n 0: Exit");

        ManagementStages selection;

        if (answer == 1) selection = ManagementStages.NEW;
        else if (answer == 2) selection = ManagementStages.PRINT;
        else if (answer == 3) selection = ManagementStages.SAVE;
        else if (answer == 4) selection = ManagementStages.OPEN;
        else if (answer == 5) selection = ManagementStages.DELETE;
        else if (answer == 0) selection = ManagementStages.EXIT;
        else {
            System.out.println("Invalid menu option, please try again.");
            selection = ManagementStages.INPUT;
        }

        return completedFuture(selection);
    }

    // NEW
    private static CompletableFuture<ManagementStages> createWordCounter() throws Exception{
        temporary = new WordCounter();
        temporary.start();

        String answer = getStringInput("Save current Word Counter? ( Y / N )");

        // Validate input
        try {
            answer = input.next("[yYnN]");
        } catch (Exception e) {
            retry(ManagementStages.NEW);
        }

        // Save or continue without saving
        if (answer.matches("[yY]")) {
            saveWordCounter();
        } else if (answer.matches("[nN]")) {
            System.out.println("Current Word Counter will not be saved.");
        } else {
            retry(ManagementStages.NEW);
        }

        return completedFuture(ManagementStages.INPUT);
    }

    // SAVE
    // Store a given word counter with an associated key
    private static void saveWordCounter() throws Exception {
        String name = getStringInput("Please provide a name for to save this Word Counter.");

        if (temporary == null) {
            log.fine("Cannot save null WordCounter.");
        }
        try {
            wordCounters.put(name, temporary);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
    }

    // OPEN
    // Access a word counter with a given key
    private static CompletableFuture<ManagementStages> accessWordCounter() {
        input = new Scanner(System.in);
        System.out.println("Please enter the name for a saved WordCounter");
        String key = input.next();

        if (key.isEmpty()) {
            log.fine("Lookup key for WordCounter is empty.");
            return completedFuture(ManagementStages.OPEN);
        }

        try {
            temporary = wordCounters.get(key);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: WordCounter could not be opened");
            return completedFuture(ManagementStages.INPUT);
        }

        temporary.start();
        return completedFuture(ManagementStages.INPUT);
    }

    // DELETE
    // Remove a word counter with a given key
    private static CompletableFuture<ManagementStages> deleteWordCounter() throws Exception {
        String key = getStringInput("Please enter the name for a saved WordCounter you would like to delete.");

        if (key.isEmpty()) {
            log.fine("Lookup key for WordCounter is empty.");
        }

        try {
            wordCounters.remove(key);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Unable to delete specified Word Counter");
            return completedFuture(ManagementStages.INPUT);
        }
        System.out.println("Deleted Word Counter: " + key);

        return completedFuture(ManagementStages.INPUT);
    }

    private static String getStringInput(String message) {
        input = new Scanner(System.in);
        System.out.println(message);
        return input.next();
    }

    private static int getIntegerInput(String message) {
        input = new Scanner(System.in);
        System.out.println(message);
        return input.nextInt();
    }

    private static void retry(ManagementStages stage) throws Exception {
        System.out.println("Invalid input, please try again.");
        start(stage);
    }
}
