package Lexer;

public record Token(TokenType type, String value, int degreeOfNesting) {}
