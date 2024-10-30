import utilities.Colour;


public class Board {

    private final Point[] points;
    private final Bar[] bars; //Player1, Player2

    public Board(){
        points = new Point[24];
        bars = new Bar[2];

        points[0] = new Point(Colour.BLACK, 2);

        points[5] = new Point(Colour.RED, 5);
        points[7] = new Point(Colour.RED, 3);
        points[11] = new Point(Colour.BLACK, 5);
        points[12] = new Point(Colour.RED, 5);
        points[16] = new Point(Colour.BLACK, 3);
        points[18] = new Point(Colour.BLACK, 5);
        points[23] = new Point(Colour.RED, 2);

        bars[0] = new Bar();
        bars[1] = new Bar();

        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                points[i] = new Point(Colour.NONE, 0);
            }
        }
    }

    public Point getPoint(int index){
        return points[index];
    }

    public Bar getBar(int index){
        return bars[index];
    }

    public int maxPoint(){
        int maxlength = 0;
        for (Point point : points) {
            if (point.getCount() > maxlength) {
                maxlength = point.getCount();
            }
        }
        return maxlength;
    }








}
