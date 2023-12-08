package com.example.yahtzeeextreme;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class YahtzeeController {

    /*
    Scene changing logic
     */
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
    protected void switchTo2Player(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("2_players.fxml"));
        Parent root = loader.load();
        YahtzeeController controller = loader.getController();
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);

        // Add the stylesheet here
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        stage.setScene(scene);
        stage.show();
        //filling the table
        controller.populateTable_2Players();
    }

    @FXML
    protected void switchTo3Player(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("3_players.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void switchTo4Player(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("4_players.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void switchToBotPlayer(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("bot_player.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    /*
    --Dice logic--
     */
    private YahtzeeDices yahtzeeDices = new YahtzeeDices();

    @FXML
    private Button dice1Button;
    @FXML
    private Button dice2Button;
    @FXML
    private Button dice3Button;
    @FXML
    private Button dice4Button;
    @FXML
    private Button dice5Button;

    @FXML
    protected void rollDice(ActionEvent event) {
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
    @FXML
    protected void toggleDice1Button() {
        toggleButtonState(dice1Button);
    }

    @FXML
    protected void toggleDice2Button() {
        toggleButtonState(dice2Button);
    }

    @FXML
    protected void toggleDice3Button() {
        toggleButtonState(dice3Button);
    }

    @FXML
    protected void toggleDice4Button() {
        toggleButtonState(dice4Button);
    }

    @FXML
    protected void toggleDice5Button() {
        toggleButtonState(dice5Button);
    }

    private boolean isButtonToggled(Button button) {
        String currentStyle = button.getStyle();
        return currentStyle.contains("-fx-background-color: #22561b");
    }

    //when clicked/selected the dice will turn green to help the user understand which dices will not change when clicking roll dices button
    private void toggleButtonState(Button button) {
        if (isButtonToggled(button)) {
            button.setStyle(""); // Set to default style (remove inline styles)
        } else {
            button.setStyle("-fx-background-color: #22561b; -fx-text-fill: white;");
        }
    }

    /*
    Tableview stuff
    */


@FXML private TableView<ScoreTableRow> gameTable;
@FXML private ScrollPane tableScrollPane; // Add the ScrollPane

@FXML private TableColumn<ScoreTableRow,String> categoryColumn;

@FXML private TableColumn<ScoreTableRow,String> scoreColumn;

@FXML private TableColumn<ScoreTableRow,String> score2Column;

@FXML private TableColumn<ScoreTableRow,String> score3Column;

@FXML private TableColumn<ScoreTableRow,String> score4Column;


    public void populateTable_2Players() {
        List<ScoreTableRow> dataList = new ArrayList<>();

        String[] categories = {
                "ACES", "TWOS", "THREES", "FOURS", "FIVES", "SIXES",
                "THREE OF A KIND", "FOUR OF A KIND", "FULL HOUSE",
                "SMALL STRAIGHT", "LARGE STRAIGHT", "YAHTZEE", "CHANCE"
        };

        for (String category : categories) {
            ScoreTableRow row = new ScoreTableRow(category);
            row.addPlayerScore("Player1", 0);
            row.addPlayerScore("Player2", 0);
            dataList.add(row);
        }

        gameTable.getItems().addAll(dataList);

        gameTable.setFixedCellSize(25); // Optional: Set the cell height to a fixed size if needed

        tableScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Disable horizontal scrolling
        tableScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Disable vertical scrolling

        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        scoreColumn.setCellValueFactory(cellData -> {
            Map<String, List<Integer>> playerScores = cellData.getValue().getPlayerScores();
            Integer player1Score = playerScores.get("Player1").stream().findFirst().orElse(0);
            return new SimpleStringProperty(player1Score.toString());
        });
        score2Column.setCellValueFactory(cellData -> {
            Map<String, List<Integer>> playerScores = cellData.getValue().getPlayerScores();
            Integer player2Score = playerScores.get("Player2").stream().findFirst().orElse(0);
            return new SimpleStringProperty(player2Score.toString());
        });
    }




}
