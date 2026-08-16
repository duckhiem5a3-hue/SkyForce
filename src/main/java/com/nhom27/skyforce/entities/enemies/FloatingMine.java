package com.nhom27.skyforce.entities.enemies;

import java.util.ArrayList;
import java.util.List;

import com.nhom27.skyforce.entities.base.EnemyObject;
import com.nhom27.skyforce.entities.weapons.EnemyBullet;
import com.nhom27.skyforce.main.Main;

public class FloatingMine extends EnemyObject {
    public static int sizeX = Main.WIDTH * 8 / 100;
    public static int sizeY = Main.WIDTH * 8 / 100;
    private double speedY = 40.0; // Trôi nổi rất chậm
    private double pulsePhase = 0;
    private boolean exploded = false;

    public FloatingMine(double startX, double startY) {
        super("obstacle_mine_red", startX, startY);
        this.health = 50;
        this.collisionDamage = 40;
        this.setPos(startX, startY, 0);
    }

    public List<EnemyBullet> explodeAndSpawnBullets() {
        List<EnemyBullet> ringBullets = new ArrayList<>();
        if (exploded) return ringBullets;
        exploded = true;

        double centerX = x + sizeX / 2.0;
        double centerY = y + sizeY / 2.0;
        double speed = 180.0;

        for (int i = 0; i < 8; i++) {
            double angleDeg = i * 45.0;
            double rad = Math.toRadians(angleDeg);
            double vx = speed * Math.sin(rad);
            double vy = speed * Math.cos(rad);
            EnemyBullet bullet = new EnemyBullet(centerX, centerY, vx, vy, 15, "bullet_enemy_round_purple");
            ringBullets.add(bullet);
        }
        return ringBullets;
    }

    @Override
    public void update() {
        y += speedY / 60.0;
        pulsePhase += 0.08;
        if (view != null) {
            double scale = 1.0 + 0.08 * Math.sin(pulsePhase);
            view.setScaleX(scale);
            view.setScaleY(scale);
        }
        setPos(x, y, 0);
        if (y > Main.HEIGHT + 80) {
            isAlive = false;
        }
        checkDeath();
    }
}
