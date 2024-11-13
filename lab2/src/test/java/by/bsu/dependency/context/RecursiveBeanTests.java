package lab2.src.test.java.by.bsu.dependency.context;

import lab2.src.main.java.by.bsu.dependency.context.ApplicationContext;
import lab2.src.main.java.by.bsu.dependency.context.AutoScanApplicationContext;
import lab2.src.main.java.by.bsu.dependency.context.SimpleApplicationContext;
import lab2.src.main.java.by.bsu.dependency.example.*;
import lab2.src.main.java.by.bsu.dependency.exceptions.ApplicationContextRecursiveDependencyException;
import lab2.src.main.java.by.bsu.dependency.recursive_exemples.*;
import lab2.src.main.java.by.bsu.dependency.exceptions.ApplicationContextDoNotContainsSuchBeanDefinitionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RecursiveBeanTests {
    @Test
    public void fallOnSimpleRecursion() {
        ApplicationContext context = new SimpleApplicationContext(RecursiveBean.class);

        assertThrows(
                ApplicationContextRecursiveDependencyException.class,
                () -> context.start()
        );
    }

    @Test
    public void fallOnIndirectRecursion() {
        ApplicationContext context = new SimpleApplicationContext(
                NotStraightRecursiveBeanOne.class, NotStraightRecursiveBeanTwo.class);

        assertThrows(
                ApplicationContextRecursiveDependencyException.class,
                () -> context.start()
        );
    }

    @Test
    public void noFallOnMuchInstances() {
        ApplicationContext context = new SimpleApplicationContext(
                NotRecursiveBeanOne.class, NotRecursiveBeanTwo.class, NotRecursiveBeanThree.class);

        context.start();
    }
}
