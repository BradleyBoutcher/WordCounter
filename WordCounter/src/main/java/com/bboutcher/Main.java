package com.bboutcher;

import com.ea.async.Async;

public class Main {

    public static void main(String[] args) {
        Async.init();                   // Required: Initiate EA Async

        if (args.length < 1)            // No arguments - start in interactive mode
        {
            Utilities.openConsole();    // Attempt to open Console if not already open
            CounterManager.start();
        }
        else
        {                               // Arguments given - start in headless mode
            System.out.println("Parameters detected, running in headless mode...");
            System.out.println(args[0]);
            FileReader.HeadlessFileReader reader = new FileReader.HeadlessFileReader(args);
            reader.print();
        }

        System.exit(0);         // Make sure the process and all associated threads are terminated.
    }
}
