package by.bsu.dependency.myexample;


import by.bsu.dependency.annotation.Bean;

@Bean
public class Pin {
    private boolean hammered = false;

    public void Pull() {
        hammered = false;
        System.out.println("Niht good niht good, neharasho!");
    }

    public void Hammer() {
        hammered = true;
        System.out.println("Ya ya, das is good");
    }

    public void State() {
        if (hammered) {
            System.out.println("Alaska");
        } else {
            System.out.println("Virginia");
        }
    }
}
