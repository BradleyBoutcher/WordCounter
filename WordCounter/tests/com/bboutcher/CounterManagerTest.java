package com.bboutcher;

import org.junit.Before;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CounterManagerTest {
    private WordCounter preExisting;
    private final String preExistingKey = "preExisting";

    @Before
    void setUp() throws Exception {
        this.preExisting = new WordCounter();

        CounterManager.saveWordCounter(preExistingKey, preExisting);
    }

    @Test
    void createWordCounter() {
        WordCounter counter = CounterManager.createWordCounter();
        assertNotNull(counter);
    }

    // Verify that the counter is the same when stored and accessed
    @Test
    void saveWordCounter() throws Exception {
        WordCounter counter = CounterManager.createWordCounter();
        String key = "test";
        CounterManager.saveWordCounter(key, counter);

        WordCounter verify = CounterManager.accessWordCounter(key);

        assertEquals(counter, verify);
    }

    @Test
    void accessWordCounter() throws Exception {
        WordCounter verify = CounterManager.accessWordCounter(this.preExistingKey);
        assertEquals(verify, this.preExisting);
    }
}