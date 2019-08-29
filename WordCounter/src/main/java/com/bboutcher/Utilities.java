package com.bboutcher;

import java.awt.GraphicsEnvironment;
import java.io.Console;
import java.util.Scanner;

final class Utilities {
    private static String os = System.getProperty("os.name");
    private static Scanner input = null;

    static void openConsole() {
        Console console = System.console();

        try {
            if (console == null && !GraphicsEnvironment.isHeadless()) {
                if (os.contains("Windows")) {
                    String filename = Main.class.getProtectionDomain().getCodeSource().getLocation().toString().substring(6);
                    Runtime.getRuntime().exec(new String[]{"cmd", "/c", "start", "cmd", "/k", "java -jar \"" + filename + "\""});
                } else {
                    String[] args = new String[] {"/bin/bash", "-c", "your_command", "with", "args"};
                    new ProcessBuilder(args).start();
                }
            } else {
                System.out.println("Console is already open.");
            }
        } catch (Exception i) {
            System.out.println("Could not open console due to an error.");
        }
    }

    static String getStringInput(String message) {
        input = new Scanner(System.in);
        System.out.println(message);
        System.out.print("> ");
        return input.next();
    }

    static int getIntegerInput(String message) {
        input = new Scanner(System.in);
        System.out.println(message);
        System.out.print("> ");
        return input.nextInt();
    }

    static Scanner getArrayInput(String message) {
        input = new Scanner(System.in);
        System.out.println(message);
        System.out.print("> ");
        return input;
    }
}
