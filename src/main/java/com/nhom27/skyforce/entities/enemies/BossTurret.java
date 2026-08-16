package com.nhom27.skyforce.entities.enemies;

import com.nhom27.skyforce.entities.base.EnemyObject;
import com.nhom27.skyforce.entities.player.Player;
import com.nhom27.skyforce.main.Main;

public class BossTurret extends EnemyObject {
    public static int sizeX = Main.WIDTH * 6 / 100;
    public static int sizeY = Main.WIDTH * 6 / 100;

    private double relX;
    private double relY;
    private long lastFireTime = 0;
    private long fireRate = 2200; // ms
    private double aimedDirX = 0;
    private double aimedDirY = 1;

    public BossTurret(double relX, double relY) {
        super("enemy_sniper_green", 0, 0);
        this.relX = relX;
        this.relY = relY;
        this.health = 200;
        this.collisionDamage = 20;
    }

    public void updatePosition(double bossCenterX, double bossCenterY) {
        this.x = bossCenterX + relX - sizeX / 2.0;
        this.y = bossCenterY + relY - sizeY / 2.0;
        setPos(this.x, this.y, 180);
    }

    public boolean timeToFire(long now, Player player) {
        if (now - lastFireTime >= fireRate) {
            lastFireTime = now;
            if (player != null && player.isAlive()) {
                double dx = player.getX() - x;
                double dy = player.getY() - y;
                double dist = Math.hypot(dx, dy);
                if (dist > 0) {
                    aimedDirX = dx / dist;
                    aimedDirY = dy / dist;
                }
            }
            return true;
        }
        return false;
    }

    public double getAimedDirX() {
        return aimedDirX;
    }

    public double getAimedDirY() {
        return aimedDirY;
    }

    @Override
    public void update() {
        checkDeath();
    }
}
