package com.example.yahtzeeextreme;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;

import java.util.Collection;

public class ScoreTableRow {
    private String category;
    private StringProperty player1Score = new SimpleStringProperty();
    private StringProperty player2Score = new SimpleStringProperty();
    private StringProperty player3Score = new SimpleStringProperty();
    private StringProperty player4Score = new SimpleStringProperty();

    public ScoreTableRow(String category) {
        this.category = category;
        this.player1Score.set("");
        this.player2Score.set("");
        this.player3Score.set("");
        this.player4Score.set("");
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

    public String getPlayer3Score() {
        return player3Score.get();
    }

    public void setPlayer3Score(String score) {
        player3Score.set(score);
    }

    public StringProperty getPlayer3ScoreProperty() {
        return player3Score;
    }

    public String getPlayer4Score() {
        return player4Score.get();
    }

    public void setPlayer4Score(String score) {
        player4Score.set(score);
    }

    public StringProperty getPlayer4ScoreProperty() {
        return player4Score;
    }

}
