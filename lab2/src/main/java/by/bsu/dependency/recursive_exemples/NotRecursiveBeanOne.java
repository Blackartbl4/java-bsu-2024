package lab2.src.main.java.by.bsu.dependency.recursive_exemples;

import lab2.src.main.java.by.bsu.dependency.annotation.Bean;
import lab2.src.main.java.by.bsu.dependency.annotation.BeanScope;
import lab2.src.main.java.by.bsu.dependency.annotation.Inject;

@Bean(scope = BeanScope.PROTOTYPE)
public class NotRecursiveBeanOne {
    @Inject
    public NotRecursiveBeanTwo A;

    @Inject
    public NotRecursiveBeanTwo B;
}
