package ru.hse.app.visualization;

import javafx.scene.Group;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import ru.hse.app.domain.Point;

import java.util.List;

class BuildingVisualizationJob {

    BuildingVisualizationJob() {

    }

    Group build(List<Point> points) {
        Group visualization = new Group();
        for(Point point : points) {
            Circle pointVisual = new Circle(point.getX(), point.getY(), 10);    //TODO (перенести радиус в настройки)
            pointVisual.setFill(Paint.valueOf("YELLOW"));                               //TODO (перенести цвет в настройки)
            pointVisual.setStroke(Paint.valueOf("BLACK"));                              //TODO (перенести цвет контура в настройки)
            pointVisual.setStrokeWidth(2);
            visualization.getChildren().add(pointVisual);
        }

        return visualization;
    }

}
