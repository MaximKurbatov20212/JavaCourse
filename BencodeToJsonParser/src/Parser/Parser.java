package Parser;

import Lexer.Token;
import java.util.List;
import static Lexer.TokenType.*;

public class Parser {
    private List<Token> tokens;
    private int curPos;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public Expr parse() {
        if(tokens.get(0).type() != START_DICT) {
            return null;
        }
        Expr expr =  parseDict();
        return expr;
    }

    private Expr parseExpr() {
        Token token = tokens.get(curPos);
        switch (token.type()) {
            case ARR, NUM -> {
                return parsePrimary();
            }

            case START_LIST -> {
                return parseList();
            }
            case START_DICT -> {
                return parseDict();
            }

            case EOF ->  {
                return null;
            }

            default -> {
                throw new AssertionError();
            }
        }
    }

    private Expr parseDict() {
        curPos++;
        BDict bDict = new BDict();
        while (true) {
            Expr expr = parsePair();
            if(expr == null) return bDict;
            bDict.add(expr);
        }
    }

    private Expr parseList() {
        curPos++;
        BList bList = new BList();
        while (true) {
            Token token = tokens.get(curPos);
            switch (token.type()) {
                case END_LIST -> {
                    curPos++;
                    return bList;
                }

                default -> {
                    bList.add(parseExpr());
                }
            }
        }
    }

    private Expr parsePair() throws PairNotFoundException {
        Array left = parseArray();
        Expr right = parsePrimary();
        if(left == null || right == null) return null;
        return new Pair(left, right);
    }

    private Array parseArray() {
        Token token = tokens.get(curPos++);
        if(token.type() == EOF) return null;
        if(token.type() != ARR) throw new RuntimeException();
        return new Array(token.value());
    }

    private Expr parsePrimary() {
        Token token = tokens.get(curPos++);
        if(token.type() == EOF) return null;
        if(token.type() == ARR) {
            return new Array(token.value());
        }
        return new Number(Integer.parseInt(token.value()));
    }
}
