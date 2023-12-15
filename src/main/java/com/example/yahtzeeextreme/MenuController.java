package com.example.yahtzeeextreme;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.util.Objects;

public class MenuController {

    private Stage stage;
    private Scene scene;





    @FXML protected void switchTo2Player(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("fxmlFiles/2_players.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);

        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles/GameStyles.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();


        MP3Player.playBackgroundMusic("src/main/resources/com/example/yahtzeeextreme/sounds/pet the peepo 1 hours (128kbps).mp3");
        MP3Player.setVolume(0.5);
    }

    @FXML protected void switchTo3Player(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("fxmlFiles/3_players.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles/GameStyles.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
        MP3Player.playBackgroundMusic("src/main/resources/com/example/yahtzeeextreme/sounds/pet the peepo 1 hours (128kbps).mp3");
        MP3Player.setVolume(0.5);
    }

    @FXML protected void switchTo4Player(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("fxmlFiles/4_players.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles/GameStyles.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
        MP3Player.playBackgroundMusic("src/main/resources/com/example/yahtzeeextreme/sounds/pet the peepo 1 hours (128kbps).mp3");
        MP3Player.setVolume(0.5);
    }
}
