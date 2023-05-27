package com.example.brawlheroes;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {
    @FXML
    private TextField ipField;
    @FXML
    private TextField portField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Settings settings = MenuController.getServerSettings();
        ipField.setText(settings.getIp());
        portField.setText(settings.getPort());
    }
    @FXML
    public void onBack() {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("menu.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        scene.getStylesheets().addAll(this.getClass().getResource("menuStyle.css").toExternalForm());
        App.getStage().setScene(scene);
    }
    @FXML
    public void onSave() {
        Settings settings = new Settings();
        settings.setPort(portField.getText());
        settings.setIp(ipField.getText());
        MenuController.setServerSettings(settings);
        settings.save();
    }
}
