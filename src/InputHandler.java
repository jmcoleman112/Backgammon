/**
 * The InputHandler class is responsible for handling user input in the Backgammon game.
 * It provides methods to read input from the console and to validate various commands.
 *
 * <p>Features include:</p>
 * <ul>
 *   <li>Reading input from the console.</li>
 *   <li>Validating different types of commands.</li>
 * </ul>
 *
 * <p>Author: jmcoleman112</p>
 *
 * @see java.util.Scanner
 */

import java.util.Scanner;


public class InputHandler {
    private final Scanner scanner;


    /**
     * Constructs an InputHandler with a new Scanner for reading console input.
     */
    public InputHandler(){
        this.scanner = new Scanner(System.in);
    }

    /**
     * Reads input from the console.
     *
     * @return the input entered by the user
     */
    public String getInput(){
        return scanner.nextLine();
    }

    /**
     * Checks if the input is a hint command.
     *
     * @param input the input to check
     * @return true if the input is a hint command, false otherwise
     */
    public boolean isHintCommand(String input){
        if(input == null) return false;
        return input.equalsIgnoreCase("hint"); }


    /**
     * Checks if the input is a quit command.
     *
     * @param input the input to check
     * @return true if the input is a quit command, false otherwise
     */
    public boolean isQuitCommand(String input) {
        if(input == null) return false;
        return input.equalsIgnoreCase("Q");
    }


    /**
     * Checks if the input is a board command.
     *
     * @param input the input to check
     * @return true if the input is a board command, false otherwise
     */
    public boolean isBoardCommand(String input) {
        if(input == null) return false;
        return input.equalsIgnoreCase("board");
    }


    /**
     * Checks if the input is a set board command.
     *
     * @param input the input to check
     * @return true if the input is a set board command, false otherwise
     */
    public boolean isSetBoardCommand(String input) {
        if(input == null) return false;
        return input.length() ==80 && input.substring(0, 8).equalsIgnoreCase("setboard");
    }


    /**
     * Checks if the input is a double command.
     *
     * @param input the input to check
     * @return true if the input is a double command, false otherwise
     */
    public boolean isDoubleCommand(String input) {
        if(input == null) return false;
        return input.equalsIgnoreCase("double");}


    /**
     * Checks if the input is a file command.
     *
     * @param input the input to check
     * @return true if the input is a file command, false otherwise
     */
    public boolean isfileCommand(String input) {
        if(input == null) return false;
        return input.length() >= 5 && input.substring(0, 5).equalsIgnoreCase("test ");}



    /**
     * Checks if the input is a roll command.
     *
     * @param input the input to check
     * @return true if the input is a roll command, false otherwise
     */
    public boolean isRollCommand(String input) {
        if(input == null) return false;
        return input.length() >= 4 && input.substring(0, 4).equalsIgnoreCase("roll");
    }


    /**
     * Checks if the input is a roll test command.
     *
     * @param input the input to check
     * @return true if the input is a roll test command, false otherwise
     */
    public boolean isrolltestcommand(String input) {
        if(input == null) return false;
        if (input.length() != 8) {
            return false;
        }
        try {
            int x = Integer.parseInt(input.substring(5, 6));
            int y = Integer.parseInt(input.substring(7, 8));
            return x >= 1 && x <= 6 && y >= 1 && y <= 6;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    /**
     * Checks if the input is a roll one test command.
     *
     * @param input the input to check
     * @return true if the input is a roll one test command, false otherwise
     */
    public boolean isrollOnetestcommand(String input) {
        if(input == null) return false;
        if (input.length() != 6) {
            return false;
        }
        try {
            int x = Integer.parseInt(input.substring(5, 6));
            return x >= 1 && x <= 6;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    /**
     * Checks if the input is a pip command.
     *
     * @param input the input to check
     * @return true if the input is a pip command, false otherwise
     */
    public boolean isPipCommand(String input) {
        if(input == null) return false;
        return input.equalsIgnoreCase("pip");}


    /**
     * Closes the scanner.
     */
    public void closeScanner(){
        scanner.close();
    }
}