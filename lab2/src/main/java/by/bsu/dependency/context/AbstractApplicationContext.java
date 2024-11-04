package lab2.src.main.java.by.bsu.dependency.context;

import lab2.src.main.java.by.bsu.dependency.annotation.Bean;
import lab2.src.main.java.by.bsu.dependency.annotation.BeanScope;
import lab2.src.main.java.by.bsu.dependency.annotation.Inject;
import lab2.src.main.java.by.bsu.dependency.annotation.PostConstruct;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public abstract class AbstractApplicationContext implements ApplicationContext {
    @Override
    public void start() {
        this.ClassedBeans_.forEach((type, scope) -> {
            if (scope == BeanScope.SINGLETON)
                this.InstantiatedBeans_.put(type, Instantiate_(type));
        });
        this.InstantiatedBeans_.forEach((_, obj) -> PostConstruct_(Inject_(obj)));
        Status_ = ContextStatus.STARTED;
    }

    @Override
    public boolean isRunning() {
        return Status_ == ContextStatus.STARTED;
    }

    @Override
    public boolean containsBean(String name) {
        return BeanTemplates_.containsKey(name);
    }

    @Override
    public Object getBean(String name) {
        return GetBean(GetBeanType(name));
    }

    @Override
    public <T> T getBean(Class<T> clazz) {
        return (T)GetBean(clazz);
    }

    @Override
    public boolean isSingleton(String name) {
        return GetBeanScope(GetBeanType(name)) == BeanScope.SINGLETON;
    }

    @Override
    public boolean isPrototype(String name) {
        return GetBeanScope(GetBeanType(name)) == BeanScope.PROTOTYPE;
    }

    private final HashMap<String, Class<?>> BeanTemplates_ = new HashMap<>();
    private final HashMap<Class<?>, BeanScope> ClassedBeans_ = new HashMap<>();
    private final HashMap<Class<?>, Object> InstantiatedBeans_ = new HashMap<>();

    private static class BeanHelper {
        public record BeanValue(String name, Class<?> type, BeanScope scope){};

        private static String CombineName(Class<?> type) {
            return type.getName().substring(0,0).toLowerCase() + type.getName().substring(1);
        }

        public static BeanValue GetBin(Class<?> type) {
            var annot = type.getAnnotation(Bean.class);
            return new BeanValue(
                    annot == null ? CombineName(type) : annot.name(),
                    type,
                    annot == null ? BeanScope.SINGLETON : annot.scope()
            );
        }
    }

    protected void AddBean(Class<?> type) {
        var bean = BeanHelper.GetBin(type);
        BeanTemplates_.put(bean.name, bean.type);
        ClassedBeans_.put(bean.type, bean.scope);
    }

    private Class<?> GetBeanType(String name) {
        return BeanTemplates_.get(name);
    }
    private BeanScope GetBeanScope(Class<?> type) { return ClassedBeans_.get(type); }

    private void CheckBeanCorrectness_(Class<?> type) {
        if (!ClassedBeans_.containsKey(type))
            throw new RuntimeException();
    }

    private Object GetBean(Class<?> type) {
        CheckBeanCorrectness_(type);

        if (GetBeanScope(type) == BeanScope.SINGLETON)
            return InstantiatedBeans_.get(type);
        return PostConstruct_(Inject_(Instantiate_(type)));
    }

    private Object PostConstruct_(Object obj) {
        Arrays.stream(obj.getClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(PostConstruct.class))
                .forEach(method -> {
                    method.setAccessible(true);
                    try {
                        method.invoke(obj);
                    } catch (InvocationTargetException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });
        return obj;
    }

    private Object Inject_(Object obj) {
        Arrays.stream(obj.getClass().getDeclaredFields())
                .filter(fld->fld.isAnnotationPresent(Inject.class))
                .forEach(fld -> {
                    fld.setAccessible(true);
                    try {
                        fld.set(obj, GetBean(fld.getType()));
                    } catch (IllegalAccessException ex) { throw new RuntimeException(ex); }
                });
        return obj;
    }

    private Object Instantiate_(Class<?> type) {
        try {
            return type.getDeclaredConstructor(new Class[]{}).newInstance();
        } catch (NoSuchMethodException | InvocationTargetException |
                 InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    protected enum ContextStatus {
        NOT_STARTED,
        STARTED
    }

    private ContextStatus Status_ = ContextStatus.NOT_STARTED;
}