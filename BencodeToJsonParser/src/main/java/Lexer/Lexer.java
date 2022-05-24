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

    // CR: make private, use scan(BufferedReader) instead
    public List<Token> getTokens() throws IOException {
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
                switch (c) {
                    // CR: what about tabs? better to use Character.isWhitespace(char)
                    case ' ' -> curPos++;

                    // CR: we don't know anything about json elements at this point
                    // CR: so you shouldn't add json related stuff to tokens
                    case 'd' -> addToken(new Token(TokenType.START_DICT, "{"));

                    case 'l' -> addToken(new Token(TokenType.START_LIST, "["));

                    case 'i' -> addToken(getNumber(line));

                    case 'e' -> addToken(new Token(TokenType.END_ELEMENT, ""));

                    default -> {
                        ErrorReporter.report(unknownChar(line, curPos, c));
                        hasErrors = true;
                        curPos++;
                    }
                }
            }
        }
        catch (IndexOutOfBoundsException e) {
            // CR: it's cheaper to check range in advance instead. also this way your program is much more clear:
            // CR: you have fewer jumps from method to method skipping some additional methods along the way
            ErrorReporter.report(invalidLength(line, curPos));
            return null;
        }
        catch (InvalidFormatException e) {
            // CR: reason to have error reporter is to report multiple errors before we stop. in your case you report
            // CR: one error and stop
            ErrorReporter.report(invalidFormat(line, curPos));
            return null;
        }
        // CR: that's not a good idea since now all your checked exceptions became unchecked
        // CR: it means that if you introduce new checked exception type in your program and you want to handle it differently
        // CR: then you may forget to do it here
        // CR: also you mask all unexpected checked exceptions in your program this way
        catch (Exception e) {
            // CR: it is more common to throw exceptions then errors. the reason is that errors are usually introduced by jvm
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
                // CR: better report, at least with position
                throw new InvalidFormatException(line);
            }
            curPos++;
        }
        // CR: catch NumberFormatException and show helpful error message
        return Integer.parseInt(line.substring(start, curPos++));
    }


    private Token getString(String line) {
        int len = getLen(line);

        // CR: always false
        if (len == -1) return null;

        int pos = curPos;

        StringBuilder result = new StringBuilder();
        // CR: what if there are fewer chars in string then pos + len? add test for this case
        for (int i = pos; i < pos + len; i++) {
            char c = line.charAt(i);

            if (!isUTF8(c)) {
                // CR: message would be "c"
                ErrorReporter.report(((Character) c).toString());
                hasErrors = true;
            }

            result.append(c);
        }
        curPos = pos + len;
        return new Token(TokenType.STR, result.toString());
    }

    // CR: merge with getLen
    private Token getNumber(String line) {
        char c;
        int start = ++curPos;
        while ((c = line.charAt(curPos)) != 'e') {
            if (!isDigit(c)) {
                throw new InvalidFormatException(line.substring(start, curPos));
            }
            curPos++;
        }
        // CR: better to use Integer.parseInter in order to check that number is not too big
        return new Token(TokenType.NUM, line.substring(start, curPos));
    }

    private static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    // CR: isAscii, not utf8
    private boolean isUTF8(char c) {
        return c <= 127;
    }

    // CR: all this three methods are practically the same, merge them
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
