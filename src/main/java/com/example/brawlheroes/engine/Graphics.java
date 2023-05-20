package com.example.brawlheroes.engine;

import com.example.brawlheroes.Consts;
import com.example.brawlheroes.engine.weapons.Bullet;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class Graphics {
    private Scene scene;
    private Canvas canvas;
    private World world;
    public Graphics(World world) {
        canvas = new Canvas(Consts.WINDOW_WIDTH, Consts.WINDOW_HEIGHT);
        VBox box = new VBox(canvas);
        scene = new Scene(box);
        scene.setCursor(Cursor.NONE);
        this.world = world;
    }

    public Scene getScene() {
        return scene;
    }

    public void draw() {
        GraphicsContext context = canvas.getGraphicsContext2D();
        context.clearRect(0, 0, Consts.WINDOW_WIDTH, Consts.WINDOW_HEIGHT);
        context.setFill(Color.BLACK);
        context.fillRect(0,0, Consts.WINDOW_WIDTH, Consts.WINDOW_HEIGHT);
        context.save();
        context.translate(Consts.WINDOW_WIDTH / 2 - world.getMainHero().getPosition().getX(),
                Consts.WINDOW_HEIGHT / 2 - world.getMainHero().getPosition().getY());
        drawFloor(context);
        drawWalls(context);
        drawBullets(context);
        drawEnemy(context);
        drawHero(context);
        context.restore();
        drawCrosshair(context);
    }
    private void drawFloor(GraphicsContext context) {
        context.drawImage(world.getFloor(), 0, 0, world.getMapSize().getX(), world.getMapSize().getY());
    }
    private void drawWalls(GraphicsContext context) {
        for(Entity wall : world.getObjects()) {
            context.drawImage(wall.getImage(), wall.getPosition().getX(), wall.getPosition().getY(),
                    Consts.TILE_SIZE, Consts.TILE_SIZE);
        }
    }
    private void drawHero(GraphicsContext context) {
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
    private void drawEnemy(GraphicsContext context) {
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
    private void drawBullets(GraphicsContext context) {
        for(Bullet bullet : world.getBullets()) {
            context.save();
            context.translate(bullet.getPosition().getX(), bullet.getPosition().getY());
            double dot = bullet.getDirection().dot(new Vector2D(1, 0));
            double angle = Math.acos(dot) * 180 / Math.PI;
            context.rotate(bullet.getDirection().getY() > 0 ? angle : -angle);
            context.drawImage(bullet.getImage(), -bullet.getHitbox().getWidth() / 2.0, -bullet.getHitbox().getHeight() / 2.0,
                    bullet.getHitbox().getWidth(), bullet.getHitbox().getHeight());
            context.restore();
        }
    }
    private void drawCrosshair(GraphicsContext context) {
        context.drawImage(world.getLoader().getCrosshairImage(), Controls.getMouseX() - 10, Controls.getMouseY() - 10, 20, 20);
    }
}
