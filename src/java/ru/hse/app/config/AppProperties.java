package ru.hse.app.config;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.io.File;

public class AppProperties {

    private static AppProperties instance = new AppProperties();
    private String configFilePath = "src/resources/app.config";
    private Properties prop;

    private AppProperties() {
        try {
            init();
            System.out.println("AppProperties was initialized");
        } catch (Exception e) {
            System.err.println("AppProperties was not initialized");
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

    public String getMainFXMLPath() {
        return prop.getProperty("app.main.fxml.path");
    }

    public int getWindowWidth() {
        return Integer.parseInt(prop.getProperty("app.window.width"));
    }

    public int getWindowHeight() {
        return Integer.parseInt(prop.getProperty("app.window.height"));
    }


}
