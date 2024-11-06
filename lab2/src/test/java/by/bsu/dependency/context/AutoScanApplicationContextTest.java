package lab2.src.test.java.by.bsu.dependency.context;

import lab2.src.main.java.by.bsu.dependency.context.AbstractApplicationContext;
import lab2.src.main.java.by.bsu.dependency.context.ApplicationContext;
import lab2.src.main.java.by.bsu.dependency.context.AutoScanApplicationContext;
import lab2.src.main.java.by.bsu.dependency.context.HardCodedSingletonApplicationContext;
import lab2.src.main.java.by.bsu.dependency.example.*;
import lab2.src.main.java.by.bsu.dependency.exceptions.ApplicationContextDoNotContainsSuchBeanDefinitionException;
import lab2.src.main.java.by.bsu.dependency.exceptions.ApplicationContextNotStartedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AutoScanApplicationContextTest {

    private ApplicationContext applicationContext;

    @BeforeEach
    void init() {
        applicationContext = new AutoScanApplicationContext("lab2.src.main.java.by.bsu.dependency.example");
    }

    @Test
    public void containstAll() {
        applicationContext.start();

        assertThat(applicationContext.containsBean("PCCP")).isTrue();
        assertThat(applicationContext.containsBean("PCCS")).isTrue();
        assertThat(applicationContext.containsBean("firstBean")).isTrue();
        assertThat(applicationContext.containsBean("otherBean")).isTrue();
        assertThat(applicationContext.containsBean("unnamedBean")).isTrue();
    }

    @Test
    public void excludeNotTests() {
        applicationContext.start();

        assertThat(applicationContext.containsBean("main")).isFalse();
        assertThrows(
                ApplicationContextDoNotContainsSuchBeanDefinitionException.class,
                () -> applicationContext.getBean(Main.class)
        );
    }
}
