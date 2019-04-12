package ru.hse.app.visualization;

import javafx.scene.Group;
import ru.hse.app.animation.AnimationManager;
import ru.hse.app.controller.MainController;
import ru.hse.app.domain.Point;
import ru.hse.app.domain.PointVisual;
import ru.hse.app.repository.PointsRepository;
import ru.hse.app.settings.VisualizationSettings;
import ru.hse.app.view.AnimationSettingsVisualizer;

import java.util.Collections;
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

    public void buildVisualization(String filePath) throws Exception {
        LoadingPointsJob loader = new LoadingPointsJob();
        List<Point> points = loader.loadPoints(filePath);
        System.out.println("Points loaded: " + points.size());

        Collections.sort(points, (a,b) -> {
            if(a.getT() > b.getT()) { return 1; }
            if(a.getT() == b.getT()) { return 0; }
            else { return -1; }
        });
        PointsRepository.getInstance().savePoints(points);
        MainController.getController().onSelectionButtonClick();

        updatePoints();
    }

    public void updatePoints() {
        AnimationManager.getInstance().setCurrentAnimationByName(null);
        MainController.getController().getCoordSystem().getChildren().remove(visualizationGroup);

        pointVisuals = new BuildingVisualizationJob().build(
                PointsRepository.getInstance().getSelectedPoints());
        visualizationGroup = new Group();
        visualizationGroup.getChildren().addAll(pointVisuals);
        MainController.getController().getCoordSystem().getChildren().add(visualizationGroup);
        System.out.println("Points created: " + pointVisuals.size());

        AnimationManager.getInstance().initAnimations();
        System.out.println("Animations initialized");
    }

    public Group getVisualizationGroup() {
        return visualizationGroup;
    }

    public List<PointVisual> getPointVisuals() {
        return pointVisuals;
    }
}
