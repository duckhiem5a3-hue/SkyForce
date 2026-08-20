package com.nhom27.skyforce.entities.enemies;

import com.nhom27.skyforce.entities.base.EnemyObject;
import com.nhom27.skyforce.entities.player.Player;
import com.nhom27.skyforce.entities.weapons.EnemyBullet;
import com.nhom27.skyforce.main.Main;
import com.nhom27.skyforce.managers.GameManager;

public class SwarmEnemy extends EnemyObject {
    public enum TrajectoryType {
        STRAIGHT,
        SINE_WAVE,
        DIAGONAL
    }

    private TrajectoryType trajectoryType = TrajectoryType.STRAIGHT;
    private double speedY;
    private double speedX;
    private double originX;
    private double amplitude;
    private double frequency;
    private double phase;

    public SwarmEnemy() {
        this(TrajectoryType.STRAIGHT, 250.0, 0, 60.0, 0.025, 0);
    }

    public SwarmEnemy(double speedY, double speedX) {
        this(TrajectoryType.STRAIGHT, speedY, speedX, 60.0, 0.025, 0);
    }

    public SwarmEnemy(TrajectoryType trajectoryType, double speedY, double speedX) {
        this(trajectoryType, speedY, speedX, 60.0, 0.025, 0);
    }

    public SwarmEnemy(TrajectoryType trajectoryType, double speedY, double speedX, double amplitude, double frequency,
            double phase) {
        super("enemy_swarm_black", 10, 10);
        this.trajectoryType = trajectoryType;
        this.speedY = speedY;
        this.speedX = speedX;
        this.amplitude = amplitude;
        this.frequency = frequency;
        this.phase = phase;
    }

    public void spawnAt(double startX, double startY) {
        super.setPos(startX, startY);
        this.originX = startX;
    }

    @Override
    public void attack(GameManager gm, long now, Player player) {
        if (!isAlive() && gm != null && gm.getCurrentStageLevel() == 8) {
            double startX = getX() + getSizeX() / 2.0;
            double startY = getY() + getSizeY() / 2.0;
            double targetedVx = 0;
            double targetedVy = 250.0;
            if (player != null && player.isAlive()) {
                double dx = player.getX() - startX;
                double dy = player.getY() - startY;
                double dist = Math.hypot(dx, dy);
                if (dist > 0) {
                    targetedVx = (dx / dist) * 250.0;
                    targetedVy = (dy / dist) * 250.0;
                }
            }
            EnemyBullet sBullet = new EnemyBullet(startX, startY, targetedVx, targetedVy, 15,
                    "bullet_enemy_round_purple");
            gm.spawnEnemyBullet(sBullet);
        }
    }

    @Override
    public void update() {
        y += speedY / 60.0;

        if (trajectoryType == TrajectoryType.SINE_WAVE) {
            x = originX + amplitude * Math.sin(frequency * y + phase);
        } else if (trajectoryType == TrajectoryType.DIAGONAL) {
            x += speedX / 60.0;
        }

        double angle = 0;
        if (trajectoryType == TrajectoryType.SINE_WAVE) {
            double dx = amplitude * frequency * Math.cos(frequency * y + phase) * (speedY / 60.0);
            double dy = speedY / 60.0;
            angle = Math.toDegrees(Math.atan2(dy, dx)) - 90;
        } else if (trajectoryType == TrajectoryType.DIAGONAL) {
            angle = Math.toDegrees(Math.atan2(speedY, speedX)) - 90;
        }

        setPos(x, y, angle);

        if (y > Main.HEIGHT + sizeY || x < -sizeX - 200 || x > Main.WIDTH + 200) {
            isAlive = false;
        }
        checkDeath();
    }
}
