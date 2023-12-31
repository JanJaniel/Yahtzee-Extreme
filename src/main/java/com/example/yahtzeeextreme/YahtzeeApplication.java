package com.example.yahtzeeextreme;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class YahtzeeApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        try{

            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("fxmlFiles/main_menu_view.fxml")));
            Scene mainMenuScene = new Scene(root);
            mainMenuScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles/MainmenuStyles.css")).toExternalForm());
            stage.getIcons().add(new Image(getClass().getResource("/com/example/yahtzeeextreme/images/Squidward.png").toExternalForm()));

            //stage.initStyle(StageStyle.UTILITY);
            stage.setTitle("Yahtzee Extreme");
            stage.setScene(mainMenuScene);
            stage.show();
            MP3Player.playBackgroundMusic("src/main/resources/com/example/yahtzeeextreme/sounds/chip 5 minutes (128kbps).mp3");

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        launch();
    }


}