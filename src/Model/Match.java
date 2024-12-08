//===============================================================
//By Group 33: Jack Coleman (21207103) and Naoise Golden (21376026)
//GITHUB ID: jmcoleman112, NaoiseG
//================================================================

package Model;

import View.Colour;

public class Match {

    private int RedScore;
    private int BlueScore;
    private int doubleCount;
    private int matchLength;
    private int doubleowner;
    private int matchWinner;

    public Match(){
        this.RedScore = 0;
        this.BlueScore = 0;
        this.doubleCount = 1;
        doubleowner = -1; //No initial double owner
        matchWinner = -1; //No winner
    }

    public void setScore(int score, int player){ //For testing
        if(player ==0) RedScore = score;
        else if(player ==1) BlueScore = score;
    }

    public void setMatchLength(int matchLength){
        this.matchLength = matchLength;
    }

    public void setDoubleOwner(int player){
        doubleowner = player;
    }

    public int getDoubleOwner(){
        return doubleowner;
    }

    //Updates score based on points scored
    public void updateScore(int player, Board board){
        if (player == 0){
            RedScore+=returnPointsScored(player, board);
        }
        else{
            BlueScore+=returnPointsScored(player, board);;
        }
        doubleCount = 1;
    }

    public String printScore(){
        return Colour.RED.shader() + RedScore + Colour.NONE.shader()+ "-" +Colour.BLUE.shader() + BlueScore + Colour.NONE.shader();
    }

    public int printScorelength(){
        String Score = RedScore + "-" + BlueScore;
        return Score.length();
    }

    public int getRedScore(){
        return RedScore;
    }
    public int getBlueScore(){
        return BlueScore;
    }
    public int getMatchLength(){
        return matchLength;
    }
    public int getDoubleCount(){
        return doubleCount;
    }

    public void updateDoubleCount(){
        doubleCount*=2;
    }

    //Checks if double is legeal
    public boolean doublelegality(int player){
        if (doubleowner == -1){ //Anyone can double initially
            return true;
        }
        else return doubleowner == player; //Must own dice
    }

    public boolean noMatchWinner(){
        if(BlueScore >= matchLength){
            matchWinner = 1;
            return false;
        }
        else if(RedScore >= matchLength){
            matchWinner = 0;
            return false;
        }
        else return true;
    }

    //Returns points scored based on type of win
    public int returnPointsScored(int winner, Board board){
        int loser = (winner + 1) % 2;
        int i_start;
        int i_end;
        if(winner == 1) {
            i_start = 18;
            i_end = 23;
        }
        else{
            i_start = 0;
            i_end = 5;
        }

        if(board.getTotalPipCount(winner) > 0) return doubleCount; //Forfeit

        else

        if(board.getEnd(loser).getCount() > 0) return doubleCount; //Single

        else if(board.getBar(loser).getCount() > 0) return 3*doubleCount; //Backgammon

        else for(int i = i_start; i <= i_end; i++) {
            if(board.getPoint(i).getCount() > 0) return 3*doubleCount; //Backgammon
        }
        for(int i = 6; i <= 17; i++){
            if(board.getPoint(i).getCount() > 0) return 2*doubleCount; //Gammon
        }
        return doubleCount; //Single
    }

    public int getMatchWinner(){
        return matchWinner;
    }

}
