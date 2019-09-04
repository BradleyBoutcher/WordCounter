package com.bboutcher;

import javax.swing.JFrame;

import java.awt.FileDialog;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;

import java.awt.GraphicsEnvironment;
import java.io.File;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import static com.ea.async.Async.await;
import static java.util.concurrent.CompletableFuture.completedFuture;

/**
 * Data structure for storing file path lists and their associated word count
 * Uses stateful async management for a friendly user interface
 *
 * C Bradley Boutcher 2019
 */

class WordCounter {
    enum WordCounterStages {
        INPUT,
        INIT,
        ADD,
        REMOVE,
        LOOKUP,
        PRINT,
        LIST,
        EXIT,
    }

    // File reader for parsing current path list
    private FileReader reader;

    // File paths
    private ArrayList<File> paths;

    // Identifier for this WordCounter
    private String name = "";

    WordCounter(){
        System.out.println("* New Word Counter Created *");
        this.reader = new FileReader();
    }

    void start() {
        this.start(WordCounterStages.INIT);
    }

    CompletableFuture<WordCounterStages> start(WordCounterStages stage) {
        WordCounterStages currentStage;

        System.out.println();
        if (name.equals("")) System.out.println("-- New WordCounter --");
        else System.out.println("-- Word Counter: " + name + " --");

        switch (stage) {
            case INIT: {
                currentStage = await(gatherPaths());
                return start(currentStage);
            }
            case INPUT: {
                currentStage = await(handleInput());
                return start(currentStage);
            }
            case ADD: {
                currentStage = await(gatherPaths());
                return start(currentStage);
            }
            case REMOVE: {
                currentStage = await(removePath());
                return start(currentStage);
            }
            case LOOKUP: {
                currentStage = await(lookup());
                return start(currentStage);
            }
            case PRINT: {
                currentStage = await(printCurrentWordCount());
                return start(currentStage);
            }
            case LIST: {
                currentStage = await(printPaths());
                return start(currentStage);
            }
            case EXIT: {
                break;
            }
        }
        return completedFuture(null);
    }

    private CompletableFuture<WordCounterStages> handleInput()
    {
        int answer = Utilities.getIntegerInput("Please choose a command: " +
                "\n 1: Add a file path to this Word Reader " +
                "\n 2: Remove a file path from this Word Reader" +
                "\n 3: Print the aggregate word count of this Word Reader" +
                "\n 4: List file paths in this word reader" +
                "\n 0: Exit");

        WordCounterStages selection;
        if      (answer == 1) selection = WordCounterStages.ADD;
        else if (answer == 2) selection = WordCounterStages.REMOVE;
        else if (answer == 3) selection = WordCounterStages.PRINT;
        else if (answer == 4) selection = WordCounterStages.LIST;
        else if (answer == 0) selection = WordCounterStages.EXIT;
        else {
            System.out.println("Invalid input, please try again.");
            selection = WordCounterStages.INPUT;
        }

        return completedFuture(selection);
    }

    public CompletableFuture<Void> processFiles()
    {
        try
        {
            this.paths.forEach((f) ->
            {
                HashMap<String, Integer> temp = await(this.reader.processNewFile(f));
            });
        } catch (Exception e) {
            System.out.println("Unable to process file list");
        }

        return completedFuture(null);
    }

    private CompletableFuture<WordCounterStages> gatherPaths()
    {
        String answer = Utilities.getStringInput("Open file explorer? (Y / N)");

        // Use UI or command line
        if (answer.matches("[yY]")) this.paths = await(desktopFileGather());
        else if (answer.matches("[nN]")) this.paths = await(commandLineFileGather());
        else {
            System.out.println("Invalid input, please try again.");
            start(WordCounterStages.INIT);
        }

        // User did not select any paths - start over
        if (this.paths == null || this.paths.size() == 0)
        {
            return completedFuture(WordCounterStages.INIT);
        }

        // Update Word Count with new Files
        await(this.processFiles());

        return completedFuture(WordCounterStages.INPUT);
    }

    /**
     * Create a hidden JFrame to allow file selection through native desktop file explorer
     *
     * @return
     */
    private CompletableFuture<ArrayList<File>> desktopFileGather()
    {
        ArrayList<File> chosenPaths = new ArrayList<>();
        JFrame frame;

        // Gather information about user displays
        try
        {
            GraphicsEnvironment ge = GraphicsEnvironment
                    .getLocalGraphicsEnvironment();
            GraphicsDevice[] gc = ge.getScreenDevices();
            GraphicsConfiguration configuration = null;
            for (GraphicsDevice d: gc) {
                for (GraphicsConfiguration c: d.getConfigurations()) {
                    if (c != null) {
                        configuration = c;
                        break;
                    }
                }
            }
            // Build a jFrame for containing our explorer
            frame = new JFrame("WordCounter", configuration);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Unable to display visual file gatherer - please use an alternative method.");
            return completedFuture(chosenPaths);
        }


        // Open file explorer
        try
        {
            FileDialog fd = new FileDialog(frame, "Please choose one or more files", FileDialog.LOAD);
            fd.setDirectory("C:\\");

            // Filter out non text files - uncomment to filer non generic text files
            // fd.setFilenameFilter((dir, name) -> name.matches(".*(\\.txt)") || name.matches("^[.]"));

            fd.setMultipleMode(true);
            fd.setVisible(true);

            // Handle selected files
            if (fd.getFiles() == null || fd.getFiles().length == 0)
            {
                System.out.println("You did not choose any files, please try again.");
                return completedFuture(chosenPaths);
            }
            else Collections.addAll(chosenPaths, fd.getFiles());

        } catch (Exception e) {
            System.out.println("Error while opening desktop files.");
        }

        return completedFuture(chosenPaths);
    }

    /**
     * Retrieve a list of paths from the command line, delimited by a space
     *
     * @return
     */
    private CompletableFuture<ArrayList<File>> commandLineFileGather()
    {
        ArrayList<File> chosenPaths;
        String[] pathArray = Utilities.getArrayInput("Please provide a list of file paths, delimited by a space.");

        if (pathArray == null || pathArray.length < 1) {
            System.out.println("No path list has been generated.");
            return completedFuture(null);
        }
        // Attempt to convert the path to a file and add to running list
        chosenPaths = await(this.reader.convertPathsToFiles(pathArray));

        return completedFuture(chosenPaths);
    }

    /**
     * Lookup the count for a specific word
     */
    private CompletableFuture<WordCounterStages> lookup() {
        String key = Utilities.getStringInput("Please enter the key to search for.");

        if (key.equals("")) {
            System.out.println("No key entered, exiting...");
            return completedFuture(WordCounterStages.INPUT);
        }

        Integer value = 0;

        try {
            value = this.reader.wordCount.get(key);
        } catch (Exception e) {
            System.out.println("Invalid or non existent key entered, please try again.");
            return completedFuture(WordCounterStages.LOOKUP);
        }

        System.out.println(key + ": " + value);

        String answer = Utilities.getStringInput("Would you like to lookup another word? (Y / N)");
        if (answer.matches("[yY]")) return completedFuture(WordCounterStages.LOOKUP);
            // If invalid input, give them another chance to remove instead of returning to menu
        else if (!answer.matches("[nN]"))
        {
            System.out.println("Invalid input, exiting...");
        }
        return completedFuture(WordCounterStages.INPUT);
    }

    /**
     * Display a list of current saved paths
     *
     * @return
     */
    private CompletableFuture<WordCounterStages> printPaths()
    {
        int count = 0;
        for (File f : paths) {
            System.out.println(count++ + ": " + f.getPath());
        }
        return completedFuture(WordCounterStages.INPUT);
    }

    /**
     * Print the current word count
     *
     * @return
     */
    public CompletableFuture<WordCounterStages> printCurrentWordCount()
    {
        try {
            await(reader.print());
        } catch (Exception e) {
            System.out.println("There was an issue printing the word count for this Word Counter");
        }
        return completedFuture(WordCounterStages.INPUT);
    }

    /**
     * Remove a file from the WordCounter list, and decrement the total count
     * in our FileReader
     *
     * @return
     */
    private CompletableFuture<WordCounterStages> removePath()
    {
        if (this.paths.size() == 0)
        {
            System.out.println("File path list is empty.");
            return completedFuture(WordCounterStages.INPUT);
        }

        await(printPaths());

        int target = Utilities.getIntegerInput("Please enter the index of the path you wish to remove");
        try
        {
            File removed = this.paths.remove(target);
            await(this.reader.processRemoveFile(removed));
        } catch (Exception e) {
            System.out.println("Unable to remove path. Please try again");
        }

        String answer = Utilities.getStringInput("Would you like to remove another path? (Y / N)");
        if (answer.matches("[yY]")) return completedFuture(WordCounterStages.REMOVE);
        // If invalid input, give them another chance to remove instead of returning to menu
        else if (!answer.matches("[nN]"))
        {
            System.out.println("Invalid input, please try again.");
            return completedFuture(WordCounterStages.REMOVE);
        }

        return completedFuture(WordCounterStages.INPUT);
    }

    /**
     * Update or set the identifier for a WordCounter
     *
     * @param newName - New name for WordCounter
     * @return
     */
    CompletableFuture<Void> setName(String newName)
    {
        if (newName.equals("")) {
            System.out.println("Please enter a name with more than one character.");
        } else {
            name = newName;
        }
        return completedFuture(null);
    }
}
