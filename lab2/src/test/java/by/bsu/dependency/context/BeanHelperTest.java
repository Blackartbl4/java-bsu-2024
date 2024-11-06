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

public class BeanHelperTest {
    @Test
    public void testNameFormingFromType() {
        assertThat(AbstractApplicationContext.BeanHelper.CombineName(UnnamedBean.class)).isEqualTo("unnamedBean");
        assertThat(AbstractApplicationContext.BeanHelper.CombineName(FirstBean.class)).isEqualTo("firstBean");
        assertThat(AbstractApplicationContext.BeanHelper.CombineName(OtherBean.class)).isEqualTo("otherBean");
    }
}
