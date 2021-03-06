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
    private int tokenPosition; // just position of the first char in token

    private final ErrorReporter errorReporter = new ErrorReporter();

    private final BufferedReader br;

    private Lexer(BufferedReader br) {
        this.br = br;
    }

    private List<Token> getTokens() {
        String line;

        while ((line = getLine()) != null) {
            numberOfLine++;
            if(scan(line) == null) return null;
        }
        if (errorReporter.hasErrors()) return null;

        tokens.add(new Token(TokenType.EOF, "", numberOfLine, tokenPosition));
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
            tokenPosition = curPos;

            if (isDigit(c)) {
                Token string = getString(line);
                if(string != null) {
                    tokens.add(string);
                }
                continue;
            }

            switch (c) {

                case 'd' -> addToken(new Token(TokenType.START_DICT, "", numberOfLine, tokenPosition));

                case 'l' -> addToken(new Token(TokenType.START_LIST, "", numberOfLine, tokenPosition));

                case 'i' -> {
                    curPos++; // skip 'i'
                    Integer number = getNumber(line, 'e');
                    if(number == null) {
                        curPos++;
                        continue;
                    }
                    addToken(new Token(TokenType.NUM, String.valueOf(number), numberOfLine, tokenPosition));
                }

                case 'e' -> addToken(new Token(TokenType.END_ELEMENT, "", numberOfLine, tokenPosition));

                default -> {
                    errorReporter.report(invalidFormat(line, curPos, c));
                    curPos++;
                }
            }
        }
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
        return new Token(TokenType.STR, result.toString(), numberOfLine, tokenPosition);
    }

    private Integer getNumber(String line, char stopChar) {
        char c;
        int start = curPos;
        boolean hasNumberError = false;

        while (curPos < line.length() && (c = line.charAt(curPos)) != stopChar) {
            if (!isDigit(c) && !isNumberNegation(c, curPos, start)) {
                errorReporter.report(invalidFormat(line, curPos, c));
                hasNumberError = true;
            }
            curPos++;
        }

        if (curPos == line.length()) {
            errorReporter.report(("""
                    line: %d, position: %d
                    %s
                    %s^--- No closing '%c' for number
                    """
            ).formatted(numberOfLine, curPos, line, " ".repeat(start), stopChar));
            hasNumberError = true;
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
