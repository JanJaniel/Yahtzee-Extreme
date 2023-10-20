package com.example.yahtzeeextreme;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    @FXML
    private Label demo;

    @FXML
    private TableView<RowData> yahtzeeTableView;
    public TableColumn<RowData, String> name;
    public TableColumn<RowData, Integer> player1;
    public TableColumn<RowData, Integer> player2;

    @FXML
    protected void onButtonClick() {
        demo.setText("Ooops looks like this game is not really finished right now. Come back later!");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

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
}