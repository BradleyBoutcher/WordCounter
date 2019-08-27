package com.bboutcher;

import com.ea.async.Async;

public class Main {

    public static void main(String[] args) throws Exception {
        Async.init();
        CounterManager.start();
    }
}
