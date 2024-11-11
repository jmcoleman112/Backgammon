public class Backgammon { //Class to run game logic
    private final Board board;
    private final Dice dice;
    private final InputHandler inputHandler;
    private final MoveHandler moveHandler;
    private Players players;

    public Backgammon() {
        this.board = new Board();
        this.dice = new Dice();
        this.inputHandler = new InputHandler();
        this.moveHandler = new MoveHandler(board);
    }

    public static void main(String[] args){
        Backgammon game = new Backgammon();

        InputHandler inputHandler = game.getInputHandler();

        if(game.Welcome()){
            game.setPlayers();

            int player = game.decideFirstPlayer(); //Player one goes first

            while(!game.getBoard().checkWin(0) && !game.getBoard().checkWin(1)){ //Neither player has won
                Display.displayBoard(game.getBoard(), player);
                game.processTurn(player);

                player = (player == 0) ? 1 : 0;
            }
        }
        if(game.getBoard().checkWin(0)) Display.printWinMessage(game.getPlayers(), 0);
        else if(game.getBoard().checkWin(1)) Display.printWinMessage(game.getPlayers(), 1);

        inputHandler.closeScanner();
    }

    public void setPlayers(){
        this.players = new Players();
    }

    public Board getBoard(){
        return board;
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

    public boolean Welcome() { //Loop until game started
        System.out.println("=====================================");
        System.out.println("Welcome to Backgammon! This is a implementation of Backgammon created by Jack Coleman and Naoise Golden.");
        System.out.println("For instructions on how to play, please read the README file.");
        System.out.println("=====================================\n\n");
        System.out.println("Press Enter to start or Q to quit\n");
        System.out.flush();

        return inputHandler.startMessage();
    }

    public void processTurn(int player) { //One roll per turn
        promptPlayer(player);
        String userInput = inputHandler.getInput();
        int[] rollValues = new int[2];

       boolean turnInProgress = true;
        while (turnInProgress) {
            if (inputHandler.isRollCommand(userInput)) {
                rollValues = rollTurn(player);
                moveHandler.legalmoves(player, rollValues[0], rollValues[1]);
                turnInProgress = false;
            } else if (inputHandler.isQuitCommand(userInput)) {
                quitGame();
                turnInProgress = false;
            } else if (inputHandler.isPipCommand(userInput)) {
                Display.displayPipCount(getBoard(), players);
            } else if (inputHandler.isHintCommand(userInput)) { //Display hints
                Display.displayHint(false, true);
            } else if (inputHandler.isBoardCommand(userInput)){
                Display.displayBoard(board, player);
            }else {
                System.out.println("Error: Please enter a valid command\nFor a list of valid commands type 'hint'");
            }
            if (turnInProgress) {
                promptPlayer(player);
                userInput = inputHandler.getInput();
            }
        }

        // Process choosing a move from the list of legal ones
        chooseMove(player, rollValues);
    }

    public void chooseMove(int player, int[] rollValues) {
        boolean turnInProgress = true;
        while (turnInProgress) {
            System.out.println(players.getPlayerName(player) + Display.resetColour() + " please choose a move from the list above (e.g., 'a', 'b', etc.): ");
            String moveInput = inputHandler.getInput();

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
                Display.displayBoard(board, player);
            }else {
                System.out.println("Error: Please enter a valid command\nFor a list of valid commands type 'hint'");
            }
        }
    }

    public int decideFirstPlayer() { //Change to use rolls as roll for first turn ///////
        int[] rolls = new int[2];

        int first = 0;

        while(rolls[0] == rolls[1]){
            System.out.println("Players must roll the dice to see who goes first\n");

            for (int i = 0; i < 2; i++){
                promptPlayer(i);

                while(true) {
                    String userInput = inputHandler.getInput();

                    if (inputHandler.isRollCommand(userInput)) {
                        rolls[i] = oneRoll();
                        System.out.println(players.getPlayerName(i) + Display.resetColour() + " rolled a " + rolls[i] + "\n\n");
                        break;
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
            System.out.println("Rolls were equal!");
        }

        //Use dice
        Display.printDiceFace(rolls[0], rolls[1]);
        Display.displayBoard(getBoard(), first);
        moveHandler.legalmoves(first, rolls[0], rolls[1]);
        chooseMove(first, rolls);
        first = (first == 0) ? 1 : 0;
        return first;
    }

    public void quitGame(){
        System.out.println("Thank You for playing!");
        System.out.println("Quitting game\n\n");
        System.exit(0);
    }

    public int[] rollTurn(int player) {
        int[] rollValues;
        rollValues = dice.rollDice();
        Display.printDiceFace(rollValues[0], rollValues[1]);
        System.out.println(players.getPlayerName(player) + Display.resetColour() + " rolled a " + rollValues[0] + " and a " + rollValues[1]);

        return rollValues;
    }

    public int oneRoll(){
        int[] rollValues;
        rollValues = dice.rollDice();
        Display.printDiceFace(rollValues[0], -1);
        return rollValues[0];
    }

    public void promptPlayer(int player){
        System.out.println(players.getPlayerName(player) + "'s" + Display.resetColour() + " turn");
        System.out.print("Please Enter a Command: ");
    }
}
