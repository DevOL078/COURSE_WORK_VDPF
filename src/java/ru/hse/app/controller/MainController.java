package ru.hse.app.controller;

import javafx.fxml.FXML;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import ru.hse.app.config.AppProperties;
import ru.hse.app.domain.CoordAxe;
import ru.hse.app.domain.CoordSystem;
import ru.hse.app.domain.MemoryCamera;

public class MainController {

    @FXML
    private AnchorPane mainAnchorPane;

    @FXML
    private Pane workPane;

    @FXML
    private GridPane buttonsGrid;

    @FXML
    private TextField selectionTextField;

    private CoordSystem coordSystem;
    private AppProperties appProperties = AppProperties.getInstance();
    private double mousePosX;
    private double mousePosY;

    @FXML
    public void initialize() {
        coordSystem = new CoordSystem();

        SubScene subScene = new SubScene(coordSystem, appProperties.getWindowWidth(),
                appProperties.getWindowHeight() * 0.7, true, SceneAntialiasing.DISABLED);
        workPane.getChildren().add(subScene);

        subScene.setCamera(coordSystem.getCamera());
        subScene.widthProperty().bind(workPane.widthProperty());
        subScene.heightProperty().bind(workPane.heightProperty());

        settingScroll(appProperties.getScrollOffset());
        settingCameraMoving();

        Circle testPoint = new Circle(50, 50, 10);
        testPoint.setFill(Paint.valueOf("YELLOW"));
        coordSystem.getChildren().add(testPoint);
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
        double cameraStartZ = appProperties.getWindowHeight() * 0.35;
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

}
