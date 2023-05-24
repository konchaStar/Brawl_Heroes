package com.example.brawlheroes.engine.weapons;

import com.example.brawlheroes.engine.World;
import javafx.geometry.Rectangle2D;

public class Rifle extends Weapon {
    public Rifle() {
        delay = 200;
        lastShot = 0;
        ammo = 30;
    }

    @Override
    public void fire(World world) {
        if(System.currentTimeMillis() - lastShot > delay && ammo > 0) {
            ammo--;
            world.addBullet(new Bullet(world.getMainHero(), world.getMainHero().getPosition(), new Rectangle2D(0, 0, 15, 10),
                    world.getLoader().getBulletImage(), world.getMainHero().getDirection(),
                    10, 600), true);
            lastShot = System.currentTimeMillis();
        }
    }
}
