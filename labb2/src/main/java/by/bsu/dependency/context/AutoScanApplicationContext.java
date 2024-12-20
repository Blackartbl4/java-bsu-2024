package by.bsu.dependency.context;

import by.bsu.dependency.annotation.Bean;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.util.Set;

public class AutoScanApplicationContext extends AbstractApplicationContext {

    /**
     * Создает контекст, содержащий классы из пакета {@code packageName}, помеченные аннотацией {@code @Bean}.
     * <br/>
     * Если имя бина в анноации не указано ({@code name} пустой), оно берется из названия класса.
     * <br/>
     * Подразумевается, что у всех классов, переданных в списке, есть конструктор без аргументов.
     *
     * @param packageName имя сканируемого пакета
     */
    public AutoScanApplicationContext(String packageName) {
        Reflections reflections = new Reflections(packageName, new SubTypesScanner(false));
        reflections.getSubTypesOf(Object.class).stream().filter(a -> a.isAnnotationPresent(Bean.class)).forEach(this::AddingClassesToMapByName);
    }
}
