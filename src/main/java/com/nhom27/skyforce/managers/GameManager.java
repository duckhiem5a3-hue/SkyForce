package com.nhom27.skyforce.managers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.nhom27.skyforce.audio.AudioManager;
import com.nhom27.skyforce.entities.base.EnemyObject;
import com.nhom27.skyforce.entities.base.GameObject;
import com.nhom27.skyforce.entities.enemies.*;
import com.nhom27.skyforce.entities.items.*;
import com.nhom27.skyforce.entities.player.Player;
import com.nhom27.skyforce.entities.weapons.Bullet;
import com.nhom27.skyforce.entities.weapons.EnemyBullet;
import com.nhom27.skyforce.main.Main;
import com.nhom27.skyforce.scenes.PlayScene;

import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

public class GameManager {
    private Pane gameLayoutPane;
    PlayScene playScene;

    private VFXManager vfxManager;

    private Player player;
    private List<EnemyObject> enemies;
    private List<PowerUp> powerUps;
    private ShooterEnemy shooterEnemy;

    private AnimationTimer gameLoop;
    private Random random;

    private int score;
    private int wave;
    private int nextWaveScore;
    private int goldCollected;
    private boolean isPaused;
    private boolean isGameOver;
    private long lastEnemyWaveTime;
    private long lastShooterDeathTime;

    private int currentStageLevel = 1;

    // Quản lý kịch bản Level 1
    private long levelStartTime = 0;
    private boolean spawned3s = false;
    private boolean spawned7s = false;
    private boolean spawned12s = false;
    private boolean spawned18s = false;
    private long lastFlyRainTime = 0;
    private boolean warningTriggered = false;
    private boolean miniBossSpawned = false;
    private boolean isVictory = false;

    // Quản lý kịch bản Level 2
    private boolean lvl2_spawned3s = false;
    private boolean lvl2_spawned8s = false;
    private boolean lvl2_spawned16s = false;
    private boolean lvl2_spawned22s = false;
    private boolean lvl2_spawned30s = false;
    private boolean lvl2_spawned35s = false;
    private long lvl2_lastFlyRainTime = 0;
    private boolean lvl2_spawned60s = false;
    private List<SniperEnemy> lvl2_deathSquad = new ArrayList<>();

    // Quản lý kịch bản Level 3
    private boolean lvl3_spawned5s = false;
    private int lvl3_queue5s_count = 0;
    private long lvl3_last5s_time = 0;

    private boolean lvl3_spawned12s = false;
    private int lvl3_queue12s_count = 0;
    private long lvl3_last12s_time = 0;

    private boolean lvl3_spawned18s = false;

    private boolean lvl3_spawned25s = false;
    private int lvl3_queue25s_count = 0;
    private long lvl3_last25s_time = 0;

    private long lvl3_lastRainTime = 0;

    private boolean lvl3_spawned50s = false;

    private boolean lvl3_spawned70s = false;
    private List<SwarmEnemy> lvl3_finalWave = new ArrayList<>();

    // Quản lý kịch bản Level 4
    private boolean lvl4_spawned5s = false;
    private boolean lvl4_spawned12s = false;
    private boolean lvl4_spawned18s = false;
    private boolean lvl4_spawned25s = false;
    private boolean lvl4_spawned35s = false;
    private boolean lvl4_spawned55s = false;
    private boolean lvl4_spawned60s = false;
    private List<TankerEnemy> lvl4_wallTankers = new ArrayList<>();

    // Quản lý kịch bản Level 5
    private boolean lvl5_spawnedSwarm = false;
    private boolean lvl5_spawnedRedEnemies = false;
    private boolean lvl5_warningTriggered = false;
    private boolean lvl5_bossSpawned = false;
    private MidBoss lvl5_midBoss = null;
    private boolean lvl5_victoryTriggered = false;

    // Quản lý kịch bản Level 6
    private boolean lvl6_spawned5s = false;
    private boolean lvl6_spawned12s = false;
    private boolean lvl6_spawned25s = false;
    private boolean lvl6_spawned32s = false;
    private boolean lvl6_spawned50s = false;
    private boolean lvl6_spawned58s = false;
    private boolean lvl6_spawned75s = false;
    private long lvl6_lastMeteorRainTime = 0;

    // Quản lý kịch bản Level 7
    private boolean lvl7_spawned5s = false;
    private boolean lvl7_spawned10s = false;
    private boolean lvl7_warn18s = false;
    private boolean lvl7_spawned19s = false;
    private boolean lvl7_warn25s = false;
    private boolean lvl7_spawned26s = false;
    private boolean lvl7_spawned30s = false;
    private boolean lvl7_spawned40s = false;
    private boolean lvl7_warn50s = false;
    private boolean lvl7_spawned51s = false;
    private boolean lvl7_spawned70s = false;

    // Quản lý kịch bản Level 8
    private boolean lvl8_spawned5s = false;
    private boolean lvl8_spawned10s = false;
    private boolean lvl8_spawned25s = false;
    private boolean lvl8_spawned32s = false;
    private boolean lvl8_spawned50s = false;
    private boolean lvl8_spawned55s = false;
    private boolean lvl8_spawned75s = false;

    // Quản lý kịch bản Level 9
    private boolean lvl9_spawned5s = false;
    private boolean lvl9_warn15s = false;
    private boolean lvl9_spawned18s = false;
    private boolean lvl9_spawned25s = false;
    private boolean lvl9_spawned45s = false;
    private boolean lvl9_warn48s = false;
    private boolean lvl9_spawned49s = false;
    private boolean lvl9_spawned70s = false;
    private boolean lvl9_spawned73s = false;
    private boolean lvl9_spawned85s = false;
    private long lvl9_lastMeteorTime = 0;

    public int getCurrentStageLevel() {
        return currentStageLevel;
    }

    public void setCurrentStageLevel(int currentStageLevel) {
        this.currentStageLevel = currentStageLevel;
    }

    private java.util.Set<javafx.scene.input.KeyCode> activeKeys = new java.util.HashSet<>();

    public void handleKeyPressed(javafx.scene.input.KeyCode code) {
        activeKeys.add(code);
    }

    public void handleKeyReleased(javafx.scene.input.KeyCode code) {
        activeKeys.remove(code);
    }

    private boolean isDebug = false;

    public GameManager(Pane gameLayoutPane, PlayScene playScene) {
        this.gameLayoutPane = gameLayoutPane;
        this.playScene = playScene;
        this.enemies = new ArrayList<>();
        this.powerUps = new ArrayList<>();
        this.random = new Random();
        setupGame();
    }

    public void setupGame() {
        // Dọn dẹp và Thiết lập lại trạng thái màn chơi
        gameLayoutPane.getChildren().clear();
        enemies.clear();
        powerUps.clear();

        lastEnemyWaveTime = 0;
        score = 0;
        goldCollected = 0;
        wave = 1;
        nextWaveScore = 500;
        isPaused = false;
        isGameOver = false;

        // Reset kịch bản Level 1 & Level 2
        levelStartTime = System.currentTimeMillis();
        spawned3s = false;
        spawned7s = false;
        spawned12s = false;
        spawned18s = false;
        lastFlyRainTime = 0;
        warningTriggered = false;
        miniBossSpawned = false;
        isVictory = false;

        lvl2_spawned3s = false;
        lvl2_spawned8s = false;
        lvl2_spawned16s = false;
        lvl2_spawned22s = false;
        lvl2_spawned30s = false;
        lvl2_spawned35s = false;
        lvl2_lastFlyRainTime = 0;
        lvl2_spawned60s = false;
        lvl2_deathSquad.clear();

        lvl3_spawned5s = false;
        lvl3_queue5s_count = 0;
        lvl3_last5s_time = 0;
        lvl3_spawned12s = false;
        lvl3_queue12s_count = 0;
        lvl3_last12s_time = 0;
        lvl3_spawned18s = false;
        lvl3_spawned25s = false;
        lvl3_queue25s_count = 0;
        lvl3_last25s_time = 0;
        lvl3_lastRainTime = 0;
        lvl3_spawned50s = false;
        lvl3_spawned70s = false;
        lvl3_finalWave.clear();

        lvl4_spawned5s = false;
        lvl4_spawned12s = false;
        lvl4_spawned18s = false;
        lvl4_spawned25s = false;
        lvl4_spawned35s = false;
        lvl4_spawned55s = false;
        lvl4_spawned60s = false;
        lvl4_wallTankers.clear();

        lvl5_spawnedSwarm = false;
        lvl5_spawnedRedEnemies = false;
        lvl5_warningTriggered = false;
        lvl5_bossSpawned = false;
        lvl5_midBoss = null;
        lvl5_victoryTriggered = false;

        lvl6_spawned5s = false;
        lvl6_spawned12s = false;
        lvl6_spawned25s = false;
        lvl6_spawned32s = false;
        lvl6_spawned50s = false;
        lvl6_spawned58s = false;
        lvl6_spawned75s = false;
        lvl6_lastMeteorRainTime = 0;

        lvl7_spawned5s = false;
        lvl7_spawned10s = false;
        lvl7_warn18s = false;
        lvl7_spawned19s = false;
        lvl7_warn25s = false;
        lvl7_spawned26s = false;
        lvl7_spawned30s = false;
        lvl7_spawned40s = false;
        lvl7_warn50s = false;
        lvl7_spawned51s = false;
        lvl7_spawned70s = false;

        lvl8_spawned5s = false;
        lvl8_spawned10s = false;
        lvl8_spawned25s = false;
        lvl8_spawned32s = false;
        lvl8_spawned50s = false;
        lvl8_spawned55s = false;
        lvl8_spawned75s = false;

        lvl9_spawned5s = false;
        lvl9_warn15s = false;
        lvl9_spawned18s = false;
        lvl9_spawned25s = false;
        lvl9_spawned45s = false;
        lvl9_warn48s = false;
        lvl9_spawned49s = false;
        lvl9_spawned70s = false;
        lvl9_spawned73s = false;
        lvl9_spawned85s = false;
        lvl9_lastMeteorTime = 0;

        if (playScene != null) {
            playScene.showWarningBanner(false);
            playScene.showBottomWarning(false, 0);
        }

        this.vfxManager = new VFXManager(this.gameLayoutPane);

        // 1. Tạo đối tượng người chơi
        player = Player.createPlayerForLevel(1, Main.WIDTH * 0.5, Main.HEIGHT * 0.75);

        if (player.getView() != null) {
            gameLayoutPane.getChildren().add(player.getView());
        }

        if (player.getShieldView() != null) {
            gameLayoutPane.getChildren().add(player.getShieldView());
        }

        // Bật debug hiển thị Hitbox của Player nếu isDebug = true
        if (isDebug && player.getHitbox() != null) {
            player.getHitbox().setFill(Color.rgb(255, 0, 0, 0.3)); // Nền đỏ mờ 30%
            player.getHitbox().setStroke(Color.YELLOW); // Viền vàng
            player.getHitbox().setStrokeWidth(2);

            if (!gameLayoutPane.getChildren().contains(player.getHitbox())) {
                gameLayoutPane.getChildren().add(player.getHitbox());
            }
        }

        // setup chuột
        gameLayoutPane.setOnMouseMoved(e -> player.movePlayer(e.getX(), e.getY()));
        gameLayoutPane.setOnMouseDragged(e -> player.movePlayer(e.getX(), e.getY()));

        // 3. Game Loop Setup
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                now = now / 1_000_000; // Đổi về mili giây
                if (!isPaused && !isGameOver) {
                    updateGame(now);
                }
            }
        };
    }

    public void replacePlayerInstance(Player newPlayer) {
        if (newPlayer == null || newPlayer == player)
            return;
        if (player != null) {
            if (player.getView() != null) {
                gameLayoutPane.getChildren().remove(player.getView());
            }
            if (player.getHitbox() != null) {
                gameLayoutPane.getChildren().remove(player.getHitbox());
            }
            if (player.getShieldView() != null) {
                gameLayoutPane.getChildren().remove(player.getShieldView());
            }
        }
        player = newPlayer;
        if (player.getView() != null) {
            gameLayoutPane.getChildren().add(player.getView());
        }
        if (player.getShieldView() != null) {
            gameLayoutPane.getChildren().add(player.getShieldView());
        }
        if (isDebug && player.getHitbox() != null) {
            player.getHitbox().setFill(Color.rgb(255, 0, 0, 0.3));
            player.getHitbox().setStroke(Color.YELLOW);
            player.getHitbox().setStrokeWidth(2);
            if (!gameLayoutPane.getChildren().contains(player.getHitbox())) {
                gameLayoutPane.getChildren().add(player.getHitbox());
            }
        }
        gameLayoutPane.setOnMouseMoved(e -> player.movePlayer(e.getX(), e.getY()));
        gameLayoutPane.setOnMouseDragged(e -> player.movePlayer(e.getX(), e.getY()));
    }

    private void updateGame(long now) {
        // Cập nhật logic người chơi
        if (player != null && player.isAlive()) {
            player.update();

            // Di chuyển bằng phím (300 pixels/giây)
            if (!activeKeys.isEmpty()) {
                double speed = 300.0 / 60.0;
                double dx = 0;
                double dy = 0;
                if (activeKeys.contains(javafx.scene.input.KeyCode.W)
                        || activeKeys.contains(javafx.scene.input.KeyCode.UP))
                    dy -= speed;
                if (activeKeys.contains(javafx.scene.input.KeyCode.S)
                        || activeKeys.contains(javafx.scene.input.KeyCode.DOWN))
                    dy += speed;
                if (activeKeys.contains(javafx.scene.input.KeyCode.A)
                        || activeKeys.contains(javafx.scene.input.KeyCode.LEFT))
                    dx -= speed;
                if (activeKeys.contains(javafx.scene.input.KeyCode.D)
                        || activeKeys.contains(javafx.scene.input.KeyCode.RIGHT))
                    dx += speed;

                if (dx != 0 || dy != 0) {
                    player.moveBy(dx, dy);
                }
            }

            // Kiểm tra xem player có cần chuyển đổi subclass khi lên level không
            Player currentLevelPlayer = Player.createPlayerForLevel(player.getLevel(), player.getX(), player.getY());
            if (!currentLevelPlayer.getClass().equals(player.getClass())) {
                currentLevelPlayer.copyStateFrom(player);
                replacePlayerInstance(currentLevelPlayer);
            }

            // Tự động bắn đạn của người chơi
            if (now - player.getTimeSinceLastBullet() >= player.getFireRate()) {
                AudioManager.getInstance().playSound("sfx_laser");
                for (Bullet bullet : player.fireBullet(enemies)) { // tạo đạn mới
                    gameLayoutPane.getChildren().add(bullet.getView()); // render đạn mới
                    if (isDebug && bullet.getHitbox() != null) { // render debug viền cho đạn mới
                        bullet.getHitbox().setFill(Color.rgb(255, 0, 0, 0.3)); // Nền đỏ
                        bullet.getHitbox().setStroke(Color.YELLOW); // Viền vàng
                        bullet.getHitbox().setStrokeWidth(2);

                        if (!gameLayoutPane.getChildren().contains(bullet.getHitbox())) {
                            gameLayoutPane.getChildren().add(bullet.getHitbox());
                        }
                    }
                }
                player.setTimeSinceLastBullet(now);
            }
        }
        // Cập nhật ShooterEnemy (bao gồm bắn đạn)
        if (shooterEnemy != null && shooterEnemy.isAlive()) {
            shooterEnemy.update();
            if (shooterEnemy.timeToFire()) {
                double startX = shooterEnemy.getX() + shooterEnemy.getSizeX() / 2 - 10;
                double startY = shooterEnemy.getY() + shooterEnemy.getSizeY();
                EnemyBullet eBullet = new EnemyBullet(startX, startY);
                gameLayoutPane.getChildren().add(eBullet.getView());
                ShooterEnemy.addBullet(eBullet);
            }
        }

        // Cập nhật đạn của người chơi
        Iterator<Bullet> bulletIter = player.getBullets().iterator();
        while (bulletIter.hasNext()) {
            Bullet b = bulletIter.next();
            b.update();
            if (!b.isAlive()) {
                gameLayoutPane.getChildren().remove(b.getView());
                gameLayoutPane.getChildren().remove(b.getHitbox());
                bulletIter.remove();
            }
        }
        // Cập nhật đạn của ShooterEnemy
        Iterator<EnemyBullet> eBulletIter = ShooterEnemy.getBulletList().iterator();
        while (eBulletIter.hasNext()) {
            EnemyBullet e = eBulletIter.next();
            e.update();
            if (!e.isAlive()) {
                gameLayoutPane.getChildren().remove(e.getView());
                gameLayoutPane.getChildren().remove(e.getHitbox());
                eBulletIter.remove();
            }
        }
        // Triệu hồi kẻ địch
        spawnEnemyWave(now);

        // Cập nhật kẻ địch (xóa an toàn nhờ vào Iterator)
        Iterator<EnemyObject> enemyIter = enemies.iterator();
        while (enemyIter.hasNext()) {
            EnemyObject e = enemyIter.next();
            if (e instanceof SniperEnemy sniperEnemy) {
                sniperEnemy.setPlayer(player);
            }
            e.update();
            if (!e.isAlive()) {
                if (currentStageLevel == 8 && e instanceof SwarmEnemy swarm) {
                    double startX = swarm.getX() + swarm.getSizeX() / 2.0;
                    double startY = swarm.getY() + swarm.getSizeY() / 2.0;
                    double targetedVx = 0;
                    double targetedVy = 250.0;
                    if (player != null && player.isAlive()) {
                        double dx = player.getX() - startX;
                        double dy = player.getY() - startY;
                        double dist = Math.hypot(dx, dy);
                        if (dist > 0) {
                            targetedVx = (dx / dist) * 250.0;
                            targetedVy = (dy / dist) * 250.0;
                        }
                    }
                    EnemyBullet sBullet = new EnemyBullet(startX, startY, targetedVx, targetedVy, 15,
                            "bullet_enemy_round_purple");
                    gameLayoutPane.getChildren().add(sBullet.getView());
                    ShooterEnemy.addBullet(sBullet);
                }
                gameLayoutPane.getChildren().remove(e.getView());
                if (e.getHitbox() != null) {
                    gameLayoutPane.getChildren().remove(e.getHitbox());
                }
                enemyIter.remove();
            } else if (e instanceof EliteEnemy eliteEnemy) {
                if (eliteEnemy.timeToFire(now)) {
                    double startX = eliteEnemy.getX() + eliteEnemy.getSizeX() / 2.0 - 5;
                    double startY = eliteEnemy.getY() + eliteEnemy.getSizeY();
                    double totalSpeed = 140.0;
                    double[] angles = { -20.0, 0.0, 20.0 };
                    for (double angleDeg : angles) {
                        double rad = Math.toRadians(angleDeg);
                        double vx = totalSpeed * Math.sin(rad);
                        double vy = totalSpeed * Math.cos(rad);
                        EnemyBullet b = new EnemyBullet(startX, startY, vx, vy, 15, "bullet_enemy_round_purple");
                        gameLayoutPane.getChildren().add(b.getView());
                        ShooterEnemy.addBullet(b);
                    }
                }
            } else if (e instanceof NormalEnemy normalEnemy) {
                if (normalEnemy.timeToFire(now)) {
                    double startX = normalEnemy.getX() + normalEnemy.getSizeX() / 2.0 - 5;
                    double startY = normalEnemy.getY() + normalEnemy.getSizeY();
                    EnemyBullet eBullet = new EnemyBullet(startX, startY, 0, 120.0, 15, "bullet_enemy_round_purple");
                    gameLayoutPane.getChildren().add(eBullet.getView());
                    if (isDebug && eBullet.getHitbox() != null) {
                        eBullet.getHitbox().setFill(Color.rgb(255, 0, 0, 0.3));
                        eBullet.getHitbox().setStroke(Color.YELLOW);
                        eBullet.getHitbox().setStrokeWidth(2);
                        gameLayoutPane.getChildren().add(eBullet.getHitbox());
                    }
                    ShooterEnemy.addBullet(eBullet);
                }
            } else if (e instanceof SniperEnemy sniperEnemy) {
                if (sniperEnemy.isReadyToFire()) {
                    double bulletSpeed = 350.0; // Bay cực nhanh 350 px/s
                    double speedX = sniperEnemy.getAimedDirX() * bulletSpeed;
                    double speedY = sniperEnemy.getAimedDirY() * bulletSpeed;
                    double startX = sniperEnemy.getX() + sniperEnemy.getSizeX() / 2.0 - 5;
                    double startY = sniperEnemy.getY() + sniperEnemy.getSizeY() / 2.0 - 5;
                    EnemyBullet eBullet = new EnemyBullet(startX, startY, speedX, speedY, 25, "bullet_enemy_laser");
                    gameLayoutPane.getChildren().add(eBullet.getView());
                    if (isDebug && eBullet.getHitbox() != null) {
                        eBullet.getHitbox().setFill(Color.rgb(255, 0, 0, 0.3));
                        eBullet.getHitbox().setStroke(Color.YELLOW);
                        eBullet.getHitbox().setStrokeWidth(2);
                        gameLayoutPane.getChildren().add(eBullet.getHitbox());
                    }
                    ShooterEnemy.addBullet(eBullet);
                }
            } else if (e instanceof MiniBoss miniBoss) {
                if (miniBoss.timeToFire(now)) {
                    double startX = miniBoss.getX() + miniBoss.getSizeX() / 2.0 - 5;
                    double startY = miniBoss.getY() + miniBoss.getSizeY();

                    double totalSpeed = 120.0; // Bay chậm 120 px/s
                    double radLeft = Math.toRadians(-20);
                    double radRight = Math.toRadians(20);

                    // Viên 1 (Giữa)
                    EnemyBullet b1 = new EnemyBullet(startX, startY, 0, totalSpeed, 15, "bullet_enemy_round_purple");
                    // Viên 2 (Trái)
                    EnemyBullet b2 = new EnemyBullet(startX, startY, totalSpeed * Math.sin(radLeft),
                            totalSpeed * Math.cos(radLeft), 15, "bullet_enemy_round_purple");
                    // Viên 3 (Phải)
                    EnemyBullet b3 = new EnemyBullet(startX, startY, totalSpeed * Math.sin(radRight),
                            totalSpeed * Math.cos(radRight), 15, "bullet_enemy_round_purple");

                    EnemyBullet[] bullets = { b1, b2, b3 };
                    for (EnemyBullet b : bullets) {
                        gameLayoutPane.getChildren().add(b.getView());
                        if (isDebug && b.getHitbox() != null) {
                            b.getHitbox().setFill(Color.rgb(255, 0, 0, 0.3));
                            b.getHitbox().setStroke(Color.YELLOW);
                            b.getHitbox().setStrokeWidth(2);
                            gameLayoutPane.getChildren().add(b.getHitbox());
                        }
                        ShooterEnemy.addBullet(b);
                    }
                }
            } else if (e instanceof TankerEnemy tankerEnemy) {
                if (tankerEnemy.timeToFire(now)) {
                    double startX = tankerEnemy.getX() + tankerEnemy.getSizeX() / 2.0 - 5;
                    double startY = tankerEnemy.getY() + tankerEnemy.getSizeY();
                    EnemyBullet eBullet = new EnemyBullet(startX, startY, 0, 120.0, 15, "bullet_enemy_round_purple");
                    gameLayoutPane.getChildren().add(eBullet.getView());
                    if (isDebug && eBullet.getHitbox() != null) {
                        eBullet.getHitbox().setFill(Color.rgb(255, 0, 0, 0.3));
                        eBullet.getHitbox().setStroke(Color.YELLOW);
                        eBullet.getHitbox().setStrokeWidth(2);
                        gameLayoutPane.getChildren().add(eBullet.getHitbox());
                    }
                    ShooterEnemy.addBullet(eBullet);
                }
            }
        }
        // kiểm tra và cập nhật cái chết của kẻ địch shooterEnemy
        if (shooterEnemy != null && !shooterEnemy.isAlive()) {
            gameLayoutPane.getChildren().remove(shooterEnemy.getCloseBox());
            // shooterEnemy cũng là 1 thành phần tham chiếu tới list enemies nên cũng được
            // cập nhật trạng thái isAlive
            lastShooterDeathTime = System.currentTimeMillis();
            shooterEnemy = null; // Reset tham chiếu về null sau khi bị xóa khỏi list (coi như không tồn tại)
        }

        // Cập nhật PowerUps
        Iterator<PowerUp> powerUpIter = powerUps.iterator();
        while (powerUpIter.hasNext()) {
            PowerUp p = powerUpIter.next();
            p.update();
            if (!p.isAlive()) {
                gameLayoutPane.getChildren().remove(p.getView());
                gameLayoutPane.getChildren().remove(p.getHitbox());
                powerUpIter.remove();
            }
        }

        // Xử lý va chạm
        handleCollisions();

        // Update HUD
        playScene.updateHUD(score, wave, goldCollected, player);

        com.nhom27.skyforce.entities.base.BossObject activeBoss = null;
        for (EnemyObject enemy : enemies) {
            if (enemy instanceof com.nhom27.skyforce.entities.base.BossObject boss && boss.isAlive()) {
                activeBoss = boss;
                break;
            }
        }
        playScene.updateBossHUD(activeBoss);

        // Check Game Over
        if (player != null && (!player.isAlive() || player.getHealth() <= 0)) {
            handleGameOver();
        }
    }

    private void spawnEnemyWave(long now) {
        if (levelStartTime == 0) {
            levelStartTime = System.currentTimeMillis();
        }
        double elapsedSec = (System.currentTimeMillis() - levelStartTime) / 1000.0;

        if (currentStageLevel == 1) {
            spawnLevel1Wave(now, elapsedSec);
        } else if (currentStageLevel == 2) {
            spawnLevel2Wave(now, elapsedSec);
        } else if (currentStageLevel == 3) {
            spawnLevel3Wave(now, elapsedSec);
        } else if (currentStageLevel == 4) {
            spawnLevel4Wave(now, elapsedSec);
        } else if (currentStageLevel == 5) {
            spawnLevel5Wave(now, elapsedSec);
        } else if (currentStageLevel == 6) {
            spawnLevel6Wave(now, elapsedSec);
        } else if (currentStageLevel == 7) {
            spawnLevel7Wave(now, elapsedSec);
        } else if (currentStageLevel == 8) {
            spawnLevel8Wave(now, elapsedSec);
        } else {
            spawnLevel9Wave(now, elapsedSec);
        }
    }

    private void spawnLevel1Wave(long now, double elapsedSec) {
        // 🎬 Giai đoạn 1: Chào sân (Giây 0 - 15)
        if (elapsedSec >= 3.0 && !spawned3s) {
            double spawnX = Main.WIDTH / 2.0 - NormalEnemy.sizeX / 2.0;
            NormalEnemy e1 = new NormalEnemy(spawnX, -NormalEnemy.sizeY, 60.0, false, false, false,
                    "enemy_normal_blue");
            spawnEnemy(e1);
            spawned3s = true;
        }

        if (elapsedSec >= 7.0 && !spawned7s) {
            NormalEnemy eLeft = new NormalEnemy(30.0, -NormalEnemy.sizeY, 80.0, false, false, false,
                    "enemy_normal_blue");
            NormalEnemy eRight = new NormalEnemy(Main.WIDTH - NormalEnemy.sizeX - 30.0, -NormalEnemy.sizeY, 80.0, false,
                    false, false, "enemy_normal_blue");
            spawnEnemy(eLeft);
            spawnEnemy(eRight);
            spawned7s = true;
        }

        if (elapsedSec >= 12.0 && !spawned12s) {
            double centerX = Main.WIDTH / 2.0 - NormalEnemy.sizeX / 2.0;
            NormalEnemy eV1 = new NormalEnemy(centerX, -NormalEnemy.sizeY, 85.0, false, false, false,
                    "enemy_normal_blue");
            NormalEnemy eV2 = new NormalEnemy(centerX - 80, -NormalEnemy.sizeY - 50, 85.0, false, false, false,
                    "enemy_normal_blue");
            NormalEnemy eV3 = new NormalEnemy(centerX + 80, -NormalEnemy.sizeY - 50, 85.0, false, false, false,
                    "enemy_normal_blue");
            spawnEnemy(eV1);
            spawnEnemy(eV2);
            spawnEnemy(eV3);
            spawned12s = true;
        }

        // 🎁 Giai đoạn 2: Trải nghiệm Sức mạnh (Giây 18 - 35)
        if (elapsedSec >= 18.0 && !spawned18s) {
            double spawnX = Main.WIDTH / 2.0 - NormalEnemy.sizeX / 2.0;
            NormalEnemy eRed = new NormalEnemy(spawnX, -NormalEnemy.sizeY, 90.0, false, false, true,
                    "enemy_normal_red");
            spawnEnemy(eRed);
            spawned18s = true;
        }

        if (elapsedSec >= 22.0 && elapsedSec <= 35.0) {
            if (now - lastFlyRainTime >= 2500) {
                int count = 2 + random.nextInt(2);
                for (int i = 0; i < count; i++) {
                    double spawnX = random.nextDouble() * (Main.WIDTH - NormalEnemy.sizeX);
                    NormalEnemy e = new NormalEnemy(spawnX, -NormalEnemy.sizeY - (i * 45), 110.0, false, false, false,
                            "enemy_normal_blue");
                    spawnEnemy(e);
                }
                lastFlyRainTime = now;
            }
        }

        // ⚠️ Giai đoạn 3: Cao trào & Kết thúc (Giây 40 - 60)
        if (elapsedSec >= 45.0 && elapsedSec < 48.0 && !warningTriggered) {
            playScene.showWarningBanner(true);
            AudioManager.getInstance().playSound("sfx_zap");
            warningTriggered = true;
        }

        if (elapsedSec >= 48.0 && !miniBossSpawned) {
            playScene.showWarningBanner(false);
            double spawnX = Main.WIDTH / 2.0 - MiniBoss.sizeX / 2.0;
            MiniBoss boss = new MiniBoss(spawnX, -MiniBoss.sizeY);
            spawnEnemy(boss);
            miniBossSpawned = true;
        }
    }

    private void spawnLevel2Wave(long now, double elapsedSec) {
        // 🎬 Giai đoạn 1: Khởi động & Ôn bài (Giây 0 - 15)
        if (elapsedSec >= 3.0 && !lvl2_spawned3s) {
            NormalEnemy eLeft = new NormalEnemy(40.0, -NormalEnemy.sizeY, 150.0, false, false, false,
                    "enemy_normal_blue");
            NormalEnemy eRight = new NormalEnemy(Main.WIDTH - NormalEnemy.sizeX - 40.0, -NormalEnemy.sizeY, 150.0,
                    false, false, false, "enemy_normal_blue");
            spawnEnemy(eLeft);
            spawnEnemy(eRight);
            lvl2_spawned3s = true;
        }

        if (elapsedSec >= 8.0 && !lvl2_spawned8s) {
            double centerX = Main.WIDTH / 2.0 - NormalEnemy.sizeX / 2.0;
            NormalEnemy eRow1 = new NormalEnemy(centerX - 100, -NormalEnemy.sizeY, 150.0, false, false, false,
                    "enemy_normal_blue");
            NormalEnemy eRow2 = new NormalEnemy(centerX, -NormalEnemy.sizeY, 150.0, false, false, false,
                    "enemy_normal_blue");
            NormalEnemy eRow3 = new NormalEnemy(centerX + 100, -NormalEnemy.sizeY, 150.0, false, false, false,
                    "enemy_normal_blue");
            spawnEnemy(eRow1);
            spawnEnemy(eRow2);
            spawnEnemy(eRow3);
            lvl2_spawned8s = true;
        }

        // ⚠️ Giai đoạn 2: Lần đầu chạm trán Sniper (Giây 15 - 30)
        if (elapsedSec >= 16.0 && !lvl2_spawned16s) {
            SniperEnemy s1 = new SniperEnemy(60.0, -SniperEnemy.sizeY, 150.0, 1500, "UP");
            s1.setBulletTexture("bullet_enemy_diamond_yellow");
            spawnEnemy(s1);
            lvl2_spawned16s = true;
        }

        if (elapsedSec >= 22.0 && !lvl2_spawned22s) {
            SniperEnemy sLeft = new SniperEnemy(60.0, -SniperEnemy.sizeY, 150.0, 1500, "LEFT");
            SniperEnemy sRight = new SniperEnemy(Main.WIDTH - SniperEnemy.sizeX - 60.0, -SniperEnemy.sizeY, 150.0, 1500,
                    "RIGHT");
            sLeft.setBulletTexture("bullet_enemy_diamond_yellow");
            sRight.setBulletTexture("bullet_enemy_diamond_yellow");
            spawnEnemy(sLeft);
            spawnEnemy(sRight);
            lvl2_spawned22s = true;
        }

        // ⚔️ Giai đoạn 3: Chiến trường hỗn loạn (Giây 35 - 55)
        if (elapsedSec >= 30.0 && !lvl2_spawned30s) {
            NormalEnemy eShieldRed = new NormalEnemy(Main.WIDTH / 2.0 - NormalEnemy.sizeX / 2.0, -NormalEnemy.sizeY,
                    120.0, false, false, true, "enemy_normal_red");
            spawnEnemy(eShieldRed);
            lvl2_spawned30s = true;
        }

        if (elapsedSec >= 35.0 && !lvl2_spawned35s) {
            double centerX = Main.WIDTH / 2.0 - NormalEnemy.sizeX / 2.0;
            NormalEnemy n1 = new NormalEnemy(centerX - 120, -NormalEnemy.sizeY, 100.0, false, false, false,
                    "enemy_normal_blue");
            NormalEnemy n2 = new NormalEnemy(centerX - 40, -NormalEnemy.sizeY, 100.0, false, false, false,
                    "enemy_normal_blue");
            NormalEnemy n3 = new NormalEnemy(centerX + 40, -NormalEnemy.sizeY, 100.0, false, false, false,
                    "enemy_normal_blue");
            NormalEnemy n4 = new NormalEnemy(centerX + 120, -NormalEnemy.sizeY, 100.0, false, false, false,
                    "enemy_normal_blue");
            SniperEnemy sBack = new SniperEnemy(centerX, -SniperEnemy.sizeY - 80, 80.0, 1500, "AUTO");
            sBack.setBulletTexture("bullet_enemy_diamond_yellow");
            spawnEnemy(n1);
            spawnEnemy(n2);
            spawnEnemy(n3);
            spawnEnemy(n4);
            spawnEnemy(sBack);
            lvl2_spawned35s = true;
        }

        if (elapsedSec >= 45.0 && elapsedSec <= 50.0) {
            if (now - lvl2_lastFlyRainTime >= 1500) {
                for (int i = 0; i < 3; i++) {
                    double spawnX = random.nextDouble() * (Main.WIDTH - NormalEnemy.sizeX);
                    NormalEnemy e = new NormalEnemy(spawnX, -NormalEnemy.sizeY - (i * 40), 160.0, false, false, false,
                            "enemy_normal_blue");
                    spawnEnemy(e);
                }
                lvl2_lastFlyRainTime = now;
            }
        }

        // 💀 Giai đoạn 4: Bài Thi Cuối Cấp (Giây 60 - 75)
        if (elapsedSec >= 60.0 && !lvl2_spawned60s) {
            double centerX = Main.WIDTH / 2.0 - SniperEnemy.sizeX / 2.0;
            SniperEnemy death1 = new SniperEnemy(centerX, -SniperEnemy.sizeY, 160.0, 1500, "AUTO");
            SniperEnemy death2 = new SniperEnemy(centerX - 90, -SniperEnemy.sizeY - 40, 140.0, 1500, "LEFT");
            SniperEnemy death3 = new SniperEnemy(centerX + 90, -SniperEnemy.sizeY - 40, 140.0, 1500, "RIGHT");
            death1.setBulletTexture("bullet_enemy_diamond_yellow");
            death2.setBulletTexture("bullet_enemy_diamond_yellow");
            death3.setBulletTexture("bullet_enemy_diamond_yellow");

            lvl2_deathSquad.clear();
            lvl2_deathSquad.add(death1);
            lvl2_deathSquad.add(death2);
            lvl2_deathSquad.add(death3);

            spawnEnemy(death1);
            spawnEnemy(death2);
            spawnEnemy(death3);
            lvl2_spawned60s = true;
        }

        if (lvl2_spawned60s && !isVictory) {
            boolean allSquadDead = lvl2_deathSquad.stream().allMatch(e -> !e.isAlive());
            if (allSquadDead || elapsedSec >= 75.0) {
                for (int c = 0; c < 15; c++) {
                    double dropX = Main.WIDTH / 2.0 + (random.nextDouble() - 0.5) * 160;
                    double dropY = Main.HEIGHT * 0.3 + (random.nextDouble() - 0.5) * 100;
                    CoinPowerUp coin = new CoinPowerUp(dropX, dropY);
                    powerUps.add(coin);
                    gameLayoutPane.getChildren().add(coin.getView());
                }
                handleVictory();
            }
        }
    }

    private void spawnLevel3Wave(long now, double elapsedSec) {
        // 🎬 Giai đoạn 1: Điệu nhảy của bầy ruồi (Giây 0 - 20)
        // 5s: 1 hàng dọc 5 con SwarmEnemy bám đuôi nhau (cách 0.2s) bay lượn sóng từ
        // bên Trái
        if (elapsedSec >= 5.0 && !lvl3_spawned5s) {
            if (lvl3_queue5s_count < 5) {
                if (now - lvl3_last5s_time >= 200) {
                    SwarmEnemy swarm = new SwarmEnemy(120.0, -SwarmEnemy.sizeY, 70.0, 0.02, 0);
                    spawnEnemy(swarm);
                    lvl3_queue5s_count++;
                    lvl3_last5s_time = now;
                }
            } else {
                lvl3_spawned5s = true;
            }
        }

        // 12s: 1 hàng dọc 5 con SwarmEnemy bám đuôi nhau bay lượn sóng từ bên Phải
        if (elapsedSec >= 12.0 && !lvl3_spawned12s) {
            if (lvl3_queue12s_count < 5) {
                if (now - lvl3_last12s_time >= 200) {
                    SwarmEnemy swarm = new SwarmEnemy(Main.WIDTH - 120.0, -SwarmEnemy.sizeY, 70.0, 0.02, Math.PI);
                    spawnEnemy(swarm);
                    lvl3_queue12s_count++;
                    lvl3_last12s_time = now;
                }
            } else {
                lvl3_spawned12s = true;
            }
        }

        // 18s: 1 con quái Đỏ mang ItemUpgrade bay thẳng chầm chậm ở giữa
        if (elapsedSec >= 18.0 && !lvl3_spawned18s) {
            NormalEnemy eUpgradeRed = new NormalEnemy(Main.WIDTH / 2.0 - NormalEnemy.sizeX / 2.0, -NormalEnemy.sizeY,
                    80.0, false, false, true, "enemy_normal_red");
            spawnEnemy(eUpgradeRed);
            lvl3_spawned18s = true;
        }

        // 🌪️ Giai đoạn 2: Cơn Lốc Màu Xanh (Giây 25 - 45)
        // 25s: 2 hàng SwarmEnemy (mỗi hàng 7 con) bay chéo đan hình chữ X
        if (elapsedSec >= 25.0 && !lvl3_spawned25s) {
            if (lvl3_queue25s_count < 7) {
                if (now - lvl3_last25s_time >= 160) {
                    SwarmEnemy leftDiag = new SwarmEnemy(30.0, -SwarmEnemy.sizeY, SwarmEnemy.TrajectoryType.DIAGONAL,
                            230.0, 160.0);
                    SwarmEnemy rightDiag = new SwarmEnemy(Main.WIDTH - 30.0, -SwarmEnemy.sizeY,
                            SwarmEnemy.TrajectoryType.DIAGONAL, 230.0, -160.0);
                    spawnEnemy(leftDiag);
                    spawnEnemy(rightDiag);
                    lvl3_queue25s_count++;
                    lvl3_last25s_time = now;
                }
            } else {
                lvl3_spawned25s = true;
            }
        }

        // 32s - 42s: Rải thảm ruồi muỗi túa ra liên tục từ khắp mép trên màn hình
        if (elapsedSec >= 32.0 && elapsedSec <= 42.0) {
            if (now - lvl3_lastRainTime >= 400) {
                double spawnX = random.nextDouble() * (Main.WIDTH - SwarmEnemy.sizeX);
                SwarmEnemy swarm = new SwarmEnemy(spawnX, -SwarmEnemy.sizeY, SwarmEnemy.TrajectoryType.STRAIGHT, 260.0,
                        0);
                spawnEnemy(swarm);
                lvl3_lastRainTime = now;
            }
        }

        // ⚠️ Giai đoạn 3: Chiến thuật nhiễu loạn (Giây 50 - 65)
        // 50s: 1 bầy SwarmEnemy lượn sóng ngang qua che tầm nhìn + 52s: 2 SniperEnemy
        // nấp sau lưng ngắm bắn
        if (elapsedSec >= 50.0 && !lvl3_spawned50s) {
            for (int i = 0; i < 8; i++) {
                SwarmEnemy swarm = new SwarmEnemy(80.0 + (i * 35), -SwarmEnemy.sizeY - (i * 25), 80.0, 0.02, 0);
                spawnEnemy(swarm);
            }

            SniperEnemy sLeft = new SniperEnemy(100.0, -SniperEnemy.sizeY - 100, 120.0, 1500, "AUTO");
            SniperEnemy sRight = new SniperEnemy(Main.WIDTH - 100.0 - SniperEnemy.sizeX, -SniperEnemy.sizeY - 100,
                    120.0, 1500, "AUTO");
            sLeft.setBulletTexture("bullet_enemy_diamond_yellow");
            sRight.setBulletTexture("bullet_enemy_diamond_yellow");
            spawnEnemy(sLeft);
            spawnEnemy(sRight);

            lvl3_spawned50s = true;
        }

        // 🎆 Giai đoạn 4: Mưa Sao Băng (Đại tiệc cuối màn - Giây 70 - 80)
        // 70s - 78s: 3 đội hình chữ V của SwarmEnemy (21 con) ập xuống cùng lúc
        if (elapsedSec >= 70.0 && !lvl3_spawned70s) {
            lvl3_finalWave.clear();
            double center = Main.WIDTH / 2.0;

            // Chữ V 1 (Giữa)
            createVFormation(center, -SwarmEnemy.sizeY, 7);
            // Chữ V 2 (Trái)
            createVFormation(center - 140, -SwarmEnemy.sizeY - 80, 7);
            // Chữ V 3 (Phải)
            createVFormation(center + 140, -SwarmEnemy.sizeY - 80, 7);

            lvl3_spawned70s = true;
        }

        // 80s: Hoàn thành Level 3
        if (lvl3_spawned70s && !isVictory) {
            boolean allWaveDead = lvl3_finalWave.stream().allMatch(e -> !e.isAlive());
            if (allWaveDead || elapsedSec >= 80.0) {
                // Rớt đại tiệc 20 đồng Vàng!
                for (int c = 0; c < 20; c++) {
                    double dropX = Main.WIDTH / 2.0 + (random.nextDouble() - 0.5) * 220;
                    double dropY = Main.HEIGHT * 0.35 + (random.nextDouble() - 0.5) * 120;
                    CoinPowerUp coin = new CoinPowerUp(dropX, dropY);
                    powerUps.add(coin);
                    gameLayoutPane.getChildren().add(coin.getView());
                }
                handleVictory();
            }
        }
    }

    private void createVFormation(double apexX, double apexY, int countPerV) {
        int half = countPerV / 2;
        for (int i = 0; i <= half; i++) {
            SwarmEnemy eCenterLeft = new SwarmEnemy(apexX - (i * 35), apexY - (i * 35),
                    SwarmEnemy.TrajectoryType.STRAIGHT, 230.0, 0);
            lvl3_finalWave.add(eCenterLeft);
            spawnEnemy(eCenterLeft);

            if (i > 0) {
                SwarmEnemy eCenterRight = new SwarmEnemy(apexX + (i * 35), apexY - (i * 35),
                        SwarmEnemy.TrajectoryType.STRAIGHT, 230.0, 0);
                lvl3_finalWave.add(eCenterRight);
                spawnEnemy(eCenterRight);
            }
        }
    }

    private void spawnLevel4Wave(long now, double elapsedSec) {
        // 🎬 Giai đoạn 1: Chạm trán Xe Tăng (Giây 0 - 20)
        // 5s: 1 con TankerEnemy to bự xuất hiện ở chính giữa
        if (elapsedSec >= 5.0 && !lvl4_spawned5s) {
            double centerX = Main.WIDTH / 2.0 - TankerEnemy.sizeX / 2.0;
            TankerEnemy t1 = new TankerEnemy(centerX, -TankerEnemy.sizeY, 60.0, true);
            spawnEnemy(t1);
            lvl4_spawned5s = true;
        }

        // 12s: 2 con TankerEnemy bay xuống ở 2 bên mép Trái và Phải
        if (elapsedSec >= 12.0 && !lvl4_spawned12s) {
            TankerEnemy tLeft = new TankerEnemy(30.0, -TankerEnemy.sizeY, 65.0, true);
            TankerEnemy tRight = new TankerEnemy(Main.WIDTH - TankerEnemy.sizeX - 30.0, -TankerEnemy.sizeY, 65.0, true);
            spawnEnemy(tLeft);
            spawnEnemy(tRight);
            lvl4_spawned12s = true;
        }

        // 18s: Quái đỏ bay qua rớt Item Nâng Cấp (PillPowerUp)
        if (elapsedSec >= 18.0 && !lvl4_spawned18s) {
            double centerX = Main.WIDTH / 2.0 - NormalEnemy.sizeX / 2.0;
            NormalEnemy eUpgradeRed = new NormalEnemy(centerX, -NormalEnemy.sizeY, 85.0, false, false, true,
                    "enemy_normal_red");
            spawnEnemy(eUpgradeRed);
            lvl4_spawned18s = true;
        }

        // 🛡️ Giai đoạn 2: Lá Chắn & Ngọn Giáo (Giây 25 - 45)
        // 25s: 1 TankerEnemy đi trước + 1 SniperEnemy nấp sau lưng (sau 1.5s)
        if (elapsedSec >= 25.0 && !lvl4_spawned25s) {
            double centerX = Main.WIDTH / 2.0 - TankerEnemy.sizeX / 2.0;
            TankerEnemy tShield = new TankerEnemy(centerX, -TankerEnemy.sizeY, 60.0, true);
            SniperEnemy sSpear = new SniperEnemy(centerX, -SniperEnemy.sizeY - 100, 140.0, 1500, "AUTO");
            sSpear.setBulletTexture("bullet_enemy_diamond_yellow");
            spawnEnemy(tShield);
            spawnEnemy(sSpear);
            lvl4_spawned25s = true;
        }

        // 35s: 2 TankerEnemy đi song song + 2 SniperEnemy nấp sau + 4 SwarmEnemy gây
        // nhiễu
        if (elapsedSec >= 35.0 && !lvl4_spawned35s) {
            double centerX = Main.WIDTH / 2.0;
            TankerEnemy t1 = new TankerEnemy(centerX - 110 - TankerEnemy.sizeX / 2.0, -TankerEnemy.sizeY, 60.0, true);
            TankerEnemy t2 = new TankerEnemy(centerX + 110 - TankerEnemy.sizeX / 2.0, -TankerEnemy.sizeY, 60.0, true);

            SniperEnemy s1 = new SniperEnemy(centerX - 110 - SniperEnemy.sizeX / 2.0, -SniperEnemy.sizeY - 80, 130.0,
                    1500, "LEFT");
            SniperEnemy s2 = new SniperEnemy(centerX + 110 - SniperEnemy.sizeX / 2.0, -SniperEnemy.sizeY - 80, 130.0,
                    1500, "RIGHT");
            s1.setBulletTexture("bullet_enemy_diamond_yellow");
            s2.setBulletTexture("bullet_enemy_diamond_yellow");

            spawnEnemy(t1);
            spawnEnemy(t2);
            spawnEnemy(s1);
            spawnEnemy(s2);

            for (int i = 0; i < 4; i++) {
                SwarmEnemy swarm = new SwarmEnemy(60.0 + (i * 90), -SwarmEnemy.sizeY - (i * 30),
                        SwarmEnemy.TrajectoryType.SINE_WAVE, 220.0, 0);
                spawnEnemy(swarm);
            }
            lvl4_spawned35s = true;
        }

        // 🧱 Giai đoạn 3: Bức Tường Tuyệt Vọng (Giây 55 - 70)
        // 55s: 3 TankerEnemy dàn hàng ngang như bức tường thép
        if (elapsedSec >= 55.0 && !lvl4_spawned55s) {
            lvl4_wallTankers.clear();
            double gap = (Main.WIDTH - (3 * TankerEnemy.sizeX)) / 4.0;
            TankerEnemy w1 = new TankerEnemy(gap, -TankerEnemy.sizeY, 55.0, true);
            TankerEnemy w2 = new TankerEnemy(gap * 2 + TankerEnemy.sizeX, -TankerEnemy.sizeY, 55.0, true);
            TankerEnemy w3 = new TankerEnemy(gap * 3 + TankerEnemy.sizeX * 2, -TankerEnemy.sizeY, 55.0, true);

            lvl4_wallTankers.add(w1);
            lvl4_wallTankers.add(w2);
            lvl4_wallTankers.add(w3);

            spawnEnemy(w1);
            spawnEnemy(w2);
            spawnEnemy(w3);
            lvl4_spawned55s = true;
        }

        // 60s: Bầy SwarmEnemy lượn sóng chui qua các khe hở
        if (elapsedSec >= 60.0 && !lvl4_spawned60s) {
            double gap = (Main.WIDTH - (3 * TankerEnemy.sizeX)) / 4.0;
            double slot1 = gap + TankerEnemy.sizeX / 2.0;
            double slot2 = gap * 2 + TankerEnemy.sizeX * 1.5;

            for (int i = 0; i < 3; i++) {
                SwarmEnemy sw1 = new SwarmEnemy(slot1, -SwarmEnemy.sizeY - (i * 40),
                        SwarmEnemy.TrajectoryType.SINE_WAVE, 240.0, 0);
                SwarmEnemy sw2 = new SwarmEnemy(slot2, -SwarmEnemy.sizeY - (i * 40),
                        SwarmEnemy.TrajectoryType.SINE_WAVE, 240.0, 0);
                spawnEnemy(sw1);
                spawnEnemy(sw2);
            }
            lvl4_spawned60s = true;
        }

        // 🎆 Giai đoạn 4: Dọn dẹp chiến trường (Giây 75 - 85)
        if (lvl4_spawned55s && !isVictory) {
            boolean wallDead = lvl4_wallTankers.stream().allMatch(e -> !e.isAlive());
            if (wallDead || elapsedSec >= 85.0) {
                // Rớt đại tiệc 25 đồng Vàng lớn!
                for (int c = 0; c < 25; c++) {
                    double dropX = Main.WIDTH / 2.0 + (random.nextDouble() - 0.5) * 240;
                    double dropY = Main.HEIGHT * 0.35 + (random.nextDouble() - 0.5) * 140;
                    CoinPowerUp coin = new CoinPowerUp(dropX, dropY);
                    powerUps.add(coin);
                    gameLayoutPane.getChildren().add(coin.getView());
                }
                handleVictory();
            }
        }
    }

    private void spawnLevel5Wave(long now, double elapsedSec) {
        // 🎬 Giai đoạn 1: Giao hàng tiếp tế (Giây 0 - 15)
        if (elapsedSec >= 3.0 && !lvl5_spawnedSwarm) {
            for (int i = 0; i < 4; i++) {
                SwarmEnemy s1 = new SwarmEnemy(80.0 + (i * 40), -SwarmEnemy.sizeY - (i * 30),
                        SwarmEnemy.TrajectoryType.SINE_WAVE, 240.0, 0);
                SwarmEnemy s2 = new SwarmEnemy(Main.WIDTH - 80.0 - (i * 40), -SwarmEnemy.sizeY - (i * 30),
                        SwarmEnemy.TrajectoryType.SINE_WAVE, 240.0, Math.PI);
                spawnEnemy(s1);
                spawnEnemy(s2);
            }
            lvl5_spawnedSwarm = true;
        }

        // 12s: 2 quái Đỏ rớt 1 ShieldPowerUp và 1 PillPowerUp 100%
        if (elapsedSec >= 10.0 && !lvl5_spawnedRedEnemies) {
            double centerX = Main.WIDTH / 2.0;
            NormalEnemy rShield = new NormalEnemy(centerX - 90, -NormalEnemy.sizeY, 90.0, false, false, true,
                    "enemy_normal_red");
            NormalEnemy rPill = new NormalEnemy(centerX + 90, -NormalEnemy.sizeY, 90.0, false, false, true,
                    "enemy_normal_red");
            spawnEnemy(rShield);
            spawnEnemy(rPill);
            lvl5_spawnedRedEnemies = true;
        }

        // 🚨 Giai đoạn 2: Cảnh Báo Đỏ (Giây 15 - 20)
        if (elapsedSec >= 15.0 && elapsedSec < 19.0 && !lvl5_warningTriggered) {
            playScene.showWarningBanner(true);
            AudioManager.getInstance().playSound("sfx_zap");
            lvl5_warningTriggered = true;
        }

        // 😈 Giai đoạn 3 & 4: Boss MidBoss Xuất Hiện (Giây 20+)
        if (elapsedSec >= 19.0 && !lvl5_bossSpawned) {
            playScene.showWarningBanner(false);
            double spawnX = Main.WIDTH / 2.0 - MidBoss.sizeX / 2.0;
            lvl5_midBoss = new MidBoss(spawnX, -MidBoss.sizeY);
            spawnEnemy(lvl5_midBoss);
            lvl5_bossSpawned = true;
        }

        // Xử lý đạn tấn công của MidBoss
        if (lvl5_midBoss != null && lvl5_midBoss.isAlive()) {
            if (lvl5_midBoss.timeToFire(now)) {
                double bossCenterX = lvl5_midBoss.getX() + lvl5_midBoss.getSizeX() / 2.0;
                double bossCenterY = lvl5_midBoss.getY() + lvl5_midBoss.getSizeY() / 2.0;

                if (!lvl5_midBoss.isPhase2()) {
                    // Phase 1: Mưa Đạn Tròn 360° (12 viên đạn tròn tím tỏa ra)
                    double totalSpeed = 120.0;
                    for (int i = 0; i < 12; i++) {
                        double angleDeg = i * (360.0 / 12.0);
                        double rad = Math.toRadians(angleDeg);
                        double vx = totalSpeed * Math.sin(rad);
                        double vy = totalSpeed * Math.cos(rad);
                        EnemyBullet b = new EnemyBullet(bossCenterX, bossCenterY, vx, vy, 15,
                                "bullet_enemy_round_purple");
                        gameLayoutPane.getChildren().add(b.getView());
                        ShooterEnemy.addBullet(b);
                    }
                } else {
                    // Phase 2: Tử Quang (2 Tia Laser 50 Dmg + 2 Đạn Tỉa Kim Cương 25 Dmg nhắm
                    // player)
                    vfxManager.spawnScreenEffect(true);
                    AudioManager.getInstance().playSound("sfx_zap");

                    // 2 Tia Laser chéo
                    EnemyBullet laser1 = new EnemyBullet(bossCenterX - 40, bossCenterY, -50.0, 380.0, 50,
                            "bullet_enemy_laser");
                    EnemyBullet laser2 = new EnemyBullet(bossCenterX + 40, bossCenterY, 50.0, 380.0, 50,
                            "bullet_enemy_laser");

                    // 2 Đạn Tỉa nhắm player
                    double targetedVx = 0;
                    double targetedVy = 350.0;
                    if (player != null && player.isAlive()) {
                        double dx = player.getX() - bossCenterX;
                        double dy = player.getY() - bossCenterY;
                        double dist = Math.hypot(dx, dy);
                        if (dist > 0) {
                            targetedVx = (dx / dist) * 350.0;
                            targetedVy = (dy / dist) * 350.0;
                        }
                    }
                    EnemyBullet diamond1 = new EnemyBullet(bossCenterX - 20, bossCenterY, targetedVx, targetedVy, 25,
                            "bullet_enemy_diamond_yellow");
                    EnemyBullet diamond2 = new EnemyBullet(bossCenterX + 20, bossCenterY, targetedVx, targetedVy, 25,
                            "bullet_enemy_diamond_yellow");

                    EnemyBullet[] phase2Bullets = { laser1, laser2, diamond1, diamond2 };
                    for (EnemyBullet b : phase2Bullets) {
                        gameLayoutPane.getChildren().add(b.getView());
                        ShooterEnemy.addBullet(b);
                    }
                }
            }
        }

        // 🎆 Giai đoạn 5: Vụ Nổ Lịch Sử khi Boss chết
        if (lvl5_bossSpawned && lvl5_midBoss != null && !lvl5_midBoss.isAlive() && !lvl5_victoryTriggered) {
            lvl5_victoryTriggered = true;
            vfxManager.spawnScreenEffect(false);

            // Vụ nổ liên hoàn
            for (int i = 0; i < 8; i++) {
                double expX = lvl5_midBoss.getX() + random.nextDouble() * lvl5_midBoss.getSizeX();
                double expY = lvl5_midBoss.getY() + random.nextDouble() * lvl5_midBoss.getSizeY();
                vfxManager.spawnExplosionSpriteSheet(expX, expY, 120, 120);
            }
            AudioManager.getInstance().playSound("sfx_explosion_enemy");

            // Rớt đại tiệc 30 đồng Vàng!
            for (int c = 0; c < 30; c++) {
                double dropX = Main.WIDTH / 2.0 + (random.nextDouble() - 0.5) * 260;
                double dropY = Main.HEIGHT * 0.35 + (random.nextDouble() - 0.5) * 150;
                CoinPowerUp coin = new CoinPowerUp(dropX, dropY);
                powerUps.add(coin);
                gameLayoutPane.getChildren().add(coin.getView());
            }

            handleVictory();
        }
    }

    private void spawnLevel6Wave(long now, double elapsedSec) {
        // 🎬 Giai đoạn 1: Hòn đá thử vàng (Giây 0 - 20)
        // 5s: 1 cục Asteroid khổng lồ rơi chầm chậm ở giữa (500 HP, Va chạm 100)
        if (elapsedSec >= 5.0 && !lvl6_spawned5s) {
            double centerX = Main.WIDTH / 2.0 - MeteorEnemy.sizeX / 2.0;
            MeteorEnemy mCenter = new MeteorEnemy(centerX, -MeteorEnemy.sizeY);
            spawnEnemy(mCenter);
            lvl6_spawned5s = true;
        }

        // 12s: 2 cục Asteroid rơi song song ở 2 bên tạo khe hẹp ở giữa + 3 SwarmEnemy
        // chui qua khe
        if (elapsedSec >= 12.0 && !lvl6_spawned12s) {
            MeteorEnemy mLeft = new MeteorEnemy(30.0, -MeteorEnemy.sizeY);
            MeteorEnemy mRight = new MeteorEnemy(Main.WIDTH - MeteorEnemy.sizeX - 30.0, -MeteorEnemy.sizeY);
            spawnEnemy(mLeft);
            spawnEnemy(mRight);

            double centerX = Main.WIDTH / 2.0;
            for (int i = 0; i < 3; i++) {
                SwarmEnemy swarm = new SwarmEnemy(centerX - SwarmEnemy.sizeX / 2.0, -SwarmEnemy.sizeY - (i * 45),
                        SwarmEnemy.TrajectoryType.STRAIGHT, 250.0, 0);
                spawnEnemy(swarm);
            }
            lvl6_spawned12s = true;
        }

        // 💣 Giai đoạn 2: Trái Bom Nổ Chậm (Giây 25 - 45)
        // 25s: 2 quả FloatingMine màu đỏ trôi lờ đờ xuống (bắn là nổ 8 đạn 360°)
        if (elapsedSec >= 25.0 && !lvl6_spawned25s) {
            double centerX = Main.WIDTH / 2.0;
            FloatingMine mine1 = new FloatingMine(centerX - 90, -FloatingMine.sizeY);
            FloatingMine mine2 = new FloatingMine(centerX + 90, -FloatingMine.sizeY);
            spawnEnemy(mine1);
            spawnEnemy(mine2);
            lvl6_spawned25s = true;
        }

        // 32s: Hàng ngang 4 quả FloatingMine + 2 SniperEnemy nấp sau lưng ngắm bắn
        if (elapsedSec >= 32.0 && !lvl6_spawned32s) {
            double gap = (Main.WIDTH - (4 * FloatingMine.sizeX)) / 5.0;
            for (int i = 0; i < 4; i++) {
                FloatingMine mine = new FloatingMine(gap + (i * (gap + FloatingMine.sizeX)), -FloatingMine.sizeY);
                spawnEnemy(mine);
            }

            SniperEnemy sLeft = new SniperEnemy(Main.WIDTH * 0.3, -SniperEnemy.sizeY - 100, 130.0, 1500, "AUTO");
            SniperEnemy sRight = new SniperEnemy(Main.WIDTH * 0.7 - SniperEnemy.sizeX, -SniperEnemy.sizeY - 100, 130.0,
                    1500, "AUTO");
            sLeft.setBulletTexture("bullet_enemy_diamond_yellow");
            sRight.setBulletTexture("bullet_enemy_diamond_yellow");
            spawnEnemy(sLeft);
            spawnEnemy(sRight);

            lvl6_spawned32s = true;
        }

        // 🗜️ Giai đoạn 3: Cỗ Máy Ép Chả (Giây 50 - 70)
        // 50s: Thiên thạch rơi liên tục ở 2 bên mép màn hình ép không gian
        if (elapsedSec >= 50.0 && elapsedSec <= 68.0) {
            if (now - lvl6_lastMeteorRainTime >= 1800) {
                MeteorEnemy mLeft = new MeteorEnemy(10.0, -MeteorEnemy.sizeY);
                MeteorEnemy mRight = new MeteorEnemy(Main.WIDTH - MeteorEnemy.sizeX - 10.0, -MeteorEnemy.sizeY);
                spawnEnemy(mLeft);
                spawnEnemy(mRight);
                lvl6_lastMeteorRainTime = now;
            }
        }

        // 58s: 1 TankerEnemy thả trôi giữa màn hình hẹp + bầy SwarmEnemy
        if (elapsedSec >= 58.0 && !lvl6_spawned58s) {
            double centerX = Main.WIDTH / 2.0 - TankerEnemy.sizeX / 2.0;
            TankerEnemy tCenter = new TankerEnemy(centerX, -TankerEnemy.sizeY, 60.0, true);
            spawnEnemy(tCenter);

            for (int i = 0; i < 4; i++) {
                SwarmEnemy swarm = new SwarmEnemy(centerX + TankerEnemy.sizeX / 2.0, -SwarmEnemy.sizeY - 80 - (i * 35),
                        SwarmEnemy.TrajectoryType.SINE_WAVE, 230.0, 0);
                spawnEnemy(swarm);
            }
            lvl6_spawned58s = true;
        }

        // ☄️ Giai đoạn 4: Mưa Sao Băng Sinh Tồn (Giây 75 - 85)
        if (elapsedSec >= 75.0 && !lvl6_spawned75s) {
            // Mưa thiên thạch đan xem kín nửa trên
            for (int i = 0; i < 6; i++) {
                double spawnX = 20.0 + i * ((Main.WIDTH - 40) / 5.0);
                MeteorEnemy m = new MeteorEnemy(spawnX, -MeteorEnemy.sizeY - (i % 2 * 60));
                spawnEnemy(m);
            }
            lvl6_spawned75s = true;
        }

        // 85s: Hoàn thành Level 6!
        if (lvl6_spawned75s && !isVictory) {
            if (elapsedSec >= 85.0) {
                // Rớt đại tiệc 25 đồng Vàng + 2 Item hỗ trợ
                for (int c = 0; c < 25; c++) {
                    double dropX = Main.WIDTH / 2.0 + (random.nextDouble() - 0.5) * 240;
                    double dropY = Main.HEIGHT * 0.35 + (random.nextDouble() - 0.5) * 140;
                    CoinPowerUp coin = new CoinPowerUp(dropX, dropY);
                    powerUps.add(coin);
                    gameLayoutPane.getChildren().add(coin.getView());
                }
                ShieldPowerUp sItem = new ShieldPowerUp(Main.WIDTH / 2.0 - 30, Main.HEIGHT * 0.3);
                powerUps.add(sItem);
                gameLayoutPane.getChildren().add(sItem.getView());

                handleVictory();
            }
        }
    }

    private void spawnLevel7Wave(long now, double elapsedSec) {
        // 🎬 Giai đoạn 1: Ảo giác bình yên (Giây 0 - 15)
        if (elapsedSec >= 3.0 && !lvl7_spawned5s) {
            for (int i = 0; i < 3; i++) {
                SwarmEnemy swarm = new SwarmEnemy(100.0 + (i * 120), -SwarmEnemy.sizeY - (i * 30),
                        SwarmEnemy.TrajectoryType.SINE_WAVE, 220.0, 0);
                spawnEnemy(swarm);
            }
            lvl7_spawned5s = true;
        }

        // 10s: Quái đỏ rớt 100% Item Upgrade Súng
        if (elapsedSec >= 10.0 && !lvl7_spawned10s) {
            double centerX = Main.WIDTH / 2.0 - NormalEnemy.sizeX / 2.0;
            NormalEnemy eUpgradeRed = new NormalEnemy(centerX, -NormalEnemy.sizeY, 80.0, false, false, true,
                    "enemy_normal_red");
            spawnEnemy(eUpgradeRed);
            lvl7_spawned10s = true;
        }

        // ⚠️ Giai đoạn 2: Lời cảnh báo từ vực thẳm (Giây 18 - 35)
        // 18s: Cảnh báo dấu chấm than đỏ ở mép dưới
        if (elapsedSec >= 18.0 && elapsedSec < 19.0 && !lvl7_warn18s) {
            playScene.showBottomWarning(true, Main.WIDTH / 2.0);
            AudioManager.getInstance().playSound("sfx_zap");
            lvl7_warn18s = true;
        }

        // 19s: 1 AmbushEnemy thốc ngược từ dưới lên!
        if (elapsedSec >= 19.0 && !lvl7_spawned19s) {
            playScene.showBottomWarning(false, 0);
            EliteEnemy ambush1 = new EliteEnemy(Main.WIDTH / 2.0 - EliteEnemy.sizeX / 2.0, Main.HEIGHT + 50);
            ambush1.setSpeedY(-350.0); // Bay ngược từ dưới lên cực nhanh
            ambush1.setPos(ambush1.getX(), ambush1.getY(), 0);
            spawnEnemy(ambush1);
            lvl7_spawned19s = true;
        }

        // 25s: Cảnh báo 3 dấu chấm than ở mép dưới
        if (elapsedSec >= 25.0 && elapsedSec < 26.0 && !lvl7_warn25s) {
            playScene.showBottomWarning(true, Main.WIDTH / 2.0);
            AudioManager.getInstance().playSound("sfx_zap");
            lvl7_warn25s = true;
        }

        // 26s: 3 con AmbushEnemy lao từ dưới lên ép người chơi lên giữa màn hình
        if (elapsedSec >= 26.0 && !lvl7_spawned26s) {
            playScene.showBottomWarning(false, 0);
            EliteEnemy a1 = new EliteEnemy(100.0, Main.HEIGHT + 50);
            EliteEnemy a2 = new EliteEnemy(Main.WIDTH / 2.0 - EliteEnemy.sizeX / 2.0, Main.HEIGHT + 50);
            EliteEnemy a3 = new EliteEnemy(Main.WIDTH - 100.0 - EliteEnemy.sizeX, Main.HEIGHT + 50);

            EliteEnemy[] ambushes = { a1, a2, a3 };
            for (EliteEnemy a : ambushes) {
                a.setSpeedY(-330.0);
                a.setPos(a.getX(), a.getY(), 0);
                spawnEnemy(a);
            }
            lvl7_spawned26s = true;
        }

        // 30s: Gọng kìm dọc (Trên: Swarm, Dưới: 2 Ambush)
        if (elapsedSec >= 30.0 && !lvl7_spawned30s) {
            for (int i = 0; i < 4; i++) {
                SwarmEnemy swarm = new SwarmEnemy(80.0 + (i * 90), -SwarmEnemy.sizeY - (i * 20),
                        SwarmEnemy.TrajectoryType.STRAIGHT, 240.0, 0);
                spawnEnemy(swarm);
            }

            EliteEnemy b1 = new EliteEnemy(Main.WIDTH * 0.3, Main.HEIGHT + 50);
            EliteEnemy b2 = new EliteEnemy(Main.WIDTH * 0.7 - EliteEnemy.sizeX, Main.HEIGHT + 50);
            b1.setSpeedY(-330.0);
            b1.setPos(b1.getX(), b1.getY(), 0);
            b2.setSpeedY(-330.0);
            b2.setPos(b2.getX(), b2.getY(), 0);
            spawnEnemy(b1);
            spawnEnemy(b2);

            lvl7_spawned30s = true;
        }

        // 🎯 Giai đoạn 3: Bắn Tỉa Ngang Hông & Thế trận Thập Tự (Giây 40 - 65)
        // 40s: 2 SniperEnemy thò ra từ 2 bên hông Trái & Phải nhắm bắn ngang
        if (elapsedSec >= 40.0 && !lvl7_spawned40s) {
            SniperEnemy sLeft = new SniperEnemy(-SniperEnemy.sizeX, Main.HEIGHT * 0.4, 150.0, 1500, "RIGHT");
            SniperEnemy sRight = new SniperEnemy(Main.WIDTH, Main.HEIGHT * 0.4, 150.0, 1500, "LEFT");
            sLeft.setBulletTexture("bullet_enemy_diamond_yellow");
            sRight.setBulletTexture("bullet_enemy_diamond_yellow");
            spawnEnemy(sLeft);
            spawnEnemy(sRight);
            lvl7_spawned40s = true;
        }

        // 50s - 60s: Thế trận Thập Tự Crossfire (Trái/Phải: SideSniper, Trên: Tanker,
        // Dưới: Ambush)
        if (elapsedSec >= 50.0 && !lvl7_warn50s) {
            playScene.showBottomWarning(true, Main.WIDTH / 2.0);
            AudioManager.getInstance().playSound("sfx_zap");
            lvl7_warn50s = true;
        }

        if (elapsedSec >= 51.0 && !lvl7_spawned51s) {
            playScene.showBottomWarning(false, 0);

            // Trên: Tanker
            double centerX = Main.WIDTH / 2.0 - TankerEnemy.sizeX / 2.0;
            TankerEnemy tTop = new TankerEnemy(centerX, -TankerEnemy.sizeY, 55.0, true);
            spawnEnemy(tTop);

            // Dưới: 2 Ambush
            EliteEnemy bot1 = new EliteEnemy(centerX - 100, Main.HEIGHT + 50);
            EliteEnemy bot2 = new EliteEnemy(centerX + 100, Main.HEIGHT + 50);
            bot1.setSpeedY(-320.0);
            bot1.setPos(bot1.getX(), bot1.getY(), 0);
            bot2.setSpeedY(-320.0);
            bot2.setPos(bot2.getX(), bot2.getY(), 0);
            spawnEnemy(bot1);
            spawnEnemy(bot2);

            // Hông Trái & Phải: 2 SideSniper
            SniperEnemy sideL = new SniperEnemy(-SniperEnemy.sizeX, Main.HEIGHT * 0.45, 140.0, 1500, "RIGHT");
            SniperEnemy sideR = new SniperEnemy(Main.WIDTH, Main.HEIGHT * 0.45, 140.0, 1500, "LEFT");
            sideL.setBulletTexture("bullet_enemy_diamond_yellow");
            sideR.setBulletTexture("bullet_enemy_diamond_yellow");
            spawnEnemy(sideL);
            spawnEnemy(sideR);

            lvl7_spawned51s = true;
        }

        // 🌪️ Giai đoạn 4: Vòng Vây Khép Kín (Giây 70 - 85)
        if (elapsedSec >= 70.0 && !lvl7_spawned70s) {
            // SwarmEnemy túa ra từ 4 hướng bay cắt chéo màn hình
            for (int i = 0; i < 4; i++) {
                // Trên xuống
                SwarmEnemy topS = new SwarmEnemy(60.0 + (i * 90), -SwarmEnemy.sizeY, SwarmEnemy.TrajectoryType.STRAIGHT,
                        250.0, 0);
                // Dưới lên
                SwarmEnemy botS = new SwarmEnemy(80.0 + (i * 90), Main.HEIGHT + SwarmEnemy.sizeY,
                        SwarmEnemy.TrajectoryType.DIAGONAL, -250.0, 0);
                // Trái sang
                SwarmEnemy leftS = new SwarmEnemy(-SwarmEnemy.sizeX, 100.0 + (i * 80),
                        SwarmEnemy.TrajectoryType.DIAGONAL, 120.0, 220.0);
                // Phải sang
                SwarmEnemy rightS = new SwarmEnemy(Main.WIDTH + SwarmEnemy.sizeX, 120.0 + (i * 80),
                        SwarmEnemy.TrajectoryType.DIAGONAL, 120.0, -220.0);

                spawnEnemy(topS);
                spawnEnemy(botS);
                spawnEnemy(leftS);
                spawnEnemy(rightS);
            }
            lvl7_spawned70s = true;
        }

        // 85s: Hoàn thành Level 7!
        if (lvl7_spawned70s && !isVictory) {
            if (elapsedSec >= 85.0) {
                for (int c = 0; c < 30; c++) {
                    double dropX = Main.WIDTH / 2.0 + (random.nextDouble() - 0.5) * 260;
                    double dropY = Main.HEIGHT * 0.35 + (random.nextDouble() - 0.5) * 140;
                    CoinPowerUp coin = new CoinPowerUp(dropX, dropY);
                    powerUps.add(coin);
                    gameLayoutPane.getChildren().add(coin.getView());
                }
                handleVictory();
            }
        }
    }

    private void spawnLevel8Wave(long now, double elapsedSec) {
        // 🎬 Giai đoạn 1: Lời chào nhuốm máu (Giây 0 - 20)
        // 5s: 1 con EliteEnemy bắn 3 viên chùm chéo
        if (elapsedSec >= 5.0 && !lvl8_spawned5s) {
            double centerX = Main.WIDTH / 2.0 - EliteEnemy.sizeX / 2.0;
            EliteEnemy elite1 = new EliteEnemy(centerX, -EliteEnemy.sizeY, 140.0);
            spawnEnemy(elite1);
            lvl8_spawned5s = true;
        }

        // 10s - 18s: 2 quái tiếp tế rớt Pill & Shield
        if (elapsedSec >= 10.0 && !lvl8_spawned10s) {
            double centerX = Main.WIDTH / 2.0;
            NormalEnemy rPill = new NormalEnemy(centerX - 80, -NormalEnemy.sizeY, 80.0, false, false, true,
                    "enemy_normal_red");
            NormalEnemy rShield = new NormalEnemy(centerX + 80, -NormalEnemy.sizeY, 80.0, false, false, true,
                    "enemy_normal_red");
            spawnEnemy(rPill);
            spawnEnemy(rShield);
            lvl8_spawned10s = true;
        }

        // 🎯 Giai đoạn 2: Lưới lửa liên thanh (Giây 25 - 45)
        // 25s: 2 EliteSniper xả liên thanh 3 viên (Burst 3)
        if (elapsedSec >= 25.0 && !lvl8_spawned25s) {
            double centerX = Main.WIDTH / 2.0;
            SniperEnemy burst1 = new SniperEnemy(centerX - 100, -SniperEnemy.sizeY, 140.0, 500, "AUTO");
            SniperEnemy burst2 = new SniperEnemy(centerX + 100, -SniperEnemy.sizeY, 140.0, 500, "AUTO");
            burst1.setBurstMode(true);
            burst2.setBurstMode(true);
            burst1.setBulletTexture("bullet_enemy_diamond_yellow");
            burst2.setBulletTexture("bullet_enemy_diamond_yellow");
            spawnEnemy(burst1);
            spawnEnemy(burst2);
            lvl8_spawned25s = true;
        }

        // 32s - 42s: Từng đàn EliteSwarm lượn sóng siêu nhanh (400 px/s) + văng Suicide
        // Bullet khi nổ
        if (elapsedSec >= 32.0 && !lvl8_spawned32s) {
            for (int i = 0; i < 6; i++) {
                SwarmEnemy sw1 = new SwarmEnemy(60.0 + (i * 45), -SwarmEnemy.sizeY - (i * 35),
                        SwarmEnemy.TrajectoryType.SINE_WAVE, 400.0, 0);
                SwarmEnemy sw2 = new SwarmEnemy(Main.WIDTH - 60.0 - (i * 45), -SwarmEnemy.sizeY - (i * 35),
                        SwarmEnemy.TrajectoryType.SINE_WAVE, 400.0, Math.PI);
                spawnEnemy(sw1);
                spawnEnemy(sw2);
            }
            lvl8_spawned32s = true;
        }

        // 🛡️ Giai đoạn 3: Pháo Đài Đỏ & Bullet Hell (Giây 50 - 70)
        // 50s: 2 con TankerEnemy dàn hàng ngang ép xuống xả đạn tỏa liên tục
        if (elapsedSec >= 50.0 && !lvl8_spawned50s) {
            double centerX = Main.WIDTH / 2.0;
            TankerEnemy t1 = new TankerEnemy(centerX - 120 - TankerEnemy.sizeX / 2.0, -TankerEnemy.sizeY, 60.0, true);
            TankerEnemy t2 = new TankerEnemy(centerX + 120 - TankerEnemy.sizeX / 2.0, -TankerEnemy.sizeY, 60.0, true);
            spawnEnemy(t1);
            spawnEnemy(t2);
            lvl8_spawned50s = true;
        }

        // 55s: 2 SideSniper burst 3 đạn liên thanh ngang màn hình
        if (elapsedSec >= 55.0 && !lvl8_spawned55s) {
            SniperEnemy sSideL = new SniperEnemy(-SniperEnemy.sizeX, Main.HEIGHT * 0.45, 140.0, 500, "RIGHT");
            SniperEnemy sSideR = new SniperEnemy(Main.WIDTH, Main.HEIGHT * 0.45, 140.0, 500, "LEFT");
            sSideL.setBurstMode(true);
            sSideR.setBurstMode(true);
            sSideL.setBulletTexture("bullet_enemy_diamond_yellow");
            sSideR.setBulletTexture("bullet_enemy_diamond_yellow");
            spawnEnemy(sSideL);
            spawnEnemy(sSideR);
            lvl8_spawned55s = true;
        }

        // 🎆 Giai đoạn 4: Điệu nhảy sinh tử (Giây 75 - 90)
        // 75s - 85s: Tổng tấn công 4 hướng
        if (elapsedSec >= 75.0 && !lvl8_spawned75s) {
            // EliteNormal thả đạn chùm từ trên
            EliteEnemy eTop1 = new EliteEnemy(Main.WIDTH * 0.3, -EliteEnemy.sizeY, 150.0);
            EliteEnemy eTop2 = new EliteEnemy(Main.WIDTH * 0.7 - EliteEnemy.sizeX, -EliteEnemy.sizeY, 150.0);
            spawnEnemy(eTop1);
            spawnEnemy(eTop2);

            // Ambush từ dưới lên
            EliteEnemy aBot1 = new EliteEnemy(Main.WIDTH * 0.2, Main.HEIGHT + 50);
            EliteEnemy aBot2 = new EliteEnemy(Main.WIDTH * 0.8 - EliteEnemy.sizeX, Main.HEIGHT + 50);
            aBot1.setSpeedY(-330.0);
            aBot1.setPos(aBot1.getX(), aBot1.getY(), 0);
            aBot2.setSpeedY(-330.0);
            aBot2.setPos(aBot2.getX(), aBot2.getY(), 0);
            spawnEnemy(aBot1);
            spawnEnemy(aBot2);

            // EliteSwarm lượn chéo
            for (int i = 0; i < 4; i++) {
                SwarmEnemy sw = new SwarmEnemy(100.0 + (i * 80), -SwarmEnemy.sizeY - (i * 30),
                        SwarmEnemy.TrajectoryType.DIAGONAL, 350.0, (i % 2 == 0 ? 150 : -150));
                spawnEnemy(sw);
            }
            lvl8_spawned75s = true;
        }

        // 90s: Hoàn thành Level 8!
        if (lvl8_spawned75s && !isVictory) {
            if (elapsedSec >= 90.0) {
                for (int c = 0; c < 35; c++) {
                    double dropX = Main.WIDTH / 2.0 + (random.nextDouble() - 0.5) * 280;
                    double dropY = Main.HEIGHT * 0.35 + (random.nextDouble() - 0.5) * 160;
                    CoinPowerUp coin = new CoinPowerUp(dropX, dropY);
                    powerUps.add(coin);
                    gameLayoutPane.getChildren().add(coin.getView());
                }
                handleVictory();
            }
        }
    }

    private void spawnLevel9Wave(long now, double elapsedSec) {
        // 🎬 Giai đoạn 1: Bữa ăn cuối cùng (Giây 0 - 15)
        // 5s - 10s: 3 chiếc máy bay tiếp tế rớt Máu, Khiên, Pill Upgrade 100%!
        if (elapsedSec >= 5.0 && !lvl9_spawned5s) {
            double centerX = Main.WIDTH / 2.0;
            NormalEnemy rHealth = new NormalEnemy(centerX - 120, -NormalEnemy.sizeY, 80.0, false, false, true,
                    "enemy_normal_red");
            NormalEnemy rShield = new NormalEnemy(centerX, -NormalEnemy.sizeY, 80.0, false, false, true,
                    "enemy_normal_red");
            NormalEnemy rPill = new NormalEnemy(centerX + 120, -NormalEnemy.sizeY, 80.0, false, false, true,
                    "enemy_normal_red");
            spawnEnemy(rHealth);
            spawnEnemy(rShield);
            spawnEnemy(rPill);
            lvl9_spawned5s = true;
        }

        // 15s: Tiếng còi báo động ré lên liên hồi!
        if (elapsedSec >= 15.0 && elapsedSec < 18.0 && !lvl9_warn15s) {
            playScene.showWarningBanner(true);
            AudioManager.getInstance().playSound("sfx_zap");
            lvl9_warn15s = true;
        }

        // ☄️ Giai đoạn 2: Lối đi hẹp (Giây 18 - 40)
        // 18s - 25s: Thiên thạch rơi liên tục tạo lối đi ngoằn ngoèo
        if (elapsedSec >= 18.0 && elapsedSec <= 38.0) {
            if (lvl9_warn15s) {
                playScene.showWarningBanner(false);
            }
            if (now - lvl9_lastMeteorTime >= 1500) {
                MeteorEnemy m1 = new MeteorEnemy(20.0, -MeteorEnemy.sizeY);
                MeteorEnemy m2 = new MeteorEnemy(Main.WIDTH - MeteorEnemy.sizeX - 20.0, -MeteorEnemy.sizeY);
                spawnEnemy(m1);
                spawnEnemy(m2);
                lvl9_lastMeteorTime = now;
            }
        }

        // 25s: Bầy EliteSwarm lướt lượn sóng qua khe hở (Ruồi tử thần - chết nhả
        // Suicide Bullet)
        if (elapsedSec >= 25.0 && !lvl9_spawned25s) {
            double centerX = Main.WIDTH / 2.0;
            for (int i = 0; i < 5; i++) {
                SwarmEnemy sw = new SwarmEnemy(centerX, -SwarmEnemy.sizeY - (i * 35),
                        SwarmEnemy.TrajectoryType.SINE_WAVE, 380.0, 0);
                spawnEnemy(sw);
            }
            lvl9_spawned25s = true;
        }

        // 🗜️ Giai đoạn 3: Máy Ép Tử Thần (Giây 45 - 65)
        // 45s: 3 EliteTanker dàn hàng ngang che 90% màn hình
        if (elapsedSec >= 45.0 && !lvl9_spawned45s) {
            double gap = (Main.WIDTH - (3 * TankerEnemy.sizeX)) / 4.0;
            TankerEnemy t1 = new TankerEnemy(gap, -TankerEnemy.sizeY, 55.0, true);
            TankerEnemy t2 = new TankerEnemy(gap * 2 + TankerEnemy.sizeX, -TankerEnemy.sizeY, 55.0, true);
            TankerEnemy t3 = new TankerEnemy(gap * 3 + TankerEnemy.sizeX * 2, -TankerEnemy.sizeY, 55.0, true);
            spawnEnemy(t1);
            spawnEnemy(t2);
            spawnEnemy(t3);
            lvl9_spawned45s = true;
        }

        // 48s: Cảnh báo 3 dấu chấm than đỏ mép dưới màn hình + 49s: 3 AmbushEnemy thốc
        // từ dưới lên!
        if (elapsedSec >= 48.0 && elapsedSec < 49.0 && !lvl9_warn48s) {
            playScene.showBottomWarning(true, Main.WIDTH / 2.0);
            AudioManager.getInstance().playSound("sfx_zap");
            lvl9_warn48s = true;
        }

        if (elapsedSec >= 49.0 && !lvl9_spawned49s) {
            playScene.showBottomWarning(false, 0);
            double gap = (Main.WIDTH - (3 * TankerEnemy.sizeX)) / 4.0;
            EliteEnemy a1 = new EliteEnemy(gap + TankerEnemy.sizeX / 2.0, Main.HEIGHT + 50);
            EliteEnemy a2 = new EliteEnemy(gap * 2 + TankerEnemy.sizeX * 1.5, Main.HEIGHT + 50);
            EliteEnemy a3 = new EliteEnemy(Main.WIDTH / 2.0 - EliteEnemy.sizeX / 2.0, Main.HEIGHT + 50);

            EliteEnemy[] ambushes = { a1, a2, a3 };
            for (EliteEnemy a : ambushes) {
                a.setSpeedY(-340.0);
                a.setPos(a.getX(), a.getY(), 0);
                spawnEnemy(a);
            }
            lvl9_spawned49s = true;
        }

        // 💣 Giai đoạn 4: Trại Mìn & Pháo Kích (Giây 70 - 85)
        // 70s: 6 quả FloatingMine rơi rải rác
        if (elapsedSec >= 70.0 && !lvl9_spawned70s) {
            for (int i = 0; i < 6; i++) {
                double mineX = 40.0 + i * ((Main.WIDTH - 80) / 5.0);
                FloatingMine mine = new FloatingMine(mineX, -FloatingMine.sizeY - (i % 2 * 50));
                spawnEnemy(mine);
            }
            lvl9_spawned70s = true;
        }

        // 73s: 4 EliteSniper thò ra từ 2 bên hông (2 Trái, 2 Phải), xả đạn Burst 3 liên
        // thanh hình chữ Thập
        if (elapsedSec >= 73.0 && !lvl9_spawned73s) {
            SniperEnemy sLeft1 = new SniperEnemy(-SniperEnemy.sizeX, Main.HEIGHT * 0.35, 140.0, 500, "RIGHT");
            SniperEnemy sLeft2 = new SniperEnemy(-SniperEnemy.sizeX, Main.HEIGHT * 0.65, 140.0, 500, "RIGHT");
            SniperEnemy sRight1 = new SniperEnemy(Main.WIDTH, Main.HEIGHT * 0.35, 140.0, 500, "LEFT");
            SniperEnemy sRight2 = new SniperEnemy(Main.WIDTH, Main.HEIGHT * 0.65, 140.0, 500, "LEFT");

            SniperEnemy[] sideSnipers = { sLeft1, sLeft2, sRight1, sRight2 };
            for (SniperEnemy s : sideSnipers) {
                s.setBurstMode(true);
                s.setBulletTexture("bullet_enemy_diamond_yellow");
                spawnEnemy(s);
            }
            lvl9_spawned73s = true;
        }

        // 🌅 Giai đoạn 5: Sống Sót (Giây 85 - 95)
        if (elapsedSec >= 85.0 && !lvl9_spawned85s) {
            lvl9_spawned85s = true;
        }

        if (lvl9_spawned85s && !isVictory) {
            if (elapsedSec >= 95.0) {
                for (int c = 0; c < 40; c++) {
                    double dropX = Main.WIDTH / 2.0 + (random.nextDouble() - 0.5) * 300;
                    double dropY = Main.HEIGHT * 0.35 + (random.nextDouble() - 0.5) * 180;
                    CoinPowerUp coin = new CoinPowerUp(dropX, dropY);
                    powerUps.add(coin);
                    gameLayoutPane.getChildren().add(coin.getView());
                }
                handleVictory();
            }
        }
    }

    private void spawnEnemy(EnemyObject newEnemy) {
        if (newEnemy != null) {
            enemies.add(newEnemy);

            if (newEnemy.getView() != null) {
                gameLayoutPane.getChildren().add(newEnemy.getView());
            }
            if (isDebug && newEnemy.getHitbox() != null) {
                newEnemy.getHitbox().setFill(Color.rgb(255, 0, 0, 0.3));
                newEnemy.getHitbox().setStroke(Color.YELLOW);
                newEnemy.getHitbox().setStrokeWidth(2);
                gameLayoutPane.getChildren().add(newEnemy.getHitbox());
            }
        }
    }

    // Xử lý va chạm
    private void handleCollisions() {
        // 1. Shooter Enemy và Player Bullet (kiểm tra đến gần để đổi hướng né)
        if (shooterEnemy != null && shooterEnemy.isAlive() && shooterEnemy.getCanDodge()) {
            double shortestVerticalDistance = 600;
            boolean bulletInRange = false;
            boolean closestBulletOnRight = true;
            for (Bullet bullet : player.getBullets()) {
                if (shooterEnemy.getCloseBox().getBoundsInParent().intersects(bullet.getHitbox().getBoundsInParent())) {
                    bulletInRange = true;
                    double V_distance = bullet.getY() - shooterEnemy.getY();
                    if (V_distance < shortestVerticalDistance && V_distance > 0) {
                        shortestVerticalDistance = V_distance;
                        if (bullet.getX() < shooterEnemy.getX()) { // đạn nằm bên trái. Rẽ phải
                            closestBulletOnRight = false;
                        } else {
                            closestBulletOnRight = true;
                        }
                    }
                }
            }
            if (bulletInRange) { // có phát hiện đạn thì mới nghĩ tới chuyện đổi hướng
                // nếu đã đi ra xa sẵn thì k cần đổi hướng
                if (closestBulletOnRight && !shooterEnemy.getDirection()
                        || !closestBulletOnRight && shooterEnemy.getDirection()) {
                } else {
                    if (closestBulletOnRight) {
                        shooterEnemy.setDirection(false);
                    } else {
                        shooterEnemy.setDirection(true);
                    }
                    shooterEnemy.lockDodge();
                    shooterEnemy.setDodgeCoolDown();
                }
            }
        }
        // 2. Player Bullets và Enemies
        for (Bullet bullet : player.getBullets()) {
            if (!bullet.isAlive())
                continue;

            for (EnemyObject enemy : enemies) {
                if (!enemy.isAlive())
                    continue;

                if (isColliding(bullet, enemy)) {
                    AudioManager.getInstance().playSound("sfx_laser_impact");
                    String skin = (player != null) ? player.getSkinId() : "blue";
                    vfxManager.spawnImpactEffect(bullet.getX() + bullet.getSizeX() / 2, bullet.getY(), skin);

                    bullet.setAlive(false);
                    enemy.takeDamage(bullet.getDamage());

                    if (!enemy.isAlive()) {
                        score += 5;
                        double scaleFactor = 4.0;
                        vfxManager.spawnExplosionSpriteSheet(enemy.getX() + enemy.getSizeX() / 2,
                                enemy.getY() + enemy.getSizeY() / 2,
                                enemy.getSizeX() * scaleFactor,
                                enemy.getSizeY() * scaleFactor);
                        AudioManager.getInstance().playSound("sfx_explosion_enemy");

                        if (enemy instanceof NormalEnemy normalEnemy && normalEnemy.isGuaranteedDrop()) {
                            PowerUp item;
                            if (currentStageLevel == 2) {
                                item = new ShieldPowerUp(enemy.getX(), enemy.getY());
                            } else {
                                item = new PillPowerUp(enemy.getX(), enemy.getY());
                            }
                            powerUps.add(item);
                            gameLayoutPane.getChildren().add(item.getView());
                        } else if (enemy instanceof MiniBoss) {
                            // MiniBoss chết: Rớt cơn mưa 15 đồng Vàng!
                            for (int c = 0; c < 15; c++) {
                                double dropX = enemy.getX() + (random.nextDouble() - 0.5) * 140;
                                double dropY = enemy.getY() + (random.nextDouble() - 0.5) * 100;
                                CoinPowerUp coin = new CoinPowerUp(dropX, dropY);
                                powerUps.add(coin);
                                gameLayoutPane.getChildren().add(coin.getView());
                            }
                            handleVictory();
                        } else if (random.nextDouble() < 0.3) {
                            spawnPowerUp(enemy.getX(), enemy.getY());
                        }
                    }
                    break;
                }
            }
        }

        if (player != null && player.isAlive()) {
            // 2. Enemies và Player
            for (EnemyObject enemy : enemies) {
                if (!enemy.isAlive())
                    continue;

                if (isColliding(enemy, player)) {
                    enemy.setAlive(false);
                    double scaleFactor = 3.0;
                    vfxManager.spawnExplosionSpriteSheet(enemy.getX() + enemy.getSizeX() / 2,
                            enemy.getY() + enemy.getSizeY() / 2,
                            enemy.getSizeX() * scaleFactor,
                            enemy.getSizeY() * scaleFactor);
                    AudioManager.getInstance().playSound("sfx_explosion_enemy");
                    player.takeDamage(enemy.getCollisionDamage());
                    vfxManager.applyPlayerGlow(player, "damaged");
                    vfxManager.spawnScreenEffect(false);
                }
            }
            // 3. Enemy Bullet và Player
            for (EnemyBullet eBullet : ShooterEnemy.getBulletList()) {
                if (!eBullet.isAlive()) {
                    continue;
                }
                if (isColliding(eBullet, player)) {
                    eBullet.setAlive(false);
                    player.takeDamage(eBullet.getDamage());
                    vfxManager.applyPlayerGlow(player, "damaged");
                    // vfxManager.spawnScreenEffect(false);
                }
            }

            // 3. PowerUps vs Player
            for (PowerUp powerUp : powerUps) {
                if (!powerUp.isAlive())
                    continue;

                if (isColliding(player, powerUp)) {
                    powerUp.setAlive(false);
                    powerUp.applyEffect(player, vfxManager);
                    if (powerUp instanceof CoinPowerUp coin) {
                        addGoldCollected(coin.getValue());
                    }
                }
            }
        }

    }

    private void spawnPowerUp(double x, double y) {
        int ramdomInt = random.nextInt(100);
        PowerUp powerUp;
        if (ramdomInt < 50) {
            powerUp = new CoinPowerUp(x, y);
        } else if (ramdomInt < 70) {
            powerUp = new PillPowerUp(x, y);
        } else if (ramdomInt < 85) {
            powerUp = new SeekerPowerUp(x, y);
        } else {
            powerUp = new ShieldPowerUp(x, y);
        }
        powerUps.add(powerUp);
        if (powerUp.getView() != null) {
            gameLayoutPane.getChildren().add(powerUp.getView());

            if (isDebug && powerUp.getHitbox() != null) {
                powerUp.getHitbox().setFill(Color.rgb(255, 0, 0, 0.3));
                powerUp.getHitbox().setStroke(Color.YELLOW);
                powerUp.getHitbox().setStrokeWidth(2);

                if (!gameLayoutPane.getChildren().contains(powerUp.getHitbox())) {
                    gameLayoutPane.getChildren().add(powerUp.getHitbox());
                }
            }
        }
    }

    // Xử lý logic va chạm 2 đối tượng
    private boolean isColliding(GameObject a, GameObject b) {
        if (a == null || b == null)
            return false;

        if (a.getHitbox() != null && b.getHitbox() != null) {
            // Kiểm tra khung bao ngoài đã va chạm chưa (giúp tối ưu hiệu năng của game)
            if (!a.getHitbox().getBoundsInParent().intersects(b.getHitbox().getBoundsInParent())) {
                return false; // Nếu khung bao ngoài chưa chạm thì chắc chắn không chạm, thoát luôn
            }
            Shape intersection = Shape.intersect(a.getHitbox(), b.getHitbox());
            return (intersection.getLayoutBounds().getWidth() > 0) && (intersection.getLayoutBounds().getHeight() > 0);
        } else if (a.getView() != null && b.getView() != null) {
            return a.getView().getBoundsInParent().intersects(b.getView().getBoundsInParent());
        }
        return false;
    }

    private void handleGameOver() {
        isGameOver = true;
        stopGame();
        PlayerDataManager.getInstance().addGold(goldCollected);
        PlayerDataManager.getInstance().checkAndUpdateHighScore(score);
        playScene.showGameOverMenu(score, goldCollected);
    }

    private void handleVictory() {
        if (isVictory)
            return;
        isVictory = true;

        score += 500;
        goldCollected += 100;

        stopGame();
        PlayerDataManager.getInstance().addGold(goldCollected);
        PlayerDataManager.getInstance().checkAndUpdateHighScore(score);

        if (playScene != null) {
            playScene.showWinMenu(score, goldCollected);
        }
    }

    public void addGoldCollected(int amount) {
        this.goldCollected += amount;
    }

    public int getGoldCollected() {
        return goldCollected;
    }

    // Lifecycle API Methods
    public void startGame() {
        if (gameLoop != null) {
            gameLoop.start();
        }
    }

    public void pauseGame() {
        isPaused = true;
    }

    public void resumeGame() {
        isPaused = false;
    }

    public void stopGame() {
        if (gameLoop != null) {
            gameLoop.stop();
        }
    }

    public void restartGame() {
        setupGame();
        startGame();
    }

    public boolean isPaused() {
        return isPaused;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public Player getPlayer() {
        return player;
    }

    public int getScore() {
        return score;
    }
}
