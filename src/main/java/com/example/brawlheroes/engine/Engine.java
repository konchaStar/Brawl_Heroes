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

import java.io.IOException;
import java.util.ArrayList;

public class Engine {
    private long time;
    private Graphics graphics;
    private long deltaTime;
    private Stage stage;
    private Connection connection;
    private boolean isStarted;
    private World world;
    private double findNearestPoint(Point2D point, Rectangle2D rect) {
        Point2D[] points = {
                new Point2D(rect.getMinX(), rect.getMinY()),
                new Point2D(rect.getMaxX(), rect.getMinY()),
                new Point2D(rect.getMinX(), rect.getMaxY()),
                new Point2D(rect.getMaxX(), rect.getMaxY())
        };
        double min = -1;
        for(int i = 0; i < points.length; i++) {
            Point2D vector = points[i].subtract(point).normalize().multiply(deltaTime / 1000.0)
                    .multiply(world.getMainHero().getSpeed());
            double dot = vector.getX() * Controls.getDirection().getX() + vector.getY() * Controls.getDirection().getY();
            if(dot < min) {
                min = dot;
            }
        }
        return min;
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
                Vector2D direction = Controls.getDirection();
                double nearest = findNearestPoint(point, wallRect);
                Point2D position = world.getMainHero().getPosition();
                world.getMainHero().setPosition(position.add(new Point2D(direction.getX() * nearest, direction.getY() * nearest)));
                break;
            }
        }
    }
    private void checkCollision() {
        Hero hero = world.getMainHero();
        Rectangle2D heroRect = new Rectangle2D(hero.getPosition().getX() - Consts.TILE_SIZE / 2, hero.getPosition().getY() - Consts.TILE_SIZE / 2,
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