package ru.hse.app.controller;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import ru.hse.app.config.AppProperties;
import ru.hse.app.domain.Point;
import ru.hse.app.repository.PointsRepository;
import ru.hse.app.visualization.VisualizationManager;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SelectionController {

    @FXML
    private TextArea pointNumbersArea;

    @FXML
    private TextArea functionArea;

    @FXML
    private Button callFunctionButton;

    @FXML
    private TableView<Point> pointsTable;

    @FXML
    private Button callNumbersButton;

    @FXML
    private Button saveButton;

    private List<Point> points;

    @FXML
    public void initialize() {
        List<Point> selectedPoints = FXCollections.observableList(
                PointsRepository.getInstance().getSelectedPoints()
        );
        if(selectedPoints.size() <= AppProperties.getInstance().getPointsMaxNumber()) {
            initTable();
            updatePointNumbersAreaText(points);
        } else {
            selectedPoints.forEach(p -> p.setIsSelected(false));
            initTable();
            saveButton.setDisable(true);
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Внимание!");
            alert.setHeaderText("Превышено максимальное количество точек.");
            alert.setContentText(String.format("Вы должны выбрать не более %d точек для построения.",
                    AppProperties.getInstance().getPointsMaxNumber()));
            alert.showAndWait();
        }

        callNumbersButton.setDisable(true);
    }

    private void initTable() {
        pointsTable.setEditable(true);
        points = PointsRepository.getInstance().getAllPoints();

        if(PointsRepository.getInstance().getAllPoints() == null) {
            return;
        }

        TableColumn<Point, Boolean> isSelectColumn = new TableColumn<>();
        TableColumn<Point, Integer> numberColumn = new TableColumn<>("№");
        TableColumn<Point, Double> tColumn = new TableColumn<>("t");
        TableColumn<Point, Double> xColumn = new TableColumn<>("x");
        TableColumn<Point, Double> yColumn = new TableColumn<>("y");

        tColumn.setCellValueFactory(param -> {
            Point point = param.getValue();
            double t = point.getT();
            return new SimpleObjectProperty<>(t);
        });
        numberColumn.setCellValueFactory(param -> {
            Point point = param.getValue();
            return new SimpleObjectProperty<>(points.indexOf(point) + 1);
        });
        xColumn.setCellValueFactory(param -> {
            Point point = param.getValue();
            double t = point.getX();
            return new SimpleObjectProperty<>(t);
        });
        yColumn.setCellValueFactory(param -> {
            Point point = param.getValue();
            double t = point.getY();
            return new SimpleObjectProperty<>(t);
        });
        isSelectColumn.setCellValueFactory(c -> {
            Point point = c.getValue();
            SimpleBooleanProperty booleanProperty = new SimpleBooleanProperty(point.isSelected());
            booleanProperty.addListener((observable, oldValue, newValue) -> {
                System.out.println("Point " + point.getT() + " changed. Selected = " + newValue);
                point.setIsSelected(newValue);
                updatePointNumbersAreaText(points);
                checkSelectedPointsNumber();
            });
            return booleanProperty;
        });
        isSelectColumn.setCellFactory(c -> {
            CheckBoxTableCell<Point, Boolean> cell = new CheckBoxTableCell<>();
            cell.setAlignment(Pos.CENTER);
            return cell;
        });

        isSelectColumn.prefWidthProperty().bind(pointsTable.widthProperty().divide(5));
        numberColumn.prefWidthProperty().bind(pointsTable.widthProperty().divide(5));
        tColumn.prefWidthProperty().bind(pointsTable.widthProperty().divide(5));
        xColumn.prefWidthProperty().bind(pointsTable.widthProperty().divide(5));
        yColumn.prefWidthProperty().bind(pointsTable.widthProperty().divide(5));

        pointsTable.getColumns().addAll(
                isSelectColumn,
                numberColumn,
                tColumn,
                xColumn,
                yColumn
        );

        pointsTable.getItems().addAll(points);

        pointNumbersArea.setOnKeyReleased(e -> {
            String text = pointNumbersArea.getText();
            text = text.replaceAll(", ", ",");
            Pattern pattern = Pattern.compile("[0-9]*([,][0-9]+)*");
            if(!pattern.matcher(text).matches()) {
                pointNumbersArea.setStyle("-fx-border-color: red");
                callNumbersButton.setDisable(true);
            }
            else {
                pointNumbersArea.setStyle("");
                callNumbersButton.setDisable(false);
            }
        });
    }

    private void updatePointNumbersAreaText(List<Point> points) {
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < points.size(); ++i) {
            Point point = points.get(i);
            if(point.isSelected()) {
                if(builder.length() != 0) {
                    builder.append(", ");
                }
                builder.append(String.valueOf(i + 1));
            }
        }
        pointNumbersArea.setText(builder.toString());
    }

    public void onCallNumbersClick() {
        String text = pointNumbersArea.getText();
        text = text.replaceAll(", ", ",");
        points.forEach(p -> p.setIsSelected(false));
        String[] numbers = text.split(",");
        for(String s : numbers) {
            int number = Integer.parseInt(s);
            SimpleBooleanProperty value = (SimpleBooleanProperty) pointsTable.getColumns().get(0).getCellObservableValue(number - 1);
            value.setValue(true);
        }
        pointsTable.refresh();
        checkSelectedPointsNumber();
    }

    private void checkSelectedPointsNumber() {
        List<Point> selectedPoints = PointsRepository.getInstance().getSelectedPoints();
        if(selectedPoints.size() > AppProperties.getInstance().getPointsMaxNumber() ||
                selectedPoints.size() == 0) {
            saveButton.setDisable(true);
        }
        else {
            saveButton.setDisable(false);
        }
    }

    public void onSaveButtonClick() {
        MainController.getController().getSelectionStage().close();
        VisualizationManager.getInstance().updatePoints();
    }


}
