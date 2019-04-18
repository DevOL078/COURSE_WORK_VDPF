package ru.hse.app.settings;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Paint;
import ru.hse.app.config.AppProperties;
import ru.hse.app.domain.PointVisual;
import ru.hse.app.visualization.VisualizationManager;

public class VisualizationSettings {

    private static VisualizationSettings instance = new VisualizationSettings();
    private AppProperties appProperties = AppProperties.getInstance();

    private DoubleProperty pointSize = new SimpleDoubleProperty(appProperties.getPointSize());
    private StringProperty pointColor = new SimpleStringProperty(appProperties.getPointColor());
    private DoubleProperty scalingCoeff = new SimpleDoubleProperty(appProperties.getScalingCoefficient());
    private Paint pointColorPaint = Paint.valueOf(pointColor.get());

    private VisualizationSettings() {
        pointSize.addListener(e -> {
            System.out.println("Change point size: " + pointSize.getValue());
            AppProperties.getInstance().setPointSize(pointSize.getValue());
        });
        pointColor.addListener(e -> {
            System.out.println("Change point color: " + pointColor.get());
            pointColorPaint = Paint.valueOf(pointColor.get());
            Group visualizationGroup = VisualizationManager.getInstance().getVisualizationGroup();
            if(visualizationGroup != null && !visualizationGroup.getChildren().isEmpty()) {
                for(Node pv : visualizationGroup.getChildren()) {
                    PointVisual pointVisual = (PointVisual)pv;
                    pointVisual.getCircle().setFill(pointColorPaint);
                }
            }
            AppProperties.getInstance().setPointColor(pointColor.get());
        });
        scalingCoeff.addListener(e -> {
            System.out.println("Change scaling coefficient: " + scalingCoeff.getValue());
            AppProperties.getInstance().setScalingCoeff(scalingCoeff.getValue());
        });
    }

    public static VisualizationSettings getInstance() {
        return instance;
    }

    public DoubleProperty getPointSize() {
        return pointSize;
    }

    public StringProperty getPointColor() {
        return pointColor;
    }

    public DoubleProperty getScalingCoeff() {return scalingCoeff; }

    public Paint getPointColorPaint() {
        return pointColorPaint;
    }
}
