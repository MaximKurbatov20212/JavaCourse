package com.github.maxwell.bencodeparser.lexer;

public record Token(TokenType type, String value, int line, int pos) {}
