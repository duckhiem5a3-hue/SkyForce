package com.nhom27.skyforce.entities.base;

import com.nhom27.skyforce.entities.player.Player;
import com.nhom27.skyforce.managers.GameManager;

public abstract class EnemyObject extends GameObject {
    protected int health;
    protected int collisionDamage;

    protected boolean isInvulnerable = true;

    public EnemyObject(String nameImage) {
        super(nameImage);
    }

    public EnemyObject(String nameImage, int health, int collisionDamage) {
        super(nameImage);
        this.health = health;
        this.collisionDamage = collisionDamage;
    }

    public EnemyObject(String nameImage, double startX, double startY) {
        super(nameImage, startX, startY);
    }

    public boolean isInvulnerable() {
        if (isInvulnerable && this.y >= 30) {
            isInvulnerable = false;
        }
        return isInvulnerable;
    }

    public void setInvulnerable(boolean invulnerable) {
        this.isInvulnerable = invulnerable;
    }

    @Override
    public void setPos(double currentX, double currentY) {
        super.setPos(currentX, currentY);
        if (isInvulnerable && this.y >= 30) {
            isInvulnerable = false;
        }
    }

    public int getCollisionDamage() {
        return collisionDamage;
    }

    public void setCollisionDamage(int collisionDamage) {
        this.collisionDamage = collisionDamage;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void takeDamage(int amount) {
        if (isInvulnerable()) {
            return;
        }
        this.health -= amount;
        checkDeath();
    }

    protected void checkDeath() {
        if (this.health <= 0) {
            this.isAlive = false;
        }
    }

    /**
     * Phương thức đa hình xử lý hành động tấn công (bắn đạn) của kẻ địch.
     * Các class con triển khai sẽ tự chịu trách nhiệm tính toán và sinh đạn tương
     * ứng.
     *
     * @param gm     Quản lý GameManager để gọi spawnEnemyBullet
     * @param now    Timestamp hiện tại
     * @param player Tham chiếu tới máy bay người chơi
     */
    public void attack(GameManager gm, long now, Player player) {
    }

    public abstract void update();
}