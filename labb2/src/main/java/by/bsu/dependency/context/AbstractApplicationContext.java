package labb2.src.main.java.by.bsu.dependency.context;

import labb2.src.main.java.by.bsu.dependency.annotation.Bean;
import labb2.src.main.java.by.bsu.dependency.annotation.BeanScope;
import labb2.src.main.java.by.bsu.dependency.annotation.Inject;
import labb2.src.main.java.by.bsu.dependency.exceptions.ApplicationContextNotStartedException;
import labb2.src.main.java.by.bsu.dependency.exceptions.NoSuchBeanDefinitionException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public abstract class AbstractApplicationContext implements ApplicationContext {
    private Map<String, Class<?>> MapStrCla = new HashMap<String, Class<?>>();
    private Map<Class<?>, Object> MapClaObj = new HashMap<>();
    private Map<Class<?>, BeanScope> MapClaSco = new HashMap<>();
    private Set<Map<Class<?>, Set<Class<?>>>> pizdec = new HashSet<>();
    private ContextStatus contextStatus_ = ContextStatus.NOT_STARTED;

    protected Object BeanConstructor(Class<?> clazz) {
        try {
            Constructor<?> constructor = clazz.getConstructor();
            constructor.setAccessible(true);
            Object beanObject = constructor.newInstance();
            Field fields[] = clazz.getDeclaredFields();
            for (var field : fields) {
                field.setAccessible(true);

                if (field.getAnnotation(Inject.class) != null ) {
                    if (field.getClass().getAnnotation(Bean.class) != null) {
                        if (MapClaSco.get(field.getClass()) == BeanScope.SINGLETON) {
                            field.set(MapClaObj.get(clazz), MapClaObj.get(field.getClass()));
                        } else {
                            field.set(MapClaObj.get(clazz), BeanConstructor(field.getClass()));
                        }
                    } else if (MapClaSco.get(field.getClass()) == BeanScope.SINGLETON) {
                        field.set(MapClaObj.get(clazz), MapClaObj.get(field.getClass()));
                    }
                }
            }
            return beanObject;
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException |
                IllegalAccessException ig) {
            throw new RuntimeException(ig);
        }
    }

    protected void ConstructorHelper(Class<?> clazz) {
        if (clazz.getAnnotation(Bean.class) != null) {
            if (clazz.getAnnotation(Bean.class).name().isEmpty()) {
                String name = clazz.getSimpleName();
                String str = name.substring(0, 2).toLowerCase() + name.substring(2);
                MapStrCla.put(str, clazz);
                MapClaSco.put(clazz, clazz.getAnnotation(Bean.class).scope());
            } else MapStrCla.put(clazz.getAnnotation(Bean.class).name(), clazz);
        } else {
            String name = clazz.getSimpleName();
            String str = name.substring(0, 2).toLowerCase() + name.substring(2);
            MapStrCla.put(str, clazz);
            MapClaSco.put(clazz, BeanScope.SINGLETON);
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
        for (var i : MapClaObj.entrySet()) {
            Class<?> clazz = i.getKey();
            if (MapClaSco.get(clazz) == BeanScope.SINGLETON) {
                BeanConstructor(clazz);
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
        if (MapClaSco.get(MapStrCla.get(name)) == BeanScope.SINGLETON) {
            return MapClaObj.get(MapStrCla.get(name));
        }
        else if (MapClaSco.get(MapStrCla.get(name)) == BeanScope.PROTOTYPE){
            return BeanConstructor(MapStrCla.get(name));
        }
        return null;
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
        if (contextStatus_ == ContextStatus.NOT_STARTED)
            throw new ApplicationContextNotStartedException("ContainsBean failed.");
        if (!MapClaObj.containsKey(clazz)) throw new NoSuchBeanDefinitionException("No such bean.");
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