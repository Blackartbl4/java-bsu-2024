package by.bsu.dependency.context;

import by.bsu.dependency.example.*;
import by.bsu.dependency.context.*;
import by.bsu.dependency.example.utoscantest.FBean;
import by.bsu.dependency.exceptions.*;
import by.bsu.dependency.annotation.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class AutoScanApplicationContextTest {

    @Test
    void testIsRunning() {
        AutoScanApplicationContext context = new AutoScanApplicationContext(FBean.class.getPackageName());
        assertThat(context.isRunning()).isFalse();
        context.start();
        assertThat(context.isRunning()).isTrue();
    }

    @Test
    void testGetBean() {
        AutoScanApplicationContext context = new AutoScanApplicationContext(FBean.class.getPackageName());
        context.start();
        assertThat(context.containsBean("mBean")).isTrue();
        assertThat(context.containsBean("fBean")).isTrue();
    }
}
