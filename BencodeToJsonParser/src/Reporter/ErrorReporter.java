package Reporter;

public class ErrorReporter {
    private static final int MAX_MESSAGES = 20;
    private int nMessages;

    public boolean report(String message) {
        nMessages++;
        System.err.println(message);
        if (nMessages >= MAX_MESSAGES) {
            System.err.println("Too many errors, stopped...");
            return false;
        }
        return true;
    }
}