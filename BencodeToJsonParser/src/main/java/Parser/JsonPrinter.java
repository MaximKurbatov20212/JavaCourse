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

        if(string == null) return null;

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
        switch (expr) {
            case Expr.BString bString -> printBString(bString);
            case Expr.BNumber bNumber -> printBNumber(bNumber);
            case Expr.BDict bDict -> printBDict(bDict);
            case Expr.BList bList -> printBList(bList);
            case null, default -> throw new RuntimeException();
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
