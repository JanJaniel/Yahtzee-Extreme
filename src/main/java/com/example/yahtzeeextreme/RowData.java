package com.example.yahtzeeextreme;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class RowData {
    private StringProperty category = new SimpleStringProperty();
    private StringProperty score1 = new SimpleStringProperty();
    private StringProperty score2 = new SimpleStringProperty();

    public RowData(String category, String score1, String score2) {
        setCategory(category);
        setScore1(score1);
        setScore2(score2);
    }

    public StringProperty categoryProperty() {
        return category;
    }

    public StringProperty score1Property() {
        return score1;
    }

    public StringProperty score2Property() {
        return score2;
    }

    public String getCategory() {
        return category.get();
    }

    public void setCategory(String category) {
        this.category.set(category);
    }

    public String getScore1() {
        return score1.get();
    }

    public void setScore1(String score1) {
        this.score1.set(score1);
    }

    public String getScore2() {
        return score2.get();
    }

    public void setScore2(String score2) {
        this.score2.set(score2);
    }
}
