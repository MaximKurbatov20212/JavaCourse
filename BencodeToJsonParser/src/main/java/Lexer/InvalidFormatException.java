// CR: packages are called in all lower case letters: Lexer -> lexer
package Lexer;

// CR: very generic exception type, better rename to smth more specific
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
