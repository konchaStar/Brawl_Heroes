package com.example.brawlheroes.engine;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;

public class Entity {
    protected Point2D position;

    protected Rectangle2D hitbox;
    protected Image image;
    public Entity() {

    }
    public Entity(Point2D position, Rectangle2D hitbox, Image image) {
        this.position = position;
        this.hitbox = hitbox;
        this.image = image;
    }

    public Point2D getPosition() {
        return position;
    }
    public Rectangle2D getHitbox() {
        return hitbox;
    }

    public void setHitbox(Rectangle2D hitbox) {
        this.hitbox = hitbox;
    }

    public void setPosition(Point2D position) {
        this.position = position;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
