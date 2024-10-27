import utilities.Colour;

public class Display {
    public static void displayBoard(Board board) {


        char horizontalLine = '\u2550'; // ━
        char verticalLine = '\u2551'; // ┃
        char topLeftCorner = '\u2554'; // ┏
        char topRightCorner = '\u2557'; // ┓
        char bottomLeftCorner = '\u255A'; // ┗
        char bottomRightCorner = '\u255D'; // ┛
        char verticalRightT = '\u2560'; // ┣
        char verticalLeftT = '\u2563'; // ┫
        char horizontalDownT = '\u2566'; // ┳
        char horizontalUpT = '\u2569'; // ┻
        char cross = '\u256C'; // ╋
        char dashedVerticalLine = '\u250A';


        //top of the board
        System.out.print(topLeftCorner);
        for (int i = 0; i < 27; i++) {
            System.out.print(horizontalLine);
        }
        System.out.print(horizontalDownT);
        for (int i = 0; i < 27; i++) {
            System.out.print(horizontalLine);
        }
        System.out.println(topRightCorner);


        //middle of the board
        for (int i = 0; i < board.maxPoint(); i++) {
            System.out.print(verticalLine);
            for (int j = 0; j < 6; j++) {
                for (int k = 0; k < 3; k++) {
                    System.out.print(" ");
                }
                if(board.getPoint(12+j).getCount() > i){
                    System.out.print(board.getPoint(12+j).getColor().shader() + "O" + Colour.NONE.shader());
                } else {
                    System.out.print(dashedVerticalLine);
                }
            }
            for (int j = 0; j < 3; j++) {
                System.out.print(" ");
            }
            System.out.print(verticalLine);
            for (int j = 0; j < 6; j++) {
                for (int k = 0; k < 3; k++) {
                    System.out.print(" ");
                }
                if(board.getPoint(18+j).getCount() > i){
                    System.out.print(board.getPoint(18+j).getColor().shader() + "O" + Colour.NONE.shader());
                } else {
                    System.out.print(dashedVerticalLine);
                }
            }
            for (int j = 0; j < 3; j++) {
                System.out.print(" ");
            }
            System.out.println(verticalLine);
        }

        //middle bar of the board
        System.out.print(verticalRightT);
        for (int i = 0; i < 27; i++) {
            System.out.print(horizontalLine);
        }
        System.out.print(cross);
        for (int i = 0; i < 27; i++) {
            System.out.print(horizontalLine);
        }
        System.out.println(verticalLeftT);
        for (int i = 0; i < board.maxPoint(); i++) {
            System.out.print(verticalLine);
            for (int j = 5; j >= 0; j--) {
                for (int k = 0; k < 3; k++) {
                    System.out.print(" ");
                }
                if(board.getPoint(6+j).getCount() > i){
                    System.out.print(board.getPoint(6+j).getColor().shader() + "O" + Colour.NONE.shader());
                } else {
                    System.out.print(dashedVerticalLine);
                }
            }
            for (int j = 0; j < 3; j++) {
                System.out.print(" ");
            }
            System.out.print(verticalLine);
            for (int j = 5; j >= 0; j--) {
                for (int k = 0; k < 3; k++) {
                    System.out.print(" ");
                }
                if(board.getPoint(j).getCount() > i){
                    System.out.print(board.getPoint(j).getColor().shader() + "O" + Colour.NONE.shader());
                } else {
                    System.out.print(dashedVerticalLine);
                }
            }
            for (int j = 0; j < 3; j++) {
                System.out.print(" ");
            }
            System.out.println(verticalLine);
        }
        System.out.print(bottomLeftCorner);
        for (int i = 0; i < 27; i++) {
            System.out.print(horizontalLine);
        }
        System.out.print(horizontalUpT);
        for (int i = 0; i < 27; i++) {
            System.out.print(horizontalLine);
        }
        System.out.println(bottomRightCorner);
    }
}
