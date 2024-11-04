package lab2.src.main.java.by.bsu.dependency.example;

import lab2.src.main.java.by.bsu.dependency.annotation.Bean;
import lab2.src.main.java.by.bsu.dependency.annotation.Inject;
import lab2.src.main.java.by.bsu.dependency.annotation.PostConstruct;

@Bean(name = "otherBean")
public class OtherBean {

    @Inject
    private FirstBean firstBean;

    void doSomething() {
        System.out.println("Hi, I'm other bean");
    }

    void doSomethingWithFirst() {
        System.out.println("Trying to shake first bean...");
        firstBean.doSomething();
    }

    // @PostConstruct
    void postConstructor() {
        System.out.println("Constructor");
        System.out.println("Trying to shake first bean...");
        firstBean.doSomething();
    }
}
