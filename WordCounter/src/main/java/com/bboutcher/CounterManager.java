package com.bboutcher;

import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Class for creating, storing, and accessing multiple
 * instances of WordCounter objects
 */
public final class CounterManager {
    private static HashMap<String, WordCounter> wordCounters = new HashMap<>();
    // Do not store the WordCounter unless specified by user
    private static WordCounter temporary = null;
    private static Logger log;

    // Private prevents instantiation by any callers
    private CounterManager() {}

    static  WordCounter createWordCounter() {
        return new WordCounter();
    }

    // Store a given word counter with an associated key
    static void saveWordCounter(String key, WordCounter counter) throws Exception {
        if (counter == null) {
            log.fine("Cannot save null WordCounter.");
        }
        try {
            wordCounters.put(key, counter);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
    }

    // Access a word counter with a given key
    static WordCounter accessWordCounter(String key) throws Exception {
        if (key.isEmpty() || key == null) {
            log.fine("Lookup key for WordCounter is empty.");
        }
        WordCounter w = null;
        try {
            w = wordCounters.get(key);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
        return w;
    }
}
