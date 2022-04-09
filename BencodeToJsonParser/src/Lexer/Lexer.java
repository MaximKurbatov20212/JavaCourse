package Lexer;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import Reporter.ErrorReporter;

public class Lexer {
    private final BufferedReader br;
    private final List<Token> tokens = new ArrayList<>();
    private int curPos = 0;
    private int curNesting = 0;
    private Stack<Token> stack = new Stack<Token>();
    private final ErrorReporter errorReporter = new ErrorReporter();

    public Lexer(BufferedReader br) {
        this.br = br;
    }

    public List<Token> getTokens() throws java.io.IOException {
        String line;
        while ((line = br.readLine()) != null) {
            scan(line);
        }
        tokens.add(new Token(TokenType.EOF, "", curNesting));

        if (!stack.empty()) {
            System.out.println("Incorrect format");
        }
        System.out.println(tokens);
        return tokens;
    }

    private Token getArray(String line) {
        int len = getLen(line);
        String res = line.substring(curPos, curPos + len);
        curPos = curPos + len;
        return new Token(TokenType.ARR, res, curNesting);
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
                case ' ' -> {
                    curPos++;
                }

                case 'd' -> {
                    tokens.add(new Token(TokenType.START_DICT, "{", curNesting));
                    stack.push(new Token(TokenType.START_DICT, "{", curNesting));
                    curPos++;
                    curNesting++;
                }

                // number
                case 'i' -> {
                    curPos++;
                    tokens.add(getNumber(line));
                }

                // list
                case 'l' -> {
                    tokens.add(new Token(TokenType.START_LIST, "[", curNesting));
                    stack.push(new Token(TokenType.START_LIST, "[", curNesting));
                    curPos++;
                    curNesting++;
                }

                // dictionary/list
                case 'e' -> {
                    if (stack.empty()) {
                        errorReporter.report("Incorrect Token END_LIST or END_DICT");
                        return;
                    }
                    curNesting--;
                    if (stack.pop().type() == TokenType.START_LIST) {
                        tokens.add(new Token(TokenType.END_LIST, "]", curNesting));
                    } else {
                        tokens.add(new Token(TokenType.END_DICT, "}", curNesting));
                    }
                    curPos++;
                }

                default -> {
                    System.out.println("Unknown char" + c);
                    curPos++;
                }
            }
        }
        tokens.add(new Token(TokenType.EOL, "", curNesting));
    }


    private int getLen(String line) {
        int len = 0;
        char c = line.charAt(curPos);
        while (c != ':') {
            if (!isDigit(c)) {
                throw new RuntimeException();
            }
            len = len * 10 + (c - '0');
            curPos++;
            c = line.charAt(curPos);
        }
        curPos++;
        return len;
    }

    private Token getNumber(String line) {
        char c;
        int start = curPos;
        while ((c = line.charAt(curPos)) != 'e') {
            if (!isDigit(c)) {
                throw new RuntimeException();
            }
            curPos++;
        }
        return new Token(TokenType.NUM, line.substring(start, curPos++), curNesting); // curPos++ skip 'e'
    }

    private static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }
}
