package com.example.yahtzeeextreme;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScoreTable {


    private String category;
    private Map<String, List<Integer>> playerScores = new HashMap<>();

    public ScoreTable(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public Map<String, List<Integer>> getPlayerScores() {
        return playerScores;
    }



}
