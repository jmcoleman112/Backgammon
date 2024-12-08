//By Group 33: Jack Coleman (21207103) and Naoise Golden (21376026)
//GITHUB ID: jmcoleman112, NaoiseG

package Model;

import View.Colour;

//Class for point on board
public class Point {

    private Colour color;
    private int count;
    private final int pipNumber;

    public Point(Colour color, int count, int pipNumber) {
        this.color = color;
        this.count = count;
        this.pipNumber = pipNumber;
    }

    public Colour getColor() {
        return color;
    }

    public void setColor(Colour color) {
        this.color = color;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    //Returns pip number based on current player
    public int getPipNumber(int player) {
        if (player == 0) return pipNumber;
        else if (player == 1) return 25 - pipNumber;
        else return 0; //Invalid player
    }

    //Pip count based on player
    public int getPipCount(int player) {
        if (player == 0 && color == Colour.RED) return getPipNumber(player) * getCount();
        else if (player == 1 && color == Colour.BLUE) return getPipNumber(player) * getCount();
        else return 0; //Player has no pips on point
    }

}
