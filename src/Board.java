import utilities.Colour;
import java.util.ArrayList;
import java.util.List;


public class Board {

    private final Point[] points;
    private final Bar[] bars; //Player1, Player2
    private final End[] ends;
    private int winner;

    public Board(){
        winner = -1;

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

    public Board(int[] red, int[] blue){ //Test board
        points = new Point[24];
        bars = new Bar[2];
        ends = new End[2];

        bars[0] = new Bar(Colour.RED);
        bars[1] = new Bar(Colour.BLUE);

        bars[0].setCount(red[24]);
        bars[1].setCount(blue[24]);

        ends[0] = new End(Colour.RED);
        ends[1] = new End(Colour.BLUE);
        ends[0].setCount(red[25]);
        ends[1].setCount(blue[25]);

        for (int i = 0; i < 24; i++){
            if (red[i] > 0){
                points[i] = new Point(Colour.RED, red[i], i + 1);
            }
            else if (blue[i] > 0){
                points[i] = new Point(Colour.BLUE, blue[i], i + 1);
            }
            else{
                points[i] = new Point(Colour.NONE, 0, i + 1);
            }
        }
    }

    public Point getPoint(int index){

        if(index < 0 || index > 23){
            return new Point(Colour.NONE, 0, 0);
        }

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
            for (int i = 6; i < 24; i++){
                if (points[i].getColor() == getPlayerColor(player)){
                    count += 1;
                }
            }
            count += bars[0].getCount();
        }
        else {
            for (int i = 0; i < 19; i++){
                if (points[i].getColor() == getPlayerColor(player)){
                    count += 1;
                }
                count += bars[1].getCount();
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

    public boolean noGameWinner(){
        for (int i = 0; i <= 1; i++){
            if(getTotalPipCount(i) == 0){
                winner = i;
                return false;
            }
        }
        return true;
    }

    public int getWinner(){
        return winner;
    }

    public void setWinner(int player){
        winner = player;
    }



    public void setBoardFromString(String boardConfig) {
        String[] parts = boardConfig.split(" ");
        if (!parts[0].equals("setboard") || parts.length != 25) {
            throw new IllegalArgumentException("Invalid board configuration string");
        }

        for (int i = 1; i < parts.length; i++) {
            String pointConfig = parts[i];
            int count = Character.getNumericValue(pointConfig.charAt(0));
            char colorChar = pointConfig.charAt(1);
            Colour color = switch (colorChar) {
                case 'N' -> Colour.NONE;
                case 'R' -> Colour.RED;
                case 'B' -> Colour.BLUE;
                default -> throw new IllegalArgumentException("Invalid color character: " + colorChar);
            };

            Point point = getPoint(i - 1);
            point.setCount(count);
            point.setColor(color);
        }
    }
    public String Wintype() {
        int winner = getWinner();
        if (winner == -1) {
            return "No winner";
        }

        int loser = (winner + 1) % 2;

        if (getEnd(loser).getCount() > 0) {
            return "Single";
        }

        if (getBar(loser).getCount() > 0) {
            return "Backgammon";
        }

        for (int i = (winner == 1 ? 18 : 0); i <= (winner == 1 ? 23 : 5); i++) {
            if (getPoint(i).getCount() > 0) {
                return "Backgammon";
            }
        }

        for (int i = 6; i <= 17; i++) {
            if (getPoint(i).getCount() > 0) {
                return "Gammon";
            }
        }

        return "Single";
    }
}
