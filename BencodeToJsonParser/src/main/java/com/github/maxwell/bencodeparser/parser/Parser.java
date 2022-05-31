package com.github.maxwell.bencodeparser.parser;

import com.github.maxwell.bencodeparser.lexer.Token;
import com.github.maxwell.bencodeparser.reporter.ErrorReporter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.github.maxwell.bencodeparser.lexer.TokenType.*;

public class Parser {
    private final List<Token> tokens;
    private int curPos = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public static Expr parse(List<Token> tokens) {
        Parser parser = new Parser(tokens);
        try {
            Expr expr = parser.parseBDict();
            if(hasExtraTokens(parser, tokens)) {
                ErrorReporter.report("Unexpected tokens at the end: " + extraTokens(parser, tokens));
                return null;
            }
            return parser.curPos == tokens.size() - 1 ? expr : null;
        }
        catch (UnexpectedTokenException e) {
            ErrorReporter.report("Unexpected token: " + parser.unexpectedToken(e));
            return null;
        }
    }

    private static String extraTokens(Parser parser, List<Token> tokens) {
        StringBuilder extraTokens = new StringBuilder();
        for(int i = parser.curPos; i < tokens.size(); i++) {
            extraTokens.append(tokens.get(i).type().toString()).append(i == tokens.size() - 1 ? "" : ", ");
        }
        return extraTokens.toString();
    }

    private static boolean hasExtraTokens(Parser parser, List<Token> tokens) {
        return parser.curPos !=  tokens.size() - 1;
    }

    String unexpectedToken(UnexpectedTokenException e) {
        return tokens.toString() + e.getToken().toString() + "\n" + " ".repeat(tokens.toString().length() +
               "Unexpected token: ".length()) + "^" + "_".repeat(e.toString().length() - 2) + "^" + " here\n"
                + "Expected: " + e.getExpectation() + ", got: " + e.getToken().type().toString() + "\n";
    }

    private Expr parseExpr() throws UnexpectedTokenException {
        Token token = peek();
        switch (token.type()) {
            case STR, NUM -> {
                return parsePrimary();
            }
            case START_LIST -> {
                return parseBList();
            }
            case START_DICT -> {
                try {
                    return parseBDict();
                }
                catch (BEntryNotFoundException e) {
                    return null;
                }
            }
            default -> throw new UnexpectedTokenException(token, "STR, NUM, START_DICT or START_LIST");
        }
    }

    private Expr parseBDict() throws BEntryNotFoundException, UnexpectedTokenException {
        Token token = consume();
        if(token.type() != START_DICT) throw new UnexpectedTokenException(token, "START_DICT");
        List<Expr.BEntry> entries = new ArrayList<>();

        while(!isEndToken()) {
            entries.add(parseBEntry());
        }

        entries.sort(Comparator.comparing(o -> o.left().value()));

        consume();
        return new Expr.BDict(entries);
    }

    private Expr parseBList() throws UnexpectedTokenException {
        Token token = consume();
        if(token.type() != START_LIST) throw new UnexpectedTokenException(token, "START_LIST");
        List<Expr> expressions = new ArrayList<>();

        while(!isEndToken()) {
            expressions.add(parseExpr());
        }
        consume();
        return new Expr.BList(expressions);
    }

    private Token consume() throws UnexpectedTokenException {
        if(curPos == tokens.size()) {
            throw new UnexpectedTokenException(tokens.get(curPos - 1), "END_ELEMENT");
        }
        return tokens.get(curPos++);
    }

    private Token peek() throws UnexpectedTokenException {
        if(curPos == tokens.size()) {
            throw new UnexpectedTokenException(tokens.get(curPos - 1), "END_ELEMENT");
        }
        return tokens.get(curPos);
    }

    private boolean isEndToken() throws UnexpectedTokenException {
        return peek().type() == END_ELEMENT;
    }

    private Expr.BEntry parseBEntry() throws BEntryNotFoundException, UnexpectedTokenException {
        Expr.BString left = parseString();
        // CR: why parse value if key is not found?
        // If key is not found then throws Exception
        Expr right = parseExpr();

        if(right == null) {
            throw new BEntryNotFoundException();
        }

        return new Expr.BEntry(left, right);
    }

    private Expr.BString parseString() throws UnexpectedTokenException {
        Token token = consume();
        if (token.type() != STR) throw new UnexpectedTokenException(token, "STR");
        return new Expr.BString(token.value());
    }

    private Expr parsePrimary() throws UnexpectedTokenException {
        Token token = consume();
        switch (token.type()) {
            case STR -> {
                return new Expr.BString(token.value());
            }
            case NUM -> {
                return new Expr.BNumber(Integer.parseInt(token.value()));
            }
            default -> throw new UnexpectedTokenException(token, "STR or NUM");
        }
    }
}
