package by.bsu.dependency.myexample;


import by.bsu.dependency.annotation.Bean;
import by.bsu.dependency.annotation.BeanScope;
import by.bsu.dependency.annotation.PostConstruct;

@Bean(scope = BeanScope.PROTOTYPE)
public class Sheet {
    private String str;


    @PostConstruct
    private void Init() {
        str = "Опять праздник!";
    }

    public void TearOff() {
        System.out.println("И снова третье сентября!");
    }

    public void CheckTheData(){
        System.out.println(str);
    }
}
