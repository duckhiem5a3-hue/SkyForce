package com.nhom27.skyforce.entities.player;

import java.util.ArrayList;
import java.util.List;

import com.nhom27.skyforce.entities.base.EnemyObject;
import com.nhom27.skyforce.entities.base.GameObject;
import com.nhom27.skyforce.entities.weapons.Bullet;
import com.nhom27.skyforce.entities.weapons.SeekerBullet;
import com.nhom27.skyforce.main.Main;
import com.nhom27.skyforce.utils.AssetManager;

import javafx.animation.PauseTransition;

public class Player extends GameObject {
    protected int level;
    protected int currentXp;
    protected int xpToNextLevel;
    protected int maxLevel = 5;

    protected int damage;
    protected int health;
    protected int maxHealth;
    protected double speedBulletX;
    protected double speedBulletY;

    protected long timeSinceLastBullet;
    private List<Bullet> bullets;
    protected long fireRate; // Thời gian chờ giữa các lần bắn (mili-giây)
    protected long seekerBuffEndTime = 0;
    protected PauseTransition currentGlowTimer;
    private int calculateXpRequirement(int lvl) {
        switch (lvl) {
            case 1:
                return 100;
            case 2:
                return 250;
            case 3:
                return 450;
            case 4:
                return 700;
            default:
                return 1000;
        }
    }

    private void setDefault() {
        this.level = 1;
        this.currentXp = 0;
        this.xpToNextLevel = calculateXpRequirement(1);
        this.timeSinceLastBullet = 0;
        applyLevelStats();
    }

    public void addXp(int amount) {
        if (amount <= 0)
            return;
        this.currentXp += amount;

        while (this.level < this.maxLevel && this.currentXp >= this.xpToNextLevel) {
            this.currentXp -= this.xpToNextLevel;
            levelUp();
        }

        if (this.level >= this.maxLevel) {
            this.currentXp = this.xpToNextLevel;
        }
    }

    public void levelUp() {
        if (this.level < this.maxLevel) {
            this.level++;
            this.xpToNextLevel = calculateXpRequirement(this.level);
            applyLevelStats();
        }
    }

    private void applyLevelStats() {
        switch (this.level) {
            case 1:
                this.damage = 20;
                this.fireRate = 200;
                this.maxHealth = 100;
                this.speedBulletX = 0;
                this.speedBulletY = -400;
                break;
            case 2:
                this.damage = 30;
                this.fireRate = 180;
                this.maxHealth = 120;
                this.speedBulletX = 0;
                this.speedBulletY = -450;
                break;
            case 3:
                this.damage = 45;
                this.fireRate = 150;
                this.maxHealth = 140;
                this.speedBulletX = 0;
                this.speedBulletY = -500;
                break;
            case 4:
                this.damage = 60;
                this.fireRate = 120;
                this.maxHealth = 160;
                this.speedBulletX = 0;
                this.speedBulletY = -550;
                break;
            case 5:
                this.damage = 80;
                this.fireRate = 100;
                this.maxHealth = 200;
                this.speedBulletX = 0;
                this.speedBulletY = -600;
                break;
        }
        this.health = this.maxHealth;
    }

    public Player(double startX, double startY) {
        super("player_ship_1", startX, startY);
        this.hitbox = AssetManager.getSpriteInfo("player_ship_1").getHitbox();
        this.setPos(startX, startY);
        this.bullets = new ArrayList<>();
        setDefault();
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getLevel() {
        return level;
    }

    public int getCurrentXp() {
        return currentXp;
    }

    public int getXpToNextLevel() {
        return xpToNextLevel;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setHealth(int health) {
        this.health = Math.max(0, Math.min(health, maxHealth));
    }

    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health <= 0) {
            this.health = 0;
            this.isAlive = false;
        }
    }

    public void heal(int amount) {
        this.health = Math.min(maxHealth, this.health + amount);
    }

    public long getTimeSinceLastBullet() {
        return timeSinceLastBullet;
    }

    public void setTimeSinceLastBullet(long timeSinceLastBullet) {
        this.timeSinceLastBullet = timeSinceLastBullet;
    }

    public void setFireRate(long fireRate) {
        this.fireRate = fireRate;
    }

    public long getFireRate() {
        return fireRate;
    }

    public void addBullet(List<Bullet> bullet) {
        bullets.addAll(bullet);
    }

    public List<Bullet> getBullets() {
        return bullets;
    }

    public void activateSeekerBuff(long durationMs) {
        //thời điểm kết thúc buff = thời điểm bắt đầu buff (hiện tại) cộng thời gian buff 
        this.seekerBuffEndTime = System.currentTimeMillis() + durationMs;    
    }

    public boolean isSeekerActive() {
        //kiểm tra xem thời điểm hiện tại đã vượt quá thời điểm hết buff chưa 
        return System.currentTimeMillis() < seekerBuffEndTime; 
    }

    public long getSeekerBuffTimeRemaining() {
        //tính thời gian còn lại (thời điểm hết buff trừ thời điểm hiện tại) 
        return Math.max(0, seekerBuffEndTime - System.currentTimeMillis());
    }

    public PauseTransition getGlowTimer() {
        return this.currentGlowTimer;
    }
    public void setGlowTimer(PauseTransition timer) {
        this.currentGlowTimer = timer;
    }
    public List<Bullet> fireBullet() {
        return fireBullet(null);
    }

    public List<Bullet> fireBullet(List<EnemyObject> enemies) {
        List<Bullet> bulletsToSpawn = new ArrayList<>();
        double startX = x + sizeX / 2;
        double startY = y;

        Bullet b = new Bullet("bullet_player_1", startX, startY, speedBulletX, speedBulletY, damage);
        bulletsToSpawn.add(b);

        if (isSeekerActive()) {
            SeekerBullet leftSeeker = new SeekerBullet(startX - 15, startY, damage / 6, -1, enemies);
            SeekerBullet rightSeeker = new SeekerBullet(startX + 15, startY, damage / 6, 1, enemies);
            bulletsToSpawn.add(leftSeeker);
            bulletsToSpawn.add(rightSeeker);
        }

        addBullet(bulletsToSpawn);
        return bulletsToSpawn;
    }

    public void movePlayer(double mouseX, double mouseY) {
        // Đảm bảo máy bay người chơi luôn hiển thị trong khung hình
        double clampedX = Math.min(Math.max((sizeX / 2), mouseX), Main.WIDTH - sizeX / 2);
        double clampedY = Math.min(Math.max((sizeY / 2), mouseY), Main.HEIGHT - sizeY / 2);
        // Căn cho con trỏ chuột trỏ vào trọng tâm máy bay
        setPos(clampedX - (sizeX / 2), clampedY - (sizeY / 2));
    }

    @Override
    public void update() {
    }
}