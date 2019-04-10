package ru.hse.app.animation.impls;

import javafx.animation.*;
import javafx.scene.Group;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import ru.hse.app.animation.IAnimation;
import ru.hse.app.domain.PointVisual;
import ru.hse.app.settings.VisualizationSettings;
import ru.hse.app.visualization.VisualizationManager;

import java.util.ArrayList;
import java.util.List;

public class DynamicPointsAnimation implements IAnimation {

    private String name = "Динамические точки";
    private List<DynamicPoint> dynamicPoints;

    public DynamicPointsAnimation() {
        dynamicPoints = new ArrayList<>();

        init();
    }

    private void init() {
        dynamicPoints.clear();
        List<PointVisual> points = VisualizationManager.getInstance().getPointVisuals();

        points.forEach(p -> {
            int index = points.indexOf(p);
            if(index != points.size() - 1) {
                DynamicPoint dynamicPoint = new DynamicPoint(p.getCircle(), points.get(index + 1).getCircle());
                dynamicPoints.add(dynamicPoint);
                dynamicPoint.radiusProperty().bind(VisualizationSettings.getInstance().getPointSize().divide(2));
                dynamicPoint.setFill(VisualizationSettings.getInstance().getPointColorPaint());
                dynamicPoint.setStroke(Paint.valueOf("BLACK"));
                dynamicPoint.setStrokeWidth(2);
            }
        });
    }

    @Override
    public void play() {
        Group visGroup = VisualizationManager.getInstance().getVisualizationGroup();
        visGroup.getChildren().addAll(dynamicPoints);
        dynamicPoints.forEach(DynamicPoint::play);
    }

    @Override
    public void stop() {
        Group visGroup = VisualizationManager.getInstance().getVisualizationGroup();
        visGroup.getChildren().removeAll(dynamicPoints);
        dynamicPoints.forEach(DynamicPoint::stop);
    }

    @Override
    public String getName() {
        return this.name;
    }

    private static class DynamicPoint extends Circle{
        private TranslateTransition transition;

        DynamicPoint(Circle startPoint, Circle finishPoint) {
            transition = new TranslateTransition(new Duration(2000));
            transition.setFromX(startPoint.getCenterX());
            transition.setFromY(startPoint.getCenterY());
            transition.setToX(finishPoint.getCenterX());
            transition.setToY(finishPoint.getCenterY());
            transition.setInterpolator(Interpolator.LINEAR);
            transition.setCycleCount(TranslateTransition.INDEFINITE);
            transition.setNode(this);
        }

        void play() {
            transition.play();
        }

        void stop() {
            transition.stop();
        }
    }
}
