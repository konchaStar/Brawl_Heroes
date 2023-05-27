package com.example.brawlheroes;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

public class App extends Application {
    private static Stage stage;
    public static Stage getStage() {
        return stage;
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().addAll(this.getClass().getResource("menuStyle.css").toExternalForm());
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.setOnCloseRequest((x) -> {
            System.exit(0);
        });
        stage.show();
        Settings settings = new Settings();
        settings.loadSettings();
        MenuController.setServerSettings(settings);
        this.stage = stage;
    }

}
