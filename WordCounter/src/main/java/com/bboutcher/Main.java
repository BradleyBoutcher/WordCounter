package com.bboutcher;

import com.ea.async.Async;

public class Main {

    public static void main(String[] args) {
        Utilities.openConsole();
        Async.init();
        CounterManager.start();
    }
}
