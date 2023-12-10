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

            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main_menu_view.fxml")));
            Scene mainMenuScene = new Scene(root);
            mainMenuScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles/MainmenuStyles.css")).toExternalForm());
            //stage.getIcons().add(new Image("/com/example/yahtzeeextreme/images/icon_top_left.jpg"));
            stage.initStyle(StageStyle.UTILITY);
            stage.setTitle("Yahtzee Extreme");
            stage.setScene(mainMenuScene);
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        launch();
    }


}