package ru.hse.app.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.hse.app.animation.AnimationManager;
import ru.hse.app.config.AppProperties;
import ru.hse.app.domain.CoordAxe;
import ru.hse.app.domain.CoordSystem;
import ru.hse.app.domain.MemoryCamera;
import ru.hse.app.domain.PointVisual;
import ru.hse.app.info.InfoManager;
import ru.hse.app.main.Main;
import ru.hse.app.repository.PointsRepository;
import ru.hse.app.settings.VisualizationSettings;
import ru.hse.app.view.AnimationSettingsVisualizer;
import ru.hse.app.visualization.VisualizationManager;

import java.io.*;

public class MainController {

    @FXML
    private AnchorPane mainAnchorPane;

    @FXML
    private Pane workPane;

    @FXML
    private Button loadButton;

    @FXML
    private Button settingsButton;

    @FXML
    private GridPane settingsPane;

    @FXML
    private Button zoomMinusButton;

    @FXML
    private Button zoomPlusButton;

    @FXML
    private Button animationSettingsButton;

    @FXML
    private VBox animationsBox;

    @FXML
    private AnchorPane animationsPane;

    @FXML
    private TextField pointSizeTextField;

    @FXML
    private ColorPicker pointColorPicker;

    @FXML
    private TextField scalingCoeffTextField;

    @FXML
    private Button selectionButton;

    @FXML
    private Button searchButton;

    @FXML
    private Label allPointsLabel;

    @FXML
    private Label visualPointsLabel;

    private CoordSystem coordSystem;
    private AppProperties appProperties = AppProperties.getInstance();
    private double mousePosX;
    private double mousePosY;
    private static MainController instance;
    private Stage searchStage;
    private Stage selectionStage;
    private double cameraOffset = 30;

    @FXML
    public void initialize() {
        instance = this;
        coordSystem = new CoordSystem();

        SubScene subScene = new SubScene(coordSystem, appProperties.getWindowWidth(),
                appProperties.getWindowHeight() * 0.7, true, SceneAntialiasing.DISABLED);
        workPane.getChildren().add(subScene);

        subScene.setCamera(coordSystem.getCamera());
        subScene.widthProperty().bind(workPane.widthProperty());
        subScene.heightProperty().bind(workPane.heightProperty());

        settingScroll(appProperties.getScrollOffset());
        settingCameraMoving();

        initSettingsValues();
        initSettingsHandlers();

        settingsButton.setDisable(true);
        animationSettingsButton.setDisable(true);
        selectionButton.setDisable(true);
        searchButton.setDisable(true);

        allPointsLabel.setText("");
        visualPointsLabel.setText("");
    }

    private void initSettingsValues() {
        pointSizeTextField.setText(
                String.valueOf(
                        VisualizationSettings.getInstance().getPointSize().get()
                )
        );
        pointColorPicker.setValue(
                Color.valueOf(
                        VisualizationSettings.getInstance().getPointColor().get()
                )
        );
        scalingCoeffTextField.setText(
                String.valueOf(
                        VisualizationSettings.getInstance().getScalingCoeff().get()
                )
        );
    }

    private void initSettingsHandlers() {
        pointSizeTextField.setOnAction(e -> {
            try {
                double value = Double.parseDouble(pointSizeTextField.getText());
                pointSizeTextField.setStyle("");
                VisualizationSettings.getInstance().getPointSize().set(value);
            } catch (NumberFormatException ex) {
                pointSizeTextField.setStyle("-fx-border-color: red");
            }

        });
        pointColorPicker.setOnAction(e -> {
            Color color = pointColorPicker.getValue();
            String value = color.toString();
            String rgbValue = value.substring(2, 8);
            VisualizationSettings.getInstance().getPointColor().set(rgbValue);
        });
        scalingCoeffTextField.setOnAction(e -> {
            try {
                double value = Double.parseDouble(scalingCoeffTextField.getText());
                scalingCoeffTextField.setStyle("");
                VisualizationSettings.getInstance().getScalingCoeff().set(value);
                AnimationManager.getInstance().setCurrentAnimationByName(null);
                AnimationManager.getInstance().initAnimations();
            } catch (NumberFormatException ex) {
                scalingCoeffTextField.setStyle("-fx-border-color: red");
            }
        });
    }

    private void settingScroll(double offset) {
        workPane.setOnScroll(e->{
            if(e.getDeltaY() > 0 && coordSystem.getCamera().getTranslateZ() < 1800 - offset){
                coordSystem.getCamera().setTranslateZ(coordSystem.getCamera().getTranslateZ() + offset);
            }
            else if(e.getDeltaY() < 0 && coordSystem.getCamera().getTranslateZ() > offset){
                coordSystem.getCamera().setTranslateZ(coordSystem.getCamera().getTranslateZ() - offset);
            }
            updateAxesZoom(coordSystem);
        });
    }

    private void updateAxesZoom(CoordSystem coordSystem) {
        CoordAxe axeOX = coordSystem.getAxeOX();
        CoordAxe axeOY = coordSystem.getAxeOY();
        MemoryCamera camera = (MemoryCamera)coordSystem.getCamera();
        double cameraStartZ = appProperties.getWindowHeight() * 0.40;
        double dz = camera.getTranslateZ() - camera.getLastPosZ();

        axeOX.getLine().setEndX(axeOX.getLine().getEndX() + dz *
                (appProperties.getWindowWidth() / 2) / cameraStartZ);
        axeOX.getLine().setStartX(axeOX.getLine().getStartX() + (-1) * dz *
                (appProperties.getWindowWidth() / 2) / cameraStartZ);
        axeOY.getLine().setEndY(axeOY.getLine().getEndY() + dz);
        axeOY.getLine().setStartY(axeOY.getLine().getStartY() + (-1) * dz);

        camera.updateLastPosition();
    }

    private void settingCameraMoving() {
        workPane.setOnMousePressed(e -> {
            mousePosX = e.getSceneX();
            mousePosY = e.getSceneY();
        });

        workPane.setOnMouseDragged(e -> {
            double dx = e.getSceneX() - mousePosX;
            double dy = e.getSceneY() - mousePosY;
            mousePosX = e.getSceneX();
            mousePosY = e.getSceneY();

            coordSystem.getCamera().setTranslateX(coordSystem.getCamera().getTranslateX() - dx);
            coordSystem.getCamera().setTranslateY(coordSystem.getCamera().getTranslateY() + dy);

            updateAxesMoving(coordSystem);
        });

    }

    private void updateAxesMoving(CoordSystem coordSystem) {
        CoordAxe axeOX = coordSystem.getAxeOX();
        CoordAxe axeOY = coordSystem.getAxeOY();
        MemoryCamera camera = (MemoryCamera)coordSystem.getCamera();
        double dx = camera.getTranslateX() - camera.getLastPosX();
        double dy = camera.getTranslateY() - camera.getLastPosY();

        axeOX.getLine().setStartX(axeOX.getLine().getStartX() + dx);
        axeOX.getLine().setEndX(axeOX.getLine().getEndX() + dx);

        axeOY.getLine().setStartY(axeOY.getLine().getStartY() + dy);
        axeOY.getLine().setEndY(axeOY.getLine().getEndY() + dy);


        camera.updateLastPosition();
    }

    public void moveCameraTo(PointVisual pointVisual) {
        System.out.printf("Found point: %.3f %.3f %.3f\n",
                pointVisual.getPoint().getT(),
                pointVisual.getPoint().getX(),
                pointVisual.getPoint().getY());
        coordSystem.getCamera().setTranslateX(pointVisual.getPoint().getX());
        coordSystem.getCamera().setTranslateY(pointVisual.getPoint().getY());

        updateAxesMoving(coordSystem);

        InfoManager.getInstance().showPointInfo(pointVisual);

        searchStage.close();
    }

    public void onLoadButtonClick() {
        FileChooser fileChooser = new FileChooser();
        File inputFile = fileChooser.showOpenDialog(Main.getStage());
        if(inputFile == null) {
            return;
        }
        try {
            VisualizationManager.getInstance().clear();
            animationsBox.getChildren().clear();
            VisualizationManager.getInstance().buildVisualization(inputFile.getAbsolutePath());
            if(VisualizationManager.getInstance().getPointVisuals().size() > 0) {
                AnimationSettingsVisualizer.getInstance().loadAnimations(animationsBox);
                System.out.println("Animations menu initialized");

                settingsButton.setDisable(false);
                animationSettingsButton.setDisable(false);
                selectionButton.setDisable(false);
                searchButton.setDisable(false);

                int allPointsSize = PointsRepository.getInstance().getAllPoints().size();
                int visualPointsSize = VisualizationManager.getInstance().getPointVisuals().size();
                allPointsLabel.setText("Всего точек: " + allPointsSize);
                visualPointsLabel.setText("На графике: " + visualPointsSize);
            } else {
                settingsButton.setDisable(true);
                animationSettingsButton.setDisable(true);
                selectionButton.setDisable(true);
                searchButton.setDisable(true);

                allPointsLabel.setText("");
                visualPointsLabel.setText("");
            }
        } catch (FileNotFoundException e) {
            System.err.println("File choosing error");
        } catch (NullPointerException e) {
            System.out.println("Empty list of points for visualization");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Нет точек для построения");
            alert.setContentText("Выборка точек для построения пуста. Нажмите 'Сохранить' в окне выбора точек для построения.");
            alert.setTitle("Предупреждение");
            alert.show();
        } catch (Exception e) {
            System.err.println("Points reading error");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Ошибка чтения файла");
            alert.setContentText("Выбранный файл не соответствует формату.");
            alert.setTitle("Ошибка");
            alert.show();
        }
    }

    public void onSettingsButtonClick() {
        settingsPane.setVisible(!settingsPane.isVisible());
        if(animationsPane.isVisible()) {
            animationsPane.setVisible(false);
        }
    }

    public void onAnimationSettingsButtonClick() {
        animationsPane.setVisible(!animationsPane.isVisible());
        if(settingsPane.isVisible()) {
            settingsPane.setVisible(false);
        }
    }

    public void onSearchButtonClick() {
        try {
            System.out.println("Search click");
            searchStage = new Stage();
            searchStage.initModality(Modality.WINDOW_MODAL);
            searchStage.initOwner(Main.getStage());
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/search.fxml"));
            Scene scene = new Scene(root, 400, 400);
            searchStage.setScene(scene);
            searchStage.setTitle("Поиск точек");
            searchStage.initModality(Modality.WINDOW_MODAL);
            searchStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onSelectionButtonClick() {
        try {
            System.out.println("Selection start");
            selectionStage = new Stage();
            selectionStage.initModality(Modality.WINDOW_MODAL);
            selectionStage.initOwner(Main.getStage());
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/selection.fxml"));
            Scene scene = new Scene(root, 400, 400);
            selectionStage.setScene(scene);
            selectionStage.setTitle("Выборка точек для построения");
            selectionStage.initModality(Modality.WINDOW_MODAL);
            selectionStage.showAndWait();

            int visualPointsSize = VisualizationManager.getInstance().getPointVisuals().size();
            visualPointsLabel.setText("На графике: " + visualPointsSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static MainController getController() {
        return instance;
    }

    public Stage getSelectionStage() {
        return selectionStage;
    }

    public CoordSystem getCoordSystem() {
        return coordSystem;
    }

    public void onZoomPlusButtonClick() {
        PerspectiveCamera camera = coordSystem.getCamera();
        if(camera.getTranslateZ() < 1800 - cameraOffset){
            camera.setTranslateZ(camera.getTranslateZ() - cameraOffset);
        }
        updateAxesZoom(coordSystem);
    }

    public void onZoomMinusButtonClick() {
        PerspectiveCamera camera = coordSystem.getCamera();
        if(camera.getTranslateZ() > cameraOffset){
            camera.setTranslateZ(camera.getTranslateZ() + cameraOffset);
        }
        updateAxesZoom(coordSystem);
    }

}
