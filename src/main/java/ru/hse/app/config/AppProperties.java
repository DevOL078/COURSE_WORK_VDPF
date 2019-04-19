package ru.hse.app.config;

import com.sun.javafx.fxml.PropertyNotFoundException;

import java.io.*;

public class AppProperties {

    private static AppProperties instance = new AppProperties();
    private String configFilePath = "src/main/resources/config/app.config" /*"./app.config"*/ ;
    private SortedProperties prop;

    private AppProperties() {
        try {
            init();
            System.out.println("App properties was initialized");
        } catch (Exception e) {
            System.err.println("App properties was not initialized");
            System.err.println(e.getMessage());
        }
    }

    public static AppProperties getInstance() {
        return instance;
    }

    private void init() throws Exception {
        prop = new SortedProperties();
        try(InputStream is = new FileInputStream(configFilePath)) {
            prop.load(is);
        }
    }

    public String getAppTitle() {
        return prop.getProperty("app.title");
    }

    public int getWindowWidth() {
        return 1024;
    }

    public int getWindowHeight() {
        return 640;
    }

    public double getScrollOffset() {
        return 20;
    }

    public double getFarClip() {
        return Double.parseDouble(prop.getProperty("app.camera.far.clip"));
    }

    public double getCameraAngle() {
        return 90;
    }

    public int getPointsMaxNumber() {
        return 1000;
    }

    public double getPointSize() {return Double.parseDouble(prop.getProperty("app.point.size"));}

    public String getPointColor() {return prop.getProperty("app.point.color");}

    public Double getScalingCoefficient() {return Double.parseDouble(prop.getProperty("app.point.scale.coeff"));}

    public void setPointSize(double value) {
        prop.setProperty("app.point.size", String.valueOf(value));
        storeConfigs();
    }

    public void setPointColor(String color) {
        prop.setProperty("app.point.color", color);
        storeConfigs();
    }

    public void setScalingCoeff(double value) {
        prop.setProperty("app.point.scale.coeff", String.valueOf(value));
        storeConfigs();
    }

    private void storeConfigs() {
        try(BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream(configFilePath)
                )
        )) {
            prop.store(writer);
            System.out.println("New configurations saved");
        } catch (IOException e) {
            System.err.println("Store error");
            e.printStackTrace();
        }
    }
}
