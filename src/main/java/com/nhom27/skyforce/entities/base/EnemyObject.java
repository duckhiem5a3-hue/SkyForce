package com.nhom27.skyforce.entities.base;

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

    public abstract void update();
}