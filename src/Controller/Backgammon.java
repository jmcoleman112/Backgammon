//===============================================================
//By Group 33: Jack Coleman (21207103) and Naoise Golden (21376026)
//GITHUB ID: jmcoleman112, NaoiseG
//================================================================


package Controller;
import Model.Board;
import Model.Dice;
import Model.Match;
import Model.Players;
import View.Colour;
import View.Display;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;


/**
 * The `Backgammon` class implements the game logic for a Backgammon game.
 * It manages the board, dice, input handling, move handling, match, and players.
 * The class provides methods to start a new game, process turns, handle player inputs,
 * and determine the winner of the game and match.
 *
 * <p>Features include:</p>
 * <ul>
 *   <li>Setting up a new game or match</li>
 *   <li>Rolling dice and handling moves</li>
 *   <li>Processing player commands</li>
 *   <li>Displaying the game board and status</li>
 *   <li>Reading commands from a file</li>
 * </ul>
 *
 * <p>Usage example:</p>
 * <pre>
 * {@code
 * public static void main(String[] args) {
 *     Backgammon game = new Backgammon();
 *     game.newGame(false, null);
 * }
 * }
 * </pre>
 *
 * <p>Dependencies:</p>
 * <ul>
 *   <li>Board</li>
 *   <li>Dice</li>
 *   <li>InputHandler</li>
 *   <li>MoveHandler</li>
 *   <li>Match</li>
 *   <li>Players</li>
 *   <li>Display</li>
 * </ul>
 *
 * <p>Author: jmcoleman112</p>
 *
 * @see Model.Board
 * @see Model.Dice
 * @see Controller.InputHandler
 * @see Controller.MoveHandler
 * @see Model.Match
 * @see Model.Players
 * @see View.Display
 */
public class Backgammon { //Class to run game logic
    private Board board;
    private Dice dice;
    private InputHandler inputHandler;
    private MoveHandler moveHandler;
    private Match match;
    private Players players;
    private int gamecount;

    /**
     * Constructs a new Backgammon game instance.
     */
    public Backgammon() {
        this.board = new Board();
        this.dice = new Dice();
        this.inputHandler = new InputHandler();
        this.moveHandler = new MoveHandler(board);
        this.match = new Match();
        this.gamecount = 1;
    }

    /**
     * The main method to start the Backgammon game.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {



        //A boolean used to test the game without calling system exit
        boolean testing;
        if(args.length != 0) {
            testing = (Objects.equals(args[0], "test"));
        }
        else testing = false;


        //sets up game to be played
        boolean MatchLive = true;
        Backgammon game = new Backgammon();
        Display.welcomeMessage();
        System.out.flush();

        //While current match is ongoing
        while (MatchLive) {

            //initilize new game
            game = new Backgammon();
            InputHandler inputHandler = game.getInputHandler();
            String filename;

            filename = game.Welcome();

            //Start game from file or standard input
            if (filename != null) {
                game.fileStart(filename); //Read from file if user enters file name
            } else { //Else start using standard input
                game.setMatchLength("");
                game.setPlayers("", "", game.getInputHandler());
                game.getPlayers().setCurrentPlayer(game.decideFirstPlayer());
            }

            //While current game is ongoing
            while (game.getMatch().noMatchWinner()) {


                boolean filemode = false;
                BufferedReader reader = null;

                // Neither player has won
                while (game.getBoard().noGameWinner()) {
                    int player = game.getPlayers().getCurrentPlayer();
                    boolean turnInProgress = true;
                    Display.displayBoard(game.getBoard(), game.getPlayers().getCurrentPlayer(), game.getMatch());
                    game.promptPlayer(player);
                    String userInput = inputHandler.getInput();

                    //Read from file
                    if (inputHandler.isfileCommand(userInput)) {
                        filename = userInput.substring(5);
                        try {

                            //Process turn from file
                            reader = new BufferedReader(new FileReader(filename));
                            game.fileTurn(reader);
                        } catch (IOException e) {
                            System.err.println("Error opening the file: " + e.getMessage());
                        }

                        //Read from standard input
                    } else {
                        while (turnInProgress) {

                            //Reader is null as no file
                            turnInProgress = game.processTurn(player, userInput, null);

                            //Default value for winner
                            if(game.getBoard().getWinner()!=-1){
                                break;
                            }
                            if (turnInProgress) {
                                game.promptPlayer(player);
                                userInput = inputHandler.getInput();
                            }
                        }

                        //Check for winner from forfeit
                        if(game.getBoard().getWinner()!=-1){
                            break;
                        }

                        //Check for winner from forfeit
                        if(game.getBoard().noGameWinner()) { //Check for conventional winner
                            game.getPlayers().switchPlayer();
                            Display.displayplayerchange(game.getPlayers().getCurrentPlayer());
                        }
                    }

                    //test function
                    if(testing) {
                        System.out.println("Test complete!");
                        return; //If testing main start game, make one move for each player then return
                    }
                }

                //check fot game winner
                int winner = game.getBoard().getWinner();

                //Update score according to type of win
                game.getMatch().updateScore(winner, game.getBoard());

                // Print message to winner
                Display.printGameWinMessage(game.getPlayers(), winner, game.getMatch(), game.getBoard());
                if (game.getMatch().noMatchWinner()) {
                    game.incrementGameCount();
                    System.out.println("Game " + game.gamecount + " is now Starting.");
                    game.newGame(filemode, reader);
                }
            }

            Display.printMatchWinMessage(game.getPlayers(), game.getMatch().getMatchWinner(), game.getMatch());

            //Start new match if user wants
            MatchLive = game.newMatch();
        }

        //Quit game
        game.quitGame(false);
    }

    /**
     * Sets the players for the game.
     *
     * @param player1Name the name of player 1
     * @param player2Name the name of player 2
     * @param inputHandler the input handler
     */
    public void setPlayers(String player1Name, String player2Name, InputHandler inputHandler) {

        //Read from input
        if (player1Name.isEmpty() || player2Name.isEmpty()) this.players = new Players(inputHandler);
        else {

            //If reading from file, player names are passed as strings
            this.players = new Players(player1Name, player2Name);

        }
    }

    /**
     * Gets the current game board.
     *
     * @return the current game board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Sets the test board for the game.
     *
     * @param board the test board to set
     */
    public void setTestBoard(Board board) { //For testing purposes
        this.board = board;
        getMoveHandler().setBoard(board);
    }

    /**
     * Gets the players of the game.
     *
     * @return the players of the game
     */
    public Players getPlayers() {
        return players;
    }

    /**
     * Gets the input handler for the game.
     *
     * @return the input handler
     */
    public InputHandler getInputHandler() {
        return inputHandler;
    }

    /**
     * Gets the move handler for the game.
     *
     * @return the move handler
     */
    public MoveHandler getMoveHandler() {
        return moveHandler;
    }

    /**
     * Displays the welcome message and handles the initial input.
     *
     * @return the filename if a file command is given, otherwise null
     */
    public String Welcome() {
        System.out.print("Please Hit Enter to begin the game: ");
        String welcomestring = inputHandler.getInput();
        if (inputHandler.isfileCommand(welcomestring)) {
            return welcomestring.substring(5); //Filename
        }
        return null;
    }

    /**
     * Processes a player's turn.
     *
     * @param player    the current player
     * @param userInput the user input
     * @param reader    the buffered reader for file input
     * @return true if the turn is still in progress, false otherwise
     */
    public boolean processTurn(int player, String userInput, BufferedReader reader) {
        int[] rollValues;

        if (inputHandler.isRollCommand(userInput)) {

            //Testing command for setting roll
            if (inputHandler.isrolltestcommand(userInput)) {
                String[] rolls = userInput.substring(5).split(" ");
                rollValues = new int[]{Integer.parseInt(rolls[0]), Integer.parseInt(rolls[1])};

                //Rolled a double
                boolean doubleRoll = rollValues[0] == rollValues[1];
                Display.printDiceFace(rollValues[0], rollValues[1], doubleRoll);
            } else {
                //Random roll
                rollValues = rollTurn();
            }

            //No valid moves
            if (!moveHandler.legalmoves(player, rollValues[0], rollValues[1])) {
                return false;
            }
            chooseMove(player, reader); //Choose from list of moves

            //Rolled doubles and no winner
            if (rollValues[0] == rollValues[1] && getBoard().noGameWinner()) {
                System.out.println(Colour.getplayercolour(player)+ "━━━━━━━━━━━━━━━━━━DOUBLES! Bonus Second Roll━━━━━━━━━━━━━━━━━━\n"+Display.resetColour());
                Display.printDiceFace(rollValues[0], rollValues[1], false);
                Display.displayBoard(getBoard(), player, match);

                //No valid moves
                if (!moveHandler.legalmoves(player, rollValues[0], rollValues[1])) {
                    return false;
                }
                chooseMove(player, reader);
            }

            //Turn over
            return false;
        } else if (inputHandler.isDoubleCommand(userInput)) {

            //Player can double
            if (match.doublelegality(player)) {
                handleDoubleStakes(player, reader);
            } else {
                System.out.println("You cannot double the stakes at this time. Please enter a valid command. ");
            }
            return true;
        } else if (inputHandler.isQuitCommand(userInput)) {
            quitGame(false);
            return false;

            //Set test board
        } else if (inputHandler.isSetBoardCommand(userInput)) {
            Board UpdatedBoard = new Board();
            UpdatedBoard.setBoardFromString(userInput);
            board = UpdatedBoard;
            this.moveHandler = new MoveHandler(board);
            Display.displayBoard(getBoard(), player, match);
            return true;

            //Display pip count
        } else if (inputHandler.isPipCommand(userInput)) {
            Display.displayPipCount(getBoard(), players);
            return true;

            //Display hints
        } else if (inputHandler.isHintCommand(userInput)) {

            //Can player double or not
            boolean canDouble = (player == match.getDoubleOwner() || match.getDoubleOwner() == -1);
            Display.displayHint(canDouble, false, true);
            return true;

            //Command to display the board
        } else if (inputHandler.isBoardCommand(userInput)) {
            Display.displayBoard(board, player, match);
            return true;
        } else {
            //Invalid command entered
            System.out.println("\nError: Please enter a valid command. For a list of valid commands type 'hint'.");
            return true;
        }
    }

    /**
     * Prompts the player to choose a move.
     *
     * @param player     the current player
     * @param reader     the buffered reader for file input
     */
    public void chooseMove(int player, BufferedReader reader) {
        boolean turnInProgress = true;
        while (turnInProgress) {
            String moveInput;
            System.out.print("\n" + players.getPlayerName(player) + Display.resetColour() + " please choose a move from the list above (e.g., 'a', 'b', etc.): ");
            System.out.flush();

            //IF not reading from file get input from user
            if (reader == null) {
                moveInput = inputHandler.getInput();
            } else {
                try {
                    //Read from file
                    moveInput = reader.readLine();
                    if (moveInput == null) {
                        moveInput = inputHandler.getInput();
                    } else {
                        System.out.println(moveInput);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            //Move pieces
            if (moveHandler.isValidMoveCommand(moveInput)) {
                int[] chosenMove = moveHandler.getMoveFromCommand(moveInput);
                moveHandler.executeMove(chosenMove[0], chosenMove[1]);
                moveHandler.executeMove(chosenMove[2], chosenMove[3]);


                //End turn
                turnInProgress = false;
            } else if (inputHandler.isQuitCommand(moveInput)) {
                quitGame(false);
                turnInProgress = false;

                //Turn continues if move not made
            } else if (inputHandler.isPipCommand(moveInput)) {
                Display.displayPipCount(getBoard(), players);

                //Can display hint, pip count or board during turn
            } else if (inputHandler.isHintCommand(moveInput)) {
                boolean canDouble = (player == match.getDoubleOwner() || match.getDoubleOwner() == -1);
                Display.displayHint(canDouble, true, true);
            } else if (inputHandler.isBoardCommand(moveInput)) {
                Display.displayBoard(board, player, match);

                //Can't double after roll
            } else if (inputHandler.isDoubleCommand(moveInput)) {
                System.out.println("You cannot double the stakes at this time. Please enter a valid command");
            } else {
                System.out.println("\nError: Please enter a valid command. For a list of valid commands type 'hint'.");
            }
        }
    }

    /**
     * Decides the first player based on dice rolls.
     *
     * @return the index of the first player
     */
    public int decideFirstPlayer() {
        int[] rolls = new int[2];

        int first = 0;
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━ROLL TO SEE WHO GOES FIRST━━━━━━━━━━━━━━━━━━━━━━━━");

        //Players roll until a winner
        while (rolls[0] == rolls[1]) {

            for (int i = 0; i < 2; i++) {
                promptPlayer(i);

                while (true) {
                    String userInput = inputHandler.getInput();

                    if (inputHandler.isRollCommand(userInput)) {

                        //Set roll command for testing
                        if (inputHandler.isrollOnetestcommand(userInput)) {
                            String[] roll = userInput.substring(5).split(" ");
                            rolls[i] = Integer.parseInt(roll[0]);
                            Display.printDiceFace(rolls[i], -1, false);
                            break;
                        } else {
                            rolls[i] = oneRoll();
                            break;
                        }
                    }
                    if (inputHandler.isQuitCommand(userInput)) {
                        quitGame(false);
                        break;
                    } else if (inputHandler.isHintCommand(userInput)) {
                        Display.displayHint(false, false, false);
                    } else {
                        System.out.println("\nError: Please enter a valid command. For a list of valid commands type 'hint.'");
                        promptPlayer(i);
                    }
                }
            }
            if (rolls[0] != rolls[1]) {

                //Higher roll goes first
                first = (rolls[0] > rolls[1] ? 0 : 1);
                System.out.println(players.getPlayerName(first) + Display.resetColour() + " rolled higher and will go first.\n");
                break;
            }
            System.out.println("Rolls were equal! Roll again to decide who goes first\n");
        }

        //Use dice
        Display.displayBoard(getBoard(), first, match);
        Display.printDiceFace(rolls[0], rolls[1], false);
        moveHandler.legalmoves(first, rolls[0], rolls[1]);
        chooseMove(first, null);
        first = (first == 0) ? 1 : 0;

        //Change player
        Display.displayplayerchange(first);

        //Current player
        return first;
    }

    /**
     * Decides the first player based on dice rolls from a file.
     *
     * @param reader the buffered reader for file input
     */
    public void decideFirstPlayerFile(BufferedReader reader) {
        int[] rolls = new int[2];

        int first = 0;
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━ROLL TO SEE WHO GOES FIRST━━━━━━━━━━━━━━━━━━━━━━━━");
        while (rolls[0] == rolls[1]) {

            for (int i = 0; i < 2; i++) {
                promptPlayer(i);
                while (true) {

                    //Read from file
                    String userInput;
                    try {

                        //Read from file
                        userInput = reader.readLine();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    //get input from user instead
                    if (userInput == null) {
                        userInput = inputHandler.getInput();
                    } else {

                        //Print input from file
                        userInput = userInput.trim();
                        System.out.println(userInput);
                    }

                    //check for roll command
                    if (inputHandler.isRollCommand(userInput)) {
                        if (inputHandler.isrollOnetestcommand(userInput)) {


                            //Should use test roll command if reading from file so rolls are predictable
                            String[] roll = userInput.substring(5).split(" ");
                            rolls[i] = Integer.parseInt(roll[0]);
                            Display.printDiceFace(rolls[i], -1, false);
                            break;
                        } else {
                            rolls[i] = oneRoll();
                            break;
                        }
                    }

                    //check for quit command
                    if (inputHandler.isQuitCommand(userInput)) {
                        quitGame(false);
                        break;

                        //check for hint comand
                    } else if (inputHandler.isHintCommand(userInput)) {
                        Display.displayHint(false, false, false);
                    } else {

                        //no valid command given
                        System.out.println("\nError: Please enter a valid command. For a list of valid commands type 'hint'.");
                        promptPlayer(i);
                    }
                }
            }
            if (rolls[0] != rolls[1]) {

                // Higher roll goes first
                first = (rolls[0] > rolls[1] ? 0 : 1);
                System.out.println(players.getPlayerName(first) + Display.resetColour() + " rolled higher and will go first\n");
                break;
            }
            System.out.println("Rolls were equal! Roll again to decide who goes first\n");
        }

        // Use dice
        Display.printDiceFace(rolls[0], rolls[1], false);
        Display.displayBoard(getBoard(), first, match);

        //search for all legal moves
        moveHandler.legalmoves(first, rolls[0], rolls[1]);

        //detect who the first player is gonna be
        chooseMove(first, reader);
        first = (first == 0) ? 1 : 0;
        Display.displayplayerchange(first);
        players.setCurrentPlayer(first);
    }

    /**
     * Gets the match instance.
     *
     * @return the match instance
     */
    public Match getMatch() {
        return match;
    }

    /**
     * Quits the game.
     * @param testing boolean to test without calling system exit
     */
    public void quitGame(boolean testing) {
        //To test without calling system exit
        System.out.println("Thank You for playing!");
        System.out.println("Quitting game\n\n");
        inputHandler.closeScanner();
        if(!testing) System.exit(0); //To test without calling system exit
    }

    /**
     * Sets the match length.
     *
     * @param input the input for match length
     */
    public void setMatchLength(String input) {
        System.out.print("Please enter the length of the match: ");
        System.out.flush();

        //User input
        if (input.isEmpty()) {
            input = inputHandler.getInput();
        } else {
            System.out.println(input);
        }

        //set match length from input
        int matchLength;
        while (true) {
            try {
                matchLength = Integer.parseInt(input);

                //Check for positive integer value
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

    /**
     * Rolls the dice for a player's turn.
     *
     * @return an array containing the roll values
     */
    public int[] rollTurn() { //Generate random rolls

        //generate random rolls
        int[] rollValues;
        rollValues = dice.rollDice();
        boolean doubleRoll = rollValues[0] == rollValues[1];
        Display.printDiceFace(rollValues[0], rollValues[1], doubleRoll);

        return rollValues;
    }

    /**
     * Rolls a single die.
     *
     * @return the value of the roll
     */
    public int oneRoll() { //Single random roll

        //generate only one roll
        int[] rollValues;
        rollValues = dice.rollDice();
        Display.printDiceFace(rollValues[0], -1, false);
        return rollValues[0];
    }

    /**
     * Handles the doubling of stakes.
     *
     * @param player the current player
     * @param reader the buffered reader for file input
     */
    public void handleDoubleStakes(int player, BufferedReader reader) {

        //I/O stuff
        System.out.println(players.getPlayerName(player) + " has offered to double the stakes to " + match.getDoubleCount() * 2 + "! \n");
        System.out.print(players.getPlayerName((player == 0) ? 1 : 0) + " do you accept? (accept/reject): ");
        System.out.flush();
        String userInput;

        //check if input is file or user
        if (reader == null) {
            userInput = inputHandler.getInput();
        } else {
            try {

                //file input stuff
                userInput = reader.readLine();
                if (userInput == null) {

                    //user input stuff
                    userInput = inputHandler.getInput();
                } else {

                    //print input from file
                    System.out.println(userInput);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        //asking the user to acceot or reject
        while (!userInput.equalsIgnoreCase("accept") && !userInput.equalsIgnoreCase("reject")) {
            System.out.print("Please enter a valid response (accept/reject): ");
            System.out.flush();

            //check if input is file or user
            if (reader == null) {
                userInput = inputHandler.getInput();
            } else {
                try {

                    //file input stuff
                    userInput = reader.readLine();
                    if (userInput == null) {

                        //user input stuff
                        userInput = inputHandler.getInput();
                    } else {

                        //print input from file
                        System.out.println(userInput);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        //check if user accepted or rejected
        if (userInput.equalsIgnoreCase("accept")) {
            match.updateDoubleCount();

            //Double the double count
            System.out.println("\n"+players.getPlayerName(player) + " has doubled the stakes!");
            player = (player == 0) ? 1 : 0;

            //Set new dice owne
            match.setDoubleOwner(player);
        } else {

            //Forfeit occurs
            board.setWinner(player);
            System.out.println("\n"+players.getPlayerName((player == 0) ? 1 : 0) + " has declined the offer to double the stakes.");
        }
    }

    /**
     * Prompts the player to enter a command.
     *
     * @param player the current player
     */
    public void promptPlayer(int player) {

        //get a command from the player
        System.out.print("\n"+players.getPlayerName(player) + "'s" + Display.resetColour() + " turn. ");
        System.out.print("Please Enter a Command: ");
        System.out.flush();
    }

    /**
     * Starts the game using commands from a file.
     *
     * @param filename the name of the file containing the commands
     */
    public void fileStart(String filename) {

        //grab a file name and set up a reader for the file from the start
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            setMatchLength(reader.readLine().trim());
            setPlayers(reader.readLine().trim(), reader.readLine().trim(), getInputHandler());

            //operate starting of game from reader
            decideFirstPlayerFile(reader);

            //Reads all inputs from file
            fileTurn(reader);

        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }

    /**
     * Processes turns using commands from a file.
     *
     * @param reader the buffered reader for file input
     */
    public void fileTurn(BufferedReader reader) {
        try {

            //grab line from reader
            String line = reader.readLine();
            while (line != null) {
                Display.displayBoard(board, players.getCurrentPlayer(), match);

                //grab line from reader
                boolean turnInProgress = true;
                while (turnInProgress && line != null) {
                    System.out.print(players.getPlayerName(players.getCurrentPlayer()) + "'s" + Display.resetColour() + " turn. ");
                    System.out.print("Please Enter a Command: ");
                    System.out.println(line);

                    //Process turn using file
                    turnInProgress = processTurn(players.getCurrentPlayer(), line, reader);


                    //play the actual turn
                    if (turnInProgress) {
                        line = reader.readLine();
                    }
                }

                //switch player if no winner
                if (!turnInProgress && getBoard().noGameWinner()) {
                    players.switchPlayer();
                    Display.displayplayerchange(players.getCurrentPlayer());
                }

                //check for winner
                if (!board.noGameWinner()) {

                    //Win occurred
                    int winner = board.getWinner();
                    getMatch().updateScore(winner, getBoard());
                    Display.printGameWinMessage(players, winner, match, board);

                    //Start new game still reading from file
                    if (getMatch().noMatchWinner()) {
                        incrementGameCount();
                        System.out.println("Game " + gamecount + " is now Starting.");
                        newGame(true, reader);
                    }
                }
                line = reader.readLine();
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }

    /**
     * Starts a new game.
     *
     * @param filemode whether the game is in file mode
     * @param reader   the buffered reader for file input
     */
    public void newGame(boolean filemode, BufferedReader reader) {
        this.board = new Board();

        //Reset double dice owner
        match.setDoubleOwner(-1);
        getMoveHandler().setBoard(board); //New board

        //figure out if its a file or a player
        if (!filemode) {
            getPlayers().setCurrentPlayer(decideFirstPlayer());
        } else {
            decideFirstPlayerFile(reader);
        }
    }

    /**
     * Increments the game count.
     */
    public void incrementGameCount() {
        gamecount++; //Keeps track of number of games
    }


    /**
     * Gets the game count.
     *
     * @return the game count
     */
    public int getGamecount(){
        return gamecount;
    }

    /**
     * Prompts the user to start a new match.
     *
     * @return true if the user wants to start a new match, false otherwise
     */
    public boolean newMatch() {

        //check if another game is wanted
        System.out.print("Would you like to play another match? (y/n): ");
        System.out.flush();
        String userInput = inputHandler.getInput();

        //syntax check to ensure its a valid input
        while (!userInput.equalsIgnoreCase("y") && !userInput.equalsIgnoreCase("n") && !userInput.equalsIgnoreCase("q")) {
            System.out.print("Please enter a valid response (y/n): ");
            System.out.flush();
            userInput = inputHandler.getInput();
        }

        //return if the user wants to play another match
        return userInput.equalsIgnoreCase("y"); //User wants new match
    }


    /**
     * Sets the input handler for the game.
     *
     * @param inputHandler the input handler to set
     */
    public void setInputHandler(InputHandler inputHandler){ //For testing input
        this.inputHandler = inputHandler;
    }

}
