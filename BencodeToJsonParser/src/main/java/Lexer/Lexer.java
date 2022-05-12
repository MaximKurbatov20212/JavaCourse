package Lexer;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Reporter.ErrorReporter;

public class Lexer {
    private final List<Token> tokens = new ArrayList<>();
    private int curPos;
    private boolean hasErrors = false;

    private final BufferedReader br;

    public Lexer(BufferedReader br) {
        this.br = br;
    }

    public List<Token> getTokens() throws java.io.IOException {
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
                        ErrorReporter.report(unknownChar(line, curPos, c));
                        hasErrors = true;
                        curPos++;
                    }
                }
            }
        }
        catch (IndexOutOfBoundsException e) {
            ErrorReporter.report(invalidLength(line, curPos));
            return null;
        }
        catch (InvalidFormatException e) {
            ErrorReporter.report(invalidFormat(line, curPos));
            return null;
        }
        catch (Exception e) {
            throw new AssertionError("Something wrong");
        }

        curPos = 0;
        return hasErrors ? null : tokens;
    }


    private void addToken(Token token) {
        tokens.add(token);
        curPos++;
    }

    private int getLen(String line) {
        char c;
        int start = curPos;
        while ((c = line.charAt(curPos)) != ':') {
            if (!isDigit(c)) {
                throw new InvalidFormatException(line);
            }
            curPos++;
        }
        return Integer.parseInt(line.substring(start, curPos++));
    }


    private Token getArray(String line) {
        int len = getLen(line);

        if (len == -1) return null;

        int pos = curPos;

        StringBuilder result = new StringBuilder();
        for (int i = pos; i < pos + len; i++) {
            char c = line.charAt(i);

            if (!isUTF8(c)) {
                ErrorReporter.report(((Character) c).toString());
                hasErrors = true;
            }

            result.append(c);
        }
        curPos = pos + len;
        return new Token(TokenType.STR, result.toString());
    }

    private Token getNumber(String line) {
        char c;
        int start = ++curPos;
        while ((c = line.charAt(curPos)) != 'e') {
            if (!isDigit(c)) {
                throw new InvalidFormatException(line.substring(start, curPos));
            }
            curPos++;
        }
        return new Token(TokenType.NUM, line.substring(start, curPos));
    }

    private static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isUTF8(char c) {
        return c <= 127;
    }

    private String unknownChar(String line, int pos, char c) {
        return """
                Unknown char '%c':
                %s
                %s^--- here
                """.formatted(c, line, " ".repeat(pos));
    }

    private String invalidLength(String line, int pos) {
        return """
                Invalid length:
                %s
                %s^--- here
                """.formatted(line, " ".repeat(pos - 2));
    }

    private String invalidFormat(String line, int pos) {
        return """
                Invalid symbol '%c':
                %s
                %s^--- here
                """.formatted(line.charAt(pos), line, " ".repeat(pos));
    }
}
