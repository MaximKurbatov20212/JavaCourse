package Parser;

import Lexer.Token;

import java.util.ArrayList;
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
            case STR, NUM -> {
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
        Parser.Expr.BDict bDict = new Parser.Expr.BDict();
        // CR: until d....e
        while (true) {
            Expr expr = parsePair();
            if(expr == null) return bDict;
            bDict.add(expr);
        }
    }

    private Expr parseList() {
        curPos++;
        Parser.Expr.BList bList = new Parser.Expr.BList();
        // CR: until l....e
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
        Parser.Expr.BString left = parseString();
        // CR: parse any expr
        Expr right = parsePrimary();
        if(left == null || right == null) return null;
        return new Expr.BEntry(left, right);
    }

    private Parser.Expr.BString parseString() {
        Token token = tokens.get(curPos++);
        if(token.type() == EOF) return null;
        if(token.type() != STR) throw new RuntimeException();
        return new Parser.Expr.BString(token.value());
    }

    private Expr parsePrimary() {
        Token token = tokens.get(curPos++);
        if(token.type() == EOF) return null;
        if(token.type() == STR) {
            return new Parser.Expr.BString(token.value());
        }
        return new Parser.Expr.BNumber(Integer.parseInt(token.value()));
    }
}
