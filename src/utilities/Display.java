package utilities;

public class Display { //Constants for printing board sections
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

    //Method to display entire board based on current player
    public static void displayBoard(Board board, int player, Match match) { //Static method
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

        //Overlay
        printOverlay(match, player);
    }

    //Prints solid line
    private static void printBars(int count){
        for (int i = 0; i < count; i++) {
            System.out.print(horizontalLine);
        }
    }

    //Repetitive spaces
    private static void printSpace(int count){
        for (int i = 0; i < count; i++) {
            System.out.print(" ");
        }
    }

    //Prints middle parts above and below middle bar depending on the argument bottom
    private static void printMiddle(Board board, int bottom, Match match){
        int i_start = bottom == 1 ? board.maxPoint() - 1 : 0;
        int i_end = bottom == 1 ? -1 : board.maxPoint();
        int i_step = bottom == 1 ? -1 : 1;

        int j_start = bottom == 1 ? 5 : 0;
        int j_end = bottom == 1 ? -1 : 6;
        int j_step = bottom == 1 ? -1 : 1;
        //Values set depending on top or bottom half

        for (int i = i_start; i != i_end; i += i_step) { //Print counter area
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
            else if(i == board.maxPoint() - 1) printDoubleDice(bottom == 1 ? 2 : 0, -1, match);
            else System.out.println();
        }
    }

    //Prints dice face after a roll
    public static void printDiceFace(int number1, int number2, boolean doubleRoll) {
        String[] dice1 = getDiceFace(number1);
        String[] dice2 = number2 == -1 ? new String[5] : getDiceFace(number2);

        if(doubleRoll){ //Print again if a double

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

    //Prints the pip numbers corresponding to the active player
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

    //Prints overlay containing match length, score etc
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

    //Prints message corresponding to winner
    public static void printGameWinMessage(Players players, int player, Match match, Board board){
        System.out.println("\n\n\033[32m━━━━━━━━━━━━━━━━━━ "+players.getPlayerName(player) + "\033[32m Wins With a "+board.Wintype()+ "! ━━━━━━━━━━━━━━━━━━━━"+Colour.NONE.shader());
        printSpace(20);
        System.out.println("Match Score is now: " + match.printScore());
    }

    //Prints message corresponding to winner
    public static void printMatchWinMessage(Players players, int player, Match match){
        System.out.print("\n\033[32m"+topLeftCorner);
        printBars(60);
        System.out.println(topRightCorner);

        System.out.print(verticalLine); printSpace(25);
        System.out.print("Match Over!");
        printSpace(24);System.out.println(verticalLine);


        System.out.print(verticalLine);printSpace(10);
        System.out.print("Congratulations " + players.getPlayerName(player) + "\033[32m! You have won the Match!");
        printSpace(9-players.getCurrentLength());System.out.println(verticalLine);

        System.out.print(verticalLine);printSpace(20);
        System.out.print("Final score was: " + match.printScore());
        printSpace(23-match.printScorelength());System.out.println("\u001B[32m"+verticalLine);



        System.out.print(bottomLeftCorner);
        printBars(60);
        System.out.println(bottomRightCorner+Colour.NONE.shader());
    }

    //Prints hints
    public static void displayHint(boolean doubleOwner, boolean rolled, boolean started){
        System.out.println("\n     Hint of all allowed commands:");
        if (started) {
            System.out.println("     Enter 'pip' to display pip count for both players");
            System.out.println("     Enter 'board' to display game board");
        }
        System.out.println("     Enter 'q' to quit game");
        if (!rolled){
            System.out.println("     Enter 'roll' to roll dice"); //Can only do if haven't rolled already.
            if(doubleOwner) {
                System.out.println("     Enter 'double' to double the stakes"); //Can only double if owns dice
            }
        }
        else{
            System.out.println("     To make a move choose an option from the above list:");
        }
    }

    //Returns string for dice printing corresponding to number
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

    //Returns string for double dice
    private static String[] getDoubleDice(){
        return new String[]{
                "┌───┐",
                "│ D │",
                "└───┘"
        };
    }

    //Prints a layer of double dice corresponding to location on board and dice owner
    private static void printDoubleDice(int layer, int player, Match match) {
        if (player != match.getDoubleOwner()) { //Player doesn't own dice
            System.out.println();
        } else {
            String[] doubleDice = getDoubleDice();
            if (player == 0) {
                System.out.print(Colour.RED.shader());
                if (layer == 2) layer = 0; //Need to print in reverse order
                if (layer == 3) layer = 2; //Actually printing layer 2
            } else if (player == 1) System.out.print(Colour.BLUE.shader());
            System.out.println("  " + doubleDice[layer] + resetColour());
        }
    }
    public static void welcomeMessage(){
        System.out.println();
        System.out.println();
        System.out.print(topLeftCorner);
        printBars(87);
        System.out.println(topRightCorner);
        System.out.print(verticalLine);
        System.out.print("Welcome to Backgammon! This is a implementation of Backgammon created by Jack Coleman  ");
        System.out.println(verticalLine);
        System.out.print(verticalLine);
        System.out.print("and Naoise Golden. For instructions on how to play, please read the README file.       ");
        System.out.println(verticalLine);

        System.out.print(bottomLeftCorner);
        printBars(87);
        System.out.println(bottomRightCorner);
        System.out.println();
        System.out.println();
    }


    //Resets display colour
    public static String resetColour(){
        return Colour.NONE.shader();
    }

    public static void displayplayerchange(int player){
        System.out.println(Colour.getplayercolour(player)+"\n━━━━━━━━━━━━━━━━━━━━━━━PLAYER CHANGE━━━━━━━━━━━━━━━━━━━━━━━━━━"+Colour.NONE.shader()+"\n");
    }
}
