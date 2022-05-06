package Lexer;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Reporter.ErrorReporter;

public class Lexer {
    private final List<Token> tokens = new ArrayList<>();
    private int curPos;

    private final BufferedReader br;
    private final ErrorReporter errorReporter = new ErrorReporter();


    public Lexer(BufferedReader br) {
        this.br = br;
    }

    public List<Token> getTokens() throws java.io.IOException {
        String line;
        while ((line = br.readLine()) != null) {
            scan(line);
        }
        tokens.add(new Token(TokenType.EOF, ""));
        System.err.println(tokens);
        return tokens;
    }

    public static List<Token> scan(BufferedReader br) throws IOException {
        Lexer lexer = new Lexer(br);
        return lexer.getTokens();
    }

    private Token getArray(String line) {
        int len = getLen(line);
        String res = line.substring(curPos, curPos + len);
        curPos = curPos + len;
        return new Token(TokenType.STR, res);
    }

    private void scan(String line) {
        while (curPos < line.length()) {
            char c = line.charAt(curPos);

            // array
            if (isDigit(c)) {
                tokens.add(getArray(line));
                continue;
            }
            switch (c) {
                case ' ' -> curPos++;

                case 'd' -> addToken(new Token(TokenType.START_DICT, "{"));

                // list
                case 'l' -> addToken(new Token(TokenType.START_LIST, "["));

                // number
                case 'i' -> addToken(getNumber(line));

                // dictionary/list
                case 'e' -> addToken(new Token(TokenType.END_BRACKET, ""));

                default -> {
                    errorReporter.report("Unknown char" + c);
                    curPos++;
                }
            }
        }
//        tokens.add(new Token(TokenType.EOL, ""));
        curPos = 0;
    }

    private void addToken(Token token) {
        tokens.add(token);
        curPos++;
    }

    private int getLen(String line) {
        char c;
        int start = curPos;
        while((c = line.charAt(curPos)) != ':') {
            if (!isDigit(c)) {
                throw new RuntimeException();
            }
            curPos++;
        }
        return Integer.parseInt(line.substring(start, curPos++));
    }

    private Token getNumber(String line) {
        char c;
        int start = ++curPos;
        while ((c = line.charAt(curPos)) != 'e') {
            if (!isDigit(c)) {
                throw new RuntimeException();
            }
            curPos++;
        }
        return new Token(TokenType.NUM, line.substring(start, curPos++)); // curPos++ skip 'e'
    }

    private static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }
}
