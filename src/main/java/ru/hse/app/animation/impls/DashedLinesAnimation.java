package ru.hse.app.animation.impls;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import ru.hse.app.animation.IAnimation;
import ru.hse.app.domain.Point;
import ru.hse.app.domain.PointVisual;
import ru.hse.app.visualization.VisualizationManager;

import java.util.ArrayList;
import java.util.List;

public class DashedLinesAnimation implements IAnimation {

    private String name = "Динамические отрезки";
    private List<DynamicLine> lines;

    public DashedLinesAnimation() {
        this.lines = new ArrayList<>();
    }

    @Override
    public void init() {
        lines.clear();
        List<PointVisual> pointVisuals = VisualizationManager.getInstance().getPointVisuals();

        for(int i = 0; i < pointVisuals.size() - 1; ++i) {
            Circle c1 = pointVisuals.get(i).getCircle();
            Circle c2 = pointVisuals.get(i+1).getCircle();
            DynamicLine line = new DynamicLine(c1, c2);
            lines.add(line);
        }
    }

    @Override
    public void play() {
        lines.forEach(DynamicLine::playAnimation);
        Group visGroup = VisualizationManager.getInstance().getVisualizationGroup();
        visGroup.getChildren().addAll(lines);
    }

    @Override
    public void stop() {
        lines.forEach(DynamicLine::stopAnimation);
        Group visGroup = VisualizationManager.getInstance().getVisualizationGroup();
        visGroup.getChildren().removeAll(lines);
    }

    @Override
    public String getName() {
        return this.name;
    }

    private class DynamicLine extends Line{
        private Timeline timeline;

        DynamicLine (Circle start, Circle end) {
            super.startXProperty().bind(start.centerXProperty());
            super.startYProperty().bind(start.centerYProperty());
            super.endXProperty().bind(end.centerXProperty());
            super.endYProperty().bind(end.centerYProperty());
            super.getStrokeDashArray().setAll(2d, 5d);
            super.setStrokeWidth(2);
            initTimeLine();
        }

        private void initTimeLine() {
            final double maxOffset =
                super.getStrokeDashArray().stream().reduce(0d,(a, b) -> a + b);

            timeline = new Timeline(
                    new KeyFrame(
                            Duration.ZERO,
                            new KeyValue(
                                    super.strokeDashOffsetProperty(),
                                    0,
                                    Interpolator.LINEAR
                            )
                    ),
                    new KeyFrame(
                            Duration.seconds(1),
                            new KeyValue(
                                    super.strokeDashOffsetProperty(),
                                    -maxOffset,
                                    Interpolator.LINEAR
                            )
                    )
            );
            timeline.setCycleCount(Timeline.INDEFINITE);
        }

        void playAnimation() {
            timeline.play();
        }

        void stopAnimation() {
            timeline.stop();
        }
    }
}

