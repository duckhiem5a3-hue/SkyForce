package com.nhom27.skyforce.entities.enemies;

import com.nhom27.skyforce.entities.base.EnemyObject;
import com.nhom27.skyforce.main.Main;

public class EliteEnemy extends EnemyObject {
    public static int sizeX = Main.WIDTH * 8 / 100;
    public static int sizeY = Main.WIDTH * 8 / 100;
    private double speedY = 180.0; // Vừa
    private long lastFireTime = 0;
    private long fireRate = 2200; // ms

    public EliteEnemy(double startX, double startY) {
        super("enemy_normal_red", startX, startY);
        this.health = 80;
        this.collisionDamage = 30;
        this.setPos(startX, startY, 180);
    }

    public EliteEnemy(double startX, double startY, double speedY) {
        this(startX, startY);
        this.speedY = speedY;
    }

    public double getSpeedY() {
        return speedY;
    }

    public void setSpeedY(double speedY) {
        this.speedY = speedY;
    }

    public boolean timeToFire(long now) {
        if (now - lastFireTime >= fireRate) {
            lastFireTime = now;
            return true;
        }
        return false;
    }

    @Override
    public void update() {
        y += speedY / 60.0;
        double angle = (speedY < 0) ? 0 : 180;
        setPos(x, y, angle);
        if (y > Main.HEIGHT + 100 || y < -100) {
            isAlive = false;
        }
        checkDeath();
    }
}
