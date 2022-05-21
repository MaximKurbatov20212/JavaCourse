package Parser;

import Lexer.Token;

public class UnexpectedTokenException extends Exception {
    Token token;

    UnexpectedTokenException(Token token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return token.toString();
    }
}
