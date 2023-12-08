package com.example.yahtzeeextreme;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Person {
    private StringProperty id = new SimpleStringProperty();
    private StringProperty name = new SimpleStringProperty();
    private StringProperty age = new SimpleStringProperty();
    private StringProperty email = new SimpleStringProperty();

    // Constructors

    public Person(String id, String name, String age, String email) {
        this.id.set(id);
        this.name.set(name);
        this.age.set(age);
        this.email.set(email);
    }

    // Getters and setters for JavaFX properties

    public StringProperty idProperty() {
        return id;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty ageProperty() {
        return age;
    }

    public StringProperty emailProperty() {
        return email;
    }

    // Other getters and setters as needed
}
