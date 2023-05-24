package com.example.brawlheroes.engine;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;

public class Item extends Entity {
    private int spawnerId;

    public Item(Point2D position, Rectangle2D hitbox, Image image, int spawnerId) {
        super(position, hitbox, image);
        this.spawnerId = spawnerId;
    }

    public int getSpawnerId() {
        return spawnerId;
    }
}
