package ru.hse.app.domain;

import javafx.scene.Group;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import ru.hse.app.info.InfoManager;
import ru.hse.app.info.InfoPane;
import ru.hse.app.settings.VisualizationSettings;

public class PointVisual extends Group {

    private Point point;
    private Circle circle;
    private InfoPane infoPane;

    public PointVisual(Point point) {
        this.point = point;
        this.infoPane = new InfoPane(point.getInfoText(), 100, 70);
        initCircle();
        initInfoPane();
        initHandlers();
    }

    private void initCircle() {
        circle = new Circle();
        circle.setCenterX(point.getX());
        circle.setCenterY(point.getY());
        circle.radiusProperty().bind(VisualizationSettings.getInstance().getPointSize());
        circle.setFill(VisualizationSettings.getInstance().getPointColorPaint());
        circle.setStroke(Paint.valueOf("BLACK"));                              //TODO (перенести цвет контура в настройки)
        circle.setStrokeWidth(2);
        super.getChildren().add(circle);
    }

    private void initInfoPane() {
        infoPane.setVisible(false);
        infoPane.setTranslateX(point.getX());
        infoPane.setTranslateY(point.getY());
        super.getChildren().add(infoPane);
    }

    private void initHandlers() {
        circle.setOnMouseClicked(e -> {
            System.out.println(String.format("Info point: t=%.3f x=%.3f y=%.3f", point.getT(), point.getX(), point.getY()));
            InfoManager.getInstance().showPointInfo(this);
        });
        infoPane.setOnMouseClicked(e -> {
            InfoManager.getInstance().hideCurrentInfo();
        });
    }

    public InfoPane getInfoPane() {
        return this.infoPane;
    }

    public Circle getCircle() {return this.circle;}

}
