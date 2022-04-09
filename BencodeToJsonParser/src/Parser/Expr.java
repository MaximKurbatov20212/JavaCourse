package Parser;

import java.util.ArrayList;
import java.util.List;

//  ParseToken:
//
//                            ParseRight(ParseToken)
//                          /
//  ParseDict -> parsePair
//                          \
//                            ParseLeft(ParseToken)
//
//  ParseList -> ParseToken
//  ParseArray -> can do it
//  ParseNumber -> can do it
//

sealed interface Expr {
    record BString(String value) implements Expr {}

    record BNumber(int value) implements Expr {}

    record BEntry(BString left, Expr right) implements Expr {}

    record BList(List<Expr> list) implements Expr {
        void add(Expr expr) {
            list.add(expr);
        }
    }

    record BDict(List<BEntry> list) implements Expr {
        void add(BEntry pair) {
            list.add(pair);
        }
    }
}

//class Parser {
//    List<Token> tokens;
//    int pos;
//
//    Expr parseList() {
//        consume(L);
//        List<Parser.Expr.BEntry> entries = new ArrayList<>();
//        while (isEndToken()) {
//            entries.add(parseEntry());
//        }
//        return new Expr.BDict(entries);
//    }
//}
