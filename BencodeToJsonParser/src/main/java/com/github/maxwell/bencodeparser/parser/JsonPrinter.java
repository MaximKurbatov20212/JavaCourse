package com.github.maxwell.bencodeparser.parser;

import java.util.List;

public class JsonPrinter {
    private final int shift = 4;

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
            result.append(printShifts(shifts + shift)).append(printBExpr(list.get(i),shifts + shift)).append((i == list.size() - 1) ? "\n" : ",\n");
        }
        return result.append(printShifts(shifts)).append("]").toString();
    }

    private String printBDict(Expr.BDict bDict, int shifts) {
        List<Expr.BEntry> list = bDict.list();
        if (list.size() == 0) return "{\n" + printShifts(shifts) + "\n}";

        StringBuilder result = new StringBuilder("{\n");
        for (int i = 0; i < list.size(); i++) {
            result.append(printShifts(shifts + shift)).append(printBEntry(list.get(i), shifts + shift)).append((i == list.size() - 1) ? "\n" : ",\n");
        }
        return result.append(printShifts(shifts)).append("}").toString();
    }

    private String printBString(Expr.BString value, int shifts) {
        return "\"" + value.value() + "\"";
    }

    private String printBEntry(Expr.BEntry entry, int shifts) {
        return printBString(entry.left(), shifts) + " : " + printBExpr(entry.right(), shifts);
    }

    private String printBNumber(Expr.BNumber value, int shift)  {
        return  String.valueOf(value.value());
    }

    private String printBExpr(Expr expr, int shifts) {
        if(expr instanceof Expr.BString) {
            return printBString((Expr.BString) expr, shifts);
        }
        else if(expr instanceof Expr.BNumber) {
            return printBNumber((Expr.BNumber) expr, shifts);
        }
        else if(expr instanceof Expr.BList) {
            return printBList((Expr.BList) expr, shifts);
        }
        else if(expr instanceof  Expr.BDict) {
            return printBDict((Expr.BDict) expr, shifts);
        }
        else if(expr instanceof  Expr.BEntry) {
            return printBEntry((Expr.BEntry) expr, shifts);
        }
        throw new RuntimeException("Invalid type. Type is not a successor of Expr");
    }

    private static String printShifts(int shifts) {
        return " ".repeat(shifts);
    }

}
