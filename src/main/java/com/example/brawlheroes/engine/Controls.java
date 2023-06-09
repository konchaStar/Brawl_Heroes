package com.example.brawlheroes.engine;

import com.example.brawlheroes.App;
import com.example.brawlheroes.Consts;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.media.Media;

import java.io.IOException;

public class Controls {
    private static boolean up = false;
    private static boolean down = false;
    private static boolean left = false;
    private static boolean right = false;
    private static double mouseX;
    private static double mouseY;

    public static void setControls(Scene scene, World world, Engine engine) {
        scene.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.A) {
                left = true;
            }
            if(e.getCode() == KeyCode.W) {
                up = true;
            }
            if(e.getCode() == KeyCode.S) {
                down = true;
            }
            if(e.getCode() == KeyCode.D) {
                right = true;
            }
            if(e.getCode() == KeyCode.ESCAPE) {
                if(!engine.isStarted()) {
                    Platform.runLater(() -> {
                        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("menu.fxml"));
                        Scene newScene = null;
                        try {
                            newScene = new Scene(fxmlLoader.load());
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        newScene.getStylesheets().addAll(App.class.getResource("menuStyle.css").toExternalForm());
                        App.getStage().setScene(newScene);
                    });
                }
            }
            if(e.getCode() == KeyCode.E) {
                world.getMainHero().setSelected((world.getMainHero().getSelected() + 1) % world.getMainHero().getWeapons());
            }
            if(e.getCode() == KeyCode.Q) {
                world.getMainHero().setSelected((world.getMainHero().getSelected() - 1 +
                        world.getMainHero().getWeapons()) % world.getMainHero().getWeapons());
            }
            if(e.getCode() == KeyCode.SPACE) {
                if(world.getMainHero().isAlive() && System.currentTimeMillis() - world.getMainHero().getLastStrafe() > Consts.STRAFE_DELAY
                        && !(getDirection().getX() == 0 && getDirection().getY() == 0)) {
                    world.getMainHero().setSpeed(Consts.STRAFE_SPEED);
                    world.getMainHero().setLastStrafe(System.currentTimeMillis());
                } else if(!world.getMainHero().isAlive()) {
                    engine.respawn();
                }
            }
        });
        scene.setOnKeyReleased(e -> {
            if(e.getCode() == KeyCode.A) {
                left = false;
            }
            if(e.getCode() == KeyCode.W) {
                up = false;
            }
            if(e.getCode() == KeyCode.S) {
                down = false;
            }
            if(e.getCode() == KeyCode.D) {
                right = false;
            }
        });
        scene.setOnMouseMoved(e -> {
            mouseX = e.getX();
            mouseY = e.getY();
        });
        scene.setOnMousePressed(e -> {
            if(e.getButton() == MouseButton.PRIMARY && world.getMainHero().isAlive() && engine.isStarted()) {
                world.getMainHero().fire(world);
            }
        });
    }
    public static Vector2D getDirection() {
        Vector2D vector = new Vector2D(0,0);
        if(up) {
            vector.add(new Vector2D(0,-1));
        }
        if(left) {
            vector.add(new Vector2D(-1,0));
        }
        if(right) {
            vector.add(new Vector2D(1,0));
        }
        if(down) {
            vector.add(new Vector2D(0,1));
        }
        return vector;
    }

    public static double getMouseX() {
        return mouseX;
    }

    public static double getMouseY() {
        return mouseY;
    }
}
