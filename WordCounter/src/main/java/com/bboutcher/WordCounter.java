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
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

import static com.ea.async.Async.await;
import static java.util.concurrent.CompletableFuture.completedFuture;

/**
 * Data structure for storing file path lists and their associated word count
 * Uses stateful async management for a friendly user interface
 *
 * C Bradley Boutcher 2019
 */

public class WordCounter {
    enum WordCounterStages {
        INPUT,
        INIT,
        ADD,
        REMOVE,
        PRINT,
        LIST,
        EXIT,
    }

    // Aggregated words and their count
    private HashMap<String, Integer> words;
    // File paths
    private ArrayList<File> paths;

    // Current stage / state of open WordCounter
    private WordCounterStages currentStage;

    WordCounter(){
        System.out.println("* New Word Counter Created *");
    }

    public void start() {
        this.start(WordCounterStages.INIT);
    }

    private CompletableFuture<WordCounterStages> start(WordCounterStages stage) {
        System.out.println();

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
            case PRINT: {
                currentStage = await(printPaths());
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

    private CompletableFuture<WordCounterStages> handleInput() {
        Scanner input = new Scanner(System.in);
        System.out.println(
                "Please choose a command: " +
                        "\n 1: Add a file path to this Word Reader " +
                        "\n 2: Remove a file path from this Word Reader" +
                        "\n 3: Print the aggregate word count of this Word Reader" +
                        "\n 4: List file paths in this word reader" +
                        "\n 0: Exit");
        int answer = input.nextInt();

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

    public void readFile() {
        System.out.println("Reading..");
    }

    private CompletableFuture<WordCounterStages> gatherPaths() {
        String answer = Utilities.getStringInput("Open file explorer? (Y / N)");

        // Use UI or command line
        if (answer.matches("[yY]")) this.paths = await(desktopFileGather());
        else if (answer.matches("[nN]")) this.paths = await(commandLineFileGather());
        else {
            System.out.println("Invalid input, please try again.");
            start(WordCounterStages.INIT);
        }

        // User did not select any paths - start over
        if (this.paths == null || this.paths.size() == 0) {
            return completedFuture(WordCounterStages.INIT);
        }

        return completedFuture(WordCounterStages.INPUT);
    }

    private CompletableFuture<ArrayList<File>> desktopFileGather() {
        ArrayList<File> chosenPaths = new ArrayList<>();
        JFrame frame;

        // Gather information about user displays
        try {
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

        try {
            FileDialog fd = new FileDialog(frame, "Please choose one or more files", FileDialog.LOAD);
            fd.setDirectory("C:\\");

            // Filter out non text files
            fd.setFilenameFilter((dir, name) -> !name.matches(".*(\\.txt)") || name.matches("^[.]"));

            fd.setMultipleMode(true);
            fd.setVisible(true);

            // Handle selected files
            if (fd.getFiles() == null || fd.getFiles().length == 0) {
                System.out.println("You did not choose any files, please try again.");
                return completedFuture(chosenPaths);
            }
            else Collections.addAll(chosenPaths, fd.getFiles());

        } catch (Exception e) {
            System.out.println("Error while opening desktop files.");
        }

        return completedFuture(chosenPaths);
    }

    private CompletableFuture<ArrayList<File>> commandLineFileGather() {
        ArrayList<File> chosenPaths = new ArrayList<>();
        Scanner list = Utilities.getArrayInput("Please provide a list of file paths, delimited by a space.");
        while (list.hasNext()) {
            chosenPaths.add(new File(list.next()));
        }
        return completedFuture(chosenPaths);
    }

    private CompletableFuture<WordCounterStages> printPaths() {
        int count = 0;
        for (File f : paths) {
            System.out.println(count++ + ": " + f.getPath());
        }
        return completedFuture(WordCounterStages.INPUT);
    }

    private CompletableFuture<WordCounterStages> removePath() {
        if (this.paths.size() == 0) {
            System.out.println("File path list is empty.");
            return completedFuture(WordCounterStages.INPUT);
        }

        await(printPaths());
        int target = Utilities.getIntegerInput("Please enter the index of the path you wish to remove");
        try {
            this.paths.remove(target);
        } catch (Exception e) {
            System.out.println("Unable to remove path. Please try again");
        }

        String answer = Utilities.getStringInput("Would you like to remove another path?");
        if (answer.matches("[yY]")) return completedFuture(WordCounterStages.REMOVE);
        // If invalid input, give them another chance to remove instead of returning to menu
        else if (!answer.matches("[nN]")) {
            System.out.println("Invalid input, please try again.");
            start(WordCounterStages.REMOVE);
        }

        return completedFuture(WordCounterStages.INPUT);
    }

}
