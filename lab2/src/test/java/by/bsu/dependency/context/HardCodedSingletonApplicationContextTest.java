package lab2.src.test.java.by.bsu.dependency.context;

import lab2.src.main.java.by.bsu.dependency.example.*;
import lab2.src.main.java.by.bsu.dependency.context.*;
import lab2.src.main.java.by.bsu.dependency.annotation.*;
import lab2.src.main.java.by.bsu.dependency.exceptions.ApplicationContextDoNotContainsSuchBeanDefinitionException;
import lab2.src.main.java.by.bsu.dependency.exceptions.ApplicationContextNotStartedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HardCodedSingletonApplicationContextTest {

    private ApplicationContext applicationContext;

    @BeforeEach
    void init() {
        applicationContext = new HardCodedSingletonApplicationContext(
                FirstBean.class,
                OtherBean.class,
                SingletonPostConstructCheck.class,
                PrototypePostConstructCheck.class);
    }

    @Test
    void testIsRunning() {
        assertThat(applicationContext.isRunning()).isFalse();
        applicationContext.start();
        assertThat(applicationContext.isRunning()).isTrue();
    }

    @Test
    void testContextContainsNotStarted() {
        assertThrows(
                ApplicationContextNotStartedException.class,
                () -> applicationContext.containsBean("firstBean")
        );
    }

    @Test
    void testContextContainsBeans() {
        applicationContext.start();

        assertThat(applicationContext.containsBean("firstBean")).isTrue();
        assertThat(applicationContext.containsBean("otherBean")).isTrue();
        assertThat(applicationContext.containsBean("randomName")).isFalse();
    }

    @Test
    void testContextGetBeanNotStarted() {
        assertThrows(
                ApplicationContextNotStartedException.class,
                () -> applicationContext.getBean("firstBean")
        );
    }

    @Test
    void testGetBeanReturns() {
        applicationContext.start();

        assertThat(applicationContext.getBean("firstBean")).isNotNull().isInstanceOf(FirstBean.class);
        assertThat(applicationContext.getBean("otherBean")).isNotNull().isInstanceOf(OtherBean.class);
    }

    @Test
    void testGetBeanThrows() {
        applicationContext.start();

        assertThrows(
                ApplicationContextDoNotContainsSuchBeanDefinitionException.class,
                () -> applicationContext.getBean("randomName")
        );
    }

    @Test
    void testIsSingletonReturns() {
        assertThat(applicationContext.isSingleton("firstBean")).isTrue();
        assertThat(applicationContext.isSingleton("otherBean")).isFalse();
    }

    @Test
    void testIsSingletonThrows() {
        assertThrows(
                ApplicationContextDoNotContainsSuchBeanDefinitionException.class,
                () -> applicationContext.isSingleton("randomName")
        );
    }

    @Test
    void testIsPrototypeReturns() {
        assertThat(applicationContext.isPrototype("firstBean")).isFalse();
        assertThat(applicationContext.isPrototype("otherBean")).isTrue();
    }

    @Test
    void testIsPrototypeThrows() {
        assertThrows(
                ApplicationContextDoNotContainsSuchBeanDefinitionException.class,
                () -> applicationContext.isPrototype("randomName")
        );
    }

    @Test
    void testIsSingletonPrototypeReturnsCorrect() {
        applicationContext.start();

        assertThat(applicationContext.getBean("firstBean") == applicationContext.getBean("firstBean")).isTrue();
        assertThat(applicationContext.getBean("otherBean") == applicationContext.getBean("otherBean")).isFalse();
    }

    @Test
    void testIsConstructsCorrect() {
        applicationContext.start();

        assertThat(((PrototypePostConstructCheck)applicationContext.getBean("PCCP")).IsConstructed).isTrue();
        assertThat(((SingletonPostConstructCheck)applicationContext.getBean("PCCS")).IsConstructed).isTrue();
    }
}
