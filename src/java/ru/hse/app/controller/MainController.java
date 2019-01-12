package ru.hse.app.controller;

import javafx.fxml.FXML;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import ru.hse.app.config.AppProperties;
import ru.hse.app.domain.CoordSystem;

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
        });
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
        });

    }

}
