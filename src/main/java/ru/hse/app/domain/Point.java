package ru.hse.app.domain;

import javafx.geometry.Point2D;

public class Point extends Point2D {

    private double t;
    private boolean isSelected;

    public Point(double t, double x, double y){
        super(x,y);
        this.t = t;
        this.isSelected = true;
    }

    public double getT(){
        return t;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean value) {
        isSelected = value;
    }

    public String getInfoText() {
        return String.format("X = %.3f\nY = %.3f\nT = %.3f", super.getX(), super.getY(), this.t);
    }



}
