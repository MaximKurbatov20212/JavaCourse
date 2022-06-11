package com.github.maxwell.bencodeparser.reporter;

public class ErrorReporter {
    private static final int MAX_MESSAGES = 20;
    private int nMessages;

    public void report(String message) {
        nMessages++;
        System.err.println(message);
        if (nMessages >= MAX_MESSAGES) {
            System.err.println("Too many errors, stopped...");
        }
    }

    public boolean tooManyErrors() {
        return nMessages >= MAX_MESSAGES;
    }
    public boolean hasErrors() {
        return nMessages > 0;
    }
}