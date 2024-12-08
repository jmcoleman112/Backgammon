//By Group 33: Jack Coleman (21207103) and Naoise Golden (21376026)
//GITHUB ID: jmcoleman112, NaoiseG

package Model;

public class Dice {
    private int dice1;
    private int dice2;

    //initilize dice as impossible roles
    public Dice(){
        this.dice1 = -1;
        this.dice2 = -1;
    }

    //get dice values
    public int[] getDiceValues(){
        int[] values = new int[2];
        values[0] = dice1;
        values[1] = dice2;
        return values;
    }

    //generate one dice roll between 1 and 6
    public void firstRoll(){
        dice1 = (int) (Math.random() * 6) + 1;
    }


    //generate two dice rolls between 1 and 6
    public int[] rollDice() {
        dice1 = (int) (Math.random() * 6) + 1;
        dice2 = (int) (Math.random() * 6) + 1;
        int[] values = new int[2];
        values[0] = dice1;
        values[1] = dice2;

        //if doubles, return 4 dice rolls instead of 2
        if(values[0]==values[1]){
            values = new int[]{values[0], values[0], values[0], values[0]};
        }
        return values;
    }

}
