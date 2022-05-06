package Lexer;

public class InvalidFormatException extends RuntimeException {
    private final String msg;
    InvalidFormatException(String msg) {
        this.msg = msg;
    }

    @Override
    public String getMessage() {
        return msg;
    }
}
