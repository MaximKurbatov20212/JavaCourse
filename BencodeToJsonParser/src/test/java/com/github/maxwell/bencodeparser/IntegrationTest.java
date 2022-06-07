package com.github.maxwell.bencodeparser;

import com.github.maxwell.bencodeparser.lexer.Lexer;
import com.github.maxwell.bencodeparser.lexer.Token;
import com.github.maxwell.bencodeparser.parser.Expr;
import com.github.maxwell.bencodeparser.parser.JsonPrinter;
import com.github.maxwell.bencodeparser.parser.Parser;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class IntegrationTest {

    private static List<Token> scan(String expressions) {
        BufferedReader br = new BufferedReader(new StringReader(expressions));
        return Lexer.scan(br);
    }

    public String parse(List<Token> tokens) {
        if(tokens == null) return null;
        Expr expr = Parser.parse(tokens);
        return expr == null ? null : JsonPrinter.print(expr);
    }

    @Test
    public void emptyDictionary() {
        String str = "d e"; // ok
        String rightAnswer = "{\n\n}";
        String answer = parse((scan(str)));
        assertEquals(rightAnswer, answer);
    }

    @Test
    public void extraTokensAfterResult() {
        String str = "d 5:large i10e e e e"; // ok
        String answer = parse((scan(str)));
        assertNull(answer);
    }

    @Test
    public void tooLargeNumber() {
        String str = "d 5:large i10000000000000000000000000e e"; // ok
        String answer = parse((scan(str)));
        assertNull(answer);
    }

    @Test
    public void emptyList() {
        String str = "d 9:emptyList l e e"; // ok
        String rightAnswer = """
        {
            "emptyList" : [
            ]
        }""";
        assertEquals(rightAnswer, parse((scan(str))));
    }

    @Test
    public void test() {
        String str = "d 1:a i1e e"; // ok
        String rightAnswer = """
        {
            "a" : 1
        }""";
        assertEquals(rightAnswer, parse((scan(str))));
    }

    @Test
    public void innerList() {
        String str = "d 5:list1 l 5:list2 l i2e e e e"; // ok
        String rightAnswer = """
        {
            "list1" : [
                "list2",
                [
                    2
                ]
            ]
        }""";
        assertEquals(rightAnswer, parse((scan(str))));
    }

    @Test
    public void innerDictionary() {
        String str = "d 1:a d 1:b d 1:d d 1:a i1e e e e e"; // ok
        String rightAnswer = """
        {
            "a" : {
                "b" : {
                    "d" : {
                        "a" : 1
                    }
                }
            }
        }""";
        assertEquals(rightAnswer, parse((scan(str))));
    }


    @Test
    public void skipSpaces() {
        String str = "d                                              10:spaces....                                 10:spaces....                   e"; // ok
        String rightAnswer = """
        {
            "spaces...." : "spaces...."
        }""";
        assertEquals(rightAnswer, parse((scan(str))));
    }

    @Test
    public void changeCollocation() {
        String str = "d 1:b i1e 1:a i2e e"; // ok
        String rightAnswer = """
        {
            "a" : 2,
            "b" : 1
        }""";
        assertEquals(rightAnswer, parse((scan(str))));
    }
}
