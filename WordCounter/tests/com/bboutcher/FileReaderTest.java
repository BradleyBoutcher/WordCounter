package com.bboutcher;

import static com.ea.async.Async.await;

import org.junit.jupiter.api.Test;
import java.io.FileNotFoundException;
import java.util.List;

import com.ea.async.Async;

class FileReaderTest {

    @Test
    void readFile() throws FileNotFoundException {
        FileReader reader = new FileReader();
        List<String> results = await(reader.readFile("test"));
        List<String> results2 = await(reader.readFile("test2"));
        System.out.println(results2.toString());
        System.out.println(results.toString());
    }
}
