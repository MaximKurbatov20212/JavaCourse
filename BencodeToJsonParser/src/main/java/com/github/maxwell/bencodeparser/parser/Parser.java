package com.github.maxwell.bencodeparser.parser;

import com.github.maxwell.bencodeparser.lexer.Token;
import com.github.maxwell.bencodeparser.reporter.ErrorReporter;

import java.util.ArrayList;
import java.util.List;

import static com.github.maxwell.bencodeparser.lexer.TokenType.*;

public class Parser {
    private final List<Token> tokens;
    private int curPos = 0;
    private final ErrorReporter errorReporter = new ErrorReporter();

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public static Expr parse(List<Token> tokens) {
        if(tokens == null) return null;

        Parser parser = new Parser(tokens);
        return parser.parse();
    }

    private Expr parse() {
        try {
            Expr expr = parseBDict();
            if(hasExtraTokens()) {
                errorReporter.report("Unexpected tokens at the end: " + extraTokens() + "\n");
                return null;
            }
            return errorReporter.hasErrors() ? null : expr;
        }
        catch (UnexpectedTokenException e) {
            errorReporter.report(unexpectedToken(e));
            return null;
        }
    }

    private String extraTokens() {
        return tokens.stream()
                .skip(curPos)
                .map(Token::type)
                .toList()
                .toString();
    }

    private boolean hasExtraTokens() {
        return curPos !=  tokens.size() - 1;
    }

    String unexpectedToken(UnexpectedTokenException e) {
        return "line " + e.getToken().line() + ", " + "position " + e.getToken().pos() + " unexpected token:\n"
                + "expected " + e.getExpectation() + ", got " + e.getToken().type().toString() + "\n";
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
        int start = curPos;

        while(!isEndToken()) {
            entries.add(parseBEntry());
        }

        checkEntriesOrder(entries, start);

        consume();
        return new Expr.BDict(entries);
    }

    private void checkEntriesOrder(List<Expr.BEntry> entries, int start) {
        for(int i = 1; i < entries.size(); i++) {
            if((entries.get(i - 1).left().value()).compareTo(entries.get(i).left().value()) > 0) {
                errorReporter.report("line: " + tokens.get(start + 2 * i).line() + ", position: " + tokens.get(start + 2 * i).pos()
                        + ", unexpected token: " + tokens.get(start + 2 * i).toString() + "\n"
                        + "Because previous key: " + tokens.get(start + 2 * (i - 1)) + " is bigger than current\n"
                );
            }
        }
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

    private Expr.BEntry parseBEntry() {
        return new Expr.BEntry(parseString(), parseExpr());
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
