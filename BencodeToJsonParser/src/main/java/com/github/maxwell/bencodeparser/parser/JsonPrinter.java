package com.github.maxwell.bencodeparser.parser;

public class JsonPrinter {
    private final StringBuilder result = new StringBuilder();

    public static String print(Expr string) {
        if(string == null) return null;
        JsonPrinter printer = new JsonPrinter();
        return printer.printBExpr(string);
    }

    private void printBDict(Expr.BDict bDict) {
        result.append(bDict.toString(0));
    }

    private String printBExpr(Expr expr) {
        switch (expr) {
            case Expr.BDict bDict -> printBDict(bDict);
            case null, default -> throw new RuntimeException();
        }
        return result.toString();
    }
}
