package lexer;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import reporter.ErrorReporter;

import static java.lang.Character.isWhitespace;

public class Lexer {
    private final List<Token> tokens = new ArrayList<>();
    private int curPos;
    private boolean hasErrors = false;

    private final BufferedReader br;

    public Lexer(BufferedReader br) {
        this.br = br;
    }

    private List<Token> getTokens() throws IOException {
        String line;

        while ((line = br.readLine()) != null) {
            if(scan(line) == null) return null;
        }

        tokens.add(new Token(TokenType.EOF, ""));
        return tokens;
    }

    public static List<Token> scan(BufferedReader br) throws IOException {
        Lexer lexer = new Lexer(br);
        return lexer.getTokens();
    }

    private List<Token> scan(String line) {
        try {
            while (curPos < line.length()) {
                char c = line.charAt(curPos);

                // string
                if (isDigit(c)) {
                    tokens.add(getString(line));
                    continue;
                }
                if(isWhitespace(c)) curPos++;

                switch (c) {

                    case 'd' -> addToken(new Token(TokenType.START_DICT, ""));

                    case 'l' -> addToken(new Token(TokenType.START_LIST, ""));

                    case 'i' -> addToken(new Token(TokenType.NUM, String.valueOf(getNumber(line, 'e'))));

                    case 'e' -> addToken(new Token(TokenType.END_ELEMENT, ""));

                    default -> {
                        ErrorReporter.report(invalidFormat(line, curPos, c));
                        hasErrors = true;
                        curPos++;
                    }
                }
            }
        }
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

        if(curPos + len > line.length()) throw new ParseBencodeException(line, line.charAt(curPos - 1), curPos - 1);

        int start = curPos;
        StringBuilder result = new StringBuilder();
        for (int i = start; i < start + len; i++) {
            char c = line.charAt(i);
            if (!isASCII(c)) {
                ErrorReporter.report(invalidFormat(line, curPos, c));
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
        while ((c = line.charAt(curPos)) != stopChar) {
            if (!isDigit(c)) {
                throw new ParseBencodeException(line, c, curPos);
            }
            curPos++;
        }
        try {
            return Integer.parseInt(line.substring(start, curPos++));
        }
        catch (NumberFormatException e) {
            ErrorReporter.report("...");
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
                Unexpected symbol '%c':
                %s
                %s^--- here
                """.formatted(c, line, " ".repeat(pos));
    }
}
