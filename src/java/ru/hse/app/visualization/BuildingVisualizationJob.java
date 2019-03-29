package ru.hse.app.visualization;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import ru.hse.app.domain.Point;
import ru.hse.app.domain.PointVisual;

import java.util.List;

class BuildingVisualizationJob {

    BuildingVisualizationJob() {

    }

    Group build(List<Point> points) {
        Group visualization = new Group();
        for(Point point : points) {
            PointVisual pointVisual = new PointVisual(point);
            visualization.getChildren().add(pointVisual);
        }

        return visualization;
    }

}
