package ru.hse.app.visualization;

import javafx.scene.Group;
import ru.hse.app.animation.AnimationManager;
import ru.hse.app.domain.Point;
import ru.hse.app.domain.PointVisual;
import ru.hse.app.settings.VisualizationSettings;

import java.util.List;

public class VisualizationManager {

    private VisualizationSettings visualizationSettings = VisualizationSettings.getInstance();
    private static VisualizationManager instance = new VisualizationManager();
    private Group visualizationGroup;
    private List<PointVisual> pointVisuals;

    private VisualizationManager() {
    }

    public static VisualizationManager getInstance() {
        return instance;
    }

    public Group buildVisualization(String filePath) throws Exception {
        LoadingPointsJob loader = new LoadingPointsJob();
        List<Point> points = loader.loadPoints(filePath);
        System.out.println("Points loaded: " + points.size());

        //Selection

        pointVisuals = new BuildingVisualizationJob().build(points);
        visualizationGroup = new Group();
        visualizationGroup.getChildren().addAll(pointVisuals);
        return visualizationGroup;
    }

    public Group getVisualizationGroup() {
        return visualizationGroup;
    }

    public List<PointVisual> getPointVisuals() {
        return pointVisuals;
    }
}
