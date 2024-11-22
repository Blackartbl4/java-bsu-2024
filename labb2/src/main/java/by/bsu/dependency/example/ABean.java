package by.bsu.dependency.example;

import by.bsu.dependency.annotation.Bean;
import by.bsu.dependency.annotation.BeanScope;
import by.bsu.dependency.annotation.Inject;
import by.bsu.dependency.annotation.PostConstruct;

@Bean/*(scope = BeanScope.PROTOTYPE)*/
public class ABean {
    @Inject
    public BBean b;
    public int a;
    @PostConstruct
    private void SetInt() {
        a = 5;
    }
}
