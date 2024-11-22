package by.bsu.dependency.context;

import by.bsu.dependency.annotation.Bean;
import by.bsu.dependency.annotation.BeanScope;
import by.bsu.dependency.annotation.Inject;
import by.bsu.dependency.annotation.PostConstruct;
import by.bsu.dependency.exceptions.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public abstract class AbstractApplicationContext implements ApplicationContext {
    private final Map<String, Class<?>> MapStrCla = new HashMap<String, Class<?>>();
    private final Map<Class<?>, Object> MapClaObj = new HashMap<>();
    private final Map<Class<?>, BeanScope> MapClaSco = new HashMap<>();
    private final Map<Class<?>, Set<Class<?>>> BeanDependencies = new HashMap<>();
    private ContextStatus contextStatus_ = ContextStatus.NOT_STARTED;

    protected void PostConstruct(Class<?> clazz, Object bean) {
        Method[] methods = clazz.getDeclaredMethods();
        for (var method : methods) {
            try {
                method.setAccessible(true);
                if (method.isAnnotationPresent(PostConstruct.class)) {
                    method.invoke(bean);
                }
            }
            catch (IllegalAccessException | InvocationTargetException ex)  {
                throw new PostConstructException(ex);
            }
        }
    }

    protected void CheckForCycleDependencies(Class<?> depSource, Class<?> target) {
        if (BeanDependencies.containsKey(depSource)) {
            for (var injectedClass : BeanDependencies.get(depSource)) {
                if (MapClaSco.get(injectedClass) == BeanScope.SINGLETON) continue;;
                BeanDependencies.get(target).add(injectedClass);
                Set<?> set = BeanDependencies.get(injectedClass);
                if (BeanDependencies.containsKey(injectedClass) && BeanDependencies.get(injectedClass).contains(target))
                    throw new CycleDependencyException("Cycle dependency!");
                CheckForCycleDependencies(injectedClass, target);
            }
        }
    }

    protected void InjectDependencies(Class<?> clazz, Object beanObject) {
        try {
            for (var field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(Inject.class)) {
                    if (!BeanDependencies.containsKey(clazz)) {
                        Set<Class<?>> clazzDependencies = new HashSet<>();
                        BeanDependencies.put(clazz, clazzDependencies);
                        if (MapClaSco.get(clazz) == BeanScope.PROTOTYPE && MapClaSco.get(field.getType()) == BeanScope.PROTOTYPE) clazzDependencies.add(field.getType());
                    } else if (MapClaSco.get(clazz) == BeanScope.PROTOTYPE && MapClaSco.get(field.getType()) == BeanScope.PROTOTYPE) {
                        BeanDependencies.get(clazz).add(field.getType());
                    }
                    if (MapClaSco.get(field.getType()) == BeanScope.PROTOTYPE) {
                        CheckForCycleDependencies(field.getType(), field.getType());
                    }

                    if (MapClaSco.get(field.getType()) == BeanScope.SINGLETON) {
                        field.set(beanObject, MapClaObj.get(field.getType()));
                    } else {
                        field.set(beanObject, BeanConstructor(field.getType()));
                    }
                }
            }
        } catch (IllegalAccessException ex) {
            throw new InjectException(ex);
        }
    }

    protected Object BeanConstructor(Class<?> clazz) {
        try {
            Constructor<?> constructor = clazz.getConstructor();
            constructor.setAccessible(true);
            Object beanObject = constructor.newInstance();
            InjectDependencies(clazz, beanObject);
            PostConstruct(clazz, beanObject);
            return beanObject;
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException |
                IllegalAccessException ig) {
            throw new RuntimeException(ig);
        }
    }

    protected void AddingClassesToMapByName(Class<?> clazz) {
        if (clazz.getDeclaredAnnotation(Bean.class) != null) {
            if (clazz.getDeclaredAnnotation(Bean.class).name().isEmpty()) {
                String name = clazz.getSimpleName();
                String str = name.substring(0, 1).toLowerCase() + name.substring(1);
                MapStrCla.put(str, clazz);
                MapClaSco.put(clazz, clazz.getDeclaredAnnotation(Bean.class).scope());
            } else {
                MapStrCla.put(clazz.getDeclaredAnnotation(Bean.class).name(), clazz);
                MapClaSco.put(clazz, clazz.getDeclaredAnnotation(Bean.class).scope());
            }
        } else {
            String name = clazz.getSimpleName();
            String str = name.substring(0, 1).toLowerCase() + name.substring(1);
            MapStrCla.put(str, clazz);
            MapClaSco.put(clazz, BeanScope.SINGLETON);
        }
    }

    @Override
    public void start() {
        if (contextStatus_ == ContextStatus.STARTED) return;

        for (var name : MapStrCla.entrySet()) {
            Class<?> clazz = name.getValue();
            if (MapClaSco.get(clazz) == BeanScope.SINGLETON) try {
                Constructor<?> constructor = clazz.getConstructor();
                constructor.setAccessible(true);
                Object beanObject = constructor.newInstance();
                MapClaObj.put(clazz, beanObject);
            } catch (InvocationTargetException | NoSuchMethodException | InstantiationException |
                     IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        for (var i : MapClaObj.entrySet()) {
            Class<?> clazz = i.getKey();
            InjectDependencies(clazz, MapClaObj.get(clazz));
            PostConstruct(clazz, MapClaObj.get(clazz));
        }
        contextStatus_ = ContextStatus.STARTED;
    }

    @Override
    public boolean isRunning() {
        return contextStatus_ == ContextStatus.STARTED;
    }

    @Override
    public boolean containsBean(String name) {
        if (contextStatus_ == ContextStatus.NOT_STARTED) {
            throw new ApplicationContextNotStartedException("ContainsBean failed.");
        }
        return MapStrCla.containsKey(name);
    }

    @Override
    public Object getBean(String name) {
        return (Object) getBean(MapStrCla.get(name));
    }

    @Override
    public boolean isSingleton(String name) {
        if (!MapStrCla.containsKey(name)) throw new NoSuchBeanDefinitionException("No such bean.");
        return (MapClaSco.get(MapStrCla.get(name)) == BeanScope.SINGLETON);
    }

    @Override
    public boolean isPrototype(String name) {
        if (!MapStrCla.containsKey(name)) throw new NoSuchBeanDefinitionException("No such bean.");
        return (MapClaSco.get(MapStrCla.get(name)) == BeanScope.PROTOTYPE);
    }

    @Override
    public <T> T getBean(Class<T> clazz) {
        if (contextStatus_ == ContextStatus.NOT_STARTED) {
            throw new ApplicationContextNotStartedException("ContainsBean failed.");
        }
        if (!MapStrCla.containsValue(clazz)) {
            throw new NoSuchBeanDefinitionException("No such bean.");
        }
        if (MapClaSco.get(clazz) == BeanScope.SINGLETON) {
            return (T) MapClaObj.get(clazz);
        } else {
            return (T) BeanConstructor(clazz);
        }
    }

    protected enum ContextStatus {
        NOT_STARTED,
        STARTED
    }
}