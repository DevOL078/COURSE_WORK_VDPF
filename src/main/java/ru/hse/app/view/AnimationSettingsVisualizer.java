package ru.hse.app.view;

import javafx.scene.layout.VBox;
import ru.hse.app.animation.AnimationManager;
import ru.hse.app.animation.IAnimation;
import ru.hse.app.view.ui.AnimationToggleButton;
import ru.hse.app.view.ui.AnimationWrapper;

import java.util.*;

public class AnimationSettingsVisualizer {

    private static AnimationSettingsVisualizer instance = new AnimationSettingsVisualizer();
    private List<AnimationWrapper> wrappers;

    private AnimationSettingsVisualizer() {
        wrappers = new ArrayList<>();
    }

    public static AnimationSettingsVisualizer getInstance() {
        return instance;
    }

    public void loadAnimations(VBox container) {
        Map<String, IAnimation> animations = AnimationManager.getInstance().getAnimations();
        for(IAnimation animation : animations.values()) {
            animation.init();
            AnimationWrapper wrapper = new AnimationWrapper(animation);
            wrapper.getToggleButton().setOnMouseClicked(e -> {
                AnimationToggleButton button = wrapper.getToggleButton();
                if(button.isSwitchedProperty().get()) {
                    AnimationManager.getInstance().setCurrentAnimationByName(null);
                    button.setIsSwitched(false);
                } else {
                    //Off current animation button
                    Optional<AnimationWrapper> optionalWrapper = wrappers.stream()
                            .filter(w -> w.getToggleButton().isSwitchedProperty().get())
                            .findAny();
                    optionalWrapper.ifPresent(animationWrapper ->
                            animationWrapper.getToggleButton().setIsSwitched(false));

                    //On this button
                    button.setIsSwitched(true);

                    //Set this animation as current
                    AnimationManager.getInstance().setCurrentAnimationByName(
                            wrapper.getAnimation().getName()
                    );
                }
            });
            wrappers.add(wrapper);
            container.getChildren().add(wrapper);
        }
    }

    public List<AnimationWrapper> getWrappers() {
        return this.wrappers;
    }

}
