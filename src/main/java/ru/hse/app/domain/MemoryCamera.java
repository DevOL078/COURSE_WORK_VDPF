package ru.hse.app.domain;

import javafx.scene.PerspectiveCamera;

public class MemoryCamera extends PerspectiveCamera {

    private double lastPosX = 0;
    private double lastPosY = 0;
    private double lastPosZ = 0;

    MemoryCamera(boolean fixedEyeAtCameraZero) {
        super(fixedEyeAtCameraZero);
    }

    public void updateLastPosition() {
        lastPosX = super.getTranslateX();
        lastPosY = super.getTranslateY();
        lastPosZ = super.getTranslateZ();
    }

    public double getLastPosX() {
        return lastPosX;
    }

    public double getLastPosY() {
        return lastPosY;
    }

    public double getLastPosZ() {
        return lastPosZ;
    }

}
