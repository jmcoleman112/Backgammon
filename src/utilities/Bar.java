package utilities;


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
    }
}
