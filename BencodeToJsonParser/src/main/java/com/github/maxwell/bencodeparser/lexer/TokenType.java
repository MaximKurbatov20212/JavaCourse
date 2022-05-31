package com.github.maxwell.bencodeparser.lexer;

public enum TokenType {
    NUM,
    STR,
    START_LIST,
    START_DICT,
    END_ELEMENT,
    EOF
}
