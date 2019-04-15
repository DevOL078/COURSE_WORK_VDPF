package ru.hse.app.controller;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import ru.hse.app.config.AppProperties;
import ru.hse.app.domain.Point;
import ru.hse.app.repository.PointsRepository;
import ru.hse.app.selection.functional.Function;
import ru.hse.app.visualization.VisualizationManager;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class SelectionController {

    @FXML
    private TextArea pointNumbersArea;

    @FXML
    private Button callFunctionButton;

    @FXML
    private TableView<Point> pointsTable;

    @FXML
    private Button callNumbersButton;

    @FXML
    private TextField functionField;

    @FXML
    private TextField pointCountField;

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

        pointNumbersArea.setOnKeyReleased(e -> {
            String text = pointNumbersArea.getText();
            text = text.replaceAll(", ", ",");
            Pattern pattern = Pattern.compile("[0-9]*([,][0-9]+)*");
            patternWarningAndDisable(pattern, text, pointNumbersArea, callNumbersButton);
        });

        pointCountField.setOnKeyReleased(e -> {
            String text = pointCountField.getText();
            Pattern pattern = Pattern.compile("[0-9]*");
            patternWarningAndDisable(pattern, text, pointCountField, callFunctionButton);
        });
    }

    private void patternWarningAndDisable(Pattern pattern, String text, Node node, Button button) {
        if(!pattern.matcher(text).matches()) {
            node.setStyle("-fx-border-color: red");
            button.setDisable(true);
        }
        else {
            node.setStyle("");
            button.setDisable(false);
        }
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
        String[] numbersStrArr = text.split(",");
        List<Integer> numbers = new ArrayList<>();
        for(String s : numbersStrArr) {
            Integer i = Integer.parseInt(s);
            numbers.add(i);
        }
        selectPoints(numbers);
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

    public void onCallFunctionClick() {
        String expression = functionField.getText();
        int pointsCount = Integer.parseInt(pointCountField.getText());

        List<Integer> pointsNumbers = getPointsNumbers(expression, pointsCount);
        selectPoints(pointsNumbers);
        pointsTable.refresh();
        checkSelectedPointsNumber();
    }

    private void selectPoints(List<Integer> numbers) {
        points.forEach(p -> p.setIsSelected(false));
        for(Integer i : numbers) {
            SimpleBooleanProperty value = (SimpleBooleanProperty) pointsTable.getColumns().get(0).getCellObservableValue(i-1);
            value.setValue(true);
        }
    }

    private List<Integer> getPointsNumbers(String expression, int pointsCount) {
        Function function = new Function(expression);
        List<Integer> pointsNumbers = new ArrayList<>();
        int i = 1;
        while(i <= pointsCount) {
            int number = function.calculate(i);
            pointsNumbers.add(number);
            i++;
        }
        return pointsNumbers;
    }

    public void onSaveButtonClick() {
        MainController.getController().getSelectionStage().close();
        VisualizationManager.getInstance().updatePoints();
    }


}
