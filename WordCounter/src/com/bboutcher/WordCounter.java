package com.bboutcher;

import javax.swing.JFrame;

import static com.ea.async.Async.await;
import static java.util.concurrent.CompletableFuture.completedFuture;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.FileDialog;
import java.awt.Label;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

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
    private static Logger log = Logger.getLogger("Word Counter Management Service");
    // Obtaining user input
    private static Scanner input;

    WordCounter(){
        System.out.println("* New Word Counter Created *");
    }

    public void start() {
        this.start(WordCounterStages.INIT);
    }

    private CompletableFuture<WordCounterStages> start(WordCounterStages stage) {
        switch (stage) {
            case INIT: {
                currentStage = await(gatherPaths());
                return start(currentStage);
            }
            case INPUT: {
                currentStage = await(handleInput());
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
        input = new Scanner(System.in);
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
        input = new Scanner(System.in);
        System.out.println("Open file explorer? (Y / N)");
        String answer = "";

        // Validate input
        try {
            answer = input.next("[yYnN]");
        } catch (Exception e) {
            System.out.println("Invalid input, please try again.");
            start(WordCounterStages.INIT);
        }

        if (answer.matches("[yY]")) this.paths = await(desktopFileGather());
        else if (answer.matches("[nN]")) this.paths = await(commandLineFileGather());
        else {
            System.out.println("Invalid input, please try again.");
            start(WordCounterStages.INIT);
        }

        return completedFuture(WordCounterStages.INPUT);
    }

    private CompletableFuture<ArrayList<File>> desktopFileGather() {
        ArrayList<File> chosenPaths = new ArrayList<>();

        JFrame frame = new JFrame("WordCounter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(false);

        try {
            FileDialog fd = new FileDialog(frame, "Please choose one or more files", FileDialog.LOAD);
            fd.setDirectory("C:\\");

            // Filter out non text files
            fd.setFilenameFilter((dir, name) -> {
                    if (name.matches("[*].txt") || name.matches("^[.]")) {
                        return true;
                    }
                    return false;
            });

            fd.setMultipleMode(true);
            fd.setVisible(true);

            // Handle selected files
            if (fd.getFiles() == null) System.out.println("You cancelled choosing files.");
            else Collections.addAll(chosenPaths, fd.getFiles());

        } catch (Exception e) {
            System.out.println("Error while opening desktop files.");
        }

        return completedFuture(chosenPaths);
    }

    private CompletableFuture<ArrayList<File>> commandLineFileGather() {
        ArrayList<File> chosenPaths = new ArrayList<>();
        return completedFuture(chosenPaths);
    }

    private CompletableFuture<WordCounterStages> printPaths() {
        int count = 0;
        for (File f : paths) {
            System.out.println(count++ + ": " + f.getPath());
        }
        return completedFuture(WordCounterStages.INPUT);
    }

}
