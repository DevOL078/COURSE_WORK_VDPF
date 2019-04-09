package ru.hse.app.config;

import java.io.*;
import java.util.Properties;

public class AppProperties {

    private static AppProperties instance = new AppProperties();
    private String configFilePath = "src/main/resources/app.config";
    private Properties prop;

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
        prop = new Properties();
        InputStream is = new FileInputStream(configFilePath);

        prop.load(is);
    }

    public String getAppVersion() {
        return prop.getProperty("app.version");
    }

    public String getAppTitle() {
        return prop.getProperty("app.title");
    }

    public int getWindowWidth() {
        return Integer.parseInt(prop.getProperty("app.window.width"));
    }

    public int getWindowHeight() {
        return Integer.parseInt(prop.getProperty("app.window.height"));
    }

    public double getScrollOffset() {
        return Double.parseDouble(prop.getProperty("app.camera.offset"));
    }

    public double getFarClip() {
        return Double.parseDouble(prop.getProperty("app.camera.far.clip"));
    }

    public double getCameraAngle() {
        return Double.parseDouble(prop.getProperty("app.camera.angle"));
    }

    public double getPointSize() {return Double.parseDouble(prop.getProperty("app.point.size"));}

    public String getPointColor() {return prop.getProperty("app.point.color");}

    public void setPointSize(double value) {
        prop.setProperty("app.point.size", String.valueOf(value));
        storeConfigs();
    }

    public void setPointColor(String color) {
        prop.setProperty("app.point.color", color);
        storeConfigs();
    }

    private void storeConfigs() {
        try {
            prop.store(new FileOutputStream(configFilePath), "Last changing");
            System.out.println("New configurations saved");
        } catch (IOException e) {
            System.err.println("Store error");
            e.printStackTrace();
        }
    }
}
