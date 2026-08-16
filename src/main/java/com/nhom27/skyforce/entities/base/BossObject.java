package com.nhom27.skyforce.entities.base;

public abstract class BossObject extends EnemyObject {
    protected int maxHealth;
    protected String bossName;

    public BossObject(String nameImage, double startX, double startY, int maxHealth, String bossName) {
        super(nameImage, startX, startY);
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.bossName = bossName;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public String getBossName() {
        return bossName;
    }

    public double getHealthPercentage() {
        return Math.max(0, (double) health / maxHealth);
    }
}
