package Parser;

import Lexer.Token;
import Reporter.ErrorReporter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static Lexer.TokenType.*;

public class Parser {
    private static  List<Token> tokens;
    private static int curPos = 0;

    public Parser(List<Token> tokens) {
        reset();
        Parser.tokens = tokens;
    }

    public static Expr parse(List<Token> tokens) {
        reset();
        Parser.tokens = tokens;

        try {
            Expr expr = parseBDict();
            return curPos == tokens.size() - 1 ? expr : null;
        }

        catch (UnexpectedTokenException e) {
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
                    ErrorReporter.report("Invalid entry format");
                    return null;
                }
            }
            case EOF -> {
                return null;
            }
            default -> throw new UnexpectedTokenException(token);
        }
    }

    private static Expr parseBDict() throws BEntryNotFoundException, UnexpectedTokenException {
        Token token = consume();
        if(token.type() != START_DICT) throw new UnexpectedTokenException(token);
        List<Expr.BEntry> entries = new ArrayList<>();

        while(!isEndToken()) {
            entries.add((Expr.BEntry)parseBEntry());
        }

        entries.sort((o1, o2) -> o1.left().value().compareTo(o2.left().value()));

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
        return tokens.get(curPos++);
    }

    private static Token peek() {
        return tokens.get(curPos);
    }

    private static boolean isEndToken() {
        return peek().type() == END_BRACKET ;
    }

    private static Expr parseBEntry() throws BEntryNotFoundException, UnexpectedTokenException {
        Expr.BString left = parseString();
        Expr right = parseExpr();

        if(parseBEntryError(left, right)) {
            throw new BEntryNotFoundException();
        }
        return new Expr.BEntry(left, right);
    }

    private static boolean parseBEntryError(Expr left, Expr right) {
        return (left == null || right == null);
    }

    private static Expr.BString parseString() throws UnexpectedTokenException {
        Token token = consume();
        if (token.type() != STR) throw new UnexpectedTokenException(token);
        return new Expr.BString(token.value());
    }

    private static Expr parsePrimary() throws UnexpectedTokenException {
        Token token = consume();
        switch (token.type()) {
            case EOL, EOF -> {
                return null;
            }
            case STR -> {
                return new Expr.BString(token.value());
            }
            case NUM -> {
                return new Expr.BNumber(Integer.parseInt(token.value()));
            }
            default -> throw new UnexpectedTokenException(token);
        }
    }

    public static void reset() {
        curPos = 0;
        tokens = null;
    }
}
