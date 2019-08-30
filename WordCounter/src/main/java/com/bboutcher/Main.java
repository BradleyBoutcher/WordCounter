package com.bboutcher;

import com.ea.async.Async;

public class Main {

    public static void main(String[] args) {
        Utilities.openConsole();   // Attempt to open Console if not already open
        Async.init();              // Required: Initiate EA Async
        CounterManager.start();
        System.exit(0);     // Make sure the process and all associated threads are terminated.
    }
}
