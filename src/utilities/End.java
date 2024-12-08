package utilities;

//inherited class of the point class
public class End extends Point{


    //initilizer
    public End(Colour colour){
        super(colour, 0, 0);
    }

    //override point method to implement getPipNumber
    @Override
    public int getPipNumber(int player){ //Should never be called
        return 0; //Should always be 0 if called
    }
}
