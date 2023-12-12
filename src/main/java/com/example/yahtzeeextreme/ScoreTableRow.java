package com.example.yahtzeeextreme;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ScoreTableRow {
    private String category;
    private StringProperty player1Score = new SimpleStringProperty();
    private StringProperty player2Score = new SimpleStringProperty();

    public ScoreTableRow(String category) {
        this.category = category;
        this.player1Score.set("");
        this.player2Score.set("");
    }

    public String getCategory() {
        return category;
    }

    public String getPlayer1Score() {
        return player1Score.get();
    }

    public StringProperty getPlayer1ScoreProperty() {
        return player1Score;
    }

    public void setPlayer1Score(String score) {
        player1Score.set(score);
    }

    public String getPlayer2Score() {
        return player2Score.get();
    }

    public StringProperty getPlayer2ScoreProperty() {
        return player2Score;
    }

    public void setPlayer2Score(String score) {
        player2Score.set(score);
    }
}
