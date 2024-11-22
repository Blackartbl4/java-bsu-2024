package by.bsu.dependency.myexample;

import by.bsu.dependency.context.SimpleApplicationContext;

public class Main {
    public static void main(String[] args) {
        SimpleApplicationContext context = new SimpleApplicationContext(Calendar.class, Sheet.class, Pin.class);
        context.start();

        Sheet sh1 = (Sheet) context.getBean(Sheet.class);
        sh1.CheckTheData();
        sh1.TearOff();

        Calendar calendar = (Calendar) context.getBean(Calendar.class);
        calendar.HammerPin();
        Pin pin = (Pin) context.getBean(Pin.class);
        pin.State();
    }
}
