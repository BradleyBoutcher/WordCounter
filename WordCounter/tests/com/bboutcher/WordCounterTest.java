package com.bboutcher;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

class WordCounterTest {

    @Test
    void readFile() throws FileNotFoundException {
        FileReader reader = new FileReader();
        reader.readFile("test");
    }
}