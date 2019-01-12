package ru.hse.app.domain;

import javafx.beans.binding.DoubleExpression;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;

public class CoordAxe extends Group {

    private Line line;
    private Label label;

    public CoordAxe(double startX, double startY, double endX, double endY, String labelText) {
        this.line = new Line(startX, startY, endX, endY);
        super.getChildren().add(this.line);
        createTails(10, 5);
        createLabel(labelText);
    }

    private void createTails(double arrowLength, double arrowWidth) {
        Line arrow1 = new Line();
        Line arrow2 = new Line();

        DoubleProperty sx = line.startXProperty();
        DoubleProperty sy = line.startYProperty();
        DoubleProperty ex = line.endXProperty();
        DoubleProperty ey = line.endYProperty();

        arrow1.endXProperty().bind(ex);
        arrow1.endYProperty().bind(ey);
        arrow2.endXProperty().bind(ex);
        arrow2.endYProperty().bind(ey);

        DoubleExpression sub1 = DoubleProperty.doubleExpression(sx.subtract(sy).multiply(sx.subtract(sy)));
        DoubleExpression sub2 = DoubleProperty.doubleExpression(ex.subtract(ey).multiply(ex.subtract(ey)));
        DoubleExpression sqrt = DoubleProperty.doubleExpression(new SimpleDoubleProperty(Math.sqrt(sub1.add(sub2).get())));
        DoubleExpression factor = DoubleProperty.doubleExpression(new SimpleDoubleProperty(arrowLength).divide(sqrt));
        DoubleExpression factorO = DoubleProperty.doubleExpression(new SimpleDoubleProperty(arrowWidth).divide(sqrt));

        DoubleExpression dx = sx.subtract(ex).multiply(factor);
        DoubleExpression dy = sy.subtract(ey).multiply(factor);
        DoubleExpression ox = sx.subtract(ex).multiply(factorO);
        DoubleExpression oy = sy.subtract(ey).multiply(factorO);

        arrow1.startXProperty().bind(ex.add(dx).subtract(oy));
        arrow1.startYProperty().bind(ey.add(dy).add(ox));
        arrow2.startXProperty().bind(ex.add(dx).add(oy));
        arrow2.startYProperty().bind(ey.add(dy).subtract(ox));

        arrow1.strokeWidthProperty().bind(line.strokeWidthProperty());
        arrow2.strokeWidthProperty().bind(line.strokeWidthProperty());
        arrow1.strokeProperty().bind(line.strokeProperty());
        arrow2.strokeProperty().bind(line.strokeProperty());

        super.getChildren().addAll(arrow1, arrow2);
    }

    private void createLabel(String labelText) {
        label = new Label(labelText);
        label.setFont(Font.font(18));
        super.getChildren().add(label);
    }

    public Line getLine() {
        return line;
    }

    public Label getLabel() {
        return label;
    }

}
