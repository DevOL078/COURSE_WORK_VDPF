package ru.hse.app.view.ui;

import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class AnimationToggleButton extends Parent {

    private BooleanProperty isSwitched = new SimpleBooleanProperty(false);
    private TranslateTransition translateAnimation = new TranslateTransition(Duration.seconds(0.25));
    private FillTransition fillAnimation = new FillTransition(Duration.seconds(0.25));
    private ParallelTransition switchedAnimation = new ParallelTransition(translateAnimation, fillAnimation);

    private double width = 40;
    private double height = 20;

    AnimationToggleButton() {
        initToggleButton();
    }

    private void initToggleButton() {
        Rectangle rec = new Rectangle(width, height);
        rec.setArcWidth(height);
        rec.setArcHeight(height);
        rec.setFill(Color.WHITE);
        rec.setStroke(Color.LIGHTGRAY);

        Circle trigger = new Circle(height / 2);
        trigger.setCenterX(height / 2);
        trigger.setCenterY(height / 2);
        trigger.setFill(Color.WHITE);
        trigger.setStroke(Color.LIGHTGRAY);

        super.getChildren().addAll(rec, trigger);

        translateAnimation.setNode(trigger);
        fillAnimation.setShape(rec);

        isSwitched.addListener((obs, oldState, newState) -> {
            boolean isOn = newState;
            translateAnimation.setToX(isOn ? width - height : 0);
            fillAnimation.setFromValue(isOn ? Color.WHITE : Color.GREEN);
            fillAnimation.setToValue(isOn ? Color.GREEN : Color.WHITE);

            switchedAnimation.play();
        });
    }

    public BooleanProperty isSwitchedProperty() {
        return isSwitched;
    }

    public void setIsSwitched(boolean value) {
        isSwitched.set(value);
    }

}
