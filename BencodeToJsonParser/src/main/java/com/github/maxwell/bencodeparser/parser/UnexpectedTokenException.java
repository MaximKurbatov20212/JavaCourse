package com.github.maxwell.bencodeparser.parser;

import com.github.maxwell.bencodeparser.lexer.Token;

public class UnexpectedTokenException extends RuntimeException {
    private final Token token;
    private final String expectation;

    UnexpectedTokenException(Token token, String expectation) {
        this.token = token;
        this.expectation = expectation;
    }

    public Token getToken() {
        return token;
    }

    public String getExpectation() {
        return expectation;
    }

    @Override
    public String toString() {
        return token.toString();
    }
}
