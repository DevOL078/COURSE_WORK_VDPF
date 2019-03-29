package ru.hse.app.visualization;

import javafx.scene.Group;
import ru.hse.app.domain.Point;
import ru.hse.app.settings.VisualizationSettings;

import java.util.List;

public class VisualizationManager {

    private VisualizationSettings visualizationSettings = VisualizationSettings.getInstance();
    private static VisualizationManager instance = new VisualizationManager();
    private Group visualizationGroup;

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

        visualizationGroup = new BuildingVisualizationJob().build(points);
        return visualizationGroup;
    }

    public Group getVisualizationGroup() {
        return visualizationGroup;
    }
}
