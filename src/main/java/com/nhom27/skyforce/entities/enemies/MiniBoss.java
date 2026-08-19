package com.nhom27.skyforce.entities.enemies;

import com.nhom27.skyforce.entities.base.BossObject;
import com.nhom27.skyforce.entities.player.Player;
import com.nhom27.skyforce.entities.weapons.EnemyBullet;
import com.nhom27.skyforce.managers.GameManager;

public class MiniBoss extends BossObject {
    private double targetY;
    private double speedY;
    private boolean hasStopped;
    private long lastBulletTime;
    private long fireRate;

    public MiniBoss() {
        super("boss_mini_red", 600, "LEVEL 1 MINI-BOSS");
        this.collisionDamage = 500;
        this.targetY = 100.0;
        this.speedY = 80.0;
        this.hasStopped = false;
        this.lastBulletTime = 0;
        this.fireRate = 2000;
    }

    public boolean hasStopped() {
        return hasStopped;
    }

    public boolean timeToFire(long now) {
        if (hasStopped && (now - lastBulletTime >= fireRate)) {
            lastBulletTime = now;
            return true;
        }
        return false;
    }

    @Override
    public void attack(GameManager gm, long now, Player player) {
        if (gm != null && timeToFire(now)) {
            double startX = getX() + getSizeX() / 2.0 - 5;
            double startY = getY() + getSizeY();

            double totalSpeed = 120.0;
            double radLeft = Math.toRadians(-20);
            double radRight = Math.toRadians(20);

            EnemyBullet b1 = new EnemyBullet(startX, startY, 0, totalSpeed, 15, "bullet_boss_mini_red");
            EnemyBullet b2 = new EnemyBullet(startX, startY, totalSpeed * Math.sin(radLeft),
                    totalSpeed * Math.cos(radLeft), 15, "bullet_boss_mini_red");
            EnemyBullet b3 = new EnemyBullet(startX, startY, totalSpeed * Math.sin(radRight),
                    totalSpeed * Math.cos(radRight), 15, "bullet_boss_mini_red");

            EnemyBullet[] bullets = { b1, b2, b3 };
            for (EnemyBullet b : bullets) {
                gm.spawnEnemyBullet(b);
            }
        }
    }

    @Override
    public void update() {
        if (!hasStopped) {
            if (y < targetY) {
                y += speedY / 60.0;
                setPos(x, y);
            } else {
                y = targetY;
                hasStopped = true;
                setPos(x, y);
            }
        }
        checkDeath();
    }
}
