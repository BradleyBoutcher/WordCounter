package com.bboutcher;

import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class CounterManagerTest {
    private Object manager = CounterManager.class;

    private WordCounter preExisting;
    private final String preExistingKey = "preExisting";

    private Method create;
    private Method delete;
    private Method access;

    @Before
    void setUp() throws Exception {
        try {
            create = manager.getClass().getDeclaredMethod("createWordCounter", CounterManager.class);
            create.setAccessible(true);
            delete = manager.getClass().getDeclaredMethod("deleteWordCounter", CounterManager.class);
            delete.setAccessible(true);
            access = manager.getClass().getDeclaredMethod("accessWordCounter", CounterManager.class);
            access.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    void createWordCounter() throws IllegalAccessException, InvocationTargetException {
        try {
            create.invoke(manager.getClass());
        } catch (IllegalAccessException | InvocationTargetException i) {
            i.printStackTrace();
            throw i;
        }
    }

    // Verify that the counter is the same when stored and accessed
    @Test
    void saveWordCounter() throws Exception {

    }

    @Test
    void accessWordCounter() throws Exception {
    }
}
