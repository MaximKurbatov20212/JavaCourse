package Parser;

public class JsonPrinter {
    static Expr expr;
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
        result.append(bDict.toString(0));
    }

    private static void printBExpr(Expr expr) {
        switch (expr) {
            case Expr.BDict bDict -> printBDict(bDict);
            case null, default -> throw new RuntimeException();
        }
    }
}
