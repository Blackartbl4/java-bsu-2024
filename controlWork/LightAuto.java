package controlWork;

public class LightAuto extends AbstractAuto{
    private int max_speed_;
    LightAuto(String name, int color, int rating, int max_speed) {
        name_ = name;
        color_ = color;
        rating_ = rating;
        max_speed_ = max_speed;
    }

    @Override
    public int Cost(){
        return k1 * rating_ * max_speed_ - k2 * color_;
    }
}
