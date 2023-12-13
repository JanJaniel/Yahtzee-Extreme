package com.example.yahtzeeextreme;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URISyntaxException;
import java.io.IOException;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.net.URL;
import java.util.*;

import java.io.IOException;
import java.util.stream.Collectors;

public class GameController {

    private Stage stage;
    private YahtzeeDices yahtzeeDices = new YahtzeeDices();
    @FXML
    private Label playerTurn;
    @FXML
    private Label turnsLeftLabel;

    @FXML private Label countdown;
    private CountdownTimer timer = new CountdownTimer(countdown, 5, () -> finishTurn());
    private static boolean successfulMove = true;

    public static void setSuccessfulMove(boolean successfulMove) {
        GameController.successfulMove = successfulMove;
    }

    private int currentPlayer = 1;
    private int turnsLeft = 3;

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
    private TableView<ScoreTableRow> gameTable;
    @FXML
    private ScrollPane tableScrollPane; // Add the ScrollPane
    @FXML
    private TableColumn<ScoreTableRow, String> categoryColumn;
    @FXML
    private TableColumn<ScoreTableRow, String> scoreColumn;
    @FXML
    private TableColumn<ScoreTableRow, String> score2Column;
    @FXML
    private TableColumn<ScoreTableRow, String> score3Column;
    @FXML
    private TableColumn<ScoreTableRow, String> score4Column;

    public void initialize() {
        List<ScoreTableRow> dataList = new ArrayList<>();

        String[] categories = {
                "ACES", "TWOS", "THREES", "FOURS", "FIVES", "SIXES", "BONUS",
                "THREE OF A KIND", "FOUR OF A KIND", "FULL HOUSE",
                "SMALL STRAIGHT", "LARGE STRAIGHT", "YAHTZEE", "CHANCE", "SCORE"
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

        scoreColumn.setCellValueFactory(cellData -> cellData.getValue().getPlayer1ScoreProperty());
        score2Column.setCellValueFactory(cellData -> cellData.getValue().getPlayer2ScoreProperty());


        gameTable.setRowFactory(tv -> {
            TableRow<ScoreTableRow> row = new TableRow<>() {
                @Override
                protected void updateItem(ScoreTableRow item, boolean empty) {
                    super.updateItem(item, empty);
                    // Remove previous style classes
                    getStyleClass().remove("special-row");

                    // Apply style to the 7th and the last row
                    if (!empty && (getIndex() == 6 || getIndex() == gameTable.getItems().size() - 1)) {
                        getStyleClass().add("special-row");
                    }
                }
            };
            row.setEditable(false); // Retain existing functionality
            return row;
        });

        // Handle the row click to update values
        gameTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1 && !gameTable.getSelectionModel().isEmpty()) {
                if (turnsLeft < 3) {
                    updateCellValue();
                }
            }
        });
    }

    @FXML
    protected void switchToMainMenu(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main_menu_view.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles/MainmenuStyles.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();


        MP3Player.playBackgroundMusic("src/main/resources/com/example/yahtzeeextreme/sounds/chip 5 minutes (128kbps).mp3");

    }
    private void updateCellValue() {

        ScoreTableRow selectedItem = gameTable.getSelectionModel().getSelectedItem();
        TableColumn<ScoreTableRow, ?> selectedColumn = gameTable.getFocusModel().getFocusedCell().getTableColumn();

        String category = gameTable.getItems().stream()
                .filter(item -> item.equals(selectedItem)) // Find the selected item
                .findFirst()
                .map(ScoreTableRow::getCategory)
                .orElse(null);


        String newValue = String.valueOf(calculateCurrentScore(yahtzeeDices.getDices(), category));

        // Determine which column is selected and update the corresponding score

        if (selectedColumn == scoreColumn && selectedItem.getPlayer1Score().isEmpty() && currentPlayer == 1) {
            selectedItem.setPlayer1Score(newValue);

            if(newValue.equals("0")){
                MP3Player.playSound("src/main/resources/com/example/yahtzeeextreme/sounds/classic_hurt.mp3");
            }else {
                MP3Player.playSound("src/main/resources/com/example/yahtzeeextreme/sounds/hitmarker_2.mp3");
            }
            updateScoreRow();
            successfulMove = true;
            finishTurn();
        } else if (selectedColumn == score2Column && selectedItem.getPlayer2Score().isEmpty() && currentPlayer == 2) {
            selectedItem.setPlayer2Score(newValue);

            if(newValue.equals("0")){
                MP3Player.playSound("src/main/resources/com/example/yahtzeeextreme/sounds/classic_hurt.mp3");
            }else {
                MP3Player.playSound("src/main/resources/com/example/yahtzeeextreme/sounds/hitmarker_2.mp3");
            }
            updateScoreRow();
            successfulMove = true;
            finishTurn();
        }
        gameTable.refresh();
    }

    private void updateScoreRow() {
        ScoreTableRow scoreRow = gameTable.getItems().get(gameTable.getItems().size() - 1); // Assuming SCORE row is the last row
        int player1Sum = calculatePlayerSum(1);
        int player2Sum = calculatePlayerSum(2);

        scoreRow.setPlayer1Score(String.valueOf(player1Sum));
        scoreRow.setPlayer2Score(String.valueOf(player2Sum));
    }

    private int calculatePlayerSum(int player) {
        int size = gameTable.getItems().size();
        return gameTable.getItems().stream()
                .limit(size - 1)  // Exclude the last element
                .filter(row -> row != null)
                .mapToInt(row -> player == 1 ? getScoreValue(row.getPlayer1Score()) : getScoreValue(row.getPlayer2Score()))
                .sum();
    }

    private int getScoreValue(String score) {
        return score.isEmpty() ? 0 : Integer.parseInt(score);
    }

    @FXML
    protected void rollDice() {
        if (turnsLeft == 3) {

            if (successfulMove) {
                timer = new CountdownTimer(countdown, 5, () -> finishTurn());
            } else {
                timer = new CountdownTimer(countdown, 10, () -> finishTurn());
            }
            timer.start();
        }

        if (turnsLeft > 0) {
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

            MP3Player.playSound("src/main/resources/com/example/yahtzeeextreme/sounds/dice-142528.mp3");

        }

    }

    // resets the dices for next player
    @FXML
    private void finishTurn() {
        System.out.println("finishTurn: "+successfulMove);
        clearDiceValuesAndStyles();
        if (areAllCellsFilled()) {
            String winner = determineWinner(); // Method to determine the winner
            showAlert("Game Over", winner);
        }
        switchPlayers();

        timer.stop();

    }




    private void showAlert(String title, String content) {
        try {
            MP3Player.stopBackgroundMusic();
            timer.stop();
            MP3Player.playBackgroundMusic("src/main/resources/com/example/yahtzeeextreme/sounds/Wii Shop Channel Theme (Trap Remix) (128kbps).mp3");

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(content);

            // Style the dialog pane directly
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.setStyle("-fx-background-color: #2c3e50; -fx-font-size: 16px; -fx-font-family: 'Arial';");

            // Style the buttons
            for (Node node : dialogPane.lookupAll(".button")) {
                ((Button) node).setStyle("-fx-background-color: #343A40; -fx-text-fill: white;");
            }

            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace(); // Print the error details
        }
    }



    private String determineWinner() {
        ScoreTableRow scoreRow = gameTable.getItems().get(gameTable.getItems().size() - 1); // Assuming SCORE row is the last row
        int player1Sum = calculatePlayerSum(1);
        int player2Sum = calculatePlayerSum(2);

        if (player1Sum > player2Sum) {
            return "Player 1 wins!";
        } else if (player2Sum > player1Sum) {
            return "Player 2 wins!";
        } else {
            return "It's a tie!";
        }
    }


    private boolean areAllCellsFilled() {
        for (ScoreTableRow row : gameTable.getItems()) {
            // Check if either player's score is an empty string
            if (row.getPlayer1Score().isEmpty() || row.getPlayer2Score().isEmpty()) {
                return false;
            }
        }
        return true;
    }



    public void clearDiceValuesAndStyles(){
        System.out.println("clearvaluesandstyles: "+successfulMove);
        if(successfulMove){
            countdown.setText("5");
        }else {
            countdown.setText("10");
        }
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

    }

    public void switchPlayers(){
        if (currentPlayer == 2) {
            currentPlayer = 1;
            playerTurn.setText("Player: " + currentPlayer);
        } else {
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

    //when clicked/selected the dice will turn green to help the user understand
    // which dices will not change when clicking roll dices button
    private void toggleButtonState(Button button) {
        MP3Player.playSound("src/main/resources/com/example/yahtzeeextreme/sounds/minecraft---menu-click-2-made-with-Voicemod-technology.mp3");
        if (isButtonToggled(button)) {
            button.setStyle(""); // Set to default style (remove inline styles)
        } else {
            button.setStyle("-fx-background-color: #22561b; -fx-text-fill: white;");
        }
    }

    // Score logic
    private int calculateCurrentScore(Dice[] diceValues, String category) {
        int score = 0;
        switch (category) {
            case "ACES":
                for (Dice dice : diceValues) {
                    if (dice.getValue() == 1) {
                        score += 1;
                    }
                }
                break;
            case "TWOS":
                for (Dice dice : diceValues) {
                    if (dice.getValue() == 2) {
                        score += 2;
                    }
                }
                break;
            case "THREES":
                for (Dice dice : diceValues) {
                    if (dice.getValue() == 3) {
                        score += 3;
                    }
                }
                break;
            case "FOURS":
                for (Dice dice : diceValues) {
                    if (dice.getValue() == 4) {
                        score += 4;
                    }
                }
                break;
            case "FIVES":
                for (Dice dice : diceValues) {
                    if (dice.getValue() == 5) {
                        score += 5;
                    }
                }
                break;
            case "SIXES":
                for (Dice dice : diceValues) {
                    if (dice.getValue() == 6) {
                        score += 6;
                    }
                }
                break;
            case "THREE OF A KIND":
                if(isThreeOfAKind(diceValues)){
                    for (Dice num : diceValues) {
                        score += num.getValue();
                    }
                }
            break;
            case "FOUR OF A KIND":
                if(isFourOfAKind(diceValues)){
                    for (Dice num : diceValues) {
                        score += num.getValue();
                    }
                }
            break;
            case "FULL HOUSE":
                if(isFullHouse(diceValues)){
                    score = 25;
                }
            break;
            case "SMALL STRAIGHT":
                System.out.println(hasLargeStraight(diceValues));
                if(hasSmallStraight(diceValues)){
                    score = 30;
                }
            break;
            case "LARGE STRAIGHT":
                if (hasLargeStraight(diceValues)){
                    score = 40;
                }
            break;
            case "YAHTZEE":
                if(areAllNumbersSame(diceValues)){
                    score = 50;
                }
            break;

            case"CHANCE":
                for (Dice num : diceValues) {
                    score += num.getValue();
                }
            break;
        }
        return score;
    }


    private static boolean isThreeOfAKind(Dice[] numbers){

        Map<Integer, Integer> countMapThree = new HashMap<>();

        for (Dice d : numbers) {
            countMapThree.put(d.getValue(), countMapThree.getOrDefault(d.getValue(), 0) + 1);
        }
        // Checking if any number occurs at least four times
        for (int count : countMapThree.values()) {
            if (count >= 3) {
               return true;
            }
        }
        return false;
    }

    private static boolean isFourOfAKind(Dice[] numbers){

        Map<Integer, Integer> countMapThree = new HashMap<>();

        for (Dice d : numbers) {
            countMapThree.put(d.getValue(), countMapThree.getOrDefault(d.getValue(), 0) + 1);
        }

        // Checking if any number occurs at least three times
        for (int count : countMapThree.values()) {
            if (count >= 4) {
                return true;
            }
        }
        return false;
    }

    private static boolean isFullHouse(Dice[] numbers) {
        // Use a HashMap to count the occurrences of each number
        Map<Integer, Integer> countMap = new HashMap<>();
        // Count occurrences
        for (Dice num : numbers) {
            countMap.put(num.getValue(), countMap.getOrDefault(num.getValue(), 0) + 1);
        }
        // Check for a full house
        boolean hasThreeOfAKind = false;
        boolean hasPair = false;

        for (int count : countMap.values()) {
            if (count == 3) {
                hasThreeOfAKind = true;
            } else if (count == 2) {
                hasPair = true;
            }
        }

        return hasThreeOfAKind && hasPair;
    }

    private static boolean areAllNumbersSame(Dice[] diceValues) {
        int firstNumber = diceValues[0].getValue();

        for (int i = 0; i < 5; i++) {
            if (diceValues[i].getValue() != firstNumber) {

                return false;
            }
        }
        return true;
    }

    public static boolean hasSmallStraight(Dice[] numbers) {

        Arrays.sort(numbers, Comparator.comparingInt(Dice::getValue));

        // Check for a small straight
        for (int i = 0; i < numbers.length - 3; i++) {
            if (numbers[i + 1].getValue() - numbers[i].getValue() <= 1 &&
                    numbers[i + 2].getValue() - numbers[i + 1].getValue() <= 1 &&
                    numbers[i + 3].getValue() - numbers[i + 2].getValue() <= 1) {
                return true; // If there are four consecutive numbers, it's a small straight
            }
        }
        return false;


    }


    public static boolean hasLargeStraight(Dice[] numbers) {
        // Sort the array based on the dice values
        Arrays.sort(numbers, Comparator.comparingInt(Dice::getValue));

        // Check for each possible large straight
        for (int i = 0; i < numbers.length - 4; i++) {
            if (numbers[i].getValue() + 1 == numbers[i + 1].getValue() &&
                    numbers[i + 1].getValue() + 1 == numbers[i + 2].getValue() &&
                    numbers[i + 2].getValue() + 1 == numbers[i + 3].getValue() &&
                    numbers[i + 3].getValue() + 1 == numbers[i + 4].getValue()) {
                return true;
            }
        }

        return false;
    }
}

