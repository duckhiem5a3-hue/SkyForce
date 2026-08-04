package com.nhom27.skyforce.entities.enemies;

import com.nhom27.skyforce.entities.base.EnemyObject;
import com.nhom27.skyforce.main.Main;

public class SineOrbitEnemy extends EnemyObject {
    public static int sizeX = Main.WIDTH / 12;
    public static int sizeY = Main.WIDTH / 12;
    private double baseY;

    public SineOrbitEnemy(double startY) {
        super("enemy_sine_orbit", 0, startY);
        this.baseY = startY;
        this.health = 70;
    }

    public SineOrbitEnemy() {
        this(150);
    }

    @Override
    public void update() {
        double timeSinceCreation = this.timeLived % 300;
        double currentX = (Main.WIDTH + 100) * (timeSinceCreation / 300.0) - 50;
        double currentY = baseY + 120 * Math.sin(timeSinceCreation * Math.PI / 150);

        double deltaX = (Main.WIDTH + 100) / 300.0;
        double deltaY = 120 * (Math.PI / 150) * Math.cos(timeSinceCreation * Math.PI / 150);

        double rotateAngle = 90 + Math.toDegrees(Math.atan2(deltaY, deltaX));
        this.setPos(currentX, currentY, rotateAngle);

        this.timeLived += 1;
        baseY += 0.5; // slowly drift down
        if (this.timeLived > 600 || currentY > Main.HEIGHT + 100) {
            this.isAlive = false;
        }
        checkDeath();
    }
}
