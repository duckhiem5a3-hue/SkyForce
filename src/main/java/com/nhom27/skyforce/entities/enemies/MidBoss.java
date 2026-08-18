package com.nhom27.skyforce.entities.enemies;

import com.nhom27.skyforce.entities.base.BossObject;
import com.nhom27.skyforce.main.Main;

public class MidBoss extends BossObject {
    private double targetY;
    private double speedY;
    private double swaySpeedX;
    private boolean swayRight;
    private boolean hasStopped;
    private long lastBulletTime;
    private long phase1FireRate; // 3 giây nhả 1 vòng 12 viên đạn tím
    private long phase2FireRate; // Phase 2: Laser + Đạn tỉa

    public MidBoss() {
        super("boss_mid_red", 3000, "LEVEL 5 MID-BOSS: PHÁO ĐÀI BAY");
        this.collisionDamage = 500;
        this.targetY = 120.0;
        this.speedY = 70.0;
        this.swaySpeedX = 100.0;
        this.swayRight = true;
        this.hasStopped = false;
        this.lastBulletTime = 0;
        this.phase1FireRate = 3000;
        this.phase2FireRate = 2500;
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
        setPos(x, y);
        checkDeath();
    }
}
