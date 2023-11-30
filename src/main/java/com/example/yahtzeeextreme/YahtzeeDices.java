package com.example.yahtzeeextreme;


/*
The set of 5 dices for the game
 */
public class YahtzeeDices {

    private Dice[] dices;

    public YahtzeeDices(){
        dices = new Dice[5];
        rollAllDices();
    }

    public void rollAllDices(){
        for (int i = 0; i < dices.length; i++) {
            dices[i] = new Dice();
        }
    }

    public Dice[] getDices() {
        return dices;
    }

}
