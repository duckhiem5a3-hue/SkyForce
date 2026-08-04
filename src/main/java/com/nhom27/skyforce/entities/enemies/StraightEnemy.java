package com.nhom27.skyforce.entities.enemies;

import java.util.Random;

import com.nhom27.skyforce.entities.base.EnemyObject;
import com.nhom27.skyforce.main.Main;
import com.nhom27.skyforce.utils.AssetManager;

public class StraightEnemy extends EnemyObject {
    public static int sizeX = Main.WIDTH * 7 / 100;
    public static int sizeY = Main.WIDTH * 7 / 100;
    private double speedY;
    private double randomStopY;

    public StraightEnemy(double startX, double startY) {
        super("enemy_straight", startX, startY);
        this.speedY = 100.0;
        this.health = 50;
        this.hitbox = AssetManager.getSpriteInfo("enemy_straight").getHitbox();
        Random random = new Random();
        this.randomStopY = random.nextDouble() * Main.HEIGHT / 2;
        this.setPos(startX, startY);
    }

    public StraightEnemy(double startX, double startY, double speedY) {
        this(startX, startY);
        this.speedY = speedY;
    }

    @Override
    public void update() {
        if (y <= randomStopY) {
            y += speedY / 60.0;
            setPos(x, y, 180);
            checkDeath();
        }
    }
}
