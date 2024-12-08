//===============================================================
//By Group 33: Jack Coleman (21207103) and Naoise Golden (21376026)
//GITHUB ID: jmcoleman112, NaoiseG
//================================================================

package Model;


import View.Colour;

//inherited class of point class
public class Bar extends Point{


    //initilize Bar method
    public Bar(Colour colour){
        super(colour, 0, 25);
    }


    //override abstract for PIP method
    @Override
    public int getPipCount(int player){
        return getCount() * 25;
    } //Always 25 per counter
}
