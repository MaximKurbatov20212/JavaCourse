package parser;

import lexer.Token;
import reporter.ErrorReporter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static lexer.TokenType.*;

public class Parser {
    private static List<Token> tokens;
    private static int curPos = 0;

    public Parser(List<Token> tokens) {
        Parser.tokens = tokens;
    }

    public static Expr parse(List<Token> tokens) {
        Parser parser = new Parser(tokens);
        try {
            Expr expr = parseBDict();
            // CR: also you need to check what kind of token you have and print error message if it is invalid
            return curPos == tokens.size() - 1 ? expr : null;
        }
        // CR: both exceptions should be checked or unchecked
        catch (UnexpectedTokenException e) {
            // CR: very uninformative message: where this token was, why is it unexpected...
            ErrorReporter.report("Unexpected token: " + e);
            return null;
        }
    }

    private static Expr parseExpr() throws UnexpectedTokenException {
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
            default -> throw new UnexpectedTokenException(token);
        }
    }

    private static Expr parseBDict() throws BEntryNotFoundException, UnexpectedTokenException {
        Token token = consume();
        if(token.type() != START_DICT) throw new UnexpectedTokenException(token);
        List<Expr.BEntry> entries = new ArrayList<>();

        while(!isEndToken()) {
            entries.add(parseBEntry());
        }

        entries.sort(Comparator.comparing(o -> o.left().value()));

        consume();
        return new Expr.BDict(entries);
    }

    private static Expr parseBList() throws UnexpectedTokenException {
        Token token = consume();
        if(token.type() != START_LIST) throw new UnexpectedTokenException(token);
        List<Expr> expressions = new ArrayList<>();

        while(!isEndToken()) {
            expressions.add(parseExpr());
        }
        consume();
        return new Expr.BList(expressions);
    }

    private static Token consume() {
        try {
            return tokens.get(curPos++);
        }
        catch (IndexOutOfBoundsException e) {

        }
    }

    private static Token peek() {
        return tokens.get(curPos);
    }

    private static boolean isEndToken() {
        return peek().type() == END_ELEMENT;
    }

    private static Expr.BEntry parseBEntry() throws BEntryNotFoundException, UnexpectedTokenException {
        Expr.BString left = parseString();
        // CR: why parse value if key is not found?
        // If key is not found then throws Exception
        Expr right = parseExpr();

        if(right == null) {
            throw new BEntryNotFoundException();
        }

        return new Expr.BEntry(left, right);
    }

    private static Expr.BString parseString() throws UnexpectedTokenException {
        Token token = consume();
        if (token.type() != STR) throw new UnexpectedTokenException(token);
        return new Expr.BString(token.value());
    }

    private static Expr parsePrimary() throws UnexpectedTokenException {
        Token token = consume();
        switch (token.type()) {
            case STR -> {
                return new Expr.BString(token.value());
            }
            case NUM -> {
                return new Expr.BNumber(Integer.parseInt(token.value()));
            }
            default -> throw new UnexpectedTokenException(token);
        }
    }
}
