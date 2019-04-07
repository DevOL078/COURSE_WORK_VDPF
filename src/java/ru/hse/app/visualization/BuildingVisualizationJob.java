package ru.hse.app.visualization;

import javafx.scene.Group;
import ru.hse.app.domain.Point;
import ru.hse.app.domain.PointVisual;

import java.util.ArrayList;
import java.util.List;

class BuildingVisualizationJob {

    BuildingVisualizationJob() {

    }

    List<PointVisual> build(List<Point> points) {
        List<PointVisual> pointVisuals = new ArrayList<>();
        for(Point point : points) {
            PointVisual pointVisual = new PointVisual(point);
            pointVisuals.add(pointVisual);
        }

        return pointVisuals;
    }

}
