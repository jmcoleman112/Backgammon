//By Group 33: Jack Coleman (21207103) and Naoise Golden (21376026)
//GITHUB ID: jmcoleman112, NaoiseG

package Model;

import View.Colour;

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
