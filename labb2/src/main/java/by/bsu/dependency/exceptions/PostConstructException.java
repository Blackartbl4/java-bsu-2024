package by.bsu.dependency.exceptions;

public class PostConstructException extends RuntimeException {
    public PostConstructException(String message) {
        super(message);
    }

    public PostConstructException(Exception ex) {
        super(ex);
    }
}
