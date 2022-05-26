package parser;

public class JsonPrinter {
    static Expr expr;
    static final StringBuilder result = new StringBuilder();

    public JsonPrinter(Expr expr) {
        JsonPrinter.expr = expr;
    }

    // CR: use the same pattern as in Lexer
    public static String print(Expr string) {
        if(string == null) return null;

        JsonPrinter printer = new JsonPrinter(string);
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
