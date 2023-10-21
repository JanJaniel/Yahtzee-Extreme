package com.example.yahtzeeextreme;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class YahtzeeController {


    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    protected void switchToMainMenu(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("main_menu_view.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    protected void switchToGame(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("yahtzee_game_view.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}




























//-----gamescene---------------------------------------------------------
//
//    @FXML
//    protected void handleEnterKeyPressed(){
//        error.setText("-noch nicht implementiert-");
//    }
//
//    @FXML
//    protected void onButtonClick() {
//        String randomNumbers = generateRandomNumbers();
//        String currentPlayerTurn = playersTurn.getText();
//
//        if (currentPlayerTurn.equals("Turn: Player 1")){
//            playersTurn.setText("Turn: Player 2");
//        } else if (currentPlayerTurn.equals("Turn: Player 2")) {
//            playersTurn.setText("Turn: Player 1");
//        }
//
//        // Update the Label with the random numbers
//        randomNumbersLabel.setText(randomNumbers);
//    }













//    private String generateRandomNumbers() {
//        Random random = new Random();
//        StringBuilder numbers = new StringBuilder();
//
//        for (int i = 0; i < 5; i++) {
//            if (i > 0) {
//                numbers.append(", ");
//            }
//            int randomNumber = random.nextInt(6) + 1; // Random number between 1 and 6
//            numbers.append(randomNumber);
//        }
//
//        return numbers.toString();
//    }



//    playersTurn.setText("Turn: Player 1");
//
////        int numPlayers = Integer.parseInt(numPlayersField.getText());
//
//        // Create and add columns based on the number of players
////        for (int playerNum = 1; playerNum <= numPlayers; playerNum++) {
////            TableColumn<RowData, Integer> playerColumn = new TableColumn<>("Player " + playerNum);
////            playerColumn.setCellValueFactory(new PropertyValueFactory<>("score" + playerNum));
////            yahtzeeTableView.getColumns().add(playerColumn);
////        }
//
//        ObservableList<RowData> data = FXCollections.observableArrayList();
//
//        data.add(new RowData(" One ", "", ""));
//        data.add(new RowData(" Two ", "", ""));
//        data.add(new RowData(" Three ", "", ""));
//        data.add(new RowData(" Four ", "", ""));
//        data.add(new RowData(" Five ", "", ""));
//        data.add(new RowData(" Six ", "", ""));
//        data.add(new RowData(" Three of  a Kind ", "", ""));
//        data.add(new RowData(" Four of  a Kind ", "", ""));
//        data.add(new RowData(" Full House ", "", ""));
//        data.add(new RowData(" Small Straight ", "", ""));
//        data.add(new RowData(" Large Straight ", "", ""));
//        data.add(new RowData(" YAHTZEE ", "", ""));
//
//
//        yahtzeeTableView.setItems(data);