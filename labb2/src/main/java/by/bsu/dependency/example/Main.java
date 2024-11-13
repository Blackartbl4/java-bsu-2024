package labb2.src.main.java.by.bsu.dependency.example;

import labb2.src.main.java.by.bsu.dependency.context.*;

public class Main {

    public static void main(String[] args) throws NoSuchMethodException {
        AbstractApplicationContext applicationContext = new SimpleApplicationContext(
                FirstBean.class, OtherBean.class
        );
        applicationContext.start();

        FirstBean firstBean = (FirstBean) applicationContext.getBean("firstBean");
        OtherBean otherBean = (OtherBean) applicationContext.getBean("otherBean");

        firstBean.doSomething();
        otherBean.doSomething();

        // Метод падает, так как в классе HardCodedSingletonApplicationContext не реализовано внедрение зависимостей
        // otherBean.doSomethingWithFirst();
    }
}
