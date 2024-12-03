import utilities.Colour;

public class End extends Point{

    public End(Colour colour){
        super(colour, 0, 0);
    }

    @Override
    public int getPipNumber(int player){ //Should never be called
        return 0; //Should always be 0 if called
    }
}
