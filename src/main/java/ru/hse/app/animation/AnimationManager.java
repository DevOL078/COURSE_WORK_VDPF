package ru.hse.app.animation;

import ru.hse.app.animation.impls.*;
import ru.hse.app.view.AnimationSettingsVisualizer;
import ru.hse.app.view.ui.AnimationWrapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnimationManager {

    private static AnimationManager instance = new AnimationManager();

    private Map<String, IAnimation> animations;
    private IAnimation currentAnimation;

    private AnimationManager(){
        animations = new HashMap<>();

        IAnimation dashedLinesAnimation = new DashedLinesAnimation();
        IAnimation dynamicPointsAnimation = new DynamicPointsAnimation();
        IAnimation flyingPointAnimation = new FlyingPointAnimation();
        IAnimation scalingAnimation = new ScalingAnimation();
        IAnimation stepsAnimation = new StepsAnimation();


        animations.put(dashedLinesAnimation.getName(), dashedLinesAnimation);
        animations.put(dynamicPointsAnimation.getName(), dynamicPointsAnimation);
        animations.put(flyingPointAnimation.getName(), flyingPointAnimation);
        animations.put(scalingAnimation.getName(), scalingAnimation);
        animations.put(stepsAnimation.getName(), stepsAnimation);
    }

    public static AnimationManager getInstance() {
        return instance;
    }

    public void initAnimations() {
        animations.values().forEach(IAnimation::init);
    }

    public void setCurrentAnimationByName(String name) {
        if(currentAnimation != null) {
            currentAnimation.stop();
            System.out.println("Animation stopped");
            currentAnimation = null;
        }
        if(name != null) {
            currentAnimation = animations.get(name);
            if(currentAnimation != null) {
                System.out.println("Current animation: " + currentAnimation.getName());
                currentAnimation.play();
                System.out.println("Animation started");
            }
        } else {
            List<AnimationWrapper> wrapperList = AnimationSettingsVisualizer
                    .getInstance()
                    .getWrappers();
            wrapperList.forEach(w -> w.getToggleButton().setIsSwitched(false));
        }
    }

    public Map<String, IAnimation> getAnimations() {
        return this.animations;
    }

}
