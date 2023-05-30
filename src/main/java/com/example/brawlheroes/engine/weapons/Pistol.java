package com.example.brawlheroes.engine.weapons;

import com.example.brawlheroes.engine.World;
import javafx.geometry.Rectangle2D;
import javafx.scene.media.MediaPlayer;

public class Pistol extends Weapon {
    public Pistol() {
        delay = 500;
        lastShot = 0;
        ammo = 20;
    }
    @Override
    public void fire(World world) {
        if(System.currentTimeMillis() - lastShot > delay && ammo > 0) {
            new MediaPlayer(world.getLoader().getPistol()).play();
            ammo--;
            world.addBullet(new Bullet(world.getMainHero(), world.getMainHero().getPosition(), new Rectangle2D(0, 0, 15, 10),
                    world.getLoader().getBulletImage(), world.getMainHero().getDirection(),
                    10, 600), true);
            lastShot = System.currentTimeMillis();
        }
    }
}
