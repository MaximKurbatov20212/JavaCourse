import lexer.Lexer;
import lexer.Token;
import parser.JsonPrinter;
import parser.Parser;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IntegrationTest {

    private static List<Token> scan(String expressions) throws IOException {
        BufferedReader br = new BufferedReader(new StringReader(expressions));
        return Lexer.scan(br);
    }

    public String parse(List<Token> tokens) {
        return JsonPrinter.print(Parser.parse(tokens));
    }


    @Test
    public void emptyDictionary() throws IOException {
        String str = "d e"; // ok
        String rightAnswer = "{\n\n}";
        assertEquals(rightAnswer, parse((scan(str))));
    }

    @Test
    public void emptyList() throws IOException {
        String str = "d 9:emptyList l e e"; // ok
        String rightAnswer = """
        {
            "emptyList" : [
            ]
        }""";
        assertEquals(rightAnswer, parse((scan(str))));
    }

    @Test
    public void test() throws IOException {
        String str = "d 1:a i1e e"; // ok
        String rightAnswer = """
        {
            "a" : 1
        }""";
        assertEquals(rightAnswer, parse((scan(str))));
    }

    @Test
    public void innerList() throws IOException {
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
    public void innerDictionary() throws IOException {
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
    public void skipSpaces() throws IOException {
        String str = "d                                              10:spaces....                                 10:spaces....                   e"; // ok
        String rightAnswer = """
        {
            "spaces...." : "spaces...."
        }""";
        assertEquals(rightAnswer, parse((scan(str))));
    }

    @Test
    public void changeCollocation() throws IOException {
        String str = "d 1:b i1e 1:a i2e e"; // ok
        String rightAnswer = """
        {
            "a" : 2,
            "b" : 1
        }""";
        assertEquals(rightAnswer, parse((scan(str))));
    }
}
