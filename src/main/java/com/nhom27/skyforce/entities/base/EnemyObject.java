package com.nhom27.skyforce.entities.base;

public abstract class EnemyObject extends GameObject {
    protected int health;
    protected int collisionDamage;

    public EnemyObject(String nameImage) {
        super(nameImage);
    }

    public EnemyObject(String nameImage, double startX, double startY) {
        super(nameImage, startX, startY);
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