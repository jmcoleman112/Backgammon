import utilities.Colour;

public class Display {
    private static final char horizontalLine = '\u2550'; // ━
    private static final char verticalLine = '\u2551'; // ┃
    private static final char topLeftCorner = '\u2554'; // ┏
    private static final char topRightCorner = '\u2557'; // ┓
    private static final char bottomLeftCorner = '\u255A'; // ┗
    private static final char bottomRightCorner = '\u255D'; // ┛
    private static final char verticalRightT = '\u2560'; // ┣
    private static final char verticalLeftT = '\u2563'; // ┫
    private static final char horizontalDownT = '\u2566'; // ┳
    private static final char horizontalUpT = '\u2569'; // ┻
    private static final char cross = '\u256C'; // ╋
    private static final char dashedVerticalLine = '\u250A';
    private static final char box = '\u2588'; // █

    public static void displayBoard(Board board, int player, Match match) {
        String[] doubleDice = getDoubleDice();
        printPipNumbers(board, true, player);

        //top of the board
        System.out.print(topLeftCorner);
        printBars(27);
        System.out.print(horizontalDownT);
        System.out.print(horizontalLine);
        System.out.print(horizontalDownT);
        printBars(27);
        System.out.print(horizontalDownT);
        System.out.print(horizontalLine);
        System.out.print(horizontalLine);
        System.out.print(topRightCorner);
        printDoubleDice(0, 1, match); //Top layer of dice



        //middle top of the board
        printMiddle(board, 0, match);

        //middle bar of the board
        System.out.print(verticalRightT);
        printBars(27);
        System.out.print(cross);
        System.out.print(horizontalLine);
        System.out.print(cross);
        printBars(27);
        System.out.print(cross);
        System.out.print(horizontalLine);
        System.out.print(horizontalLine);
        System.out.print(verticalLeftT);
        printDoubleDice(1, -1, match);

        //Middle bottom of board
        printMiddle(board, 1, match);

        //bottom of board
        System.out.print(bottomLeftCorner);
        printBars(27);
        System.out.print(horizontalUpT);
        System.out.print(horizontalLine);
        System.out.print(horizontalUpT);
        printBars(27);
        System.out.print(horizontalUpT);
        System.out.print(horizontalLine);
        System.out.print(horizontalLine);
        System.out.print(bottomRightCorner);
        printDoubleDice(3, 0, match); //Top layer of dice

        printPipNumbers(board, false, player);

        printOverlay(match, player);
    }

    private static void printBars(int count){
        for (int i = 0; i < count; i++) {
            System.out.print(horizontalLine);
        }
    }

    private static void printMiddle(Board board, int bottom, Match match){
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
            System.out.print(verticalLine);

            if(i+1 <= 2) printDoubleDice(i+1, (bottom + 1) % 2, match);
            else if(i == 4) printDoubleDice(bottom == 1 ? 2 : 0, -1, match);
            else System.out.println();
        }
    }

    public static void printDiceFace(int number1, int number2, boolean doubleRoll) {
        String[] dice1 = getDiceFace(number1);
        String[] dice2 = number2 == -1 ? new String[5] : getDiceFace(number2);

        if(doubleRoll){

            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 4; j++) {
                    System.out.print(dice1[i] + "   ");
                }
                System.out.println();
            }
        }
        else{
            for (int i = 0; i < 5; i++) {
                if (number2 == -1) {
                    System.out.println(dice1[i]);
                } else {
                    System.out.println(dice1[i] + "   " + dice2[i]);
                }
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
        }

        System.out.println();
    }
    public static void displayPipCount(Board board, Players players){
        System.out.println("Pip count for " + players.getPlayerName(0) + resetColour() + " is: " + board.getTotalPipCount(0));
        System.out.println("Pip count for " + players.getPlayerName(1) + resetColour() + " is: " + board.getTotalPipCount(1) + "\n");
    }

    public static void printOverlay(Match match, int player){
        System.out.print(topLeftCorner);
        printBars(60);
        System.out.println(topRightCorner);
        System.out.print(verticalLine);
        if(match.getRedScore()>9){
            System.out.print(" "+Colour.RED.shader()+match.getRedScore()+Colour.NONE.shader()+" - ");
        }
        else{
            System.out.print("  "+Colour.RED.shader()+match.getRedScore()+Colour.NONE.shader()+" - ");
        }
        if(match.getBlueScore()>9){
            System.out.print(Colour.BLUE.shader()+match.getBlueScore()+Colour.NONE.shader()+" ");
        }
        else{
            System.out.print(Colour.BLUE.shader()+match.getBlueScore()+Colour.NONE.shader()+"  ");
        }
        System.out.print(dashedVerticalLine);
        if(match.getMatchLength()>9){
            System.out.print(" Match Length: " + match.getMatchLength()+" ");
        }
        else{
            System.out.print(" Match Length: " + match.getMatchLength()+"  ");
        }
        System.out.print(dashedVerticalLine);
        if(match.getDoubleCount()>9){
            System.out.print(Colour.getplayercolour(match.getDoubleOwner())+" Current Stake: " +match.getDoubleCount()+" " + resetColour());
        }
        else{
            System.out.print(Colour.getplayercolour(match.getDoubleOwner())+" Current Stake: " +match.getDoubleCount()+"  " + resetColour());
        }
        System.out.print(dashedVerticalLine);
        System.out.print(" " + Colour.getplayercolour(player) + box+" to Play " + resetColour());
        System.out.println(verticalLine);
        System.out.print(bottomLeftCorner);
        printBars(60);
        System.out.println(bottomRightCorner);



    }

    public static void printGameWinMessage(Players players, int player, Match match){
        System.out.println("=-=-=-=-=-=-=-"+players.getPlayerName(player) + resetColour() + " Wins! =-=-=-=-=-=-=-");
        System.out.println("Match Score is now: " + match.printScore());
    }

    public static void printMatchWinMessage(Players players, int player, Match match){
        System.out.println("Game Over");
        System.out.println("Congratulations " + players.getPlayerName(player) + resetColour() + "! You win!");
        System.out.println("Final score was: " + match.printScore());
    }

    public static void displayHint(boolean rolled, boolean started){
        System.out.println("Hint of all allowed command:");
        if (started) {
            System.out.println("Enter 'pip' to display pip count for both players");
            System.out.println("Enter 'board' to display game board");
        }
        System.out.println("Enter 'q' to quit game");
        if (!rolled){
            System.out.println("Enter 'roll' to roll dice"); //Can only do if haven't rolled already. Add this/////////////////
        }
        else{
            System.out.println("To make a move choose an option from the following list:");
        }
        //////////Add moves
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

    private static String[] getDoubleDice(){
        return new String[]{
                "┌───┐",
                "│ D │",
                "└───┘"
        };
    }

    private static void printDoubleDice(int layer, int player, Match match){
        if(player != match.getDoubleOwner()){
            System.out.println();
        }
        else{
            String[] doubleDice = getDoubleDice();
            if(player == 0) {
                System.out.print(Colour.RED.shader());
                if(layer == 2) layer = 0; // Need to print in reverse order
                if(layer == 3) layer = 2; //Actually printing layer 2
            }
            else  if (player == 1) System.out.print(Colour.BLUE.shader());
            System.out.println("  " + doubleDice[layer] + resetColour());
        }
    }

    public static String resetColour(){
        return Colour.NONE.shader();
    }
}
