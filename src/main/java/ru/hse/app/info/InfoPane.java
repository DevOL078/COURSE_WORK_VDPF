package ru.hse.app.info;

import javafx.geometry.Point3D;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class InfoPane extends StackPane {

    private String infoText;

    public InfoPane(String infoText, double width, double height) {
        super.setPrefWidth(width);
        super.setPrefHeight(height);
        this.infoText = infoText;
        init();
    }

    private void init() {
        super.setTranslateZ(2);
        super.setRotationAxis(new Point3D(1,0,0));
        super.setRotate(180);
        super.setStyle("-fx-background-color: white; -fx-border-color: black;");

        Label label = new Label(infoText);
        super.getChildren().add(label);
    }

}
