package by.bsu.dependency.exceptions;

public class NoSuchBeanDefinitionException extends RuntimeException{
    public NoSuchBeanDefinitionException(String str) {
        super(str);
    }

    public NoSuchBeanDefinitionException() {
        super();
    }
}
