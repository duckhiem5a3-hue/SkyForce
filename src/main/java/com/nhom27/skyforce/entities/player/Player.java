package com.nhom27.skyforce.entities.player;

import java.util.ArrayList;
import java.util.List;

import com.nhom27.skyforce.entities.base.GameObject;
import com.nhom27.skyforce.entities.weapons.Bullet;
import com.nhom27.skyforce.main.Main;
import com.nhom27.skyforce.utils.AssetManager;

public class Player extends GameObject {
    protected int level;
    protected int health;
    protected int maxHealth;
    protected boolean gettingBuffed;
    protected int timeInBuff;
    protected long timeSinceLastBullet;
    private List<Bullet> bullets;
    protected long fireRate; // Thời gian chờ giữa các lần bắn (mili-giây)

    private void setDefault() {
        this.level = 1;
        this.maxHealth = 100;
        this.health = maxHealth;
        this.gettingBuffed = false;
        this.timeInBuff = 0;
        this.timeSinceLastBullet = 0;
        this.fireRate = 200;
    }

    // public Player(Shape shape) {
    // super("player_ship_1", Main.WIDTH / 2, Main.HEIGHT * (3 / 4), sizeX, sizeY);
    // setDefault();
    // this.hitbox = shape != null ? shape : new Rectangle(sizeX, sizeY);
    // this.setPos(Main.WIDTH / 2, Main.HEIGHT * (3 / 4));
    // }

    public Player(double startX, double startY) {
        super("player_ship_1", startX, startY);
        this.bullets = new ArrayList<>();
        setDefault();
        this.hitbox = AssetManager.getSpriteInfo("player_ship_1").getHitbox();
        this.setPos(startX, startY);
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

    public boolean isGettingBuffed() {
        return gettingBuffed;
    }

    public void setGettingBuffed(boolean buffed) {
        this.gettingBuffed = buffed;
        if (buffed) {
            this.timeInBuff = 600; // 10s at 60 FPS
        }
    }

    public int getTimeInBuff() {
        return timeInBuff;
    }

    public void setTimeInBuff(int timeInBuff) {
        this.timeInBuff = timeInBuff;
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

        if (gettingBuffed) {
            Bullet b1 = new Bullet("bullet_player_1", startX, startY, 0, -800);
            Bullet b2 = new Bullet("bullet_player_2", startX - 15, startY, -100, -300);
            Bullet b3 = new Bullet("bullet_player_2", startX + 15, startY, 100, -300);

            bulletsToSpawn.add(b1);
            bulletsToSpawn.add(b2);
            bulletsToSpawn.add(b3);
        } else {
            Bullet b = new Bullet("bullet_player_1", startX, startY, 0, -100);
            bulletsToSpawn.add(b);
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
        timeSinceLastBullet++;
        if (gettingBuffed) {
            timeInBuff--;
            if (timeInBuff <= 0) {
                gettingBuffed = false;
                timeInBuff = 0;
            }
        }
    }
}