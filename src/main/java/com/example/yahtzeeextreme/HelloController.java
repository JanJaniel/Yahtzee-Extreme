package com.example.yahtzeeextreme;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.util.Random;

import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    @FXML
    private Label demo;
    @FXML
    private Label randomNumbersLabel;
    @FXML
    private Label playersTurn;
    @FXML
    private TextField numPlayersField;
    @FXML
    private TableView<RowData> yahtzeeTableView;
    public TableColumn<RowData, String> name;
    @FXML
    private Label error;

    @FXML
    protected void handleEnterKeyPressed(){
        error.setText("-noch nicht implementiert-");
    }


    @FXML
    protected void onButtonClick() {
        String randomNumbers = generateRandomNumbers();
        String currentPlayerTurn = playersTurn.getText();

        if (currentPlayerTurn.equals("Turn: Player 1")){
            playersTurn.setText("Turn: Player 2");
        } else if (currentPlayerTurn.equals("Turn: Player 2")) {
            playersTurn.setText("Turn: Player 1");
        }

        // Update the Label with the random numbers
        randomNumbersLabel.setText(randomNumbers);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        playersTurn.setText("Turn: Player 1");

//        int numPlayers = Integer.parseInt(numPlayersField.getText());

        // Create and add columns based on the number of players
//        for (int playerNum = 1; playerNum <= numPlayers; playerNum++) {
//            TableColumn<RowData, Integer> playerColumn = new TableColumn<>("Player " + playerNum);
//            playerColumn.setCellValueFactory(new PropertyValueFactory<>("score" + playerNum));
//            yahtzeeTableView.getColumns().add(playerColumn);
//        }

        ObservableList<RowData> data = FXCollections.observableArrayList();

        data.add(new RowData(" One ", "", ""));
        data.add(new RowData(" Two ", "", ""));
        data.add(new RowData(" Three ", "", ""));
        data.add(new RowData(" Four ", "", ""));
        data.add(new RowData(" Five ", "", ""));
        data.add(new RowData(" Six ", "", ""));
        data.add(new RowData(" Three of  a Kind ", "", ""));
        data.add(new RowData(" Four of  a Kind ", "", ""));
        data.add(new RowData(" Full House ", "", ""));
        data.add(new RowData(" Small Straight ", "", ""));
        data.add(new RowData(" Large Straight ", "", ""));
        data.add(new RowData(" YAHTZEE ", "", ""));


        yahtzeeTableView.setItems(data);

    }

    private String generateRandomNumbers() {
        Random random = new Random();
        StringBuilder numbers = new StringBuilder();

        for (int i = 0; i < 5; i++) {
            if (i > 0) {
                numbers.append(", ");
            }
            int randomNumber = random.nextInt(6) + 1; // Random number between 1 and 6
            numbers.append(randomNumber);
        }

        return numbers.toString();
    }
}