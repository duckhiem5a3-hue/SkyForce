package com.nhom27.skyforce.entities.enemies;

import com.nhom27.skyforce.entities.base.EnemyObject;
import com.nhom27.skyforce.entities.player.Player;
import com.nhom27.skyforce.main.Main;

public class SniperEnemy extends EnemyObject {
    public enum State {
        ENTERING,
        AIMING,
        SHOOTING,
        EXITING
    }

    private State state;
    private double speedY;
    private double exitSpeedX;
    private double exitSpeedY;
    private String exitDirection; // AUTO, UP, LEFT, RIGHT
    private double targetY;
    private long pauseStartTime;
    private long pauseDurationMs; // ngắm dừng
    private boolean readyToFire;
    private double aimedAngle = 0;
    private double aimedDirX = 0;
    private double aimedDirY = 1;
    private String bulletTexture = "bullet_enemy_diamond_yellow";
    private Player playerRef;

    private boolean burstMode = false;
    private int burstShotsRemaining = 0;
    private long lastBurstFireTime;

    public SniperEnemy() {
        this(150.0, 1500, "AUTO");
    }

    public SniperEnemy(double targetY, long pauseDurationMs, String exitDirection) {
        super("enemy_sniper_green", 40, 20);
        this.state = State.ENTERING;
        this.speedY = 200.0;
        this.targetY = targetY;
        this.pauseDurationMs = pauseDurationMs;
        this.exitDirection = exitDirection;
    }

    public SniperEnemy(double startX, double startY, double targetY, long pauseDurationMs, String exitDirection) {
        this(targetY, pauseDurationMs, exitDirection);
        setPos(startX, startY, 180);
    }

    public void setBurstMode(boolean burstMode) {
        this.burstMode = burstMode;
        if (burstMode) {
            this.pauseDurationMs = 500; // 0.5s ngắm nhanh
        }
    }

    public String getBulletTexture() {
        return bulletTexture;
    }

    public void setBulletTexture(String bulletTexture) {
        this.bulletTexture = bulletTexture;
    }

    public void setPlayer(Player player) {
        this.playerRef = player;
    }

    public State getState() {
        return state;
    }

    public boolean isReadyToFire() {
        long now = System.currentTimeMillis();
        if (state == State.SHOOTING) {
            if (!burstMode) {
                if (readyToFire) {
                    readyToFire = false;
                    return true;
                }
            } else {
                if (readyToFire && burstShotsRemaining == 0) {
                    burstShotsRemaining = 3;
                    lastBurstFireTime = now;
                    burstShotsRemaining--;
                    return true;
                } else if (burstShotsRemaining > 0 && (now - lastBurstFireTime >= 150)) {
                    lastBurstFireTime = now;
                    burstShotsRemaining--;
                    if (burstShotsRemaining == 0) {
                        readyToFire = false;
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public double getAimedDirX() {
        return aimedDirX;
    }

    public double getAimedDirY() {
        return aimedDirY;
    }

    public void updateAimAndState(Player player) {
        if (player != null) {
            this.playerRef = player;
        }
        Player targetPlayer = this.playerRef;

        if (state == State.ENTERING) {
            y += speedY / 60.0;
            setPos(x, y);
            if (y >= targetY) {
                y = targetY;
                setPos(x, y);
                state = State.AIMING;
                pauseStartTime = System.currentTimeMillis();
            }
        } else if (state == State.AIMING) {
            // Xoay nòng súng về phía người chơi
            if (targetPlayer != null && targetPlayer.isAlive()) {
                double enemyCenterX = x + sizeX / 2.0;
                double enemyCenterY = y + sizeY / 2.0;
                double playerCenterX = targetPlayer.getX() + targetPlayer.getSizeX() / 2.0;
                double playerCenterY = targetPlayer.getY() + targetPlayer.getSizeY() / 2.0;

                double dx = playerCenterX - enemyCenterX;
                double dy = playerCenterY - enemyCenterY;
                double dist = Math.hypot(dx, dy);

                if (dist > 0) {
                    aimedDirX = dx / dist;
                    aimedDirY = dy / dist;
                    aimedAngle = Math.toDegrees(Math.atan2(dy, dx)) - 90; // -90 độ do ảnh của enemy hướng xuống dưới
                }
            }
            setPos(x, y, aimedAngle);

            // Dừng lại ngắm
            if (System.currentTimeMillis() - pauseStartTime >= pauseDurationMs) {
                state = State.SHOOTING;
                readyToFire = true;
                // Quyết định hướng tẩu thoát
                if ("UP".equalsIgnoreCase(exitDirection)) {
                    exitSpeedX = 0;
                    exitSpeedY = -250.0;
                } else if ("LEFT".equalsIgnoreCase(exitDirection)) {
                    exitSpeedX = -250.0;
                    exitSpeedY = 0;
                } else if ("RIGHT".equalsIgnoreCase(exitDirection)) {
                    exitSpeedX = 250.0;
                    exitSpeedY = 0;
                } else {
                    if (x < Main.WIDTH / 2.0) {
                        exitSpeedX = -250.0;
                    } else {
                        exitSpeedX = 250.0;
                    }
                    exitSpeedY = 0;
                }
            }
        } else if (state == State.SHOOTING) {
            if (!readyToFire && burstShotsRemaining == 0) {
                state = State.EXITING;
            }
        } else if (state == State.EXITING) {
            x += exitSpeedX / 60.0;
            y += exitSpeedY / 60.0;

            double exitAngle = 180;
            if (exitSpeedY < 0) {
                exitAngle = 180;
            } else if (exitSpeedX < 0) {
                exitAngle = 90;
            } else if (exitSpeedX > 0) {
                exitAngle = -90;
            }
            setPos(x, y, exitAngle);

            if (x < -100 || x > Main.WIDTH + 100 || y < -100 || y > Main.HEIGHT + 100) {
                isAlive = false;
            }
        }
        checkDeath();
    }

    @Override
    public void update() {
        updateAimAndState(playerRef);
    }
}
