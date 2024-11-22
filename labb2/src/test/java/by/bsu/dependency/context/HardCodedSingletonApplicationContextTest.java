package by.bsu.dependency.context;

import by.bsu.dependency.example.*;
import by.bsu.dependency.context.*;
import by.bsu.dependency.exceptions.*;
import by.bsu.dependency.annotation.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HardCodedSingletonApplicationContextTest {

    private ApplicationContext applicationContext;

    @BeforeEach
    void init() {
        applicationContext = new HardCodedSingletonApplicationContext(ABean.class, BBean.class, CBean.class, DBean.class, FirstBean.class, OtherBean.class);
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
                NoSuchBeanDefinitionException.class,
                () -> applicationContext.getBean("randomName")
        );
    }

    @Test
    void testIsSingletonReturns() {
        assertThat(applicationContext.isSingleton("firstBean")).isTrue();
        assertThat(applicationContext.isSingleton("otherBean")).isTrue();
    }

    @Test
    void testIsSingletonThrows() {
        assertThrows(
                NoSuchBeanDefinitionException.class,
                () -> applicationContext.isSingleton("randomName")
        );
    }

    @Test
    void testIsPrototypeReturns() {
        assertThat(applicationContext.isPrototype("firstBean")).isFalse();
        assertThat(applicationContext.isPrototype("otherBean")).isFalse();
    }

    @Test
    void testIsPrototypeThrows() {
        assertThrows(
                NoSuchBeanDefinitionException.class,
                () -> applicationContext.isPrototype("randomName")
        );
    }

    @Test
    void testDepencies() {
        applicationContext.start();
        assertThat(applicationContext.containsBean("bBean")).isTrue();
        assertThat(applicationContext.containsBean("aBean")).isTrue();
        assertThat(applicationContext.isPrototype("bBean")).isTrue();
        assertThat(applicationContext.isPrototype("cBean")).isTrue();
        assertThat(applicationContext.getBean("cBean")).isNotNull().isInstanceOf(CBean.class);
        ABean a1 = applicationContext.getBean(ABean.class);
        DBean d1 = applicationContext.getBean(DBean.class);
        assertThat(a1 == d1.a).isTrue();
        DBean d2 = applicationContext.getBean(DBean.class);
        assertThat(d2 == d1).isFalse();
    }

    @Test
    void testPostconstruct(){
        applicationContext.start();
        ABean a1 = (ABean) applicationContext.getBean("aBean");
        assertEquals(a1.a, 5);
    }
}
