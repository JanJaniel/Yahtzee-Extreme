package com.example.yahtzeeextreme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScoreTableRow {
    private String category;
    private Integer score;
    private Map<String, List<Integer>> playerScores = new HashMap<>();

    public ScoreTableRow(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public Map<String, List<Integer>> getPlayerScores() {
        return playerScores;
    }

    public void addPlayerScore(String playerName, List<Integer> scores) {
        playerScores.put(playerName, scores);
    }

    public void addPlayerScore(String playerName, int score) {
        playerScores.computeIfAbsent(playerName, k -> new ArrayList<>()).add(score);
    }

    public void setScore(Integer score) {
        this.score = score;
    }

}
