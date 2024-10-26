public class Input {


    public void rollDice(){
        int dice1 = (int)(Math.random()*6) + 1;
        int dice2 = (int)(Math.random()*6) + 1;
        System.out.println("You rolled a " + dice1 + " and a " + dice2);
    }

    public void parseInput(String input){
        if(input.equals("roll")){
            rollDice();
        }

        if(input.equals("q")){
            System.out.println("Thank You for playing!");
            System.exit(0);
        }
    }

}
