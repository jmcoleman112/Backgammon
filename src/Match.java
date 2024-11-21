import utilities.Colour;

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
        doubleowner = -1;
        matchWinner = -1;
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

    public void updateScore(int player){
        if (player == 0){
            RedScore+=doubleCount;
        }
        else{
            BlueScore+=doubleCount;
        }
        doubleCount = 1;
    }

    public String printScore(){
        return Colour.RED.shader() + RedScore + Colour.NONE.shader()+ "-" +Colour.BLUE.shader() + BlueScore + Colour.NONE.shader();
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

    public boolean doublelegality(int player){
        if (doubleowner == -1){
            return true;
        }
        else return doubleowner == player;
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

    public int getMatchWinner(){
        return matchWinner;
    }

}
