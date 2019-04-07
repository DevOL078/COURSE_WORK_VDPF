package ru.hse.app.animation.impls;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Group;
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

        init();
    }

    private void init() {
        lines.clear();
        Group visGroup = VisualizationManager.getInstance().getVisualizationGroup();
        List<PointVisual> pointVisuals = VisualizationManager.getInstance().getPointVisuals();

        for(int i = 0; i < pointVisuals.size() - 1; ++i) {
            Point p1 = pointVisuals.get(i).getPoint();
            Point p2 = pointVisuals.get(i+1).getPoint();
            DynamicLine line = new DynamicLine(p1, p2);
            lines.add(line);
        }
    }

    @Override
    public void play() {
        lines.forEach(DynamicLine::playAnimation);
        Group visGroup = VisualizationManager.getInstance().getVisualizationGroup();
        visGroup.getChildren().addAll(lines);
        System.out.println("Animation started");
    }

    @Override
    public void stop() {
        lines.forEach(DynamicLine::stopAnimation);
        Group visGroup = VisualizationManager.getInstance().getVisualizationGroup();
        visGroup.getChildren().removeAll(lines);
        System.out.println("Animation stopped");
    }

    @Override
    public String getName() {
        return this.name;
    }

    private class DynamicLine extends Line{
        private Timeline timeline;

        DynamicLine (Point start, Point end) {
            super(start.getX(), start.getY(), end.getX(), end.getY());
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
                                    maxOffset,
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

