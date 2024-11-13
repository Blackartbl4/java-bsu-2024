package lab2.src.main.java.by.bsu.dependency.context;

import lab2.src.main.java.by.bsu.dependency.annotation.Bean;
import lab2.src.main.java.by.bsu.dependency.annotation.BeanScope;
import lab2.src.main.java.by.bsu.dependency.annotation.Inject;
import lab2.src.main.java.by.bsu.dependency.annotation.PostConstruct;
import lab2.src.main.java.by.bsu.dependency.exceptions.*;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public abstract class AbstractApplicationContext implements ApplicationContext {
    protected class DepsChecker {
        public Class<?> Owned;
        public DepsChecker Parent;

        public DepsChecker(Class<?> a, DepsChecker p) {
            Owned = a;
            Parent = p;
        }

        public DepsChecker(Class<?> a) {
            Owned = a;
            Parent = null;
        }

        void Check() {
            Arrays.stream(Owned.getDeclaredFields())
                .filter(fld -> fld.isAnnotationPresent(Inject.class))
                .peek(fld -> {
                    if (!ClassedBeans_.containsKey(fld.getType()))
                        throw new ApplicationContextDoNotContainsSuchBeanDefinitionException(BeanHelper.CombineName(fld.getType()));
                })
                .filter(fld -> ClassedBeans_.get(fld.getType()) == BeanScope.PROTOTYPE)
                .forEach(fld -> {
                    if (IsVisited(fld.getType()))
                        throw new ApplicationContextRecursiveDependencyException(CheckedType(), fld.getType());

                    new DepsChecker(fld.getType(), this).Check();
                });
        }

        boolean IsVisited(Class<?> type) {
            return Owned == type || (Parent != null && Parent.IsVisited(type));
        }

        Class<?> CheckedType() {
            if (Parent == null)
                return Owned;

            return Parent.CheckedType();
        }
    }

    @Override
    public void start() {


        this.ClassedBeans_.forEach((type, scope) -> {
            if (scope == BeanScope.PROTOTYPE)
                (new DepsChecker(type)).Check();
            if (scope == BeanScope.SINGLETON)
                this.InstantiatedBeans_.put(type, Instantiate_(type));
        });
        this.InstantiatedBeans_.forEach((idea, obj) -> PostConstruct_(Inject_(obj)));
        Status_ = ContextStatus.STARTED;
    }

    @Override
    public boolean isRunning() {
        return Status_ == ContextStatus.STARTED;
    }

    @Override
    public boolean containsBean(String name) {
        AssertRunning_();
        return BeanTemplates_.containsKey(name);
    }

    @Override
    public Object getBean(String name) {
        AssertRunning_();
        CheckBeanCorrectness_(name);
        return GetBean(GetBeanType(name));
    }

    @Override
    public <T> T getBean(Class<T> clazz) {
        AssertRunning_();
        CheckBeanCorrectness_(clazz);
        return (T)GetBean(clazz);
    }

    @Override
    public boolean isSingleton(String name) {
        CheckBeanCorrectness_(name);
        return GetBeanScope(GetBeanType(name)) == BeanScope.SINGLETON;
    }

    @Override
    public boolean isPrototype(String name) {
        CheckBeanCorrectness_(name);
        return GetBeanScope(GetBeanType(name)) == BeanScope.PROTOTYPE;
    }

    private final HashMap<String, Class<?>> BeanTemplates_ = new HashMap<>();
    private final HashMap<Class<?>, BeanScope> ClassedBeans_ = new HashMap<>();
    private final HashMap<Class<?>, Object> InstantiatedBeans_ = new HashMap<>();

    public static class BeanHelper {
        public record BeanValue(String name, Class<?> type, BeanScope scope){};

        public static String CombineName(Class<?> type) {
            return Character.toLowerCase(type.getSimpleName().charAt(0)) + type.getSimpleName().substring(1);
        }

        public static BeanValue GetBin(Class<?> type) {
            var annot = type.getAnnotation(Bean.class);
            return new BeanValue(
                    annot == null || annot.name().isEmpty() ? CombineName(type) : annot.name(),
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
            throw new ApplicationContextDoNotContainsSuchBeanDefinitionException(BeanHelper.CombineName(type));
    }

    private void CheckBeanCorrectness_(String name) {
        if (!BeanTemplates_.containsKey(name))
            throw new ApplicationContextDoNotContainsSuchBeanDefinitionException(name);
    }

    private Object GetBean(Class<?> type) {
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
                        throw new ApplicationContextPostConstructFailure(method.getName(), e);
                    }
                });
        return obj;
    }

    private Object Inject_(Object obj) {
        Arrays.stream(obj.getClass().getDeclaredFields())
                .filter(fld->fld.isAnnotationPresent(Inject.class))
                .forEach(fld -> {
                    fld.setAccessible(true);
                    CheckBeanCorrectness_(fld.getType());
                    try {
                        fld.set(obj, GetBean(fld.getType()));
                    } catch (IllegalAccessException ex) {
                        throw new ApplicationContextInjectFailure(fld.getName(), ex);
                    }
                });
        return obj;
    }

    private Object Instantiate_(Class<?> type) {
        try {
            return type.getDeclaredConstructor(new Class[]{}).newInstance();
        } catch (NoSuchMethodException | InvocationTargetException |
                 InstantiationException | IllegalAccessException e) {
            throw new ApplicationContextInstantiateFailure(type.getName(), e);
        }
    }

    protected enum ContextStatus {
        NOT_STARTED,
        STARTED
    }

    private void AssertRunning_() {
        if (!isRunning())
            throw new ApplicationContextNotStartedException();
    }

    private ContextStatus Status_ = ContextStatus.NOT_STARTED;
}