package ru.hse.app.animation.impls;

import javafx.animation.Animation;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import ru.hse.app.animation.IAnimation;
import ru.hse.app.domain.PointVisual;
import ru.hse.app.visualization.VisualizationManager;

import java.util.List;

public class ScalingAnimation implements IAnimation {

    private String name = "Изменение размера";
    private double deltaScale = 1.3;
    private ParallelTransition animation;

    @Override
    public void init() {
        animation = new ParallelTransition();
        animation.setCycleCount(Animation.INDEFINITE);
        List<PointVisual> points = VisualizationManager.getInstance().getPointVisuals();
        for(int i = 0; i < points.size(); ++i) {
            PointVisual pv = points.get(i);
            Circle circle = pv.getCircle();
            SequentialTransition animation = new SequentialTransition();

            ScaleTransition transitionBig = new ScaleTransition(new Duration(1000));
            transitionBig.setByX(deltaScale);
            transitionBig.setByY(deltaScale);
            transitionBig.setCycleCount(1);
            transitionBig.setNode(circle);

            ScaleTransition transitionSmall = new ScaleTransition(new Duration(1000));
            transitionSmall.setByX(-deltaScale);
            transitionSmall.setByY(-deltaScale);
            transitionSmall.setCycleCount(1);
            transitionSmall.setNode(circle);

            animation.getChildren().addAll(transitionBig, transitionSmall);
            animation.setDelay(new Duration(500*i));

            this.animation.getChildren().add(animation);
        }
    }

    @Override
    public void play() {
        animation.play();
    }

    @Override
    public void stop() {
        animation.stop();
        List<PointVisual> points = VisualizationManager.getInstance().getPointVisuals();
        for(PointVisual pv : points) {
            Circle circle = pv.getCircle();
            circle.setScaleX(1);
            circle.setScaleY(1);
        }
    }

    @Override
    public String getName() {
        return this.name;
    }

}
