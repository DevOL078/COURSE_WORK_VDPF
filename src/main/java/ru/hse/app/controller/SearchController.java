package ru.hse.app.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class SearchController {

    @FXML
    private TextField tField;

    @FXML
    private TextField xField;

    @FXML
    private TextField yField;

    @FXML
    private TableView pointsTable;

    @FXML
    private Button goToButton;

    @FXML
    public void initialize() {

    }

    public void onGoToClick() {
        System.out.println("Go to click");
        //TODO
    }

}
