package com.nhom27.skyforce.entities.player;

import java.util.ArrayList;
import java.util.List;

import com.nhom27.skyforce.audio.AudioManager;
import com.nhom27.skyforce.entities.base.EnemyObject;
import com.nhom27.skyforce.entities.base.GameObject;
import com.nhom27.skyforce.entities.weapons.Bullet;
import com.nhom27.skyforce.main.Main;
import com.nhom27.skyforce.managers.PlayerDataManager;
import com.nhom27.skyforce.utils.AssetManager;
import javafx.animation.PauseTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class Player extends GameObject {
    protected String skinId = "blue";
    protected int level;
    protected int currentXp;
    protected int xpToNextLevel;
    protected int maxLevel = 3;

    protected int damage;
    protected int health;
    protected int maxHealth;
    protected double speedBulletX;
    protected double speedBulletY;
    protected String currentBulletTexture = "bullet_player_1";

    protected long timeSinceLastBullet;
    private List<Bullet> bullets;
    protected long fireRate; // ms
    protected long seekerBuffEndTime = 0;
    protected long shieldBuffEndTime = 0;
    protected int shotCount = 0;
    private ImageView shieldView;

    protected PauseTransition currentGlowTimer;

    public Player(String spriteKey, double startX, double startY) {
        super(spriteKey, startX, startY);
        this.bullets = new ArrayList<>();

        Image shieldImg = AssetManager.getImage("char_shield");
        if (shieldImg != null) {
            this.shieldView = new ImageView(shieldImg);
            this.shieldView.setVisible(false);
        }
        this.timeSinceLastBullet = 0;
        setPos(startX - (sizeX / 2), startY - (sizeY / 2));
        updateShieldPosition();
    }

    public static Player createPlayerForLevel(int level, double x, double y) {
        String skin = PlayerDataManager.getInstance().getEquippedSkin();
        return createPlayerForLevel(level, x, y, skin);
    }

    public static Player createPlayerForLevel(int level, double x, double y, String skinId) {
        switch (level) {
            case 1:
                return new PlayerLevel1(x, y, skinId);
            case 2:
                return new PlayerLevel2(x, y, skinId);
            case 3:
                return new PlayerLevel3(x, y, skinId);
        }
        return null;
    }

    public String getSkinId() {
        return skinId;
    }

    public void setSkinId(String skinId) {
        if (skinId != null && !skinId.isEmpty()) {
            this.skinId = skinId;
            this.currentBulletTexture = getBulletTexture();
        }
    }

    public String getBulletTexture() {
        return "bullet_player_" + (skinId != null && !skinId.isEmpty() ? skinId : "blue");
    }

    public String getSeekerBulletTexture() {
        return "bullet_player_seeker_lv" + Math.min(Math.max(level, 1), 3);
    }

    public int calculateXpRequirement(int lvl) {
        switch (lvl) {
            case 1:
                return 100;
            case 2:
                return 250;
            default:
                return 500;
        }
    }

    public void copyStateFrom(Player oldPlayer) {
        if (oldPlayer == null)
            return;
        this.skinId = oldPlayer.skinId;
        this.level = oldPlayer.level;
        this.currentXp = oldPlayer.currentXp;
        this.xpToNextLevel = oldPlayer.xpToNextLevel;

        this.health = Math.min(this.maxHealth, oldPlayer.health + (this.maxHealth - oldPlayer.maxHealth));
        if (this.health <= 0)
            this.health = this.maxHealth;
        this.currentBulletTexture = oldPlayer.getBulletTexture();

        this.timeSinceLastBullet = oldPlayer.timeSinceLastBullet;
        this.bullets = oldPlayer.bullets;
        this.seekerBuffEndTime = oldPlayer.seekerBuffEndTime;
        this.shieldBuffEndTime = oldPlayer.shieldBuffEndTime;
        this.shotCount = oldPlayer.shotCount;

        this.setPos(oldPlayer.getX(), oldPlayer.getY());

        this.currentGlowTimer = oldPlayer.currentGlowTimer;
        if (this.shieldView != null && oldPlayer.shieldView != null) {
            this.shieldView.setVisible(oldPlayer.shieldView.isVisible());
            this.shieldView.setOpacity(oldPlayer.shieldView.getOpacity());
        }
    }

    public Player addXp(int amount) {
        if (amount <= 0)
            return this;
        this.currentXp += amount;
        int oldLevel = this.level;

        while (this.level < this.maxLevel && this.currentXp >= this.xpToNextLevel) {
            this.currentXp -= this.xpToNextLevel;
            this.level++;
            this.xpToNextLevel = calculateXpRequirement(this.level);
        }

        if (this.level >= this.maxLevel) {
            this.currentXp = this.xpToNextLevel;
        }

        if (this.level > oldLevel) {
            Player upgradedPlayer = createPlayerForLevel(this.level, x, y, skinId);
            upgradedPlayer.copyStateFrom(this);
            return upgradedPlayer;
        }
        return this;
    }

    public abstract List<Bullet> fireBullet(List<EnemyObject> enemies);

    public List<Bullet> fireBullet() {
        return fireBullet(null);
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
        if (isShieldActive()) {
            shieldBuffEndTime = 0; // Khiên vỡ ngay sau 1 lần đỡ sát thương (100% sát thương absorbed)
            if (shieldView != null) {
                shieldView.setVisible(false);
            }
            AudioManager.getInstance().playSound("sfx_player_shield_break");
            return;
        }
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
        this.seekerBuffEndTime = System.currentTimeMillis() + durationMs;
        this.shotCount = 0;
    }

    public boolean isSeekerActive() {
        return System.currentTimeMillis() < seekerBuffEndTime;
    }

    public boolean shouldFireSeeker() {
        if (!isSeekerActive())
            return false;
        return (shotCount > 0 && shotCount % 4 == 0);
    }

    public long getSeekerBuffTimeRemaining() {
        return Math.max(0, seekerBuffEndTime - System.currentTimeMillis());
    }

    public void activateShieldBuff(long durationMs) {
        this.shieldBuffEndTime = System.currentTimeMillis() + durationMs;
        if (shieldView != null) {
            shieldView.setVisible(true);
            updateShieldPosition();
        }
    }

    public boolean isShieldActive() {
        return System.currentTimeMillis() < shieldBuffEndTime;
    }

    public long getShieldBuffTimeRemaining() {
        return Math.max(0, shieldBuffEndTime - System.currentTimeMillis());
    }

    public ImageView getShieldView() {
        return shieldView;
    }

    protected void updateShieldPosition() {
        if (shieldView != null && shieldView.getImage() != null) {
            double shieldWidth = shieldView.getImage().getWidth();
            double shieldHeight = shieldView.getImage().getHeight();
            double shieldX = x + (sizeX - shieldWidth) / 2.0;
            double shieldY = y + (sizeY - shieldHeight) / 2.0;
            shieldView.setLayoutX(shieldX);
            shieldView.setLayoutY(shieldY);
        }
    }

    @Override
    public void setPos(double currentX, double currentY) {
        super.setPos(currentX, currentY);
        updateShieldPosition();
    }

    public PauseTransition getGlowTimer() {
        return this.currentGlowTimer;
    }

    public void setGlowTimer(PauseTransition timer) {
        this.currentGlowTimer = timer;
    }

    public void movePlayer(double mouseX, double mouseY) {
        double clampedX = Math.min(Math.max((sizeX / 2), mouseX), Main.WIDTH - sizeX / 2);
        double clampedY = Math.min(Math.max((sizeY / 2), mouseY), Main.HEIGHT - sizeY / 2);
        setPos(clampedX - (sizeX / 2), clampedY - (sizeY / 2));
    }

    public void moveBy(double dx, double dy) {
        double newX = x + dx;
        double newY = y + dy;
        double clampedX = Math.min(Math.max(0, newX), Main.WIDTH - sizeX);
        double clampedY = Math.min(Math.max(0, newY), Main.HEIGHT - sizeY);
        setPos(clampedX, clampedY);
    }

    @Override
    public void update() {
        if (isShieldActive()) {
            if (shieldView != null) {
                if (!shieldView.isVisible()) {
                    shieldView.setVisible(true);
                }

                long remainingMs = getShieldBuffTimeRemaining();
                if (remainingMs <= 3000) {
                    boolean blinkState = (System.currentTimeMillis() / 200) % 2 == 0;
                    shieldView.setOpacity(blinkState ? 1.0 : 0.2);
                } else {
                    shieldView.setOpacity(1.0);
                }

                updateShieldPosition();
            }
        } else {
            if (shieldView != null) {
                if (shieldView.isVisible()) {
                    shieldView.setVisible(false);
                }
                shieldView.setOpacity(1.0);
            }
        }
    }
}