package Parser;

import Lexer.Token;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import static Lexer.TokenType.*;

public class Parser {
    private final List<Token> tokens;
//    private int curPos;
    private ListIterator<Token> it;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        it = tokens.listIterator();
    }

    public Expr parse() {
        if (tokens.get(0).type() != START_DICT) return null;
        try {
            return parseBDict();
        }
        catch (UnexpectedTokenException e) {
            System.out.println("Unexpected Token");
            return null;
        }
    }

    private Expr parseExpr() throws UnexpectedTokenException {
        Token token = consume();
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
                    throw new UnexpectedTokenException();
                }
            }
            case EOF -> {
                return null;
            }
            default -> {
                throw new AssertionError();
            }
        }
    }

    private Expr parseBDict() throws BEntryNotFoundException, UnexpectedTokenException {
        if(consume().type() != START_DICT) throw new UnexpectedTokenException();
        List<Expr.BEntry> entries = new ArrayList<>();

        while(!isEndToken()) {
            entries.add((Expr.BEntry)parseBEntry());
        }
        return new Expr.BDict(entries);
    }

    private Expr parseBList() throws UnexpectedTokenException {
        if(consume().type() != START_LIST) throw new UnexpectedTokenException();
        List<Expr> expressions = new ArrayList<>();

        while(!isEndToken()) {
            expressions.add(parseExpr());
        }
        return new Expr.BList(expressions);
    }

    private Token consume() {
        return it.next();
    }

    private boolean isEndToken() {
        return consume().type() == END_BRACKET ;
    }

    private Expr parseBEntry() throws BEntryNotFoundException, UnexpectedTokenException {
        Expr.BString left = parseString();
        Expr right = parseExpr();
        if(parseBEntryError(left, right)) {
            throw new BEntryNotFoundException();
        }
        return new Expr.BEntry(left, right);
    }

    private boolean parseBEntryError(Expr left, Expr right) {
        return (left == null || right == null);
    }

    private Expr.BString parseString() throws UnexpectedTokenException {
        Token token = consume();
        if (token.type() != STR) throw new UnexpectedTokenException();
        return new Expr.BString(token.value());
    }

    private Expr parsePrimary() throws UnexpectedTokenException {
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
            default -> throw new UnexpectedTokenException();
        }
    }
}
