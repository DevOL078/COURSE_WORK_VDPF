package ru.hse.app.animation.impls;

import javafx.animation.Animation;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import ru.hse.app.animation.IAnimation;
import ru.hse.app.domain.PointVisual;
import ru.hse.app.visualization.VisualizationManager;

import java.util.List;

public class StepsAnimation implements IAnimation {

    private String name = "Следы";
    private SequentialTransition sequentialTransition;

    @Override
    public void init() {
        sequentialTransition = new SequentialTransition();
        List<PointVisual> pointVisuals = VisualizationManager.getInstance().getPointVisuals();
        for(PointVisual pv : pointVisuals) {
            Circle circle = pv.getCircle();
            ScaleTransition transition = new ScaleTransition(new Duration(500));
            transition.setNode(circle);
            transition.setFromX(0);
            transition.setToX(1);
            transition.setFromY(0);
            transition.setToY(1);

            sequentialTransition.getChildren().add(transition);
        }
        sequentialTransition.setCycleCount(Animation.INDEFINITE);
    }

    @Override
    public void play() {
        List<PointVisual> points = VisualizationManager.getInstance().getPointVisuals();
        points.forEach(p -> {
            p.getCircle().setScaleX(0);
            p.getCircle().setScaleY(0);
        });
        sequentialTransition.play();
    }

    @Override
    public void stop() {
        sequentialTransition.stop();
        List<PointVisual> points = VisualizationManager.getInstance().getPointVisuals();
        points.forEach(p -> {
            p.getCircle().setScaleX(1);
            p.getCircle().setScaleY(1);
        });
    }

    @Override
    public String getName() {
        return this.name;
    }
}
