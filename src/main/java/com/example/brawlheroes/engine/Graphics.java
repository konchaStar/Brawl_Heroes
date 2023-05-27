package com.example.brawlheroes.engine;

import com.example.brawlheroes.Consts;
import com.example.brawlheroes.engine.weapons.*;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ConcurrentModificationException;

public class Graphics {
    private Scene scene;
    private Canvas canvas;
    private World world;
    private GraphicsContext context;
    public Graphics(World world) {
        canvas = new Canvas(Consts.WINDOW_WIDTH, Consts.WINDOW_HEIGHT);
        VBox box = new VBox(canvas);
        scene = new Scene(box);
        scene.setCursor(Cursor.NONE);
        this.world = world;
        context = canvas.getGraphicsContext2D();
    }

    public Scene getScene() {
        return scene;
    }

    public void draw() {
        context.clearRect(0, 0, Consts.WINDOW_WIDTH, Consts.WINDOW_HEIGHT);
        context.setFill(Color.BLACK);
        context.fillRect(0, 0, Consts.WINDOW_WIDTH, Consts.WINDOW_HEIGHT);
        context.save();
        context.translate(Consts.WINDOW_WIDTH / 2 - world.getMainHero().getPosition().getX(),
                Consts.WINDOW_HEIGHT / 2 - world.getMainHero().getPosition().getY());
        drawFloor();
        drawWalls();
        drawItems();
        drawBullets();
        if (world.getEnemy().isAlive()) {
            drawEnemy();
        }
        if (world.getMainHero().isAlive()) {
            drawHero();
        }
        context.restore();
        drawCrosshair();
        gui();
    }
    private void drawFloor() {
        context.drawImage(world.getFloor(), 0, 0, world.getMapSize().getX(), world.getMapSize().getY());
    }
    private void drawWalls() {
        for(Entity wall : world.getObjects()) {
            context.drawImage(wall.getImage(), wall.getPosition().getX(), wall.getPosition().getY(),
                    Consts.TILE_SIZE, Consts.TILE_SIZE);
        }
    }
    private void drawHero() {
        Hero hero = world.getMainHero();
        context.save();
        context.translate(hero.getPosition().getX(), hero.getPosition().getY());
        double dot = hero.getDirection().dot(new Vector2D(1, 0));
        double angle = Math.acos(dot) * 180 / Math.PI;
        context.rotate(hero.getDirection().getY() > 0 ? angle : -angle);
        context.drawImage(hero.getImage(), -hero.getHitbox().getWidth() / 2.0, -hero.getHitbox().getHeight() / 2.0,
                hero.getHitbox().getWidth(), hero.getHitbox().getHeight());
        context.restore();
    }
    private void drawEnemy() {
        Hero enemy = world.getEnemy();
        context.save();
        context.translate(enemy.getPosition().getX(), enemy.getPosition().getY());
        double dot = enemy.getDirection().dot(new Vector2D(1, 0));
        double angle = Math.acos(dot) * 180 / Math.PI;
        context.rotate(enemy.getDirection().getY() > 0 ? angle : -angle);
        context.drawImage(enemy.getImage(), -enemy.getHitbox().getWidth() / 2.0, -enemy.getHitbox().getHeight() / 2.0,
                enemy.getHitbox().getWidth(), enemy.getHitbox().getHeight());
        context.restore();
    }
    private void drawBullets() {
        try {
            for (Bullet bullet : world.getBullets()) {
                context.save();
                context.translate(bullet.getPosition().getX(), bullet.getPosition().getY());
                double dot = bullet.getDirection().dot(new Vector2D(1, 0));
                double angle = Math.acos(dot) * 180 / Math.PI;
                context.rotate(bullet.getDirection().getY() > 0 ? angle : -angle);
                context.drawImage(bullet.getImage(), -bullet.getHitbox().getWidth() / 2.0, -bullet.getHitbox().getHeight() / 2.0,
                        bullet.getHitbox().getWidth(), bullet.getHitbox().getHeight());
                context.restore();
            }
        } catch (ConcurrentModificationException e) {

        }
    }
    private void drawCrosshair() {
        context.drawImage(world.getLoader().getCrosshairImage(), Controls.getMouseX() - 10, Controls.getMouseY() - 10, 20, 20);
    }
    private void drawItems() {
        for(Item item : world.getItems()) {
            context.save();
            context.translate(item.getPosition().getX(), item.getPosition().getY());
            context.drawImage(item.getImage(), -item.getHitbox().getWidth() / 2.0, -item.getHitbox().getHeight() / 2.0,
                    item.getHitbox().getWidth(), item.getHitbox().getHeight());
            context.restore();
        }
    }
    private void gui() {
        context.setFill(Color.RED);
        context.setFont(new Font("fonts/pixel.ttf", 30));
        context.fillText("HP " + world.getMainHero().getHealthPoints(), 10,Consts.WINDOW_HEIGHT - 40);
        context.setFill(Color.YELLOW);
        try {
            Weapon weapon = world.getMainHero().getWeaponList().get(world.getMainHero().getSelected());
            if (weapon.getClass().equals(Shotgun.class)) {
                context.drawImage(world.getLoader().getShotgunImage(), 1000, 590, 160, 40);
            } else if (weapon.getClass().equals(Rifle.class)) {
                context.drawImage(world.getLoader().getRifleImage(), 1000, 590, 160, 40);
            } else if (weapon.getClass().equals(Pistol.class)) {
                context.drawImage(world.getLoader().getPistolImage(), 1000, 590, 63, 40);
            }
            context.fillText(String.valueOf(world.getMainHero().getWeaponList().get(world.getMainHero().getSelected()).getAmmo()),
                    1000, 660);
        } catch (IndexOutOfBoundsException e) {

        }
        context.setFill(Color.WHITE);
        context.fillText(world.getMainHero().getKills() + " " + world.getMainHero().getDeath(), 10, 30);
    }
    public void respawn() {
        context.setFill(Color.RED);
        context.setFont(new Font("fonts/pixel.ttf", 60));
        context.fillText("Dead", 550, 300);
        context.setFill(Color.WHITE);
        context.fillText("Press space to respawn", 370, 360);
    }
    public void victory() {
        context.setFill(Color.BLUE);
        context.setFont(new Font("fonts/pixel.ttf", 60));
        context.fillText("VICTORY", 530, 300);
        context.fillText("Press esc to exit to menu", 370, 360);
    }
    public void defeat() {
        context.setFill(Color.RED);
        context.setFont(new Font("fonts/pixel.ttf", 60));
        context.fillText("DEFEAT", 540, 300);
        context.fillText("Press esc to exit to menu", 370, 360);
    }
    public void disconnect() {
        context.setFill(Color.RED);
        context.setFont(new Font("fonts/pixel.ttf", 60));
        context.fillText("Enemy disconnected", 450, 300);
        context.fillText("Press esc to exit to menu", 370, 360);
    }
}
