package com.nhom27.skyforce.entities.player;

import java.util.ArrayList;
import java.util.List;

import com.nhom27.skyforce.entities.base.GameObject;
import com.nhom27.skyforce.entities.weapons.Bullet;
import com.nhom27.skyforce.main.Main;
import com.nhom27.skyforce.utils.AssetManager;

public class Player extends GameObject {
    protected int level;
    protected int damage;
    protected int health;
    protected int maxHealth;
    protected double speedBulletX;
    protected double speedBulletY;

    protected long timeSinceLastBullet;
    private List<Bullet> bullets;
    protected long fireRate; // Thời gian chờ giữa các lần bắn (mili-giây)

    private void setDefault() {
        this.level = 1;
        this.timeSinceLastBullet = 0;
        applyLevelStats();
    }

    public void levelUp() {
        // Giả sử max level là 5
        if (this.level < 5) {
            this.level++;
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
                // TODO: Sinh ra 1 máy bay con bên trái
                break;
            case 3:
                this.damage = 45;
                this.fireRate = 150;
                // TODO: Sinh ra thêm 1 máy bay con bên phải
                break;
            case 4:
                this.damage = 60;
                this.fireRate = 120;
                // TODO: Nâng cấp máy bay con lên loại bắn đạn laze
                break;
            case 5:
                this.damage = 80;
                this.fireRate = 100;
                // TODO: Bật chế độ Tối thượng
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

    public List<Bullet> fireBullet() {
        List<Bullet> bulletsToSpawn = new ArrayList<>();
        double startX = x + sizeX / 2;
        double startY = y;

        Bullet b = new Bullet("bullet_player_1", startX, startY, speedBulletX, speedBulletY, damage);
        bulletsToSpawn.add(b);

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