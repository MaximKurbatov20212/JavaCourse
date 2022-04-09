package Parser;

import java.util.ArrayList;

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

abstract class Expr {}

class Array extends Expr {
    private String value;
    Array(String value) {
        this.value = value;
    }
}

class Number extends Expr {
    private int value;
    Number(int value) {
        this.value = value;
    }
}

class Pair extends Expr {
    private Array left;
    private Expr right;
    Pair(Array left, Expr right) {
        this.left = left;
        this.right = right;
    }
}

class BList extends Expr {
    private ArrayList<Expr> list = new ArrayList<Expr>();
    void add(Expr expr) {
        list.add(expr);
    }
}

class BDict extends Expr {
    private ArrayList<Expr> list = new ArrayList<Expr>();

    void add(Expr pair) {
        list.add(pair);
    }
}
