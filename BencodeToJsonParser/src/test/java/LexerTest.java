import lexer.*;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;

import org.hamcrest.MatcherAssert;
import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

public class LexerTest {

    private static List<TokenType> scan(String expressions) throws IOException {
        BufferedReader br = new BufferedReader(new StringReader(expressions));
        List<Token> tokens = Lexer.scan(br);
        return tokens == null ? null : tokens.stream().map(t -> t.type()).toList();
    }

    private static void assertTypes(List<TokenType> types, TokenType... expected) {
        MatcherAssert.assertThat(types, is(List.of(expected)));
    }

    @Test
    public void empty() throws IOException {
        assertTypes(scan(""), TokenType.EOF);
    }

    @Test
    public void unknownChar() throws IOException {
        Assert.assertNull(scan("$"));
        Assert.assertNull(scan("&"));
        Assert.assertNull(scan("-"));
    }

    @Test
    public void emptyDict() throws IOException {
        assertTypes(scan("d e"), TokenType.START_DICT, TokenType.END_ELEMENT, TokenType.EOF);
        assertTypes(scan("de"), TokenType.START_DICT, TokenType.END_ELEMENT, TokenType.EOF);
    }

    @Test
    public void emptyList() throws IOException {
        assertTypes(scan("l e"), TokenType.START_LIST, TokenType.END_ELEMENT, TokenType.EOF);
        assertTypes(scan("le"), TokenType.START_LIST, TokenType.END_ELEMENT, TokenType.EOF);
    }

    @Test
    public void lonelyToken() throws IOException {
        assertTypes(scan("d"), TokenType.START_DICT, TokenType.EOF);
        assertTypes(scan("l"), TokenType.START_LIST, TokenType.EOF);
        assertTypes(scan("i1e"), TokenType.NUM, TokenType.EOF);
        assertTypes(scan("5:hello"), TokenType.STR, TokenType.EOF);
        assertTypes(scan("e"), TokenType.END_ELEMENT, TokenType.EOF);
    }

    @Test
    public void dict() throws IOException {
        assertTypes(scan("d 1:a i1e e"), TokenType.START_DICT, TokenType.STR, TokenType.NUM, TokenType.END_ELEMENT, TokenType.EOF);
        assertTypes(scan("d1:ai1ee"), TokenType.START_DICT, TokenType.STR, TokenType.NUM, TokenType.END_ELEMENT, TokenType.EOF);
    }

    @Test
    public void correctDict() throws IOException {
        assertTypes(scan("d 1:a l i1e e e"), TokenType.START_DICT, TokenType.STR, TokenType.START_LIST, TokenType.NUM, TokenType.END_ELEMENT, TokenType.END_ELEMENT, TokenType.EOF);
        assertTypes(scan("d1:ali1eee"), TokenType.START_DICT, TokenType.STR, TokenType.START_LIST, TokenType.NUM, TokenType.END_ELEMENT, TokenType.END_ELEMENT, TokenType.EOF);
    }

    @Test
    public void incorrectString() throws IOException {
        assertNull(scan("100:a"));
    }

    @Test
    public void incorrectNumber() throws IOException {
        assertNull(scan("d 1:a i1a2e e"));
    }

    @Test
    public void strangeDigits() throws IOException {
        // some strange digits from Character.isDigit() javadoc
        String digits = "\u0660\u06F0\u0966\uFF10";
        for (int i = 0; i < digits.length(); i++) {
            assertNull(scan(String.valueOf(digits.charAt(i))));
        }
    }
}

