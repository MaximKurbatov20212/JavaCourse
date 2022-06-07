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
        return parser.parse();
    }

    private Expr parse() {
        try {
            Expr expr = parseBDict();
            if(hasExtraTokens()) {
                ErrorReporter.report("Unexpected tokens at the end: " + extraTokens());
                return null;
            }
            return curPos == tokens.size() - 1 ? expr : null;
        }
        catch (UnexpectedTokenException e) {
            ErrorReporter.report("Unexpected token: " + unexpectedToken(e));
            return null;
        }
    }

    private String extraTokens() {
        // CR: use Stream instead
        StringBuilder extraTokens = new StringBuilder();
        for(int i = curPos; i < tokens.size(); i++) {
            extraTokens.append(tokens.get(i).type().toString()).append(i == tokens.size() - 1 ? "" : ", ");
        }
        return extraTokens.toString();
    }

    private boolean hasExtraTokens() {
        return curPos !=  tokens.size() - 1;
    }

    String unexpectedToken(UnexpectedTokenException e) {
        // CR: it's better to pass token position in token and make reports like
        // CR: 'line 1, position 3: expected EOF, got STR'
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

    private Expr parseBDict() {
        Token token = consume();
        if(token.type() != START_DICT) throw new UnexpectedTokenException(token, "START_DICT");
        List<Expr.BEntry> entries = new ArrayList<>();

        while(!isEndToken()) {
            entries.add(parseBEntry());
        }

        // CR: you should not sort entries by yourself, you should check if they are in lexicographical order
        // CR: if not - report error
        entries.sort(Comparator.comparing(o -> o.left().value()));

        consume();
        return new Expr.BDict(entries);
    }

    private Expr parseBList() {
        Token token = consume();
        if(token.type() != START_LIST) throw new UnexpectedTokenException(token, "START_LIST");
        List<Expr> expressions = new ArrayList<>();

        while(!isEndToken()) {
            expressions.add(parseExpr());
        }
        consume();
        return new Expr.BList(expressions);
    }

    private Token consume() {
        if(curPos == tokens.size()) {
            throw new UnexpectedTokenException(tokens.get(curPos - 1), "END_ELEMENT");
        }
        return tokens.get(curPos++);
    }

    private Token peek() {
        if(curPos == tokens.size()) {
            throw new UnexpectedTokenException(tokens.get(curPos - 1), "END_ELEMENT");
        }
        return tokens.get(curPos);
    }

    private boolean isEndToken() {
        return peek().type() == END_ELEMENT;
    }

    // CR: probably can inline when 'if' will be removed
    private Expr.BEntry parseBEntry() {
        Expr.BString left = parseString();
        Expr right = parseExpr();

        // CR: seems that we never enter then branch
        if(right == null) {
            throw new BEntryNotFoundException();
        }

        return new Expr.BEntry(left, right);
    }

    private Expr.BString parseString() {
        Token token = consume();
        if (token.type() != STR) throw new UnexpectedTokenException(token, "STR");
        return new Expr.BString(token.value());
    }

    private Expr parsePrimary() {
        Token token = consume();
        return switch (token.type()) {
            case STR -> new Expr.BString(token.value());
            case NUM -> new Expr.BNumber(Integer.parseInt(token.value()));
            default -> throw new UnexpectedTokenException(token, "STR or NUM");
        };
    }
}
