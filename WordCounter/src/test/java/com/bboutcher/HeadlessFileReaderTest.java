package com.bboutcher;

import static org.junit.Assert.*;

/**
 * End-to-end testing of headless file reader
 */
public class HeadlessFileReaderTest extends FileReaderTest {

    @org.junit.Test
    public void convertPathsToFiles() {
        this.generateTestFiles(5);
        FileReader.HeadlessFileReader reader = new FileReader.HeadlessFileReader(this.paths);
        reader.print();

        assertEquals(this.validationWordCount, reader.wordCount);
    }
}
