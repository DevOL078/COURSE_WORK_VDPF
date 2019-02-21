package ru.hse.app.settings;

public class VisualizationSettings {

    private static VisualizationSettings instance = new VisualizationSettings();

    private VisualizationSettings() {

    }

    public static VisualizationSettings getInstance() {
        return instance;
    }
}
