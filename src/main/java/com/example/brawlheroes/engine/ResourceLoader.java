package com.example.brawlheroes.engine;

import javafx.scene.image.Image;

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
}
