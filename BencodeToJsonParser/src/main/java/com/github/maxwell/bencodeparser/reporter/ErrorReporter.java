package com.github.maxwell.bencodeparser.reporter;

public class ErrorReporter {
    private final int MAX_MESSAGES = 20;
    private int nMessages;
    private boolean canPrint = true;

    public void report(String message) {
        nMessages++;
        System.err.println(message);
        if (nMessages >= MAX_MESSAGES) {
            System.err.println("Too many errors, stopped...");
            canPrint = false;
        }
    }

    public boolean tooManyErrors() {
        return !canPrint;
    }
    public boolean hasErrors() {
        return nMessages > 0;
    }
}