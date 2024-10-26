import utilities.Colour;

public class Point {

    private Colour color;
    private int count;

    public Point(Colour color, int count) {
        this.color = color;
        this.count = count;
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

}
