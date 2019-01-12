package ru.hse.app.controller;

import javafx.fxml.FXML;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
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
    }

    private void settingScroll(double offset) {
        workPane.setOnScroll(e->{
            if(e.getDeltaY() > 0 && coordSystem.getCamera().getTranslateZ() < coordSystem.getCamera().getFarClip()){
                coordSystem.getCamera().setTranslateZ(coordSystem.getCamera().getTranslateZ() + offset);
            }
            else if(e.getDeltaY() < 0 && coordSystem.getCamera().getTranslateZ() > offset){
                coordSystem.getCamera().setTranslateZ(coordSystem.getCamera().getTranslateZ() - offset);
            }
        });
    }

}
