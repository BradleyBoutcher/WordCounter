package com.bboutcher;

import com.ea.async.Async;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

import static com.ea.async.Async.await;
import static java.util.concurrent.CompletableFuture.completedFuture;

/**
 * A utility class designed to validate and read file paths,
 * and their correlating file
 */
public class FileReader {

    // Asynchronously strip down characters
    public static CompletableFuture<String> stripDownInvalidCharacters(String s) {
        String result;
        try {
            result = s
                    .replaceAll("[^A-Za-z0-9]", " ")
                    .toLowerCase();
        } catch (Throwable t) {
            t.printStackTrace();
            return completedFuture("");
        }
        return completedFuture(result);
    }

    public CompletableFuture<Void> readFile(String file) {
        return completedFuture(null);
    }

    public CompletableFuture<List<String>> readFile(File f) throws FileNotFoundException {
        String fileName = "C:\\Users\\Bradley Boutcher\\Desktop\\test.txt";
        File textFile = new File(fileName);

        List<String> results = new ArrayList<>();

        try (Scanner in = new Scanner(textFile)) {
            in.forEachRemaining((s) -> {
                s = await(stripDownInvalidCharacters(s));
                results.add(s);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return completedFuture(results);
    }
}
