package labb2.src.main.java.by.bsu.dependency.example;

import labb2.src.main.java.by.bsu.dependency.annotation.Bean;
import labb2.src.main.java.by.bsu.dependency.annotation.Inject;

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
}
