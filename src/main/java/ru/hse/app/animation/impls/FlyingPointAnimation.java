package ru.hse.app.animation.impls;

import javafx.animation.Interpolator;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import ru.hse.app.animation.IAnimation;
import ru.hse.app.domain.Point;
import ru.hse.app.domain.PointVisual;
import ru.hse.app.visualization.VisualizationManager;

import java.util.List;

public class FlyingPointAnimation implements IAnimation {

    private String name = "Летающая точка";
    private Circle point;
    private SequentialTransition transitions;

    public FlyingPointAnimation() {
        point = new Circle();
        point.setFill(Color.valueOf("RED"));
        point.setRadius(5);
    }

    @Override
    public void init() {
        transitions = new SequentialTransition();
        List<PointVisual> points = VisualizationManager.getInstance().getPointVisuals();
        for(int i = 0; i < points.size() - 1; ++i) {
            Point start = points.get(i).getPoint();
            Point finish = points.get(i+1).getPoint();
            TranslateTransition translateTransition = new TranslateTransition(new Duration(1000));
            translateTransition.setFromX(start.getX());
            translateTransition.setFromY(start.getY());
            translateTransition.setToX(finish.getX());
            translateTransition.setToY(finish.getY());
            translateTransition.setInterpolator(Interpolator.LINEAR);
            translateTransition.setNode(point);
            PauseTransition pauseTransition = new PauseTransition(new Duration(200));
            transitions.getChildren().addAll(translateTransition, pauseTransition);
        }
        transitions.setCycleCount(TranslateTransition.INDEFINITE);
    }

    @Override
    public void play() {
        Group visGroup = VisualizationManager.getInstance().getVisualizationGroup();
        visGroup.getChildren().add(point);
        transitions.play();
    }

    @Override
    public void stop() {
        Group visGroup = VisualizationManager.getInstance().getVisualizationGroup();
        visGroup.getChildren().remove(point);
        transitions.stop();
    }

    @Override
    public String getName() {
        return this.name;
    }
}
