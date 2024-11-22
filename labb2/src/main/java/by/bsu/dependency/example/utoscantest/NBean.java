package by.bsu.dependency.example.utoscantest;

import by.bsu.dependency.annotation.Bean;
import by.bsu.dependency.annotation.BeanScope;
import by.bsu.dependency.annotation.Inject;

@Bean(scope = BeanScope.PROTOTYPE)
public class NBean {
    @Inject
    private FBean f;
}
