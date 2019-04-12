package ru.hse.app.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import ru.hse.app.domain.PointVisual;
import ru.hse.app.visualization.VisualizationManager;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SearchController {

    @FXML
    private TextField tField;

    @FXML
    private TextField xField;

    @FXML
    private TextField yField;

    @FXML
    private TableView<PointVisual> pointsTable;

    @FXML
    private Button goToButton;

    private Pattern pattern = Pattern.compile("[0-9]*[.]?[0-9]*");

    @FXML
    public void initialize() {
        goToButton.setDisable(true);
        initPointsTable();
        setValidationToField(tField);
        setValidationToField(xField);
        setValidationToField(yField);
    }

    private void initPointsTable() {
        TableColumn<PointVisual, Double> tColumn = new TableColumn<>("t");
        TableColumn<PointVisual, Double> xColumn = new TableColumn<>("x");
        TableColumn<PointVisual, Double> yColumn = new TableColumn<>("y");

        tColumn.setCellValueFactory(param -> {
            PointVisual pointVisual = param.getValue();
            double t = pointVisual.getPoint().getT();
            return new SimpleObjectProperty<>(t);
        });
        xColumn.setCellValueFactory(param -> {
            PointVisual pointVisual = param.getValue();
            double t = pointVisual.getPoint().getX();
            return new SimpleObjectProperty<>(t);
        });
        yColumn.setCellValueFactory(param -> {
            PointVisual pointVisual = param.getValue();
            double t = pointVisual.getPoint().getY();
            return new SimpleObjectProperty<>(t);
        });

        pointsTable.getColumns().addAll(tColumn, xColumn, yColumn);
        tColumn.prefWidthProperty().bind(pointsTable.widthProperty().divide(3));
        xColumn.prefWidthProperty().bind(pointsTable.widthProperty().divide(3));
        yColumn.prefWidthProperty().bind(pointsTable.widthProperty().divide(3));

        List<PointVisual> pointVisualList = VisualizationManager.getInstance().getPointVisuals();
        if(pointVisualList != null && !pointVisualList.isEmpty()) {
            pointsTable.setItems(FXCollections.observableList(pointVisualList));
        }

        pointsTable.setOnMouseClicked(e -> {
            int index = pointsTable.getSelectionModel().getSelectedIndex();
            if(index == -1) {
                goToButton.setDisable(true);
            }
            else {
                goToButton.setDisable(false);
            }
        });
    }

    private void setValidationToField(TextField field) {
        field.setOnKeyReleased(e -> {
            String text = field.getText();
            Matcher matcher = pattern.matcher(text);
            if(matcher.matches()) {
                field.setStyle(null);

                //Filter items
                String tText = tField.getText();
                String xText = xField.getText();
                String yText = yField.getText();
                List<PointVisual> items = VisualizationManager.getInstance().getPointVisuals();
                if(items == null || items.isEmpty()) { return; }
                List<PointVisual> filteredItems = items.stream().filter(pv -> {
                    String tPv = String.valueOf(pv.getPoint().getT());
                    String xPv = String.valueOf(pv.getPoint().getX());
                    String yPv = String.valueOf(pv.getPoint().getY());
                    boolean result = true;
                    if(!tText.isEmpty()) {
                        result = result && tPv.startsWith(tText);
                    }
                    if(!xText.isEmpty()) {
                        result = result && xPv.startsWith(xText);
                    }
                    if(!yText.isEmpty()) {
                        result = result && yPv.startsWith(yText);
                    }
                    return result;
                }).collect(Collectors.toList());
                pointsTable.setItems(FXCollections.observableList(filteredItems));
            }
            else {
                field.setStyle("-fx-border-color: red");
            }
        });
    }

    public void onGoToClick() {
        int index = pointsTable.getSelectionModel().getSelectedIndex();
        MainController.getController().moveCameraTo(pointsTable.getItems().get(index));
    }

}
