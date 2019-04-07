package ru.hse.app.animation;

import ru.hse.app.animation.impls.DashedLinesAnimation;
import ru.hse.app.view.AnimationSettingsVisualizer;

import java.util.HashMap;
import java.util.Map;

public class AnimationManager {

    private static AnimationManager instance = new AnimationManager();

    private Map<String, IAnimation> animations;
    private IAnimation currentAnimation;

    private AnimationManager(){
        animations = new HashMap<>();
    }

    public static AnimationManager getInstance() {
        return instance;
    }

    public void initAnimations() {
        IAnimation dashedLinesAnimation = new DashedLinesAnimation();

        animations.put(dashedLinesAnimation.getName(), dashedLinesAnimation);
    }

    public void setCurrentAnimationByName(String name) {
        if(currentAnimation != null) {
            currentAnimation.stop();
        }
        currentAnimation = animations.get(name);
        System.out.println("Current animation: " + currentAnimation.getName());
        currentAnimation.play();
    }

    public Map<String, IAnimation> getAnimations() {
        return this.animations;
    }

}
