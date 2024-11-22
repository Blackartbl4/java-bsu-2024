package by.bsu.dependency.exceptions;

public class ApplicationContextNotStartedException extends RuntimeException{
    public ApplicationContextNotStartedException(String str) {
        super(str);
    }

    public ApplicationContextNotStartedException() {
        super();
    }
}
