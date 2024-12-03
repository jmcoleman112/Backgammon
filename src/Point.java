import utilities.Colour;

public class Point {

    private Colour color;
    private int count;
    private final int pipNumber;

    public Point(Colour color, int count, int pipNumber) {
        this.color = color;
        this.count = count;
        this.pipNumber = pipNumber;
    }

    // Getter and Setter for color
    public Colour getColor() {
        return color;
    }

    public void setColor(Colour color) {
        this.color = color;
    }

    // Getter and Setter for count
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPipNumber(int player) {
        if (player == 0) return pipNumber;
        else if (player == 1) return 25 - pipNumber;
        else return 0; //Invalid player
    }

    public int getPipCount(int player) {
        if (player == 0 && color == Colour.RED) return getPipNumber(player) * getCount();
        else if (player == 1 && color == Colour.BLUE) return getPipNumber(player) * getCount();
        else return 0; //Player has no pips on point
    }

}
