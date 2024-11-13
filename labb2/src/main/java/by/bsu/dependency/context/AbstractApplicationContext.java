package labb2.src.main.java.by.bsu.dependency.context;

import labb2.src.main.java.by.bsu.dependency.annotation.Bean;
import labb2.src.main.java.by.bsu.dependency.annotation.BeanScope;
import labb2.src.main.java.by.bsu.dependency.exceptions.ApplicationContextNotStartedException;
import labb2.src.main.java.by.bsu.dependency.exceptions.NoSuchBeanDefinitionException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractApplicationContext implements ApplicationContext {
    private Map<String, Class<?>> MapStrCla = new HashMap<String, Class<?>>();
    private Map<Class<?>, Object> MapClaObj = new HashMap<>();
    private ContextStatus contextStatus_ = ContextStatus.NOT_STARTED;

    protected void ConstructorHelper(Class<?> clazz) {
        if (clazz.getAnnotation(Bean.class) != null) {
            if (clazz.getAnnotation(Bean.class).name().isEmpty()) {
                String name = clazz.getSimpleName();
                String str = name.substring(0, 2).toLowerCase() + name.substring(2);
                MapStrCla.put(str, clazz);
            } else MapStrCla.put(clazz.getAnnotation(Bean.class).name(), clazz);
        }
    }

    @Override
    public void start() {
        if (contextStatus_ == ContextStatus.STARTED) return;
        for (var name : MapStrCla.entrySet()) {
            Class<?> clazz = name.getValue();
            try {
                Constructor<?> constructor = clazz.getConstructor();
                constructor.setAccessible(true);
                Object beanObject = constructor.newInstance();
                MapClaObj.put(clazz, beanObject);
            } catch (InvocationTargetException | NoSuchMethodException | InstantiationException |
                     IllegalAccessException e) {
                throw new RuntimeException(e);
            }
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
        if (contextStatus_ == ContextStatus.NOT_STARTED) {
            throw new ApplicationContextNotStartedException("ContainsBean failed.");
        }
        if (!containsBean(name)) throw new NoSuchBeanDefinitionException("No such bean.");
        return MapClaObj.get(MapStrCla.get(name));
    }

    @Override
    public boolean isSingleton(String name) {
        if (!MapStrCla.containsKey(name)) throw new NoSuchBeanDefinitionException("No such bean.");
        return (MapStrCla.get(name).getAnnotation(Bean.class).scope() == BeanScope.SINGLETON);
    }

    @Override
    public boolean isPrototype(String name) {
        if (!MapStrCla.containsKey(name)) throw new NoSuchBeanDefinitionException("No such bean.");
        return (MapStrCla.get(name).getAnnotation(Bean.class).scope() == BeanScope.PROTOTYPE);
    }

    @Override
    public <T> T getBean(Class<T> clazz) {
        if (contextStatus_ == ContextStatus.NOT_STARTED)
            throw new ApplicationContextNotStartedException("ContainsBean failed.");
        if (!MapClaObj.containsKey(clazz)) throw new NoSuchBeanDefinitionException("No such bean.");
        if (((Bean) MapClaObj.get(clazz)).scope() == BeanScope.SINGLETON) {
            return (T) MapClaObj.get(clazz);
        } else {
            Object obj = MapClaObj.get(clazz);
            try {
                Constructor<?> constructor = clazz.getConstructor();
                constructor.setAccessible(true);
                Object newBean = constructor.newInstance();
                MapClaObj.replace(clazz, obj, newBean);
            } catch (InvocationTargetException | NoSuchMethodException | InstantiationException |
                     IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            return (T) obj;
        }
    }

    protected enum ContextStatus {
        NOT_STARTED,
        STARTED
    }

}