package by.bsu.dependency.exceptions;

public class InjectException extends RuntimeException {
    public InjectException(String message) {
        super(message);
    }

    public InjectException(Exception ex) {
        super(ex);
    }
}
