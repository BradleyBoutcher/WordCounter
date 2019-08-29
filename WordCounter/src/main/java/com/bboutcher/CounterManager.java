package com.bboutcher;

import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

import static com.ea.async.Async.await;
import static java.util.concurrent.CompletableFuture.completedFuture;

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
        LIST,
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

    static void start() {
        start(ManagementStages.INPUT);
    }

    /**
     * The state / stage management method of our management class
     * Allows us to continue to provide and act upon user input until user
     * is finished
     *
     * @param stage Current step or stage of the countermanager
     * @throws Exception
     */
    private static CompletableFuture<Void> start(ManagementStages stage) {
        System.out.println();

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
            case DELETE: {
                currentStage = await(deleteWordCounter());
                return start(currentStage);
            }
            case SAVE: {
                currentStage = await(saveWordCounter());
                return start(currentStage);
            }
            case PRINT: {
                System.out.println("The aggregated word count for the current Word Counter is...");
                return start(currentStage);
            }
            case LIST: {
                currentStage = await(listWordCounters());
                return start(currentStage);
            }
            case EXIT: {
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
    private static CompletableFuture<ManagementStages> handleInput() {

        int answer = Utilities.getIntegerInput(
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
    private static CompletableFuture<ManagementStages> createWordCounter() {
        // Instantiate & Start Interface for new Word Counter
        temporary = new WordCounter();
        temporary.start();

        String answer = Utilities.getStringInput("Save current Word Counter? ( Y / N )");

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
    private static CompletableFuture<ManagementStages> saveWordCounter() {
        String name = Utilities.getStringInput("Please provide a name for to save this Word Counter.");

        if (temporary == null) {
            log.fine("Cannot save null WordCounter.");
        }
        try {
            wordCounters.put(name, temporary);
        } catch (Exception e) {
            System.out.println("An error has occurred. Unable to save word counter.");
        }

        return completedFuture(ManagementStages.INPUT);
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
    private static CompletableFuture<ManagementStages> deleteWordCounter()
    {
        String key = Utilities.getStringInput("Please enter the name for a saved WordCounter you would like to delete.");

        if (key.isEmpty())
        {
            log.fine("Lookup key for WordCounter is empty.");
        }

        try
        {
            wordCounters.remove(key);
        } catch (Exception e)
        {
            System.out.println("Unable to delete specified Word Counter");
            return completedFuture(ManagementStages.INPUT);
        }
        System.out.println("Deleted Word Counter: " + key);

        return completedFuture(ManagementStages.INPUT);
    }

    private static void retry(ManagementStages stage)
    {
        System.out.println("Invalid input, please try again.");
        start(stage);
    }

    private static CompletableFuture<ManagementStages> listWordCounters()
    {
        if (wordCounters.size() == 0) {
            System.out.print("No WordCounters saved.");
            return completedFuture(ManagementStages.INPUT);
        }

        System.out.print("Saved Word Counters: ");
        wordCounters.forEach((k, w) -> System.out.print(k));

        return completedFuture(ManagementStages.INPUT);
    }
}
