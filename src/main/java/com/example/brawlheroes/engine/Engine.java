package com.example.brawlheroes.engine;

import com.example.brawlheroes.App;
import com.example.brawlheroes.Consts;
import com.example.brawlheroes.Network.*;
import com.example.brawlheroes.engine.weapons.Bullet;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

public class Engine {
    private long time;
    private Graphics graphics;
    private long deltaTime;
    private Stage stage;
    private Connection connection;
    private boolean isStarted;
    private World world;
    private Vector2D[] findNearestPoint(Point2D point, Rectangle2D rect) {
        Point2D[] points = {
                new Point2D(rect.getMinX(), rect.getMinY()),
                new Point2D(rect.getMaxX(), rect.getMinY()),
                new Point2D(rect.getMinX(), rect.getMaxY()),
                new Point2D(rect.getMaxX(), rect.getMaxY())
        };
        Vector2D[] result = new Vector2D[2];
        Vector2D[] vectors = new Vector2D[4];
        for(int i = 0; i < points.length; i++) {
            Point2D vector = points[i].subtract(point);
            vectors[i] = new Vector2D(vector.getX(), vector.getY());
        }
        Arrays.sort(vectors, (v1, v2) -> {
           return (int)Math.signum(v1.length() - v2.length());
        });
        result[0] = vectors[0];
        result[1] = vectors[1];
        return result;
    }
    private void solveCollision(Rectangle2D wallRect, Rectangle2D heroRect) {
        Point2D[] points = {
                new Point2D(heroRect.getMinX(), heroRect.getMinY()),
                new Point2D(heroRect.getMaxX(), heroRect.getMinY()),
                new Point2D(heroRect.getMinX(), heroRect.getMaxY()),
                new Point2D(heroRect.getMaxX(), heroRect.getMaxY())
        };
        for(Point2D point : points) {
            if(wallRect.contains(point)) {
                Vector2D direction = Controls.getDirection().multiply(-1).normalize();
                Vector2D[] nearest = findNearestPoint(point, wallRect);
                double length = Math.sqrt(nearest[0].getX() * nearest[0].getX() + nearest[0].getY() * nearest[0].getY());
                nearest[1].subtract(nearest[0]).normalize();
                nearest[0].normalize();
                double angle1 = Math.acos(Math.abs(nearest[0].getX() * direction.getX() + nearest[0].getY() * direction.getY()));
                double angle2 = Math.acos(Math.abs(nearest[0].getX() * nearest[1].getX() + nearest[0].getY() * nearest[1].getY()));
                double angle3 = Math.PI - angle1 - angle2;
                direction = direction.multiply(Math.sin(angle2) * length / Math.sin(angle3));
                Point2D position = world.getMainHero().getPosition();
                position = new Point2D(position.getX() + direction.getX(), position.getY() + direction.getY());
                world.getMainHero().setPosition(position);
                break;
            }
        }
    }

    private void checkCollision() {
        Hero hero = world.getMainHero();
        Rectangle2D heroRect = new Rectangle2D(hero.getPosition().getX() - Consts.TILE_SIZE / 2 + 1, hero.getPosition().getY() - Consts.TILE_SIZE / 2 + 1,
                hero.getHitbox().getWidth(), hero.getHitbox().getHeight());
        for(Entity wall : world.getObjects()) {
            Rectangle2D wallRect = new Rectangle2D(wall.getPosition().getX(), wall.getPosition().getY(),
                    Consts.TILE_SIZE, Consts.TILE_SIZE);
            if(wallRect.intersects(heroRect)) {
                solveCollision(wallRect, heroRect);
            }
        }
    }
    private void moveBullet() {
        for(Bullet bullet : world.getBullets()) {
            bullet.move(deltaTime / 1000.0);
        }
    }
    private void checkBulletsCollision() {
        ArrayList<Bullet> delete = new ArrayList<>();
        Rectangle2D heroRect = new Rectangle2D(world.getMainHero().getPosition().getX(), world.getMainHero().getPosition().getY(),
                Consts.TILE_SIZE, Consts.TILE_SIZE);
        Rectangle2D enemyRect = new Rectangle2D(world.getEnemy().getPosition().getX(), world.getEnemy().getPosition().getY(),
                Consts.TILE_SIZE, Consts.TILE_SIZE);
        for(Bullet bullet : world.getBullets()) {
            Rectangle2D bulletRect = new Rectangle2D(bullet.getPosition().getX(), bullet.getPosition().getY(),
                    bullet.getHitbox().getWidth(), bullet.getHitbox().getHeight());
            if(heroRect.intersects(bulletRect) && bullet.getOwner() != world.getMainHero()) {
                world.getMainHero().damage(bullet.getDamage());
                delete.add(bullet);
            } else if (enemyRect.intersects(bulletRect) && bullet.getOwner() != world.getEnemy()) {
                delete.add(bullet);
            } else {
                for (Entity wall : world.getObjects()) {
                    Rectangle2D wallRect = new Rectangle2D(wall.getPosition().getX(), wall.getPosition().getY(),
                            wall.getHitbox().getWidth(), wall.getHitbox().getHeight());
                    if (wallRect.intersects(bulletRect)) {
                        delete.add(bullet);
                        break;
                    }
                }
            }
        }
        for(Bullet bullet : delete) {
            world.deleteBullet(bullet);
        }
    }
    public void onUpdate() {
        world.getMainHero().move(deltaTime / 1000.0, Controls.getDirection());
        Vector2D direction = new Vector2D(Controls.getMouseX() - Consts.WINDOW_WIDTH / 2 + world.getMainHero().getPosition().getX(),
                Controls.getMouseY() - Consts.WINDOW_HEIGHT / 2 + world.getMainHero().getPosition().getY());
        direction.subtract(world.getMainHero().getPosition());
        world.getMainHero().setDirection(direction);
        checkCollision();
        checkBulletsCollision();
        moveBullet();
        try {
            HeroInfo info = new HeroInfo(world.getMainHero().getPosition().getX(), world.getMainHero().getPosition().getY(),
                    world.getMainHero().getDirection().getX(), world.getMainHero().getDirection().getY());
            connection.send(new Message(info, Message.MessageType.MOVE));
        } catch (IOException e) {
            disconnect();
        }
    }
    public void onStart(StartPosition position) {
        world = new World(connection);
        world.getMainHero().setPosition(new Point2D(position.getPositionX(), position.getPositionY()));
    }
    private void handleMessages() {
        try {
            while (isStarted) {
                Message message = connection.receive();
                switch (message.getType()) {
                    case MOVE -> {
                        HeroInfo info = (HeroInfo) message.getData();
                        Hero enemy = world.getEnemy();
                        enemy.setPosition(new Point2D(info.getPositionX(), info.getPositionY()));
                        enemy.setDirection(new Vector2D(info.getDirectionX(), info.getDirectionY()));
                        world.setEnemy(enemy);
                    }
                    case FIRE -> {
                        BulletInfo info = (BulletInfo) message.getData();
                        Bullet bullet = new Bullet(world.getEnemy(), new Point2D(info.getPositionX(), info.getPositionY()),
                                new Rectangle2D(0,0,15,10), world.getLoader().getBulletImage(),
                                new Vector2D(info.getDirectionX(), info.getDirectionY()), info.getDamage(), info.getSpeed());
                        world.addBullet(bullet, false);
                    }
                }
            }
        } catch (IOException e) {
            disconnect();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public Engine(Connection connection, StartPosition position) {
        this.connection = connection;
        onStart(position);
    }
    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    public void setScene(Stage stage) {
        stage.setScene(graphics.getScene());
        this.stage = App.getStage();
    }
    public boolean isStarted() {
        return isStarted;
    }

    public World getWorld() {
        return world;
    }

    public void start() {
        graphics = new Graphics(world);
        Level.loadLevel(world, "levels/lvl1.lvl");
        setScene(App.getStage());
        Controls.setControls(graphics.getScene(), world, this);
        isStarted = true;
        new Thread(this::handleMessages).start();
        new Thread(()-> {
            while (isStarted) {
                time = System.currentTimeMillis();
                onUpdate();
                graphics.draw();
                try {
                    Thread.currentThread().sleep(25);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                deltaTime = System.currentTimeMillis() - time;
            }
        }).start();
    }
    private void disconnect() {
        isStarted = false;
        graphics.disconnect();
        Platform.runLater(() -> {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("menu.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            scene.getStylesheets().addAll(App.class.getResource("menuStyle.css").toExternalForm());
            stage.setScene(scene);
        });
    }
}