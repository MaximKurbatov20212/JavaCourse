package Reporter;

public class ErrorReporter {
    private static final int MAX_MESSAGES = 20;
    private static int nMessages;

    public static boolean report(String message) {
        nMessages++;
        System.err.println(message);
        if (nMessages >= MAX_MESSAGES) {
            System.err.println("Too many errors, stopped...");
            return false;
        }
        return true;
    }
}