package ru.hse.app.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.hse.app.config.AppProperties;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource(AppProperties.getInstance().getMainFXMLPath()));
        primaryStage.setTitle(AppProperties.getInstance().getAppTitle());
        primaryStage.setScene(new Scene(root,
                AppProperties.getInstance().getWindowWidth(),
                AppProperties.getInstance().getWindowHeight()));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
