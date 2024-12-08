package utilities;

public enum Colour {
    RED("\033[0;31m"),    // red text color
    BLUE("\033[0;34m"), // Black text colo
    NONE ("\033[0m");    // No color

    private final String ansiCode;

    // Constructor to assign ANSI code to each color
    Colour(String ansiCode) {
        this.ansiCode = ansiCode;
    }


    //get player colour based on player int
    public static String getplayercolour(int player){

        //0 = red
        if (player == 0){
            return RED.shader();
        }
        // 1 = blue
        if (player ==1){
            return BLUE.shader();
        }
        // anything else is none
        else{
            return NONE.shader();
        }
    }

    // Getter method to retrieve the ANSI code
    public String shader() {
        return ansiCode;
    }

    //return the colour of the opponent
    public Colour returnopp(){
        if (this == Colour.RED){
            return Colour.BLUE;
        }
        else{
            return Colour.RED;
        }
    }


}
