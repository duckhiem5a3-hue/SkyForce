package com.nhom27.skyforce.entities.enemies;

import com.nhom27.skyforce.entities.base.BossObject;
import com.nhom27.skyforce.main.Main;

public class MidBoss extends BossObject {
    public static int sizeX = Main.WIDTH * 22 / 100;
    public static int sizeY = Main.WIDTH * 22 / 100;

    private double targetY = 120.0;
    private double speedY = 70.0;
    private double swaySpeedX = 100.0;
    private boolean swayRight = true;
    private boolean hasStopped = false;
    private long lastBulletTime = 0;
    private long phase1FireRate = 3000; // 3 giây nhả 1 vòng 12 viên đạn tím
    private long phase2FireRate = 2500; // Phase 2: Laser + Đạn tỉa

    public MidBoss(double startX, double startY) {
        super("enemy_shooter", startX, startY, 3000, "LEVEL 5 MID-BOSS: PHÁO ĐÀI BAY");
        this.collisionDamage = 50;
        this.setPos(startX, startY, 180);
    }

    public boolean isPhase2() {
        return getHealthPercentage() <= 0.5;
    }

    public boolean hasStopped() {
        return hasStopped;
    }

    public boolean timeToFire(long now) {
        long currentRate = isPhase2() ? phase2FireRate : phase1FireRate;
        if (hasStopped && (now - lastBulletTime >= currentRate)) {
            lastBulletTime = now;
            return true;
        }
        return false;
    }

    @Override
    public void update() {
        if (!hasStopped) {
            if (y < targetY) {
                y += speedY / 60.0;
            } else {
                y = targetY;
                hasStopped = true;
            }
        } else {
            // Ở Phase 2 (HP <= 50%), lắc lư trái phải liên tục
            if (isPhase2()) {
                if (swayRight) {
                    x += swaySpeedX / 60.0;
                    if (x >= Main.WIDTH - sizeX - 30) {
                        swayRight = false;
                    }
                } else {
                    x -= swaySpeedX / 60.0;
                    if (x <= 30) {
                        swayRight = true;
                    }
                }
            }
        }
        setPos(x, y, 180);
        checkDeath();
    }
}
