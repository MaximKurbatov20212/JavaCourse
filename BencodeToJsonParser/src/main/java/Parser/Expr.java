package Parser;

import java.util.List;

sealed public interface Expr {
    String toString(int shifts);
    int shift = 4;

    record BString(String value) implements Expr {
        public String toString(int shifts) {
            return "\"" + value + "\"";
        }
    }

    record BNumber(int value) implements Expr {
        public String toString(int shifts) {
            return  String.valueOf(value);
        }
    }

    record BEntry(BString left, Expr right) implements Expr {
        public String toString(int shifts) {
            return left.toString(shifts) + " : " + right.toString(shifts);
        }
    }

    record BList(List<Expr> list) implements Expr {
        public String toString(int shifts) {
            if(list.size() == 0) return "[\n" + Expr.printShifts(shifts) +  "]";

            StringBuilder result = new StringBuilder("[\n");
            for(int i = 0; i < list.size(); i++) {
                result.append(printShifts(shifts + shift)).append(list.get(i).toString(shifts + shift)).append((i == list.size() - 1) ? "\n" : ",\n");
            }
            return result.append(printShifts(shifts)).append("]").toString();
        }
    }

    record BDict(List<BEntry> list) implements Expr {
        public String toString(int shifts) {
            if (list.size() == 0) return "{\n" + Expr.printShifts(shifts) + "\n}";

            StringBuilder result = new StringBuilder("{\n");
            for (int i = 0; i < list.size(); i++) {
                result.append(printShifts(shifts + shift)).append(list.get(i).toString(shifts + shift)).append((i == list.size() - 1) ? "\n" : ",\n");
            }
            return result.append(printShifts(shifts)).append("}").toString();
        }
    }

    private static String printShifts(int shifts) {
        return " ".repeat(shifts);
    }
}