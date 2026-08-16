package com.nhom27.skyforce.entities.enemies;

import com.nhom27.skyforce.entities.base.EnemyObject;
import com.nhom27.skyforce.main.Main;

public class MeteorEnemy extends EnemyObject {
    public static int sizeX = Main.WIDTH * 14 / 100;
    public static int sizeY = Main.WIDTH * 14 / 100;
    private double speedY = 60.0;
    private double rotationAngle = 0;

    public MeteorEnemy(double startX, double startY) {
        super("meteor_big", startX, startY);
        this.health = 500;
        this.collisionDamage = 100; // 1-hit kill! Chạm là chết.
        this.setPos(startX, startY, 0);
    }

    @Override
    public void update() {
        y += speedY / 60.0;
        rotationAngle = (rotationAngle + 0.5) % 360;
        setPos(x, y, rotationAngle);
        if (y > Main.HEIGHT + 100) {
            isAlive = false;
        }
        checkDeath();
    }
}
