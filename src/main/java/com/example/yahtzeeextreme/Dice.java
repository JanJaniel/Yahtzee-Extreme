package com.example.yahtzeeextreme;

public class Dice {

    private int value;

    public Dice(){
        rollDice();
    }

    public void rollDice(){
        setValue(value = (int) (Math.random() * 6) + 1);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
