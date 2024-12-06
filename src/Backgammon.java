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
 * @see Board
 * @see Dice
 * @see InputHandler
 * @see MoveHandler
 * @see Match
 * @see Players
 * @see Display
 */

import utilities.Colour;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


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
        boolean MatchLive = true;
        Backgammon game = new Backgammon();
        Display.welcomeMessage();
        System.out.flush();
        while (MatchLive) {
            game = new Backgammon();
            InputHandler inputHandler = game.getInputHandler();
            String filename;
            filename = game.Welcome();
            if (filename != null) {
                game.fileStart(filename);
            } else {
                game.setMatchLength("");
                game.setPlayers("", "", game.getInputHandler());
                game.getPlayers().setCurrentPlayer(game.decideFirstPlayer());
            }

            while (game.getMatch().noMatchWinner()) {
                boolean filemode = false;
                BufferedReader reader = null;
                while (game.getBoard().noGameWinner()) { // Neither player has won
                    int player = game.getPlayers().getCurrentPlayer();
                    filemode = false;
                    boolean turnInProgress = true;
                    Display.displayBoard(game.getBoard(), game.getPlayers().getCurrentPlayer(), game.getMatch());
                    game.promptPlayer(player);
                    String userInput = inputHandler.getInput();
                    if (inputHandler.isfileCommand(userInput)) {
                        filemode = true;
                        filename = userInput.substring(5);
                        try {
                            reader = new BufferedReader(new FileReader(filename));
                            game.fileTurn(reader); // Pass the reader to the method
                        } catch (IOException e) {
                            System.err.println("Error opening the file: " + e.getMessage());
                        }
                    } else {
                        while (turnInProgress) {
                            turnInProgress = game.processTurn(player, userInput, null);
                            if (turnInProgress) {
                                Display.displayBoard(game.getBoard(), game.getPlayers().getCurrentPlayer(), game.getMatch());
                                game.promptPlayer(player);
                                userInput = inputHandler.getInput();
                            }
                        }
                        game.getPlayers().switchPlayer();
                        Display.displayplayerchange(game.getPlayers().getCurrentPlayer());
                    }
                }
                int winner = game.getBoard().getWinner();
                game.getMatch().updateScore(winner, game.getBoard());
                Display.printGameWinMessage(game.getPlayers(), winner, game.getMatch(), game.getBoard()); // Print message to winner
                if (game.getMatch().noMatchWinner()) {
                    game.incrementGameCount();
                    System.out.println("Game " + game.gamecount + " is now Starting.");
                    game.newGame(filemode, reader);
                }
            }

            Display.printMatchWinMessage(game.getPlayers(), game.getMatch().getMatchWinner(), game.getMatch());
            MatchLive = game.newMatch();
        }
        game.quitGame();
    }

    /**
     * Sets the players for the game.
     *
     * @param player1Name the name of player 1
     * @param player2Name the name of player 2
     */
    public void setPlayers(String player1Name, String player2Name, InputHandler inputHandler) {
        if (player1Name.isEmpty() || player2Name.isEmpty()) this.players = new Players(inputHandler);
        else {
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
    public void setTestBoard(Board board) {
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
            return welcomestring.substring(5);
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
            if (inputHandler.isrolltestcommand(userInput)) {
                String[] rolls = userInput.substring(5).split(" ");
                rollValues = new int[]{Integer.parseInt(rolls[0]), Integer.parseInt(rolls[1])};
                boolean doubleRoll = rollValues[0] == rollValues[1];
                Display.printDiceFace(rollValues[0], rollValues[1], doubleRoll);
            } else {
                rollValues = rollTurn(player);
            }
            if (!moveHandler.legalmoves(player, rollValues[0], rollValues[1])) { // No valid moves
                return false;
            }
            chooseMove(player, rollValues, reader);
            if (rollValues[0] == rollValues[1] && getBoard().noGameWinner()) {
                System.out.println(Colour.getplayercolour(player)+ "━━━━━━━━━━━━━━━━━━DOUBLES! Bonus Second Roll━━━━━━━━━━━━━━━━━━\n"+Display.resetColour());
                Display.printDiceFace(rollValues[0], rollValues[1], false);
                Display.displayBoard(getBoard(), player, match);
                if (!moveHandler.legalmoves(player, rollValues[0], rollValues[1])) { // No valid moves
                    return false;
                }
                chooseMove(player, rollValues, reader);
            }
            return false; // Turn over
        } else if (inputHandler.isDoubleCommand(userInput)) {
            if (match.doublelegality(player)) {
                handleDoubleStakes(player, reader);
            } else {
                System.out.println("You cannot double the stakes at this time. Please enter a valid command");
            }
            return true;
        } else if (inputHandler.isQuitCommand(userInput)) {
            quitGame();
            return false;
        } else if (inputHandler.isSetBoardCommand(userInput)) {
            Board UpdatedBoard = new Board();
            UpdatedBoard.setBoardFromString(userInput);
            board = UpdatedBoard;
            this.moveHandler = new MoveHandler(board);
            return true;
        } else if (inputHandler.isPipCommand(userInput)) {
            Display.displayPipCount(getBoard(), players);
            return true;
        } else if (inputHandler.isHintCommand(userInput)) { // Display hints
            Display.displayHint(false, true);
            return true;
        } else if (inputHandler.isBoardCommand(userInput)) {
            Display.displayBoard(board, player, match);
            return true;
        } else {
            System.out.println("Error: Please enter a valid command\nFor a list of valid commands type 'hint'");
            return true;
        }
    }

    /**
     * Prompts the player to choose a move.
     *
     * @param player     the current player
     * @param rollValues the roll values
     * @param reader     the buffered reader for file input
     */
    public void chooseMove(int player, int[] rollValues, BufferedReader reader) {
        boolean turnInProgress = true;
        while (turnInProgress) {
            String moveInput;
            System.out.print("\n" + players.getPlayerName(player) + Display.resetColour() + " please choose a move from the list above (e.g., 'a', 'b', etc.): ");
            System.out.flush();
            if (reader == null) {
                moveInput = inputHandler.getInput();
            } else {
                try {
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
            } else if (inputHandler.isHintCommand(moveInput)) { // Display hints
                Display.displayHint(true, true);
                moveHandler.legalmoves(player, rollValues[0], rollValues[1]);
            } else if (inputHandler.isBoardCommand(moveInput)) {
                Display.displayBoard(board, player, match);
            } else if (inputHandler.isDoubleCommand(moveInput)) {
                System.out.println("You cannot double the stakes at this time. Please enter a valid command");
            } else {
                System.out.println("Error: Please enter a valid command\nFor a list of valid commands type 'hint'");
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
        while (rolls[0] == rolls[1]) {

            for (int i = 0; i < 2; i++) {
                promptPlayer(i);

                while (true) {
                    String userInput = inputHandler.getInput();

                    if (inputHandler.isRollCommand(userInput)) {
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
                        quitGame();
                        break;
                    } else if (inputHandler.isHintCommand(userInput)) {
                        Display.displayHint(false, false);
                    } else {
                        System.out.print("Error: Please enter a valid command\nFor a list of valid commands type 'hint'");
                    }
                }
            }
            if (rolls[0] != rolls[1]) {
                first = (rolls[0] > rolls[1] ? 0 : 1); // Higher roll goes first
                System.out.println(players.getPlayerName(first) + Display.resetColour() + " rolled higher and will go first.\n");
                break;
            }
            System.out.println("Rolls were equal! Roll again to decide who goes first\n");
        }

        // Use dice
        Display.displayBoard(getBoard(), first, match);
        Display.printDiceFace(rolls[0], rolls[1], false);
        moveHandler.legalmoves(first, rolls[0], rolls[1]);
        chooseMove(first, rolls, null);
        first = (first == 0) ? 1 : 0;
        Display.displayplayerchange(first);
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
                System.out.println();
                while (true) {
                    String userInput;
                    try {
                        userInput = reader.readLine();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    if (userInput == null) {
                        userInput = inputHandler.getInput();
                    } else {
                        userInput = userInput.trim();
                    }
                    if (inputHandler.isRollCommand(userInput)) {
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
                        quitGame();
                        break;
                    } else if (inputHandler.isHintCommand(userInput)) {
                        Display.displayHint(false, false);
                    } else {
                        System.out.println("Error: Please enter a valid command\nFor a list of valid commands type 'hint'");
                    }
                }
            }
            if (rolls[0] != rolls[1]) {
                first = (rolls[0] > rolls[1] ? 0 : 1); // Higher roll goes first
                System.out.println(players.getPlayerName(first) + Display.resetColour() + " rolled higher and will go first\n");
                break;
            }
            System.out.println("Rolls were equal! Roll again to decide who goes first\n");
        }

        // Use dice
        Display.printDiceFace(rolls[0], rolls[1], false);
        Display.displayBoard(getBoard(), first, match);
        moveHandler.legalmoves(first, rolls[0], rolls[1]);
        chooseMove(first, rolls, reader);
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
     */
    public void quitGame() {
        System.out.println("Thank You for playing!");
        System.out.println("Quitting game\n\n");
        inputHandler.closeScanner();
        System.exit(0);
    }

    /**
     * Sets the match length.
     *
     * @param input the input for match length
     */
    public void setMatchLength(String input) {
        System.out.print("Please enter the length of the match: ");
        System.out.flush();
        if (input.isEmpty()) { // User input
            input = inputHandler.getInput();
        } else {
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

    /**
     * Rolls the dice for a player's turn.
     *
     * @param player the current player
     * @return an array containing the roll values
     */
    public int[] rollTurn(int player) {
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
    public int oneRoll() {
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
        System.out.println(players.getPlayerName(player) + " has offered to double the stakes to " + match.getDoubleCount() * 2 + "! \n");
        System.out.print(players.getPlayerName((player == 0) ? 1 : 0) + " do you accept? (accept/reject): ");
        System.out.flush();
        String userInput;
        if (reader == null) {
            userInput = inputHandler.getInput();
        } else {
            try {
                userInput = reader.readLine();
                if (userInput == null) {
                    userInput = inputHandler.getInput();
                } else {
                    System.out.println(userInput);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        while (!userInput.equalsIgnoreCase("accept") && !userInput.equalsIgnoreCase("reject")) {
            System.out.print("Please enter a valid response (accept/reject): ");
            System.out.flush();
            userInput = inputHandler.getInput();
        }
        if (userInput.equalsIgnoreCase("accept")) {
            match.updateDoubleCount();
            System.out.println("\n"+players.getPlayerName(player) + " has doubled the stakes!");
            player = (player == 0) ? 1 : 0;
            match.setDoubleOwner(player);
        } else {
            board.setWinner(player);
            System.out.println("\n"+players.getPlayerName((player == 0) ? 1 : 0) + " has declined the offer to double the stakes, and forfeits the game.");

            boolean filemode = (reader != null);
            int winner = board.getWinner();
            getMatch().updateScore(winner, getBoard());
            newGame(filemode, reader); // Start new game
            System.out.print("The score is now " + match.printScore() + "\n");
        }
    }

    /**
     * Prompts the player to enter a command.
     *
     * @param player the current player
     */
    public void promptPlayer(int player) {
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
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            setMatchLength(reader.readLine().trim());
            setPlayers(reader.readLine().trim(), reader.readLine().trim(), getInputHandler());
            decideFirstPlayerFile(reader);
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
            String line = reader.readLine();
            while (line != null) {
                Display.displayBoard(board, players.getCurrentPlayer(), match);
                boolean turnInProgress = true;
                while (turnInProgress && line != null) {
                    System.out.print(players.getPlayerName(players.getCurrentPlayer()) + "'s" + Display.resetColour() + " turn. ");
                    System.out.print("Please Enter a Command: ");
                    System.out.println(line);
                    turnInProgress = processTurn(players.getCurrentPlayer(), line, reader);
//                    if (getBoard().noGameWinner()) {
//                        Display.displayBoard(board, players.getCurrentPlayer(), match);
//                    }
                    if (turnInProgress) {
                        line = reader.readLine();
                    }
                }
                if (!turnInProgress && getBoard().noGameWinner()) {
                    players.switchPlayer();
                    Display.displayplayerchange(players.getCurrentPlayer());
                }
                if (!board.noGameWinner()) {
                    int winner = board.getWinner();
                    getMatch().updateScore(winner, getBoard());
                    Display.printGameWinMessage(players, winner, match, board);
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
        getMoveHandler().setBoard(board);
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
        gamecount++;
    }

    /**
     * Prompts the user to start a new match.
     *
     * @return true if the user wants to start a new match, false otherwise
     */
    public boolean newMatch() {
        System.out.print("Would you like to play another match? (y/n): ");
        System.out.flush();
        String userInput = inputHandler.getInput();
        while (!userInput.equalsIgnoreCase("y") && !userInput.equalsIgnoreCase("n") && !userInput.equalsIgnoreCase("q")) {
            System.out.print("Please enter a valid response (y/n): ");
            System.out.flush();
            userInput = inputHandler.getInput();
        }
        return userInput.equalsIgnoreCase("y");
    }

    public void setInputHandler(InputHandler inputHandler){ //For testing
        this.inputHandler = inputHandler;
    }

}
