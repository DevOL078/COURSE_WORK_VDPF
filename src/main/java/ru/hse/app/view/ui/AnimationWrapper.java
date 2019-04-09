package ru.hse.app.view.ui;

import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import ru.hse.app.animation.IAnimation;

public class AnimationWrapper extends AnchorPane {

    private IAnimation animation;
    private AnimationToggleButton button;

    public AnimationWrapper(IAnimation animation) {
        this.animation = animation;

        init();
    }

    private void init() {
        button = new AnimationToggleButton();
        Label label = new Label(animation.getName());
        AnchorPane.setLeftAnchor(label, 3.0);
        AnchorPane.setTopAnchor(label, 5.0);
        AnchorPane.setRightAnchor(button, 3.0);
        AnchorPane.setTopAnchor(button, 5.0);

        super.getChildren().addAll(label, button);
    }

    public IAnimation getAnimation() {
        return animation;
    }

    public AnimationToggleButton getToggleButton() {
        return button;
    }

}
