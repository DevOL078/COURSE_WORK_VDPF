package ru.hse.app.visualization;

import javafx.scene.Group;
import ru.hse.app.animation.AnimationManager;
import ru.hse.app.controller.MainController;
import ru.hse.app.domain.Point;
import ru.hse.app.domain.PointVisual;
import ru.hse.app.repository.PointsRepository;

import java.util.Comparator;
import java.util.List;

public class VisualizationManager {

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

        points.sort(Comparator.comparingDouble(Point::getT));
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
