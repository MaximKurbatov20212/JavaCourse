package com.github.maxwell.bencodeparser.reporter;

public class ErrorReporter {
    private static final int MAX_MESSAGES = 20;
    private static int nMessages;

    public static void report(String message) {
        nMessages++;
        System.err.println(message);
        if (nMessages >= MAX_MESSAGES) {
            // CR: you don't stop printing actually
            System.err.println("Too many errors, stopped...");
        }
    }
}