package Parser;

public class JsonPrinter {
    static Expr expr;
    static int curNesting = 0;
    static final int shift = 4;
    static final StringBuilder result = new StringBuilder();

    public JsonPrinter(Expr expr) {
        JsonPrinter.expr = expr;
    }

    public static void print() {
        result.delete(0, result.length());
        printBExpr(expr);
    }

    public static String print(Expr string) {
        result.delete(0, result.length());

        if(string == null) return "{\n}";
        printBExpr(string);
        return result.toString();
    }

    private static void printBDict(Expr.BDict bDict) {
        result.append(bDict.toString());
    }

    private static void printBList(Expr.BList bList) {
        result.append(bList.toString());
    }

    private static void printBExpr(Expr expr) {
        if(expr instanceof Expr.BString) {
            printBString((Expr.BString) expr);
        }
        else if(expr instanceof Expr.BNumber) {
            printBNumber((Expr.BNumber) expr);
        }
        else if(expr instanceof Expr.BDict) {
            printBDict((Expr.BDict) expr);
        }
        else if(expr instanceof Expr.BList) {
            printBList((Expr.BList) expr);
        }
        else {
            throw new RuntimeException();
        }
    }

    private static void printBNumber(Expr.BNumber bNumber) {
        result.append(bNumber.toString());
    }

    private static void printBString(Expr.BString bString) {
        result.append("\"").append(bString.toString()).append("\"");
    }

    public static String printShifts() {
        return " ".repeat(curNesting * shift);
    }
}
