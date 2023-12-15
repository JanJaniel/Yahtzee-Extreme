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
import java.io.IOException;
import java.util.*;

public class GameController {
    private Stage stage;
    private YahtzeeDices yahtzeeDices = new YahtzeeDices();
    @FXML private Label playerTurn;
    @FXML private Label turnsLeftLabel;
    @FXML private Label countdown;
    private CountdownTimer timer = new CountdownTimer(countdown, 5, () -> {
        try {
            finishTurn();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    });
    private static boolean successfulMove = true;
    private static int currentPlayer = 1;
    private static int totalPlayers = 2;
    private int turnsLeft = 3;

    @FXML private Button dice1Button;
    @FXML private Button dice2Button;
    @FXML private Button dice3Button;
    @FXML private Button dice4Button;
    @FXML private Button dice5Button;
    @FXML private TableView<ScoreTableRow> gameTable;
    @FXML private ScrollPane tableScrollPane;
    @FXML private TableColumn<ScoreTableRow, String> categoryColumn;
    @FXML private TableColumn<ScoreTableRow, String> scoreColumn;
    @FXML private TableColumn<ScoreTableRow, String> score2Column;
    @FXML private TableColumn<ScoreTableRow, String> score3Column;
    @FXML private TableColumn<ScoreTableRow, String> score4Column;

    public static void setSuccessfulMove(boolean successfulMove) {
        GameController.successfulMove = successfulMove;
    }
    public void setTotalPlayers(int numberOfPlayers) {
        this.totalPlayers = numberOfPlayers;
    }

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
        gameTable.setEditable(true);
        tableScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        tableScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        scoreColumn.setCellValueFactory(cellData -> cellData.getValue().getPlayer1ScoreProperty());
        score2Column.setCellValueFactory(cellData -> cellData.getValue().getPlayer2ScoreProperty());
        addOptionalPlayerColumns();

        // coloring Bonus and Score row
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
                    try {
                        updateCellValue();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    private void addOptionalPlayerColumns() {

        if (score3Column != null) {
            //   3rd player
            score3Column.setCellValueFactory(cellData -> cellData.getValue().getPlayer3ScoreProperty());
            totalPlayers = 3;
        }

        if (score4Column != null) {
            //  4th player
            score4Column.setCellValueFactory(cellData -> cellData.getValue().getPlayer4ScoreProperty());
            totalPlayers = 4;
        }
    }
    @FXML
    protected void switchToMainMenu(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("fxmlFiles/main_menu_view.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles/MainmenuStyles.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
        timer.stop();
        successfulMove = true;
        totalPlayers = 2; //back to default value
        currentPlayer = 1; //back to default
        MP3Player.playBackgroundMusic("src/main/resources/com/example/yahtzeeextreme/sounds/chip 5 minutes (128kbps).mp3");
    }

    @FXML
    protected void switchToGameOver(String winner) throws IOException {
        MP3Player.stopBackgroundMusic();
        timer.stop();
        MP3Player.playBackgroundMusic("src/main/resources/com/example/yahtzeeextreme/sounds/Wii Shop Channel Theme (Trap Remix) (128kbps).mp3");
        successfulMove = true;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxmlFiles/GameOver.fxml"));
        Parent root = loader.load();

        GameOverController gameOverController = loader.getController();
        gameOverController.setWinner(winner);

        Stage stage = (Stage) gameTable.getScene().getWindow();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("styles/GameOver.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    private void updateCellValue() throws IOException {
        ScoreTableRow clicked_on = gameTable.getSelectionModel().getSelectedItem();
        TableColumn<ScoreTableRow,?> clickedColumn = gameTable.getFocusModel().getFocusedCell().getTableColumn();

        String category = gameTable.getItems().stream()
                .filter(item -> item.equals(clicked_on))
                .findFirst()
                .map(ScoreTableRow::getCategory)
                .orElse(null);

        if (category == null || category.equals("BONUS") || category.equals("SCORE")) {
            return; // dont allow clicks an BONUS and SCORE
        }

        String points = String.valueOf(calculateCurrentScore( yahtzeeDices.getDices(), category ));

        //  update  column if empty
        if (currentPlayer == 1 && clickedColumn == scoreColumn && clicked_on.getPlayer1Score().isEmpty()) {
            clicked_on.setPlayer1Score( points) ;
        } else if (currentPlayer == 2 && clickedColumn == score2Column && clicked_on.getPlayer2Score().isEmpty()) {
            clicked_on.setPlayer2Score( points) ;
        } else if (currentPlayer == 3 && score3Column != null && clickedColumn == score3Column && clicked_on.getPlayer3Score().isEmpty()) {
            clicked_on.setPlayer3Score( points );
        } else if (currentPlayer == 4 && score4Column != null && clickedColumn == score4Column && clicked_on.getPlayer4Score().isEmpty()) {
            clicked_on.setPlayer4Score( points );
        } else {
            return;
        }

        playAppropriateSound(points);
        updateBonusPoints();
        updateScoreRow();
        successfulMove = true;
        finishTurn();
        gameTable.refresh();
    }


    private void playAppropriateSound(String newValue) {
        if (newValue.equals("0")) {
            MP3Player.playSound("src/main/resources/com/example/yahtzeeextreme/sounds/classic_hurt.mp3");
        } else {
            MP3Player.playSound("src/main/resources/com/example/yahtzeeextreme/sounds/hitmarker_2.mp3");
        }
    }


    private void updateScoreRow() {
        ScoreTableRow scoreRow = gameTable.getItems().get( gameTable.getItems().size() - 1 ); // SCORE row is the last row

        // Calculate sums for each player
        int player1Sum = calculatePlayerSum(1);
        int player2Sum = calculatePlayerSum(2);
        int player3Sum = score3Column != null ? calculatePlayerSum(3) : 0; //if player 3 mode
        int player4Sum = score4Column != null ? calculatePlayerSum(4) : 0; // if player 4 mode

        // Update the SCORE row with the calculated sums
        scoreRow.setPlayer1Score(String.valueOf( player1Sum ));
        scoreRow.setPlayer2Score(String.valueOf( player2Sum ));

        if (score3Column != null) {
            scoreRow.setPlayer3Score(String.valueOf( player3Sum ));
        }

        if (score4Column != null) {
            scoreRow.setPlayer4Score(String.valueOf( player4Sum ));
        }
    }

    private int calculatePlayerSum(int player) {
        int sum = 0;
        int size = gameTable.getItems().size();

        for (ScoreTableRow row : gameTable.getItems().subList(0, size - 1)) {
            String score;
            switch (player) {
                case 1:
                    score = row.getPlayer1Score();
                    break;
                case 2:
                    score = row.getPlayer2Score();
                    break;
                case 3:
                    score = row.getPlayer3Score();
                    break;
                case 4:
                    score = row.getPlayer4Score();
                    break;
                default:
                    score = "";
            }
            sum += getScoreValue(score);
        }

        return sum;
    }

    private int getScoreValue(String score) {
        return score.isEmpty() ? 0 : Integer.parseInt(score);
    }

    private void updateBonusPoints() {
        // Calculate and update bonus for all players
        updateBonusForPlayer(1);
        updateBonusForPlayer(2);
        if (score3Column != null) {
            updateBonusForPlayer(3);
        }
        if (score4Column != null) {
            updateBonusForPlayer(4);
        }
    }

    private void updateBonusForPlayer(int playerNumber) {
        int totalPoints = 0;
        for (int i = 0; i < 6; i++) { // Iterate over the first six rows
            ScoreTableRow row = gameTable.getItems().get(i);
            String playerScoreStr = "";
            switch (playerNumber) {
                case 1:
                    playerScoreStr = row.getPlayer1Score();
                    break;
                case 2:
                    playerScoreStr = row.getPlayer2Score();
                    break;
                case 3:
                    playerScoreStr = row.getPlayer3Score();
                    break;
                case 4:
                    playerScoreStr = row.getPlayer4Score();
                    break;
            }
            totalPoints += getScoreValue(playerScoreStr);
        }

        ScoreTableRow bonusRow = gameTable.getItems().get(6); // this is the Bonus row. It should be updated with 35 or 0 on these conditions
        if (totalPoints >= 63) {
            switch (playerNumber) {
                case 1:
                    bonusRow.setPlayer1Score("35");
                    break;
                case 2:
                    bonusRow.setPlayer2Score("35");
                    break;
                case 3:
                    bonusRow.setPlayer3Score("35");
                    break;
                case 4:
                    bonusRow.setPlayer4Score("35");
                    break;
            }
        } else {
            switch (playerNumber) {
                case 1:
                    bonusRow.setPlayer1Score("0");
                    break;
                case 2:
                    bonusRow.setPlayer2Score("0");
                    break;
                case 3:
                    bonusRow.setPlayer3Score("0");
                    break;
                case 4:
                    bonusRow.setPlayer4Score("0");
                    break;
            }
        }
    }

    @FXML
    protected void rollDice() {
        if (turnsLeft == 3) {
            if (successfulMove) {
                timer = new CountdownTimer(countdown, 5, () -> {
                    try {
                        finishTurn();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            } else {
                timer = new CountdownTimer(countdown, 10, () -> {
                    try {
                        finishTurn();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
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
    private void finishTurn() throws IOException {
        clearDiceValuesAndStyles();
        if ( isGameReadyToConclude()) {
            String winner = determineWinner();
            switchToGameOver(winner);
        }
        switchPlayers();
        timer.stop();
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

    private boolean isGameReadyToConclude() { // if a player doesnt manages to play a turn the logic for game over had to be adjusted
        boolean scoreColumnCompletelyFilled = true;
        boolean score2ColumnCompletelyFilled = true;
        boolean score3ColumnCompletelyFilled = score3Column != null;
        boolean score4ColumnCompletelyFilled = score4Column != null;
        int emptyCellsInScore2Column = 0;
        int emptyCellsInScore3Column = 0;
        int emptyCellsInScore4Column = 0;

        for (ScoreTableRow row : gameTable.getItems()) {
            // Check if any column has any empty cell and count them
            if (row.getPlayer1Score().isEmpty()) {
                scoreColumnCompletelyFilled = false;
            }
            if (row.getPlayer2Score().isEmpty()) {
                score2ColumnCompletelyFilled = false;
                emptyCellsInScore2Column++;
            }
            if (score3Column != null && row.getPlayer3Score().isEmpty()) {
                score3ColumnCompletelyFilled = false;
                emptyCellsInScore3Column++;
            }
            if (score4Column != null && row.getPlayer4Score().isEmpty()) {
                score4ColumnCompletelyFilled = false;
                emptyCellsInScore4Column++;
            }
        }

        // Logic for 4-player mode
        if (totalPlayers == 4) {
            return (scoreColumnCompletelyFilled && emptyCellsInScore2Column > 1 && emptyCellsInScore3Column > 1 && emptyCellsInScore4Column > 1) ||
                    (score2ColumnCompletelyFilled && emptyCellsInScore3Column > 2 && emptyCellsInScore4Column > 2) ||
                    (score3ColumnCompletelyFilled && emptyCellsInScore4Column > 2) ||
                    score4ColumnCompletelyFilled;
        }

        // Logic for 3-player mode
        else if (totalPlayers == 3) {
            return score3ColumnCompletelyFilled ||
                    (scoreColumnCompletelyFilled && emptyCellsInScore2Column > 1 && emptyCellsInScore3Column > 1) ||
                    (score2ColumnCompletelyFilled && emptyCellsInScore3Column > 1);
        }

        // Default 2-player logic
        return score2ColumnCompletelyFilled || (scoreColumnCompletelyFilled && emptyCellsInScore2Column > 1);
    }




    public void clearDiceValuesAndStyles(){
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

    public void switchPlayers() {
        // Increment the currentPlayer and wrap around if it exceeds totalPlayers
        currentPlayer = (currentPlayer % totalPlayers) + 1;
        playerTurn.setText("Player: " + currentPlayer);
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
                    MP3Player.playSound("src/main/resources/com/example/yahtzeeextreme/sounds/mlg-airhorn-quieter_qUq62P0.mp3");
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
        // Using a HashMap to count the occurrences of each number
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
        // Using a HashMap to count the occurrences of each number
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
        // Using a HashMap to count the occurrences of each number
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
                return true;
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

