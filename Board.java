import utilities.Colour;
import java.util.ArrayList;
import java.util.List;


public class Board {

    private final Point[] points;
    private final Bar[] bars; //Player1, Player2
    private final End[] ends;

    public Board(){
        points = new Point[24];
        bars = new Bar[2];
        ends = new End[2];


        points[0] = new Point(Colour.BLUE, 2, 1);

        points[5] = new Point(Colour.RED, 5, 6);
        points[7] = new Point(Colour.RED, 3, 8);
        points[11] = new Point(Colour.BLUE, 5, 12);
        points[12] = new Point(Colour.RED, 5, 13);
        points[16] = new Point(Colour.BLUE, 3, 17);
        points[18] = new Point(Colour.BLUE, 5, 19);
        points[23] = new Point(Colour.RED, 2, 24);

        bars[0] = new Bar(Colour.RED);
        bars[1] = new Bar(Colour.BLUE);

        ends[0] = new End(Colour.RED);
        ends[1] = new End(Colour.BLUE);

        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                points[i] = new Point(Colour.NONE, 0, i + 1);
            }
        }
    }

    public Point getPoint(int index){
        return points[index];
    }

    public int getPointCount(int index){
        return points[index].getCount();
    }

    public Colour getPointColor(int index){
        if (index < 0 || index > 23) {
            return Colour.NONE;
        }
        return points[index].getColor();
    }

    public Bar getBar(int index){
        return bars[index];
    }

    public End getEnd(int index){
        return ends[index];
    }

    public Colour getPlayerColor(int player){
        return (player == 0) ? Colour.RED : Colour.BLUE;
    }

    public boolean bearoffcheck(int player){
        int count = 0;
        if(player == 0){
            for (int i = 0; i < 6; i++){
                if (points[i].getColor() == getPlayerColor(player)){
                    count += points[i].getCount();
                }
            }
        }
        else {
            for (int i = 18; i < 24; i++){
                if (points[i].getColor() == getPlayerColor(player)){
                    count += points[i].getCount();
                }
            }
        }
        return count == 0;
    }

    public int maxPoint(){
        int maxlength = 4; //Print a min height of 4
        for (Point point : points) {
            if (point.getCount() > maxlength) {
                maxlength = point.getCount();
            }
        }
        for (End end : ends) {
            int size_needed = (int) Math.ceil((double) end.getCount() / 2);
            if (size_needed > maxlength) {
                maxlength = size_needed;
            }
        }
        return maxlength;
    }

    public int getTotalPipCount(int player){
        int pipCount = 0;
            for (Point point : points) {
                pipCount += point.getPipCount(player);
            }
            Bar bar  = bars[player];
            pipCount += bar.getPipCount(player);

        return pipCount;
    }

    public List<Integer> getColoredPoints(Colour color) {
        List<Integer> coloredPointsList = new ArrayList<>();

        // Single pass to find and collect points with the specified color
        for (int i = 0; i < points.length; i++) {
            if (points[i].getColor() == color) {
                coloredPointsList.add(i);
            }
        }

        // Convert the list to an array
        return coloredPointsList;
    }

    public boolean checkWin(int player){
        return getTotalPipCount(player) == 0;
    }






}
