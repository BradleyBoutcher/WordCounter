package com.bboutcher;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import static com.ea.async.Async.await;

import static org.junit.Assert.*;

/**
 * Testing of key methods in FileReader
 */
public class FileReaderTest extends FileReaderBaseTest {
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
     * Verify word count for a single file
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

    /**
     * Process two files, then remove one file. This verifies that the removed file is no
     * longer in the word count.
     */
    @org.junit.Test
    public void processRemoveFile() {
        this.generateTestFiles(2);

        File testFile = new File(paths[0]);
        File testFile2 = new File(paths[1]);


        System.out.println("First file parsed");
        HashMap<String, Integer> newWordCount = await(reader.processNewFile(testFile));
        await(reader.print());
        System.out.println();

        System.out.println("Second file parsed");
        HashMap<String, Integer> secondNewWordCount = await(reader.processNewFile(testFile2));
        await(reader.print());
        System.out.println();

        System.out.println("First file removed");
        HashMap<String, Integer> masterWordCountPostRemoval = await(reader.processRemoveFile(testFile));
        await(reader.print());
        System.out.println();

        assertEquals(secondNewWordCount, masterWordCountPostRemoval);
    }

}
