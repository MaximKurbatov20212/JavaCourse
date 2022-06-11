package com.github.maxwell.bencodeparser.lexer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;

import com.github.maxwell.bencodeparser.reporter.ErrorReporter;

import static java.lang.Character.isWhitespace;

public class Lexer {
    private final List<Token> tokens = new ArrayList<>();
    private int curPos;
    private int numberOfLine;
    // CR: user does not care about number of token, he cares about position in line
    private int numberOfToken = 1;

    private final ErrorReporter errorReporter = new ErrorReporter();

    private final BufferedReader br;

    public Lexer(BufferedReader br) {
        this.br = br;
    }

    private List<Token> getTokens() {
        String line;

        while ((line = getLine()) != null) {
            numberOfLine++;
            if(scan(line) == null) return null;
        }
        if (errorReporter.hasErrors()) return null;

        tokens.add(new Token(TokenType.EOF, "", numberOfLine, numberOfToken++));
        return tokens;
    }

    private String getLine() {
        try {
            return br.readLine();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static List<Token> scan(BufferedReader br) {
        Lexer lexer = new Lexer(br);
        return lexer.getTokens();
    }

    private List<Token> scan(String line) {
        while (curPos < line.length()) {
            char c = line.charAt(curPos);

            if(isWhitespace(c)) {
                curPos++;
                continue;
            }

            if (isDigit(c)) {
                Token string = getString(line);
                if(string != null)  {
                    tokens.add(string);
                }
                continue;
            }

            switch (c) {

                case 'd' -> addToken(new Token(TokenType.START_DICT, "", numberOfLine, numberOfToken++));

                case 'l' -> addToken(new Token(TokenType.START_LIST, "", numberOfLine, numberOfToken++));

                case 'i' -> {
                    curPos++; // skip 'i'
                    Integer number = getNumber(line, 'e');
                    if(number == null) {
                        curPos++;
                        continue;
                    }
                    addToken(new Token(TokenType.NUM, String.valueOf(number), numberOfLine, numberOfToken++));
                }

                case 'e' -> addToken(new Token(TokenType.END_ELEMENT, "", numberOfLine, numberOfToken++));

                default -> {
                    errorReporter.report(invalidFormat(line, curPos, c));
                    curPos++;
                }
            }
        }
        numberOfToken = 1;
        curPos = 0;
        return errorReporter.tooManyErrors() ? null : tokens;
    }

    private void addToken(Token token) {
        tokens.add(token);
        curPos++;
    }

    private Token getString(String line) {
        Integer len = getNumber(line, ':');
        curPos++; // skip ':'

        if(len == null) return null;

        if(curPos + len > line.length()) {
            errorReporter.report(invalidFormat(line, curPos - 1, line.charAt(curPos - 1)));
            curPos = line.length() - 1;
            return null;
        }

        int start = curPos;
        StringBuilder result = new StringBuilder();
        for (int i = start; i < start + len; i++) {
            char c = line.charAt(i);
            if (!isASCII(c)) {
                errorReporter.report(invalidFormat(line, curPos, c));
            }
            result.append(c);
        }
        curPos = start + len;
        return new Token(TokenType.STR, result.toString(), numberOfLine, numberOfToken++);
    }

    private Integer getNumber(String line, char stopChar) {
        char c;
        int start = curPos;
        boolean hasNumberError = false;

        while ((c = line.charAt(curPos)) != stopChar) {
            if (!isDigit(c) && !isNumberNegation(c, curPos, start)) {
                errorReporter.report(invalidFormat(line, curPos, c));
                hasNumberError = true;
            }
            curPos++;
        }

        if(hasNumberError) return null;

        try {
            return Integer.parseInt(line.substring(start, curPos));
        }
        catch (NumberFormatException e) {
            errorReporter.report(("""
                    line: %d, position: %d
                    %s
                    %s^--- Too large number here
                    """
            ).formatted(numberOfLine, curPos, line, " ".repeat(start)));
        }
        return null;
    }

    private boolean isNumberNegation(char c, int curPos, int start) {
        return c == '-' && curPos == start;
    }

    private static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isASCII(char c) {
        return c <= 127;
    }

    private String invalidFormat(String line, int pos, char c) {
        return """
                line %d: unexpected symbol '%c':
                %s
                %s^--- here
                """.formatted(numberOfLine, c, line, " ".repeat(pos));
    }
}
