import utilities.Colour;

public class Display {
    private static char horizontalLine = '\u2550'; // ━
    private static final char verticalLine = '\u2551'; // ┃
    private static char topLeftCorner = '\u2554'; // ┏
    private static char topRightCorner = '\u2557'; // ┓
    private static char bottomLeftCorner = '\u255A'; // ┗
    private static char bottomRightCorner = '\u255D'; // ┛
    private static char verticalRightT = '\u2560'; // ┣
    private static char verticalLeftT = '\u2563'; // ┫
    private static char horizontalDownT = '\u2566'; // ┳
    private static char horizontalUpT = '\u2569'; // ┻
    private static char cross = '\u256C'; // ╋
    private static char dashedVerticalLine = '\u250A';

    public static void displayBoard(Board board, int player) {
        printPipNumbers(board, true, player);

        //top of the board
        System.out.print(topLeftCorner);
        printBars();
        System.out.print(horizontalDownT);
        System.out.print(horizontalLine);
        System.out.print(horizontalDownT);
        printBars();
        System.out.print(horizontalDownT);
        System.out.print(horizontalLine);
        System.out.print(horizontalLine);
        System.out.println(topRightCorner);


        //middle top of the board
        printMiddle(board, 0);

        //middle bar of the board
        System.out.print(verticalRightT);
        printBars();
        System.out.print(cross);
        System.out.print(horizontalLine);
        System.out.print(cross);
        printBars();
        System.out.print(cross);
        System.out.print(horizontalLine);
        System.out.print(horizontalLine);
        System.out.println(verticalLeftT);

        //Middle bottom of board
        printMiddle(board, 1);

        //bottom of board
        System.out.print(bottomLeftCorner);
        printBars();
        System.out.print(horizontalUpT);
        System.out.print(horizontalLine);
        System.out.print(horizontalUpT);
        printBars();
        System.out.print(horizontalUpT);
        System.out.print(horizontalLine);
        System.out.print(horizontalLine);
        System.out.println(bottomRightCorner);

        printPipNumbers(board, false, player);
    }

    private static void printBars(){
        for (int i = 0; i < 27; i++) {
            System.out.print(horizontalLine);
        }
    }

    private static void printMiddle(Board board, int bottom){
        int i_start = bottom == 1 ? board.maxPoint() - 1 : 0;
        int i_end = bottom == 1 ? -1 : board.maxPoint();
        int i_step = bottom == 1 ? -1 : 1;

        int j_start = bottom == 1 ? 5 : 0;
        int j_end = bottom == 1 ? -1 : 6;
        int j_step = bottom == 1 ? -1 : 1;

        for (int i = i_start; i != i_end; i += i_step) {
            System.out.print(verticalLine);
            for (int j = j_start; j != j_end; j += j_step) {
                for (int k = 0; k < 3; k++) {
                    System.out.print(" ");
                }
                if(board.getPoint(12 + j - 6 * bottom).getCount() > i){
                    System.out.print(board.getPoint(12 + j - 6 * bottom).getColor().shader() + "O" + Colour.NONE.shader());
                } else {
                    System.out.print(dashedVerticalLine);
                }
            }
            for (int j = 0; j < 3; j++) {
                System.out.print(" ");
            }
            System.out.print(verticalLine); //Print BAR
            if(board.getBar(1 - bottom).getCount() > i){ //Counter on bar
                System.out.print(board.getBar(1 - bottom).getColor().shader() + "O" + Colour.NONE.shader());
            }
            else System.out.print(" ");
            System.out.print(verticalLine);

            for (int j = j_start; j != j_end; j += j_step) {
                for (int k = 0; k < 3; k++) {
                    System.out.print(" ");
                }
                if(board.getPoint(18 - 18 * bottom + j).getCount() > i){
                    System.out.print(board.getPoint(18 - 18 * bottom + j).getColor().shader() + "O" + Colour.NONE.shader());
                } else {
                    System.out.print(dashedVerticalLine);
                }
            }
            for (int j = 0; j < 3; j++) {
                System.out.print(" ");
            }
            System.out.print(verticalLine);
            if(board.getEnd(1 - bottom).getCount() > i){
                System.out.print(board.getEnd(1 - bottom).getColor().shader() + "O" + Colour.NONE.shader());
            }
            else System.out.print(dashedVerticalLine);
            if(board.getEnd(1 - bottom).getCount() > i + board.maxPoint()){
                System.out.print(board.getEnd(1 - bottom).getColor().shader() + "O" + Colour.NONE.shader());
            }
            else System.out.print(dashedVerticalLine);
            System.out.println(verticalLine);

        }
    }

    public static void printDiceFace(int number1, int number2) {
        String[] dice1 = getDiceFace(number1);
        String[] dice2 = number2 == -1 ? new String[5] : getDiceFace(number2);

        for (int i = 0; i < 5; i++) {
            if (number2 == -1) {
                System.out.println(dice1[i]);
            } else {
                System.out.println(dice1[i] + "   " + dice2[i]);
            }
        }
    }

    public static void printPipNumbers(Board board, boolean top, int player) {
        System.out.print("   ");
        if (top){
            for (int i = 12; i <= 17; i++){
                Point point = board.getPoint(i);
                int pipNumber = point.getPipNumber(player);
                System.out.printf("%2d  ", pipNumber);
            }
            System.out.print(" BAR  ");
            for (int i = 18; i <= 23; i++){
                Point point = board.getPoint(i);
                int pipNumber = point.getPipNumber(player);
                System.out.printf("%2d  ", pipNumber);
            }
            System.out.print("  END");
        }
        else {
            for (int i = 11; i >= 6; i--){
                Point point = board.getPoint(i);
                int pipNumber = point.getPipNumber(player);
                System.out.printf("%2d  ", pipNumber);
            }
            System.out.print(" BAR  ");
            for (int i = 5; i >= 0; i--){
                Point point = board.getPoint(i);
                int pipNumber = point.getPipNumber(player);
                System.out.printf("%2d  ", pipNumber);
            }
            System.out.print("  END");
            System.out.println("\n");
        }

        System.out.println();
    }
    public static void displayPipCount(Board board, Players players){
        System.out.println("Pip count for " + players.getPlayerName(0) + resetColour() + " is: " + board.getTotalPipCount(0));
        System.out.println("Pip count for " + players.getPlayerName(1) + resetColour() + " is: " + board.getTotalPipCount(1) + "\n");
    }

    public static void printWinMessage(Players players, int player){
        System.out.println("Game Over");
        System.out.println("Congratulations " + players.getPlayerName(player) + resetColour() + "! You win!");
    }

    private static String[] getDiceFace(int number) {
        return switch (number) {
            case 1 -> new String[]{
                    "┌───────┐",
                    "│       │",
                    "│   ●   │",
                    "│       │",
                    "└───────┘"
            };
            case 2 -> new String[]{
                    "┌───────┐",
                    "│ ●     │",
                    "│       │",
                    "│     ● │",
                    "└───────┘"
            };
            case 3 -> new String[]{
                    "┌───────┐",
                    "│ ●     │",
                    "│   ●   │",
                    "│     ● │",
                    "└───────┘"
            };
            case 4 -> new String[]{
                    "┌───────┐",
                    "│ ●   ● │",
                    "│       │",
                    "│ ●   ● │",
                    "└───────┘"
            };
            case 5 -> new String[]{
                    "┌───────┐",
                    "│ ●   ● │",
                    "│   ●   │",
                    "│ ●   ● │",
                    "└───────┘"
            };
            case 6 -> new String[]{
                    "┌───────┐",
                    "│ ●   ● │",
                    "│ ●   ● │",
                    "│ ●   ● │",
                    "└───────┘"
            };
            default -> new String[]{
                    "Error: Invalid dice number."
            };
        };
    }

    public static String resetColour(){
        return Colour.NONE.shader();
    }
}
