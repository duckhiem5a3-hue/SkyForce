package com.nhom27.skyforce.entities.obstacles;

import com.nhom27.skyforce.entities.base.EnemyObject;
import com.nhom27.skyforce.main.Main;

public class Asteroid extends EnemyObject {
    private double speedY = 60.0;
    private double rotationAngle = 0;

    public Asteroid(double startX, double startY) {
        super("obstacle_asteroid_large", startX, startY);
        this.health = 500;
        this.collisionDamage = 100; // 1-hit kill! Chạm là chết.
        this.setPos(startX, startY, 0);
    }

    public Asteroid() {
        super("obstacle_asteroid_large");
        this.health = 500;
        this.collisionDamage = 100; // 1-hit kill! Chạm là chết.
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
