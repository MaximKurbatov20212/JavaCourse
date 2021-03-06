package com.github.maxwell.bencodeparser;

import com.github.maxwell.bencodeparser.lexer.Token;
import com.github.maxwell.bencodeparser.lexer.TokenType;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import com.github.maxwell.bencodeparser.parser.Expr;
import com.github.maxwell.bencodeparser.parser.Expr.*;
import com.github.maxwell.bencodeparser.parser.Parser;

import static com.github.maxwell.bencodeparser.lexer.TokenType.*;

public class ParserTest {

    @Test
    public void empty() {
        Expr expr = parse(tokens());
        Assertions.assertNull(expr);
    }

    @Test
    public void emptyDict() {
        Expr expr = parse(tokens(new TokenInfo(START_DICT, ""), new TokenInfo(END_ELEMENT, "")));
        Assertions.assertNotNull(expr);
    }

    @Test
    public void dict() {
        Expr expr = parse(tokens(new TokenInfo(START_DICT, "{"), new TokenInfo(STR, "hello"), new TokenInfo(STR, "maxwell"), new TokenInfo(END_ELEMENT, "")));
        List<BEntry> list = new ArrayList<>();
        list.add(new BEntry(new BString("hello"), new BString("maxwell")));
        Assertions.assertEquals(new BDict(list), expr);
    }

    @Test
    public void list() {
        Expr expr = parse(tokens(new TokenInfo(START_DICT, "{"), new TokenInfo(STR, "hello"), new TokenInfo(START_LIST, "{"), new TokenInfo(STR, "maxwell"),  new TokenInfo(END_ELEMENT, ""), new TokenInfo(END_ELEMENT, "")));

        List<BEntry> listEntries = new ArrayList<>();
        List<Expr> listExpr = new ArrayList<>();

        listExpr.add(new BString("maxwell"));
        listEntries.add(new BEntry(new BString("hello"), new BList(listExpr)));

        Assertions.assertEquals(new BDict(listEntries), expr);
    }

    @Test
    public void emptyList() {
        Expr expr = parse(tokens(new TokenInfo(START_DICT, "{"), new TokenInfo(STR, "hello"), new TokenInfo(START_LIST, "{"),  new TokenInfo(END_ELEMENT, ""), new TokenInfo(END_ELEMENT, "")));

        List<BEntry> listEntries = new ArrayList<>();
        List<Expr> listExpr = new ArrayList<>();

        listEntries.add(new BEntry(new BString("hello"), new BList(listExpr)));

        Assertions.assertEquals(new BDict(listEntries), expr);
    }

    @Test
    public void tooManyEndBrackets() {
        Expr expr = parse(tokens(new TokenInfo(START_DICT, "{"), new TokenInfo(STR, "hello"), new TokenInfo(START_LIST, "{"), new TokenInfo(STR, "maxwell"),  new TokenInfo(END_ELEMENT, ""), new TokenInfo(END_ELEMENT, ""),  new TokenInfo(END_ELEMENT, "")));
        Assertions.assertNull(expr);

        expr = parse(tokens(new TokenInfo(START_DICT, "{"), new TokenInfo(END_ELEMENT, ""), new TokenInfo(STR, "hello"), new TokenInfo(START_LIST, "{"), new TokenInfo(STR, "maxwell"),  new TokenInfo(END_ELEMENT, ""), new TokenInfo(END_ELEMENT, ""),  new TokenInfo(END_ELEMENT, "")));
        Assertions.assertNull(expr);
    }

    @Test
    public void invalidBEntryFormat() {
        Expr expr = parse(tokens(new TokenInfo(START_DICT, "{"), new TokenInfo(NUM, "1234"), new TokenInfo(STR, "hello"),new TokenInfo(END_ELEMENT, "")));
        Assertions.assertNull(expr);

        expr = parse(tokens(new TokenInfo(START_DICT, "{"), new TokenInfo(STR, "hello"), new TokenInfo(END_ELEMENT, "")));
        Assertions.assertNull(expr);

        expr = parse(tokens(new TokenInfo(START_DICT, "{"), new TokenInfo(START_LIST, ""), new TokenInfo(END_ELEMENT, ""), new TokenInfo(STR, "hello"), new TokenInfo(END_ELEMENT, "")));
        Assertions.assertNull(expr);

        expr = parse(tokens(new TokenInfo(START_DICT, "{"), new TokenInfo(START_DICT, ""), new TokenInfo(END_ELEMENT, ""), new TokenInfo(STR, "hello"), new TokenInfo(END_ELEMENT, "")));
        Assertions.assertNull(expr);
    }

    @Test
    public void innerDict() {
        Expr expr = parse(tokens(new TokenInfo(START_DICT, "{"), new TokenInfo(STR, "inner1"),
                new TokenInfo(START_DICT, "{"), new TokenInfo(STR, "inner2"),
                new TokenInfo(START_DICT, "{"), new TokenInfo(STR, "inner3"), new TokenInfo(STR, "end"),
                new TokenInfo(END_ELEMENT, ""), new TokenInfo(END_ELEMENT, ""), new TokenInfo(END_ELEMENT, "")));

        List<BEntry> listEntriesInner1 = new ArrayList<>();
        List<BEntry> listEntriesInner2 = new ArrayList<>();
        List<BEntry> listEntriesInner3 = new ArrayList<>();
        listEntriesInner3.add(new BEntry(new BString("inner3"), new BString("end")));
        listEntriesInner2.add(new BEntry(new BString("inner2"), new BDict(listEntriesInner3)));
        listEntriesInner1.add(new BEntry(new BString("inner1"), new BDict(listEntriesInner2)));

        Assertions.assertEquals(new BDict(listEntriesInner1), expr);
    }


        private static List<Token> tokens(TokenInfo... tokenInfos) {
        List<Token> tokens = new ArrayList<>();
        int i = 0;

        for (TokenInfo tokenInfo : tokenInfos) {
            tokens.add(new Token(tokenInfo.type, (String) tokenInfo.value, 1, i++));
        }

        tokens.add(new Token(EOF, "", 1, i));
        return tokens;
    }

    private static Expr parse(List<Token> tokens) {
        return Parser.parse(tokens);
    }

    private record TokenInfo(TokenType type, Object value) {}
}
