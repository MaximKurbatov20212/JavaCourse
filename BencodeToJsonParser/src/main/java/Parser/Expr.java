package Parser;

import java.util.List;

sealed public interface Expr {

    record BString(String value) implements Expr {
        @Override
        public String toString() {
            return "\"" + value + "\"";
        }
    }

    record BNumber(int value) implements Expr {
        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    record BEntry(BString left, Expr right) implements Expr {
        @Override
        public String toString() {
            return left.toString() + " : " + right.toString();
        }
    }

    record BList(List<Expr> list) implements Expr {
        @Override
        public String toString() {
            JsonPrinter.curNesting++;

            StringBuilder result = new StringBuilder("[\n");
            for(Expr expr : list) {
                result.append(JsonPrinter.printShifts()).append(expr.toString()).append(",\n");
            }

            deleteLastComma(result);
            JsonPrinter.curNesting--;
            appendClosingBracket(result, "]");
            return result.toString();
        }
    }

    record BDict(List<BEntry> list) implements Expr {
        @Override
        public String toString() {
            StringBuilder result = new StringBuilder("{\n");
            JsonPrinter.curNesting++;

            for(BEntry bentry : list) {
                result.append(JsonPrinter.printShifts()).append(bentry.toString()).append(",\n");
            }

            deleteLastComma(result);
            JsonPrinter.curNesting--;
            appendClosingBracket(result, "}");
            return result.toString();
        }
    }

    private static void appendClosingBracket(StringBuilder string, String s) {
        string.append("\n").append(JsonPrinter.printShifts()).append(s);
    }

    private static void deleteLastComma(StringBuilder result) {
        if(!result.substring(result.length() - 2, result.length()).equals(",\n")) return; // if list is empty
        result.delete(result.length() - 2, result.length()); // delete last ",\n"
    }
}