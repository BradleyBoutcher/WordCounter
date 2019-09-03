package com.bboutcher;

import com.ea.async.Async;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.ea.async.Async.await;

import static org.junit.Assert.*;

public class FileReaderTest {
    @org.junit.Before
    public void setUp() {
        Async.init();

        System.out.println("Cleaning up any leftovers...");
        this.deleteTestFiles();
        System.out.println("Setup Finished.");
        System.out.println();
    }

    @org.junit.After
    public void tearDown() {
        System.out.println();
        System.out.println("Cleaning up...");
        this.deleteTestFiles();
        System.out.println();
    }

    /**
     * Verify that a given list of file paths generates the intended
     * files.
     */
    @org.junit.Test
    public void convertPathsToFiles() {
        this.generateTestFiles(10);

        ArrayList<File> testFiles = await(reader.convertPathsToFiles(paths));

        testFiles.forEach( (f) -> assertTrue(files.contains(f)));
    }

    /**
     * Verify accuracy for a single file
     */
    @org.junit.Test
    public void processNewFile() {
        this.generateTestFiles(1);

        File testFile = new File(paths[0]);

        HashMap<String, Integer> temp = await(reader.processNewFile(testFile));

        System.out.println();
        await(reader.print());

        assertEquals(validationWordCount, temp);
    }

    @org.junit.Test
    public void processRemoveFile() {
    }

    @org.junit.Test
    public void getWordCountFromFile() {
    }

    private final FileReader reader = new FileReader();

    private final Random random = new Random();

    private final String[] words = new String[] {
            "apple", "is", "dog", "//", "test.ing", "words", "are",
            "how", "watch", "I", "9909909", "Cardinals", "Steve", "testing",
            "colossal"
    };

    private String[] paths;
    private ArrayList<File> files = new ArrayList<>();

    private final String prefix = "testFileForFileReader";

    private HashMap<String, Integer> validationWordCount = new HashMap<>();

    private void generateTestFiles(Integer count) {
        this.paths = new String[count];
        int tracking = 0;
        try {
            while (tracking < count) {
                // Generate file name and content
                File temp = writeFile(10);
                this.paths[tracking] = temp.getAbsolutePath();
                this.files.add(temp);
                tracking++;
            }
        } catch (Exception e) {
            System.out.println("Error generating test files.");
        }

        System.out.println("Test Files generated. Current word count is: ");
        this.printValidationWordCount();
    }

    public File writeFile(Integer length) {
        String str = generateSentence(length);
        String name = generateRandomFileName();

        Path path = Paths.get(name);
        byte[] strToBytes = str.getBytes();

        try {
            Files.write(path, strToBytes);
        } catch (IOException e) {
            System.out.println("Failed writing test file");
        }

        return new File(path.toAbsolutePath().toString());
    }

    private void deleteTestFiles() {
        File folder = new File(".");
        File[] listOfFiles = folder.listFiles((f) ->
                f.getAbsolutePath().contains(prefix));

        if (listOfFiles == null || listOfFiles.length < 1) return;

        for (File f : listOfFiles) {
            String name = f.getName();

            if (f.isFile()) {
                System.out.println("File " + name + " deleted");
                if (f.delete()) continue;
                System.out.println("Error deleting " + name);
            }
        }
    }

    private void gatherTestFiles() {
        File folder = new File(".");
        File[] listOfFiles = folder.listFiles((f) ->
                f.getAbsolutePath().contains(prefix));

        if (listOfFiles == null || listOfFiles.length < 1) return;

        Collections.addAll(files, listOfFiles);

        for (File f : listOfFiles) {
            String path = f.getAbsolutePath();

        }
    }

    private String generateRandomFileName() {
        String suffix = Double.toString(Math.random());

        return prefix + "-" + suffix;
    }

    private String generateSentence(Integer length) {
        StringBuilder builder = new StringBuilder();

        while (length > 0) {
            // grab random word
            String word = words[random.nextInt(words.length)];

            word = await(FileReader.stripDownInvalidCharacters(word));

            if (!word.equals("")) {
                // save if applicable
                this.increment(word);

                // add to string
                builder.append(word);
                builder.append(" ");
            }

            length--;
        }

        return builder.toString();
    }

    private void increment(String key) {
        if (validationWordCount.containsKey(key)) {
            validationWordCount.replace(key, validationWordCount.get(key) + 1);
        } else {
            validationWordCount.put(key, 1);
        }
    }

    private void printValidationWordCount() {
        for (Map.Entry e: validationWordCount.entrySet()) {
            System.out.println(e.getKey() + ":" + e.getValue() + ",");
        }
    }
}
