package com.nhom27.skyforce.entities.weapons;

import java.util.List;
import com.nhom27.skyforce.entities.base.EnemyObject;
import com.nhom27.skyforce.main.Main;
import javafx.scene.shape.Rectangle;

public class SeekerBullet extends Bullet {
    private List<EnemyObject> enemies;
    private EnemyObject targetEnemy;
    private double totalSpeed;
    private double turnRate = 0.1;

    public SeekerBullet(double startX, double startY, int damage, int side, List<EnemyObject> enemies) {
        this("bullet_player_seeker_lv1", startX, startY, damage, side, enemies);
    }

    public SeekerBullet(String nameImage, double startX, double startY, int damage, int side,
            List<EnemyObject> enemies) {
        super(nameImage, startX, startY, side * 50.0, -100.0, damage);
        sizeX = sizeX / 2;
        sizeY = sizeY / 2;
        this.view.setFitHeight(sizeY);
        this.view.setFitWidth(sizeX);
        this.hitbox = new Rectangle(sizeX, sizeY);
        this.enemies = enemies;
        this.totalSpeed = Math.hypot(speedX, speedY) + 200;

        double initialSpeed = Math.hypot(speedX, speedY);
        if (initialSpeed > 0) {
            speedX = (speedX / initialSpeed) * totalSpeed;
            speedY = (speedY / initialSpeed) * totalSpeed;
        }

        double angle = Math.toDegrees(Math.atan2(speedY, speedX)) + 90;
        this.setPos(startX - sizeX / 2.0, startY - sizeY, angle);
    }

    private EnemyObject findClosestEnemy() {
        if (enemies == null || enemies.isEmpty()) {
            return null;
        }
        EnemyObject closest = null;
        double minDistanceSq = Double.MAX_VALUE;
        for (EnemyObject enemy : enemies) {
            if (enemy != null && enemy.isAlive()) {
                double enemyCenterX = enemy.getX() + enemy.getSizeX() / 2.0;
                double enemyCenterY = enemy.getY() + enemy.getSizeY() / 2.0;
                double distSq = Math.pow(enemyCenterX - x, 2) + Math.pow(enemyCenterY - y, 2);
                if (distSq < minDistanceSq) {
                    minDistanceSq = distSq;
                    closest = enemy;
                }
            }
        }
        return closest;
    }

    @Override
    public void update() {
        if (targetEnemy == null || !targetEnemy.isAlive()) {
            targetEnemy = findClosestEnemy();
        }

        if (targetEnemy != null && targetEnemy.isAlive()) {
            double targetCenterX = targetEnemy.getX() + targetEnemy.getSizeX() / 2.0;
            double targetCenterY = targetEnemy.getY() + targetEnemy.getSizeY() / 2.0;

            double targetAngle = Math.atan2(targetCenterY - y, targetCenterX - x);
            double targetSpeedX = totalSpeed * Math.cos(targetAngle);
            double targetSpeedY = totalSpeed * Math.sin(targetAngle);

            speedX += (targetSpeedX - speedX) * turnRate;
            speedY += (targetSpeedY - speedY) * turnRate;

            // Điều chỉnh lại tốc độ sau khi đổi hướng sao cho bằng với tốc độ ban đầu
            double currentSpeed = Math.hypot(speedX, speedY);
            if (currentSpeed > 0) {
                speedX = (speedX / currentSpeed) * totalSpeed;
                speedY = (speedY / currentSpeed) * totalSpeed;
            }
        }

        x += speedX / 60.0;
        y += speedY / 60.0;

        double angle = Math.toDegrees(Math.atan2(speedY, speedX)) + 90;
        this.setPos(x, y, angle);

        if (this.x < -sizeX || this.x > Main.WIDTH || this.y < -sizeY || this.y > Main.HEIGHT) {
            this.isAlive = false;
        }
    }
}
