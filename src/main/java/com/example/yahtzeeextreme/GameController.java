package com.example.yahtzeeextreme;

import javafx.animation.KeyFrame;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GameController {

    private Stage stage;
    private YahtzeeDices yahtzeeDices = new YahtzeeDices();
    @FXML private Label playerTurn;
    @FXML private Label turnsLeftLabel;

    private int currentPlayer = 1;
    private int turnsLeft = 3;

    @FXML private Button dice1Button;
    @FXML private Button dice2Button;
    @FXML private Button dice3Button;
    @FXML private Button dice4Button;
    @FXML private Button dice5Button;



    @FXML private TableView<ScoreTableRow> gameTable;
    @FXML private ScrollPane tableScrollPane; // Add the ScrollPane
    @FXML private TableColumn<ScoreTableRow,String> categoryColumn;
    @FXML private TableColumn<ScoreTableRow,Integer> scoreColumn;
    @FXML private TableColumn<ScoreTableRow,Integer> score2Column;
    @FXML private TableColumn<ScoreTableRow,Integer> score3Column;
    @FXML private TableColumn<ScoreTableRow,Integer> score4Column;

    public void initialize() {
        List<ScoreTableRow> dataList = new ArrayList<>();

        String[] categories = {
                "ACES", "TWOS", "THREES", "FOURS", "FIVES", "SIXES",
                "THREE OF A KIND", "FOUR OF A KIND", "FULL HOUSE",
                "SMALL STRAIGHT", "LARGE STRAIGHT", "YAHTZEE", "CHANCE"
        };

        for (String category : categories) {
            ScoreTableRow row = new ScoreTableRow(category);
            dataList.add(row);
        }

        gameTable.getItems().addAll(dataList);

        gameTable.setFixedCellSize(25);

        tableScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        tableScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

        gameTable.setEditable(true); // Set editable to true


        scoreColumn.setCellValueFactory(cellData -> cellData.getValue().getPlayer1ScoreProperty().asObject());
        score2Column.setCellValueFactory(cellData -> cellData.getValue().getPlayer2ScoreProperty().asObject());

        gameTable.setRowFactory(tv -> {
            TableRow<ScoreTableRow> row = new TableRow<>();
            row.setEditable(false);
            return row;
        });

        // Handle the row click to update values
        gameTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1 && !gameTable.getSelectionModel().isEmpty()) {
                ScoreTableRow selectedItem = gameTable.getSelectionModel().getSelectedItem();
                int newValue = calculateCurrentScore(yahtzeeDices.getDices());

                // Determine which column is selected and update the corresponding score
                TableColumn<ScoreTableRow, ?> selectedColumn = gameTable.getFocusModel().getFocusedCell().getTableColumn();
                if (selectedColumn == scoreColumn) {
                    selectedItem.setPlayer1Score(newValue);
                } else if (selectedColumn == score2Column) {
                    selectedItem.setPlayer2Score(newValue);
                }

                gameTable.refresh();
            }
        });


    }







    @FXML
    protected void switchToMainMenu(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main_menu_view.fxml")));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles/MainmenuStyles.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    @FXML protected void rollDice() {

        if (turnsLeft > 0){
            turnsLeft--;
            turnsLeftLabel.setText("Rolls left: " + turnsLeft);

            // Get the current state of each button
            boolean isDice1Toggled = isButtonToggled(dice1Button);
            boolean isDice2Toggled = isButtonToggled(dice2Button);
            boolean isDice3Toggled = isButtonToggled(dice3Button);
            boolean isDice4Toggled = isButtonToggled(dice4Button);
            boolean isDice5Toggled = isButtonToggled(dice5Button);
            if (!isDice1Toggled) yahtzeeDices.getDices()[0].rollDice();
            if (!isDice2Toggled) yahtzeeDices.getDices()[1].rollDice();
            if (!isDice3Toggled) yahtzeeDices.getDices()[2].rollDice();
            if (!isDice4Toggled) yahtzeeDices.getDices()[3].rollDice();
            if (!isDice5Toggled) yahtzeeDices.getDices()[4].rollDice();
            updateDiceLabels();
            System.out.println(calculateCurrentScore(yahtzeeDices.getDices()));

        }
    }

    // resets the dices for next player
    @FXML private void finishTurn(){
        turnsLeft = 3;
        turnsLeftLabel.setText("Turns left: " + turnsLeft);

        dice1Button.setText("");
        dice2Button.setText("");
        dice3Button.setText("");
        dice4Button.setText("");
        dice5Button.setText("");

        dice1Button.setStyle("");
        dice2Button.setStyle("");
        dice3Button.setStyle("");
        dice4Button.setStyle("");
        dice5Button.setStyle("");

        if (currentPlayer == 2){
            currentPlayer = 1;
            playerTurn.setText("Player: " + currentPlayer);
        }else{
            currentPlayer++;
            playerTurn.setText("Player: " + currentPlayer);
        }
    }

    private void updateDiceLabels() {
        Dice[] dices = yahtzeeDices.getDices();
        //updating each dice with new value
        dice1Button.setText(String.valueOf(dices[0].getValue()));
        dice2Button.setText(String.valueOf(dices[1].getValue()));
        dice3Button.setText(String.valueOf(dices[2].getValue()));
        dice4Button.setText(String.valueOf(dices[3].getValue()));
        dice5Button.setText(String.valueOf(dices[4].getValue()));
    }
    @FXML protected void toggleDice1Button() {
        toggleButtonState(dice1Button);
    }
    @FXML protected void toggleDice2Button() {
        toggleButtonState(dice2Button);
    }
    @FXML protected void toggleDice3Button() {
        toggleButtonState(dice3Button);
    }
    @FXML protected void toggleDice4Button() {
        toggleButtonState(dice4Button);
    }
    @FXML protected void toggleDice5Button() {
        toggleButtonState(dice5Button);
    }

    private boolean isButtonToggled(Button button) {
        String currentStyle = button.getStyle();
        return currentStyle.contains("-fx-background-color: #22561b");
    }

    //when clicked/selected the dice will turn green to help the user understand
    // which dices will not change when clicking roll dices button
    private void toggleButtonState(Button button) {
        if (isButtonToggled(button)) {
            button.setStyle(""); // Set to default style (remove inline styles)
        } else {
            button.setStyle("-fx-background-color: #22561b; -fx-text-fill: white;");
        }
    }

    private int calculateCurrentScore(Dice[] diceValues) {

        int score = diceValues[0].getValue() +
                    diceValues[1].getValue() +
                    diceValues[2].getValue() +
                    diceValues[3].getValue() +
                    diceValues[4].getValue() ;
        return score;
    }
}
