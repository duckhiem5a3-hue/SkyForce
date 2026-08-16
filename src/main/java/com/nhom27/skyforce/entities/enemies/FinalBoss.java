package com.nhom27.skyforce.entities.enemies;

import java.util.ArrayList;
import java.util.List;

import com.nhom27.skyforce.entities.base.BossObject;
import com.nhom27.skyforce.main.Main;

public class FinalBoss extends BossObject {
    public static int sizeX = Main.WIDTH * 25 / 100;
    public static int sizeY = Main.WIDTH * 25 / 100;

    private double targetY = 120.0;
    private double speedY = 50.0;
    private boolean hasStopped = false;
    private List<BossTurret> turrets = new ArrayList<>();

    public FinalBoss(double startX, double startY) {
        super("enemy_shooter", startX, startY, 2000, "LEVEL 10 FINAL BOSS - MOTHERSHIP");
        this.collisionDamage = 80;
        this.setPos(startX, startY, 180);

        // Tạo 3 Ụ súng nhỏ (Turrets) gắn lên thân chính
        BossTurret tLeft = new BossTurret(-sizeX * 0.35, sizeY * 0.4);
        BossTurret tCenter = new BossTurret(0, sizeY * 0.6);
        BossTurret tRight = new BossTurret(sizeX * 0.35, sizeY * 0.4);

        turrets.add(tLeft);
        turrets.add(tCenter);
        turrets.add(tRight);
    }

    public List<BossTurret> getTurrets() {
        return turrets;
    }

    public boolean hasAliveTurrets() {
        return turrets.stream().anyMatch(t -> t.isAlive());
    }

    public boolean hasStopped() {
        return hasStopped;
    }

    @Override
    public void takeDamage(int amount) {
        // Nếu còn Ụ súng (Turret), Thân chính được bảo vệ bởi Khiên (Invulnerable)
        if (hasAliveTurrets()) {
            return;
        }
        super.takeDamage(amount);
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
            setPos(x, y, 180);
        }

        // Cập nhật vị trí của các Ụ súng bám theo Thân chính
        double centerX = x + sizeX / 2.0;
        double centerY = y + sizeY / 2.0;
        for (BossTurret turret : turrets) {
            if (turret.isAlive()) {
                turret.updatePosition(centerX, centerY);
                turret.update();
            }
        }
        checkDeath();
    }
}
