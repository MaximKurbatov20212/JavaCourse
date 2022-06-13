package com.github.maxwell.bencodeparser.printer;

import com.github.maxwell.bencodeparser.parser.Expr;

import java.util.List;

public class JsonPrinter {
    private static final int SHIFT = 4;

    private JsonPrinter() {}

    public static String print(Expr string) {
        if(string == null) return null;
        JsonPrinter printer = new JsonPrinter();
        return printer.printBExpr(string, 0);
    }

    private String printBList(Expr.BList bList, int shifts) {
        List<Expr> list = bList.list();
        if(list.size() == 0) return "[\n" + printShifts(shifts) +  "]";

        StringBuilder result = new StringBuilder("[\n");
        for(int i = 0; i < list.size(); i++) {
            result.append(printShifts(shifts + SHIFT))
                    .append(printBExpr(list.get(i),shifts + SHIFT))
                    .append((i == list.size() - 1) ? "\n" : ",\n");
        }
        return result.append(printShifts(shifts)).append("]").toString();
    }

    private String printBDict(Expr.BDict bDict, int shifts) {
        List<Expr.BEntry> list = bDict.list();
        if (list.size() == 0) return "{\n" + printShifts(shifts) + "\n}";

        StringBuilder result = new StringBuilder("{\n");
        for (int i = 0; i < list.size(); i++) {
            result.append(printShifts(shifts + SHIFT))
                    .append(printBEntry(list.get(i), shifts + SHIFT))
                    .append((i == list.size() - 1) ? "\n" : ",\n");
        }
        return result.append(printShifts(shifts)).append("}").toString();
    }

    private String printBString(Expr.BString value) {
        return "\"" + value.value() + "\"";
    }

    private String printBEntry(Expr.BEntry entry, int shifts) {
        return printBString(entry.key()) + " : " + printBExpr(entry.value(), shifts);
    }

    private String printBNumber(Expr.BNumber value)  {
        return  String.valueOf(value.value());
    }

    private String printBExpr(Expr expr, int shifts) {
        return switch (expr) {
            case Expr.BString bString -> printBString(bString);
            case Expr.BNumber bNumber -> printBNumber(bNumber);
            case Expr.BEntry bEntry -> printBEntry(bEntry, shifts);
            case Expr.BDict bDict -> printBDict(bDict, shifts);
            case Expr.BList bList -> printBList(bList, shifts);
        };
    }

    private static String printShifts(int shifts) {
        return " ".repeat(shifts);
    }

}
