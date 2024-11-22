package by.bsu.dependency.myexample;


import by.bsu.dependency.annotation.Bean;
import by.bsu.dependency.annotation.Inject;

@Bean
public class Calendar {
    @Inject
    private Pin pin;

    @Inject
    private Sheet sheet;

    public void HammerPin() {
        System.out.println("Всё ещё достоин!");
        pin.Hammer();
    }

    public void PullPin() {
        System.out.println("Уже нет(");
        pin.Pull();
    }
}
