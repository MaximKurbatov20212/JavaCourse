package Parser;

import Lexer.Token;

public class UnexpectedTokenException extends Exception {
    Token token;

    UnexpectedTokenException(Token token) {
        this.token = token;
    }

     void printErrorMessage() {
        System.err.println("Unexpected token " + token.toString());
    }

    Token getToken() {
        return token;
    }
}
