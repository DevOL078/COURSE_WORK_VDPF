package ru.hse.app.domain;

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
        double sx = line.getStartX();
        double sy = line.getStartY();
        double ex = line.getEndX();
        double ey = line.getEndY();

        arrow1.setEndX(ex);
        arrow1.setEndY(ey);
        arrow2.setEndX(ex);
        arrow2.setEndY(ey);

        double factor = arrowLength / Math.sqrt((sx-sy)*(sx-sy) + (ex-ey)*(ex-ey));
        double factorO = arrowWidth / Math.sqrt((sx-sy)*(sx-sy) + (ex-ey)*(ex-ey));

        double dx = (sx - ex) * factor;
        double dy = (sy - ey) * factor;

        double ox = (sx - ex) * factorO;
        double oy = (sy - ey) * factorO;

        arrow1.setStartX(ex + dx - oy);
        arrow1.setStartY(ey + dy + ox);
        arrow2.setStartX(ex + dx + oy);
        arrow2.setStartY(ey + dy - ox);

        arrow1.strokeWidthProperty().bind(line.strokeWidthProperty());
        arrow2.strokeWidthProperty().bind(line.strokeWidthProperty());
        arrow1.strokeProperty().bind(line.strokeProperty());
        arrow2.strokeProperty().bind(line.strokeProperty());

        super.getChildren().addAll(arrow1, arrow2);
    }

    private void createLabel(String labelText) {
        label = new Label(labelText);
        label.setFont(Font.font(18));
        switch(labelText) {
            case "X": {
                label.translateXProperty().bind(line.endXProperty().subtract(20));
                label.translateYProperty().bind(line.endYProperty().add(10));
                break;
            }
            case "Y": {
                label.translateXProperty().bind(line.endXProperty().add(15));
                label.translateYProperty().bind(line.endYProperty().subtract(30));
                break;
            }
            default: {
                return;
            }
        }
        super.getChildren().add(label);
    }

    public Line getLine() {
        return line;
    }

    public Label getLabel() {
        return label;
    }

}
