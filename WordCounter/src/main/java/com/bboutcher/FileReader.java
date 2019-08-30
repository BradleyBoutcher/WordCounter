package com.bboutcher;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

import static com.ea.async.Async.await;
import static java.util.concurrent.CompletableFuture.completedFuture;

/**
 * A utility class designed to validate and read file paths,
 * and their correlating file
 *
 * C Bradley Boutcher 2019
 */
class FileReader {

    // Aggregated words and their respective count
    private HashMap<String, Integer> wordCount = new HashMap<>();

    /**
     * Remove invalid characters from the word provided
     *
     * @param s
     * @return
     */
    private static CompletableFuture<String> stripDownInvalidCharacters(String s)
    {
        String result;
        try
        {
            result = s
                    .replaceAll("[^A-Za-z]", "")
                    .toLowerCase();
        } catch (Throwable t) {
            t.printStackTrace();
            return completedFuture("");
        }
        return completedFuture(result);
    }

    /**
     * Retrieve the word count for a single file and merge it into the total
     *
     * @param f
     * @return
     */
    public CompletableFuture<Void> processNewFile(File f)
    {
        try
        {
            HashMap<String, Integer> temp = await(this.getWordCountFromFile(f));
            await(this.mergeWordCounts(temp, false));
        } catch (Exception e) {
            System.out.println("Unable to process file: " + f.getPath());
        }

        return completedFuture(null);
    }

    /**
     * Retrieve the word count for a single file and remove it from the total
     * @param f
     * @return
     */
    public CompletableFuture<Void> processRemoveFile(File f)
    {
        try {
            HashMap<String, Integer> temp = await(this.getWordCountFromFile(f));
            await(this.mergeWordCounts(temp, true));
        } catch (Exception e) {
            System.out.println("Unable to process the removal from word count of file: " + f.getPath());
        }

        return completedFuture(null);
    }

    /**
     * Retreive the word count for a single file
     *
     * @param f - A text file, only words with standard letters or numbers will be read
     * @return
     */
    private CompletableFuture<HashMap<String, Integer>> getWordCountFromFile(File f)
    {
        HashMap<String, Integer> currentWordSet = new HashMap<>();

        try (Scanner words = new Scanner(f, StandardCharsets.UTF_8.name())) {
            words.forEachRemaining((word) -> {
                word = await(stripDownInvalidCharacters(word));
                if (!word.equals("")) {
                    // If the word already exists, add it
                    try {
                        if (this.wordCount.containsKey(word))
                            this.wordCount.replace(word, this.wordCount.get(word) + 1);
                        else this.wordCount.putIfAbsent(word, 1);
                    } catch (Exception e) {
                        System.out.println("Unable to increment count for temporary key: " + word);
                    }
                }
            });
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + f.getPath());
        }

        return completedFuture(currentWordSet);
    }


    /**
     * Asynchronously merge the word count for a single file into the combined total
     *
     * This allows a thread safe method of updating the running word count, while not
     * waiting for the other files to complete their operaitons
     * @param newWordCount
     * @param @Nullable decrement - optional flag to remove the given file from the word count
     * @return
     */
    private CompletableFuture<Void> mergeWordCounts(HashMap<String, Integer> newWordCount, Boolean decrement)
    {
        try {
            newWordCount
                    .entrySet()
                    .iterator()
                    .forEachRemaining((e) -> {
                            if (decrement) await(decrementTotal(e.getKey()));
                            else await(incrementTotalWordCount(e.getKey()));
                    });
        } catch (Exception e) {
            System.out.println("Unable to update total word count");
        }
        return completedFuture(null);
    }

    /**
     * Increase the overall word count for a single word
     * Add the key if not present
     *
     * @param key
     * @return
     */
    private CompletableFuture<Void> incrementTotalWordCount(String key)
    {
        try {
            if (this.wordCount.containsKey(key)) this.wordCount.replace(key, this.wordCount.get(key) + 1);
            else this.wordCount.putIfAbsent(key, 1);
        } catch (Exception e) {
            System.out.println("Unable to increment count for key: " + key);
        }
        return completedFuture(null);
    }

    /**
     * Decrement the running total of all words for a single word
     * Remove if the key has reached 0 references
     *
     * @param key
     * @return
     */
    private CompletableFuture<Void> decrementTotal(String key)
    {
        try
        {
            // decrement
            if (this.wordCount.containsKey(key)) {
                this.wordCount.replace(key, this.wordCount.get(key) - 1);
            } else if (this.wordCount.containsKey(key) && this.wordCount.get(key) < 1) {
                this.wordCount.remove(key);
            }
        } catch (Exception e) {
            System.out.println("Unable to decrement count for key: " + key);
        }
        return completedFuture(null);
    }

    public CompletableFuture<Void> print()
    {
        try {
            System.out.println("Current Word Count: ");
            this.wordCount.forEach( (k, v) -> System.out.println(k + ": " + v));
        } catch (Exception e) {
            System.out.println("Unable to print Word Count.");
        }

        return completedFuture(null);
    }
}
