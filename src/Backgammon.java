import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Backgammon { //Class to run game logic
    private Board board;
    private final Dice dice;
    private final InputHandler inputHandler;
    private final MoveHandler moveHandler;
    private final Match match;
    private Players players;


    public Backgammon() {
        this.board = new Board();
        this.dice = new Dice();
        this.inputHandler = new InputHandler();
        this.moveHandler = new MoveHandler(board);
        this.match = new Match();
    }

    public static void main(String[] args){

        Backgammon game = new Backgammon();


//        {//Test
//            int[] red = new int[24];
//            int[] blue = new int[24];
//            Match match = new Match();
//            red[3] = 2;
//            blue[2] = 4;
//            Board board = new Board(red, blue);
//            game.setTestBoard(board);
//            game.getMoveHandler().setBoard(board);
////            Display.displayBoard(board, 0, match);
//        }



        InputHandler inputHandler = game.getInputHandler();
        String filename;
        filename = game.Welcome();
        if(filename != null){
            game.fileStart(filename);
        }
        else {
            game.setMatchLength("");
            game.setPlayers("", "");
            game.getPlayers().setCurrentPlayer(game.decideFirstPlayer());
        }
        while(!game.getBoard().checkWin()){ //Neither player has won
            int player = game.getPlayers().getCurrentPlayer();
            Display.displayBoard(game.getBoard(), player, game.getMatch());

            boolean turnInProgress = true;
            game.promptPlayer(player);
            String userInput=inputHandler.getInput();
            if(inputHandler.isfileCommand(userInput)){
                filename = userInput.substring(5);
                try {
                    try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
                        game.fileTurn(reader); // Pass the reader to the method
                    }
                } catch (IOException e) {
                    System.err.println("Error opening the file: " + e.getMessage());
                }
            }
            else {
                while (turnInProgress) {
                    turnInProgress = game.processTurn(player, userInput, null);
                    if (turnInProgress) {
                        game.promptPlayer(player);
                        userInput = inputHandler.getInput();
                    }
                }
                game.getPlayers().switchPlayer();
            }
            filename =null;
        }

        Display.printWinMessage(game.getPlayers(), game.getBoard().getWinner()); // Print message to winner
        inputHandler.closeScanner();
    }

    public void setPlayers(String player1Name, String player2Name){
        if(player1Name.isEmpty() || player2Name.isEmpty()) this.players = new Players();
        else {
            this.players = new Players(player1Name, player2Name);
        }
    }

    public Board getBoard(){
        return board;
    }

    public void setTestBoard(Board board){
        this.board = board;
        getMoveHandler().setBoard(board);
    }

    public Players getPlayers(){
        return players;
    }

    public InputHandler getInputHandler(){
        return inputHandler;
    }

    public MoveHandler getMoveHandler(){
        return moveHandler;
    }

    public String Welcome() { //Loop until game started
        System.out.println("\n\n====================================================================================================");
        System.out.println("Welcome to Backgammon! This is a implementation of Backgammon created by Jack Coleman and Naoise Golden.");
        System.out.println("For instructions on how to play, please read the README file.");
        System.out.println("====================================================================================================\n\n");
        System.out.flush();

        System.out.print("Please Hit Enter to begin the game: ");
        String welcomestring = inputHandler.getInput();
        if (inputHandler.isfileCommand(welcomestring)) {
            String filename = welcomestring.substring(5);
            try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
                return filename;
            } catch (IOException e) {
                System.err.println("Invalid Filename. Starting Game");
            }
        }
        return null;
    }

    public boolean processTurn(int player, String userInput, BufferedReader reader) { //One roll per turn
            int[] rollValues;

            if (inputHandler.isRollCommand(userInput)) {
                if(inputHandler.isrolltestcommand(userInput)){
                    String[] rolls = userInput.substring(5).split(" ");
                    rollValues = new int[]{Integer.parseInt(rolls[0]), Integer.parseInt(rolls[1])};
                    boolean doubleRoll = rollValues[0] == rollValues[1];
                    Display.printDiceFace(rollValues[0], rollValues[1], doubleRoll);
                }
                else{
                    rollValues = rollTurn(player);
                }
                if (!moveHandler.legalmoves(player, rollValues[0], rollValues[1])){ //No valid moves
                    return false;
                }
                chooseMove(player, rollValues, reader);
                if(rollValues[0] == rollValues[1]){
                    System.out.println("=======DOUBLES! Bonus Second Roll=====\n");
                    Display.displayBoard(getBoard(), player, match);
                    if (!moveHandler.legalmoves(player, rollValues[0], rollValues[1])){ //No valid moves
                        return false;
                    }
                    chooseMove(player, rollValues, reader);
                }
                return false; //Turn over
            }
            else if(inputHandler.isDoubleCommand(userInput)){
                if (match.doublelegality(player)) {
                    handleDoubleStakes(player);
                    Display.displayBoard(board, player, match);
                    return true;
                }
                else{
                    System.out.println("You cannot double the stakes at this time. Please enter a valid command");
                }
            }
            else if (inputHandler.isQuitCommand(userInput)) {
                quitGame();
                return false;
            } else if (inputHandler.isPipCommand(userInput)) {
                Display.displayPipCount(getBoard(), players);
                return true;
            } else if (inputHandler.isHintCommand(userInput)) { //Display hints
                Display.displayHint(false, true);
                return true;
            } else if (inputHandler.isBoardCommand(userInput)){
                Display.displayBoard(board, player, match);
                return true;
            }else {
                System.out.println("Error: Please enter a valid command\nFor a list of valid commands type 'hint'");
                return true;
            }
        return false; //Should be unreachable
    }

    public void chooseMove(int player, int[] rollValues, BufferedReader reader) {
        boolean turnInProgress = true;
        while (turnInProgress) {
            String moveInput;
            System.out.println(players.getPlayerName(player) + Display.resetColour() + " please choose a move from the list above (e.g., 'a', 'b', etc.): ");
            System.out.flush();
            if(reader==null){
                moveInput = inputHandler.getInput();
            }
            else{
                try {
                    moveInput = reader.readLine();
                    if(moveInput==null){
                        moveInput = inputHandler.getInput();
                    }
                    else {
                        System.out.println(moveInput);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (moveHandler.isValidMoveCommand(moveInput)) {
                int[] chosenMove = moveHandler.getMoveFromCommand(moveInput);
                moveHandler.executeMove(chosenMove[0], chosenMove[1]);
                moveHandler.executeMove(chosenMove[2], chosenMove[3]);

                turnInProgress = false;
            } else if (inputHandler.isQuitCommand(moveInput)) {
                quitGame();
                turnInProgress = false;
            } else if (inputHandler.isPipCommand(moveInput)) {
                Display.displayPipCount(getBoard(), players);
                moveHandler.legalmoves(player, rollValues[0], rollValues[1]);
            } else if (inputHandler.isHintCommand(moveInput)) { //Display hints
                Display.displayHint(true, true);
                moveHandler.legalmoves(player, rollValues[0], rollValues[1]);
            } else if (inputHandler.isBoardCommand(moveInput)){
                Display.displayBoard(board, player, match);
            }
            else if(inputHandler.isDoubleCommand(moveInput)){
                System.out.println("You cannot double the stakes at this time. Please enter a valid command");
            }
            else {
                System.out.println("Error: Please enter a valid command\nFor a list of valid commands type 'hint'");
            }
        }
    }

    public int decideFirstPlayer() { //Change to use rolls as roll for first turn ///////
        int[] rolls = new int[2];

        int first = 0;
        System.out.println("=-=-=-=-=-=-=-=-ROLL TO SEE WHO GOES FIRST-=-=-=-=-=-=-=-");
        while(rolls[0] == rolls[1]){

            for (int i = 0; i < 2; i++){
                promptPlayer(i);

                while(true) {
                    String userInput = inputHandler.getInput();

                    if (inputHandler.isRollCommand(userInput)) {
                        if(inputHandler.isrollOnetestcommand(userInput)){
                            String[] roll = userInput.substring(5).split(" ");
                            rolls[i] = Integer.parseInt(roll[0]);
                            break;
                        }
                        else {
                            rolls[i] = oneRoll();
                            break;
                        }
                    }
                    if (inputHandler.isQuitCommand(userInput)) {
                        quitGame();
                        break;
                    } else if (inputHandler.isHintCommand(userInput)){
                        Display.displayHint(false, false);
                    }else {
                        System.out.println("Error: Please enter a valid command\nFor a list of valid commands type 'hint'");
                    }
                }
            }
            if(rolls[0] != rolls[1]){
                first = (rolls[0] > rolls[1] ? 0 : 1); // Higher roll goes first
                System.out.println(players.getPlayerName(first) + Display.resetColour() + " rolled higher and will go first\n");
                break;
            }
            System.out.println("Rolls were equal! Roll again to decide who goes first\n");
        }

        //Use dice
        Display.printDiceFace(rolls[0], rolls[1], false);
        Display.displayBoard(getBoard(), first, match);
        moveHandler.legalmoves(first, rolls[0], rolls[1]);
        chooseMove(first, rolls, null);
        first = (first == 0) ? 1 : 0;
        return first;
    }

    public void decideFirstPlayer(BufferedReader reader, Players players) { //Change to use rolls as roll for first turn ///////
        int[] rolls = new int[2];

        int first = 0;
        System.out.println("=-=-=-=-=-=-=-=-ROLL TO SEE WHO GOES FIRST-=-=-=-=-=-=-=-");
        while(rolls[0] == rolls[1]){

            for (int i = 0; i < 2; i++){
                promptPlayer(i);
                System.out.println();
                while(true) {
                    String userInput = null;
                    try {
                        userInput = reader.readLine().trim();
                        System.out.println(userInput);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    if (inputHandler.isRollCommand(userInput)) {
                        if(inputHandler.isrollOnetestcommand(userInput)){
                            String[] roll = userInput.substring(5).split(" ");
                            rolls[i] = Integer.parseInt(roll[0]);
                            Display.printDiceFace(rolls[i], -1, false);
                            break;
                        }
                        else {
                            rolls[i] = oneRoll();
                            break;
                        }
                    }
                    if (inputHandler.isQuitCommand(userInput)) {
                        quitGame();
                        break;
                    } else if (inputHandler.isHintCommand(userInput)){
                        Display.displayHint(false, false);
                    }else {
                        System.out.println("Error: Please enter a valid command\nFor a list of valid commands type 'hint'");
                    }
                }
            }
            if(rolls[0] != rolls[1]){
                first = (rolls[0] > rolls[1] ? 0 : 1); // Higher roll goes first
                System.out.println(players.getPlayerName(first) + Display.resetColour() + " rolled higher and will go first\n");
                break;
            }
            System.out.println("Rolls were equal! Roll again to decide who goes first\n");
        }

        //Use dice
        Display.printDiceFace(rolls[0], rolls[1], false);
        Display.displayBoard(getBoard(), first, match);
        moveHandler.legalmoves(first, rolls[0], rolls[1]);
        chooseMove(first, rolls, reader);
        first = (first == 0) ? 1 : 0;
        players.setCurrentPlayer(first);
    }

    public Match getMatch(){
        return match;
    }

    public void quitGame(){
        System.out.println("Thank You for playing!");
        System.out.println("Quitting game\n\n");
        System.exit(0);
    }

    public void setMatchLength(String input){
        System.out.print("Please enter the length of the match: ");
        System.out.flush();
        if(input.isEmpty()){ //User input
            input = inputHandler.getInput();
        }
        else{
            System.out.println(input);
        }
        int matchLength;
        while (true) {
            try {
                matchLength = Integer.parseInt(input);
                if (matchLength > 0) {
                    break;
                } else {
                    System.out.print("Match Length must be greater than 0. Please enter the length of the match: ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Match Length Must be an integer greater than 0. Please enter the length of the match: ");
            }
            input = inputHandler.getInput();
        }
        match.setMatchLength(matchLength);
        System.out.println();
    }

    public int[] rollTurn(int player) {
        int[] rollValues;
        rollValues = dice.rollDice();
        boolean doubleRoll = rollValues[0] == rollValues[1];
        Display.printDiceFace(rollValues[0], rollValues[1], doubleRoll);
        System.out.println(players.getPlayerName(player) + Display.resetColour() + " rolled a " + rollValues[0] + " and a " + rollValues[1]);

        return rollValues;
    }

    public int oneRoll(){
        int[] rollValues;
        rollValues = dice.rollDice();
        Display.printDiceFace(rollValues[0], -1, false);
        return rollValues[0];
    }

    public void handleDoubleStakes(int player) {
        System.out.println(players.getPlayerName(player) + " has offered to double the stakes to " + match.getDoubleCount()*2 + "! ");
        System.out.print(players.getPlayerName((player == 0) ? 1 : 0) + " do you accept? (y/n): ");
        System.out.flush();
        String userInput = inputHandler.getInput();
        while (!userInput.equalsIgnoreCase("y") && !userInput.equalsIgnoreCase("n")) {
            System.out.print("Please enter a valid response (y/n): ");
            System.out.flush();
            userInput = inputHandler.getInput();
        }
        if (userInput.equalsIgnoreCase("y")) {
            match.updateDoubleCount();
            System.out.println(players.getPlayerName(player) + " has doubled the stakes!");
            player = (player == 0) ? 1 : 0;
            match.setDoubleOwner(player);
        } else {
            match.updateScore(player);
            System.out.println(players.getPlayerName((player == 0) ? 1 : 0) + " has declined the offer to double the stakes, and forfeits the game.");
            System.out.print("The score is now " + match.printScore() + "\n");
        }
    }

    public void promptPlayer(int player){
        System.out.print(players.getPlayerName(player) + "'s" + Display.resetColour() + " turn. ");
        System.out.print("Please Enter a Command: ");
        System.out.flush();
    }

    public void fileStart(String filename){
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            setMatchLength(reader.readLine().trim());
            setPlayers( reader.readLine().trim(), reader.readLine().trim());
            decideFirstPlayer(reader, players);
            fileTurn(reader);
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }

    public void fileTurn(BufferedReader reader){

        try {
            System.out.println("Reading commands from the file:");
            String line = reader.readLine();
            while (!board.checkWin() && line !=null) { //Neither player has won and file not finished
                Display.displayBoard(board, players.getCurrentPlayer(), match);
                boolean turnInProgress = true;
                while(turnInProgress && line !=null) {
                    turnInProgress = processTurn(players.getCurrentPlayer(), line, reader);
                    line = reader.readLine();
                }
                players.switchPlayer();
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }

}
