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
    private boolean hasErrors = false;

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

        tokens.add(new Token(TokenType.EOF, ""));
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
        try {
            while (curPos < line.length()) {
                char c = line.charAt(curPos);

                if(isWhitespace(c)) {
                    curPos++;
                    continue;
                }

                if (isDigit(c)) {
                    Token string = getString(line);
                    if(string == null)  {
                        hasErrors = true;
                        continue;
                    }
                    tokens.add(string);
                    continue;
                }

                switch (c) {

                    case 'd' -> addToken(new Token(TokenType.START_DICT, ""));

                    case 'l' -> addToken(new Token(TokenType.START_LIST, ""));

                    case 'i' -> {
                        curPos++; // skip 'i'
                        // CR: what about negative numbers? https://en.wikipedia.org/wiki/Bencode#Encoding_algorithm
                        int number = getNumber(line, 'e');
                        if(number == -1) {
                            hasErrors = true;
                            curPos++;
                            continue;
                        }
                        addToken(new Token(TokenType.NUM, String.valueOf(number)));
                    }

                    case 'e' -> addToken(new Token(TokenType.END_ELEMENT, ""));

                    default -> {
                        ErrorReporter.report(invalidFormat(line, curPos, c));
                        hasErrors = true;
                        curPos++;
                    }
                }
            }
        }
        // CR: you won't need parser exception anymore after the fixes
        catch (ParseBencodeException e) {
            ErrorReporter.report(invalidFormat(e.getMsg(), e.getPos(), e.getC()));
            return null;
        }

        curPos = 0;
        return hasErrors ? null : tokens;
    }

    private void addToken(Token token) {
        tokens.add(token);
        curPos++;
    }

    private Token getString(String line) {
        int len = getNumber(line, ':');
        curPos++; // skip ':'

        if(len == -1) return null;

        // CR: i think we should report problem and move to the end of line
        if(curPos + len > line.length()) throw new ParseBencodeException(line, line.charAt(curPos - 1), curPos - 1);

        int start = curPos;
        StringBuilder result = new StringBuilder();
        for (int i = start; i < start + len; i++) {
            char c = line.charAt(i);
            if (!isASCII(c)) {
                ErrorReporter.report(invalidFormat(line, curPos, c));
                // CR: you can just add method hasErrors() into ErrorReporter and call it when needed
                hasErrors = true;
            }
            result.append(c);
        }
        curPos = start + len;
        return new Token(TokenType.STR, result.toString());
    }

    private int getNumber(String line, char stopChar) {
        char c;
        int start = curPos;

        // CR: i think if we see unexpected symbol we should report problem and continue scanning starting from this symbol
        while ((c = line.charAt(curPos)) != stopChar) {
            if (!isDigit(c)) throw new ParseBencodeException(line, c, curPos);
            curPos++;
        }

        try {
            return Integer.parseInt(line.substring(start, curPos));
        }
        catch (NumberFormatException e) {
            ErrorReporter.report(line + "\n" + " ".repeat(start) + "^--- Too large number here");
        }
        return -1;
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
