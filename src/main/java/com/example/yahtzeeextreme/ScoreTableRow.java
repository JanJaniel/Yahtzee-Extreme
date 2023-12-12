package com.example.yahtzeeextreme;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class ScoreTableRow {
    private String category;
    private IntegerProperty player1Score = new SimpleIntegerProperty();
    private IntegerProperty player2Score = new SimpleIntegerProperty();

    public ScoreTableRow(String category) {
        this.category = category;
        this.player1Score.set(0);
        this.player2Score.set(0);
    }

    public String getCategory() {
        return category;
    }

    public void setPlayer1Score(int player1Score) {
        this.player1Score.set(player1Score);
    }

    public void setPlayer2Score(int player2Score) {
        this.player2Score.set(player2Score);
    }

    public IntegerProperty getPlayer1ScoreProperty() {
        return player1Score;
    }

    public IntegerProperty getPlayer2ScoreProperty() {
        return player2Score;
    }


}
