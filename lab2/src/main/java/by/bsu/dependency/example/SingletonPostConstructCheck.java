package lab2.src.main.java.by.bsu.dependency.example;

import lab2.src.main.java.by.bsu.dependency.annotation.Bean;
import lab2.src.main.java.by.bsu.dependency.annotation.BeanScope;
import lab2.src.main.java.by.bsu.dependency.annotation.PostConstruct;

@Bean(name = "PCCS", scope = BeanScope.SINGLETON)
public class SingletonPostConstructCheck {
    public boolean IsConstructed;

    @PostConstruct
    public void Construct() {
        IsConstructed = true;
    }
}
