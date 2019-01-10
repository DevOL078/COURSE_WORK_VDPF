package ru.hse.app.domain;

import javafx.geometry.Point2D;

public class Point extends Point2D {

    private double t;

    public Point(double t, double x, double y){
        super(x,y);
        this.t = t;
    }

    public double getT(){
        return t;
    }

}
