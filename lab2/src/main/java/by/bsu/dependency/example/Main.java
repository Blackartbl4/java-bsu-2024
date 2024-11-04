package lab2.src.main.java.by.bsu.dependency.example;

import lab2.src.main.java.by.bsu.dependency.context.ApplicationContext;
import lab2.src.main.java.by.bsu.dependency.context.HardCodedSingletonApplicationContext;

public class Main {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new HardCodedSingletonApplicationContext(
                FirstBean.class, OtherBean.class
        );
        applicationContext.start();

        FirstBean firstBean = (FirstBean) applicationContext.getBean("firstBean");
        OtherBean otherBean = (OtherBean) applicationContext.getBean("otherBean");

        firstBean.doSomething();
        otherBean.doSomething();

        otherBean.doSomethingWithFirst();

        // Метод падает, так как в классе HardCodedSingletonApplicationContext не реализовано внедрение зависимостей
        // otherBean.doSomethingWithFirst();
    }
}
