package utilities;

public class Bar extends Point{

    public Bar(Colour colour){
        super(colour, 0, 25);
    }

    @Override
    public int getPipCount(int player){
        return getCount() * 25;
    }
}
