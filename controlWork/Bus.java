package controlWork;

public class Bus extends AbstractAuto{
    private int doors_;
    private int places_;

    Bus(String name, int color, int rating, int doors, int places) {
        name_ = name;
        color_ = color;
        rating_ = rating;
        places_ = places;
        doors_ = doors;
    }

    @Override
    public int Cost() {
        return k1 * rating_ * doors_ + k2 * places_ - k3 * color_;
    }
}
