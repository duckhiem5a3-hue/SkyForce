package com.nhom27.skyforce.entities.enemies;

import com.nhom27.skyforce.audio.AudioManager;
import com.nhom27.skyforce.entities.base.BossObject;
import com.nhom27.skyforce.entities.player.Player;
import com.nhom27.skyforce.entities.weapons.EnemyBullet;
import com.nhom27.skyforce.main.Main;
import com.nhom27.skyforce.managers.GameManager;

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
    public void attack(GameManager gm, long now, Player player) {
        if (gm == null || !isAlive())
            return;

        if (timeToFire(now)) {
            double bossCenterX = getX() + getSizeX() / 2.0;
            double bossCenterY = getY() + getSizeY() / 2.0;

            if (!isPhase2()) {
                // Phase 1: Mưa Đạn Tròn 360° (12 viên đạn tròn tím tỏa ra)
                double totalSpeed = 120.0;
                for (int i = 0; i < 12; i++) {
                    double angleDeg = i * (360.0 / 12.0);
                    double rad = Math.toRadians(angleDeg);
                    double vx = totalSpeed * Math.sin(rad);
                    double vy = totalSpeed * Math.cos(rad);
                    EnemyBullet b = new EnemyBullet(bossCenterX, bossCenterY, vx, vy, 15,
                            "bullet_enemy_round_purple");
                    gm.spawnEnemyBullet(b);
                }
            } else {
                // Phase 2: Tử Quang (2 Tia Laser 50 Dmg + 2 Đạn Tỉa Kim Cương 25 Dmg nhắm player)
                if (gm.getVFXManager() != null) {
                    gm.getVFXManager().spawnScreenEffect("damaged");
                }
                AudioManager.getInstance().playSound("sfx_zap");

                // 2 Tia Laser chéo
                EnemyBullet laser1 = new EnemyBullet(bossCenterX - 40, bossCenterY, -50.0, 380.0, 50,
                        "bullet_enemy_laser");
                EnemyBullet laser2 = new EnemyBullet(bossCenterX + 40, bossCenterY, 50.0, 380.0, 50,
                        "bullet_enemy_laser");

                // 2 Đạn Tỉa nhắm player
                double targetedVx = 0;
                double targetedVy = 350.0;
                Player p = (player != null) ? player : gm.getPlayer();
                if (p != null && p.isAlive()) {
                    double dx = p.getX() - bossCenterX;
                    double dy = p.getY() - bossCenterY;
                    double dist = Math.hypot(dx, dy);
                    if (dist > 0) {
                        targetedVx = (dx / dist) * 350.0;
                        targetedVy = (dy / dist) * 350.0;
                    }
                }
                EnemyBullet diamond1 = new EnemyBullet(bossCenterX - 20, bossCenterY, targetedVx, targetedVy, 25,
                        "bullet_enemy_diamond_yellow");
                EnemyBullet diamond2 = new EnemyBullet(bossCenterX + 20, bossCenterY, targetedVx, targetedVy, 25,
                        "bullet_enemy_diamond_yellow");

                EnemyBullet[] phase2Bullets = { laser1, laser2, diamond1, diamond2 };
                for (EnemyBullet b : phase2Bullets) {
                    gm.spawnEnemyBullet(b);
                }
            }
        }
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
