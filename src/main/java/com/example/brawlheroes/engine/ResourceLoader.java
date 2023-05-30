package com.example.brawlheroes.engine;

import com.example.brawlheroes.App;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ResourceLoader {
    private Image heroImage;
    private Image wallImage;
    private Image bulletImage;
    private Image crosshairImage;
    private Image floorImage;
    private Image bloodImage;
    private Image shotgunImage;
    private Image rifleImage;
    private Image pistolImage;
    private Media shotgun;
    private Media pistol;
    private Media rifle;
    private Media start;
    private Media victory;
    private Media defeat;
    public ResourceLoader() {
        try {
            heroImage = new Image(new FileInputStream("sprites/hero.png"));
            bulletImage = new Image(new FileInputStream("sprites/bullet.png"));
            crosshairImage = new Image(new FileInputStream("sprites/crosshair.png"));
            wallImage = new Image(new FileInputStream("sprites/wall.png"));
            floorImage = new Image(new FileInputStream("sprites/floor.jpg"));
            bloodImage = new Image(new FileInputStream("sprites/blood.png"));
            shotgunImage = new Image(new FileInputStream("sprites/shotgun.png"));
            rifleImage = new Image(new FileInputStream("sprites/rifle.png"));
            pistolImage = new Image(new FileInputStream("sprites/pistol.png"));
            shotgun = new Media(App.class.getResource("sounds/shotgun_sound.mp3").toExternalForm());
            pistol = new Media(App.class.getResource("sounds/pistol_sound.mp3").toExternalForm());
            rifle = new Media(App.class.getResource("sounds/rifle_sound.mp3").toExternalForm());
            start = new Media(App.class.getResource("sounds/start.mp3").toExternalForm());
            victory = new Media(App.class.getResource("sounds/win.mp3").toExternalForm());
            defeat = new Media(App.class.getResource("sounds/defeat.mp3").toExternalForm());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Image getHeroImage() {
        return heroImage;
    }

    public Image getWallImage() {
        return wallImage;
    }

    public Image getBulletImage() {
        return bulletImage;
    }

    public Image getCrosshairImage() {
        return crosshairImage;
    }

    public Image getFloorImage() {
        return floorImage;
    }

    public Image getBloodImage() {
        return bloodImage;
    }
    public Image getShotgunImage() {
        return shotgunImage;
    }

    public Image getRifleImage() {
        return rifleImage;
    }

    public Media getShotgun() {
        return shotgun;
    }

    public Media getPistol() {
        return pistol;
    }

    public Media getRifle() {
        return rifle;
    }

    public Media getStart() {
        return start;
    }

    public Image getPistolImage() {
        return pistolImage;
    }

    public Media getVictory() {
        return victory;
    }

    public Media getDefeat() {
        return defeat;
    }
}
