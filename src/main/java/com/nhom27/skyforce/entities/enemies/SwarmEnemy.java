package com.nhom27.skyforce.entities.enemies;

import com.nhom27.skyforce.entities.base.EnemyObject;
import com.nhom27.skyforce.main.Main;

public class SwarmEnemy extends EnemyObject {
    public static int sizeX = Main.WIDTH * 5 / 100;
    public static int sizeY = Main.WIDTH * 5 / 100;

    public enum TrajectoryType {
        STRAIGHT,
        SINE_WAVE,
        DIAGONAL
    }

    private TrajectoryType trajectoryType = TrajectoryType.STRAIGHT;
    private double speedY = 250.0;
    private double speedX = 0.0;
    private double originX;
    private double amplitude = 60.0;
    private double frequency = 0.025;
    private double phase = 0;

    public SwarmEnemy(double startX, double startY) {
        this(startX, startY, TrajectoryType.STRAIGHT, 250.0, 0);
    }

    public SwarmEnemy(double startX, double startY, TrajectoryType trajectoryType, double speedY, double speedX) {
        super("enemy_sine_orbit", startX, startY);
        this.health = 10;
        this.collisionDamage = 10;
        this.originX = startX;
        this.trajectoryType = trajectoryType;
        this.speedY = speedY;
        this.speedX = speedX;
        this.setPos(startX, startY, 180);
    }

    public SwarmEnemy(double startX, double startY, double amplitude, double frequency, double phase) {
        super("enemy_sine_orbit", startX, startY);
        this.health = 10;
        this.collisionDamage = 10;
        this.originX = startX;
        this.trajectoryType = TrajectoryType.SINE_WAVE;
        this.amplitude = amplitude;
        this.frequency = frequency;
        this.phase = phase;
        this.speedY = 220.0;
        this.setPos(startX, startY, 180);
    }

    @Override
    public void update() {
        y += speedY / 60.0;

        if (trajectoryType == TrajectoryType.SINE_WAVE) {
            x = originX + amplitude * Math.sin(frequency * y + phase);
        } else if (trajectoryType == TrajectoryType.DIAGONAL) {
            x += speedX / 60.0;
        }

        double angle = 180;
        if (trajectoryType == TrajectoryType.SINE_WAVE) {
            double dx = amplitude * frequency * Math.cos(frequency * y + phase) * (speedY / 60.0);
            double dy = speedY / 60.0;
            angle = Math.toDegrees(Math.atan2(dy, dx)) - 90;
        } else if (trajectoryType == TrajectoryType.DIAGONAL) {
            angle = Math.toDegrees(Math.atan2(speedY, speedX)) - 90;
        }

        setPos(x, y, angle);

        if (y > Main.HEIGHT + 50 || x < -100 || x > Main.WIDTH + 100) {
            isAlive = false;
        }
        checkDeath();
    }
}
