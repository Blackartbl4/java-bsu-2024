package lab2.src.main.java.by.bsu.dependency.example;

import lab2.src.main.java.by.bsu.dependency.annotation.Bean;

@Bean(name = "firstBean")
public class FirstBean {

    void printSomething() {
        System.out.println("Hello, I'm first bean");
    }

    void doSomething() {
        System.out.println("First bean is working on a project...");
    }
}
