package com.github.maxwell.bencodeparser.lexer;

public class ParseBencodeException extends RuntimeException {
    private final String msg;
    private final int pos;
    private final char c;
    
    ParseBencodeException(String msg, char c, int pos) {
        this.msg = msg;
        this.pos = pos;
        this.c = c;
    }

    public String getMsg() {
        return msg;
    }

    public int getPos() {
        return pos;
    }

    public char getC() {
        return c;
    }
}
