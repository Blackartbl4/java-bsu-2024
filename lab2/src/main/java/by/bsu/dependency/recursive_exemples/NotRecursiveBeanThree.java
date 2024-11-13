package lab2.src.main.java.by.bsu.dependency.recursive_exemples;

import lab2.src.main.java.by.bsu.dependency.annotation.Bean;
import lab2.src.main.java.by.bsu.dependency.annotation.BeanScope;

@Bean(scope = BeanScope.PROTOTYPE)
public class NotRecursiveBeanThree {

}
