package com.nhom27.skyforce.managers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.nhom27.skyforce.audio.AudioManager;
import com.nhom27.skyforce.entities.base.BossObject;
import com.nhom27.skyforce.entities.base.EnemyObject;
import com.nhom27.skyforce.entities.base.GameObject;
import com.nhom27.skyforce.entities.enemies.*;
import com.nhom27.skyforce.entities.items.*;
import com.nhom27.skyforce.entities.obstacles.Asteroid;
import com.nhom27.skyforce.entities.player.Player;
import com.nhom27.skyforce.entities.weapons.Bullet;
import com.nhom27.skyforce.entities.weapons.EnemyBullet;
import com.nhom27.skyforce.main.Main;
import com.nhom27.skyforce.scenes.PlayScene;

import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;
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
    private List<EnemyBullet> enemyBullets;

    private AnimationTimer gameLoop;
    private Random random;

    private int score;
    private int goldCollected;
    private boolean isPaused;
    private boolean isGameOver;

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
    private boolean lvl1_miniBossDefeated = false;
    private long lvl1_bossDeathTime = 0;
    private boolean lvl1_magneticActive = false;

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
    private boolean lvl2_coinsSpawned = false;
    private long lvl2_clearTime = 0;
    private boolean lvl2_magneticActive = false;

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

    private boolean lvl3_spawned32s_meteors = false;
    private long lvl3_lastRainTime = 0;

    private boolean lvl3_spawned50s = false;
    private boolean lvl3_spawned52s = false;

    private boolean lvl3_spawned70s = false;
    private List<SwarmEnemy> lvl3_finalWave = new ArrayList<>();
    private boolean lvl3_coinsSpawned = false;
    private long lvl3_clearTime = 0;
    private boolean lvl3_magneticActive = false;

    // Quản lý kịch bản Level 4
    private boolean lvl4_spawned5s = false;
    private boolean lvl4_spawned12s = false;
    private boolean lvl4_spawned18s = false;
    private boolean lvl4_spawned25s = false;
    private boolean lvl4_spawned35s = false;
    private boolean lvl4_spawned55s = false;
    private boolean lvl4_spawned60s = false;
    private List<TankerEnemy> lvl4_wallTankers = new ArrayList<>();
    private boolean lvl4_coinsSpawned = false;
    private long lvl4_clearTime = 0;
    private boolean lvl4_magneticActive = false;

    // Quản lý kịch bản Level 5
    private boolean lvl5_spawnedSwarm = false;
    private boolean lvl5_spawnedRedEnemies = false;
    private boolean lvl5_warningTriggered = false;
    private boolean lvl5_bossSpawned = false;
    private MidBoss lvl5_midBoss = null;
    private boolean lvl5_victoryTriggered = false;
    private boolean lvl5_coinsSpawned = false;
    private long lvl5_clearTime = 0;
    private boolean lvl5_magneticActive = false;

    public int getCurrentStageLevel() {
        return currentStageLevel;
    }

    public void setCurrentStageLevel(int currentStageLevel) {
        this.currentStageLevel = currentStageLevel;
    }

    private Set<KeyCode> activeKeys = new HashSet<>();

    public void handleKeyPressed(javafx.scene.input.KeyCode code) {
        activeKeys.add(code);
    }

    public void handleKeyReleased(javafx.scene.input.KeyCode code) {
        activeKeys.remove(code);
    }

    private boolean isDebug = true;

    public GameManager(Pane gameLayoutPane, PlayScene playScene) {
        this(gameLayoutPane, playScene, 1);
    }

    public GameManager(Pane gameLayoutPane, PlayScene playScene, int currentStageLevel) {
        this.currentStageLevel = currentStageLevel;
        this.gameLayoutPane = gameLayoutPane;
        this.playScene = playScene;
        this.enemies = new ArrayList<>();
        this.powerUps = new ArrayList<>();
        this.enemyBullets = new ArrayList<>();
        this.random = new Random();
        setupGame();
    }

    public void setupGame() {
        // Dọn dẹp và Thiết lập lại trạng thái màn chơi
        gameLayoutPane.getChildren().clear();
        enemies.clear();
        powerUps.clear();
        enemyBullets.clear();

        score = 0;
        goldCollected = 0;
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
        lvl1_miniBossDefeated = false;
        lvl1_bossDeathTime = 0;
        lvl1_magneticActive = false;

        lvl2_spawned3s = false;
        lvl2_spawned8s = false;
        lvl2_spawned16s = false;
        lvl2_spawned22s = false;
        lvl2_spawned30s = false;
        lvl2_spawned35s = false;
        lvl2_lastFlyRainTime = 0;
        lvl2_spawned60s = false;
        lvl2_deathSquad.clear();
        lvl2_coinsSpawned = false;
        lvl2_clearTime = 0;
        lvl2_magneticActive = false;

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
        lvl3_spawned32s_meteors = false;
        lvl3_lastRainTime = 0;
        lvl3_spawned50s = false;
        lvl3_spawned52s = false;
        lvl3_spawned70s = false;
        lvl3_finalWave.clear();
        lvl3_coinsSpawned = false;
        lvl3_clearTime = 0;
        lvl3_magneticActive = false;

        lvl4_spawned5s = false;
        lvl4_spawned12s = false;
        lvl4_spawned18s = false;
        lvl4_spawned25s = false;
        lvl4_spawned35s = false;
        lvl4_spawned55s = false;
        lvl4_spawned60s = false;
        lvl4_wallTankers.clear();
        lvl4_coinsSpawned = false;
        lvl4_clearTime = 0;
        lvl4_magneticActive = false;

        lvl5_spawnedSwarm = false;
        lvl5_spawnedRedEnemies = false;
        lvl5_warningTriggered = false;
        lvl5_bossSpawned = false;
        lvl5_midBoss = null;
        lvl5_victoryTriggered = false;
        lvl5_coinsSpawned = false;
        lvl5_clearTime = 0;
        lvl5_magneticActive = false;

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
        // gameLayoutPane.setOnMouseMoved(e -> player.movePlayer(e.getX(), e.getY()));
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
    }

    private void updateGame(long now) {
        // Cập nhật logic người chơi
        if (player != null && player.isAlive()) {
            player.update();

            // Di chuyển bằng phím (300 pixels/giây)
            if (!activeKeys.isEmpty()) {
                double speed = 10.0;
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

        // Cập nhật đạn của kẻ địch
        Iterator<EnemyBullet> eBulletIter = enemyBullets.iterator();
        while (eBulletIter.hasNext()) {
            EnemyBullet eBullet = eBulletIter.next();
            eBullet.update();
            if (!eBullet.isAlive()) {
                if (eBullet.getView() != null) {
                    gameLayoutPane.getChildren().remove(eBullet.getView());
                }
                if (eBullet.getHitbox() != null) {
                    gameLayoutPane.getChildren().remove(eBullet.getHitbox());
                }
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
                    spawnEnemyBullet(sBullet);
                }
                gameLayoutPane.getChildren().remove(e.getView());
                if (e.getHitbox() != null) {
                    gameLayoutPane.getChildren().remove(e.getHitbox());
                }
                enemyIter.remove();
            } else if (e instanceof NormalEnemy normalEnemy) {
                if (normalEnemy.timeToFire(now)) {
                    double startX = normalEnemy.getX() + normalEnemy.getSizeX() / 2.0 - 5;
                    double startY = normalEnemy.getY() + normalEnemy.getSizeY();
                    EnemyBullet eBullet = new EnemyBullet(startX, startY, 0, 120.0, 15, "bullet_enemy_round_purple");
                    spawnEnemyBullet(eBullet);
                }
            } else if (e instanceof SniperEnemy sniperEnemy) {
                if (sniperEnemy.isReadyToFire()) {
                    double bulletSpeed = 350.0; // Bay cực nhanh 350 px/s
                    double speedX = sniperEnemy.getAimedDirX() * bulletSpeed;
                    double speedY = sniperEnemy.getAimedDirY() * bulletSpeed;
                    double startX = sniperEnemy.getX() + sniperEnemy.getSizeX() / 2.0;
                    double startY = sniperEnemy.getY() + sniperEnemy.getSizeY();
                    EnemyBullet eBullet = new EnemyBullet(startX, startY, speedX, speedY, 25, "bullet_enemy_laser");
                    spawnEnemyBullet(eBullet);
                }
            } else if (e instanceof MiniBoss miniBoss) {
                if (miniBoss.timeToFire(now)) {
                    double startX = miniBoss.getX() + miniBoss.getSizeX() / 2.0 - 5;
                    double startY = miniBoss.getY() + miniBoss.getSizeY();

                    double totalSpeed = 120.0; // Bay chậm 120 px/s
                    double radLeft = Math.toRadians(-20);
                    double radRight = Math.toRadians(20);

                    // Viên 1 (Giữa)
                    EnemyBullet b1 = new EnemyBullet(startX, startY, 0, totalSpeed, 15, "bullet_boss_mini_red");
                    // Viên 2 (Trái)
                    EnemyBullet b2 = new EnemyBullet(startX, startY, totalSpeed * Math.sin(radLeft),
                            totalSpeed * Math.cos(radLeft), 15, "bullet_boss_mini_red");
                    // Viên 3 (Phải)
                    EnemyBullet b3 = new EnemyBullet(startX, startY, totalSpeed * Math.sin(radRight),
                            totalSpeed * Math.cos(radRight), 15, "bullet_boss_mini_red");

                    EnemyBullet[] bullets = { b1, b2, b3 };
                    for (EnemyBullet b : bullets) {
                        spawnEnemyBullet(b);
                    }
                }
            } else if (e instanceof TankerEnemy tankerEnemy) {
                if (tankerEnemy.timeToFire(now)) {
                    double startX = tankerEnemy.getX() + tankerEnemy.getSizeX() / 2.0 - 5;
                    double startY = tankerEnemy.getY() + tankerEnemy.getSizeY();
                    EnemyBullet eBullet = new EnemyBullet(startX, startY, 0, 280.0, 15, "bullet_enemy_round_purple");
                    spawnEnemyBullet(eBullet);
                }
            }
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
        playScene.updateHUD(score, goldCollected, player);

        BossObject activeBoss = null;
        for (EnemyObject enemy : enemies) {
            if (enemy instanceof BossObject boss && boss.isAlive()) {
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
        }
    }

    private void spawnLevel1Wave(long now, double elapsedSec) {
        // 🎬 Giai đoạn 1: Chào sân (Giây 0 - 15)
        if (elapsedSec >= 3.0 && !spawned3s) {
            NormalEnemy e1 = new NormalEnemy(60.0, false, false, false,
                    "enemy_normal_blue");
            e1.setPos(Main.WIDTH / 2.0 - e1.getSizeX() / 2.0, -e1.getSizeY());
            spawnEnemy(e1);
            spawned3s = true;
        }

        if (elapsedSec >= 7.0 && !spawned7s) {
            NormalEnemy eLeft = new NormalEnemy(80.0, false, false, false,
                    "enemy_normal_blue");
            eLeft.setPos(0, -eLeft.getSizeY());
            NormalEnemy eRight = new NormalEnemy(80.0, false,
                    false, false, "enemy_normal_blue");
            eRight.setPos(Main.WIDTH - eRight.getSizeX(), -eRight.getSizeY());
            spawnEnemy(eLeft);
            spawnEnemy(eRight);
            spawned7s = true;
        }

        if (elapsedSec >= 12.0 && !spawned12s) {
            NormalEnemy eV1 = new NormalEnemy(85.0, false, false, false,
                    "enemy_normal_blue");
            eV1.setPos(Main.WIDTH / 2.0 - eV1.getSizeX() / 2.0, -eV1.getSizeY());

            NormalEnemy eV2 = new NormalEnemy(85.0, false, false, false,
                    "enemy_normal_blue");
            eV2.setPos(Main.WIDTH / 2.0 - eV2.getSizeX() / 2.0 - eV2.getSizeX(), -eV2.getSizeY() * 2);

            NormalEnemy eV3 = new NormalEnemy(85.0, false, false, false,
                    "enemy_normal_blue");
            eV3.setPos(Main.WIDTH / 2.0 - eV3.getSizeX() / 2.0 + eV3.getSizeX(), -eV3.getSizeY() * 2);
            spawnEnemy(eV1);
            spawnEnemy(eV2);
            spawnEnemy(eV3);
            spawned12s = true;
        }

        // 🎁 Giai đoạn 2: Trải nghiệm Sức mạnh (Giây 18 - 35)
        if (elapsedSec >= 18.0 && !spawned18s) {
            NormalEnemy eRed = new NormalEnemy(90.0, false, false, true,
                    "enemy_normal_red");
            eRed.setPos(Main.WIDTH / 2.0 - eRed.getSizeX() / 2.0, -eRed.getSizeY() * 2);
            spawnEnemy(eRed);
            spawned18s = true;
        }

        if (elapsedSec >= 22.0 && elapsedSec <= 35.0) {
            if (now - lastFlyRainTime >= 2500) {
                int count = 2 + random.nextInt(2);
                for (int i = 0; i < count; i++) {
                    NormalEnemy e = new NormalEnemy(110.0, false, false, false,
                            "enemy_normal_blue");
                    e.setPos(random.nextDouble() * (Main.WIDTH - e.getSizeX()), -e.getSizeY() - (i * e.getSizeY()));
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
            MiniBoss boss = new MiniBoss();
            boss.setPos(Main.WIDTH / 2.0 - boss.getSizeX() / 2.0, -boss.getSizeY());
            spawnEnemy(boss);
            miniBossSpawned = true;
        }

        // 🏆 Giai đoạn 4: Kết thúc Màn 1 (61s: Lực hút từ tính -> 62s: Bảng Win)
        if (lvl1_miniBossDefeated && !isVictory) {
            long elapsedSinceBossDeath = System.currentTimeMillis() - lvl1_bossDeathTime;
            // 61s: Lực hút từ tính kích hoạt sau 1s từ khi Mini-Boss nổ
            if (elapsedSinceBossDeath >= 1000 && !lvl1_magneticActive) {
                lvl1_magneticActive = true;
            }

            if (lvl1_magneticActive) {
                for (PowerUp p : powerUps) {
                    if (p instanceof CoinPowerUp coin && !coin.isMagnetized()) {
                        coin.setMagnetized(true, player);
                    }
                }
            }

            // 62s: Kết thúc - Sau 2s từ khi nổ hoặc khi toàn bộ vàng đã được hút hết
            boolean hasCoinsRemaining = powerUps.stream().anyMatch(p -> p instanceof CoinPowerUp && p.isAlive());
            if (!hasCoinsRemaining) {
                handleVictory();
            }
        }
    }

    private void spawnLevel2Wave(long now, double elapsedSec) {
        // 🎬 Giai đoạn 1: Khởi động & Ôn bài (Giây 0 - 15)
        if (elapsedSec >= 3.0 && !lvl2_spawned3s) {
            NormalEnemy eLeft = new NormalEnemy(150.0, false,
                    false, false,
                    "enemy_normal_blue");
            eLeft.setPos(0, -eLeft.getSizeY());
            NormalEnemy eRight = new NormalEnemy(150.0,
                    false, false, false, "enemy_normal_blue");
            eRight.setPos(Main.WIDTH - eRight.getSizeX(), -eRight.getSizeY());
            spawnEnemy(eLeft);
            spawnEnemy(eRight);
            lvl2_spawned3s = true;
        }

        if (elapsedSec >= 8.0 && !lvl2_spawned8s) {
            NormalEnemy eRow1 = new NormalEnemy(85.0, false, false, false,
                    "enemy_normal_blue");
            eRow1.setPos(Main.WIDTH / 2.0 - eRow1.getSizeX() / 2.0, -eRow1.getSizeY());

            NormalEnemy eRow2 = new NormalEnemy(85.0, false, false, false,
                    "enemy_normal_blue");
            eRow2.setPos(Main.WIDTH / 2.0 - eRow2.getSizeX() / 2.0 - eRow2.getSizeX(), -eRow2.getSizeY() * 2);

            NormalEnemy eRow3 = new NormalEnemy(85.0, false, false, false,
                    "enemy_normal_blue");
            eRow3.setPos(Main.WIDTH / 2.0 - eRow3.getSizeX() / 2.0 + eRow3.getSizeX(), -eRow3.getSizeY() * 2);
            spawnEnemy(eRow1);
            spawnEnemy(eRow2);
            spawnEnemy(eRow3);
            lvl2_spawned8s = true;
        }

        // ⚠️ Giai đoạn 2: Lần đầu chạm trán Sniper (Giây 15 - 30)
        if (elapsedSec >= 16.0 && !lvl2_spawned16s) {
            SniperEnemy s1 = new SniperEnemy(150.0, 1500,
                    "UP");
            spawnEnemy(s1);
            lvl2_spawned16s = true;
        }

        if (elapsedSec >= 22.0 && !lvl2_spawned22s) {
            SniperEnemy sLeft = new SniperEnemy(150.0, 1500,
                    "LEFT");
            sLeft.setPos(0, -sLeft.getSizeY());
            SniperEnemy sRight = new SniperEnemy(150.0, 1500,
                    "RIGHT");
            sRight.setPos(Main.WIDTH - sRight.getSizeX(), -sRight.getSizeY());
            spawnEnemy(sLeft);
            spawnEnemy(sRight);
            lvl2_spawned22s = true;
        }

        // ⚔️ Giai đoạn 3: Chiến trường hỗn loạn (Giây 35 - 55)
        if (elapsedSec >= 30.0 && !lvl2_spawned30s) {
            NormalEnemy eShieldRed = new NormalEnemy(120.0, false, false, true, "enemy_normal_red");
            eShieldRed.setPos(Main.WIDTH / 2.0 - eShieldRed.getSizeX(), -eShieldRed.getSizeY());
            spawnEnemy(eShieldRed);
            lvl2_spawned30s = true;
        }

        if (elapsedSec >= 35.0 && !lvl2_spawned35s) {
            NormalEnemy n1 = new NormalEnemy(100.0,
                    false, false, false,
                    "enemy_normal_blue");
            n1.setPos(Main.WIDTH / 2.0 - 2 * n1.getSizeX(), -n1.getSizeY());
            NormalEnemy n2 = new NormalEnemy(100.0,
                    false, false, false,
                    "enemy_normal_blue");
            n2.setPos(Main.WIDTH / 2.0 - n2.getSizeX(), -n2.getSizeY());
            NormalEnemy n3 = new NormalEnemy(100.0,
                    false, false, false,
                    "enemy_normal_blue");
            n3.setPos(Main.WIDTH / 2.0, -n3.getSizeY());
            NormalEnemy n4 = new NormalEnemy(100.0,
                    false, false, false,
                    "enemy_normal_blue");
            n4.setPos(Main.WIDTH / 2.0 + n4.getSizeX(), -n4.getSizeY());
            SniperEnemy sBack = new SniperEnemy(80.0,
                    1500, "AUTO");
            sBack.setPos(Main.WIDTH / 2.0 - sBack.getSizeX() / 2.0, -2 * sBack.getSizeY());
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
                    NormalEnemy e = new NormalEnemy(160.0,
                            false, false, false,
                            "enemy_normal_blue");
                    double spawnX = random.nextDouble() * (Main.WIDTH - e.getSizeX());
                    e.setPos(spawnX, -e.getSizeY() - (i * e.getSizeY()));
                    spawnEnemy(e);
                }
                lvl2_lastFlyRainTime = now;
            }
        }

        // 💀 Giai đoạn 4: Bài Thi Cuối Cấp (Giây 60 - 75)
        if (elapsedSec >= 60.0 && !lvl2_spawned60s) {
            SniperEnemy death1 = new SniperEnemy(160.0,
                    1500, "AUTO");
            death1.setPos(Main.WIDTH / 2.0 - death1.getSizeX() / 2.0, -death1.getSizeY());
            SniperEnemy death2 = new SniperEnemy(140.0, 1500, "LEFT");
            death2.setPos(Main.WIDTH / 2.0 - death2.getSizeX() / 2.0 - death2.getSizeX(), -2 * death2.getSizeY());
            SniperEnemy death3 = new SniperEnemy(140.0, 1500, "RIGHT");
            death3.setPos(Main.WIDTH / 2.0 - death3.getSizeX() / 2.0 + death3.getSizeX(), -2 * death3.getSizeY());

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
            if (allSquadDead) {
                if (!lvl2_coinsSpawned) {
                    for (int c = 0; c < 15; c++) {
                        double dropX = Main.WIDTH / 2.0 + (random.nextDouble() - 0.5) * 160;
                        double dropY = Main.HEIGHT * 0.3 + (random.nextDouble() - 0.5) * 100;
                        CoinPowerUp coin = new CoinPowerUp(dropX, dropY);
                        powerUps.add(coin);
                        gameLayoutPane.getChildren().add(coin.getView());
                    }
                    lvl2_coinsSpawned = true;
                    lvl2_clearTime = System.currentTimeMillis();
                }

                long elapsedSinceClear = System.currentTimeMillis() - lvl2_clearTime;
                if (elapsedSinceClear >= 500 && !lvl2_magneticActive) {
                    lvl2_magneticActive = true;
                }

                if (lvl2_magneticActive) {
                    for (PowerUp p : powerUps) {
                        if (p instanceof CoinPowerUp coin && !coin.isMagnetized()) {
                            coin.setMagnetized(true, player);
                        }
                    }
                }

                boolean hasCoinsRemaining = powerUps.stream().anyMatch(p -> p instanceof CoinPowerUp && p.isAlive());
                if (!hasCoinsRemaining) {
                    handleVictory();
                }
            }
        }
    }

    private void spawnLevel3Wave(long now, double elapsedSec) {
        // ==================================================
        // 🎬 GIAI ĐOẠN 1: ĐIỆU NHẢY CỦA BẦY RUỒI (Giây 0 - 20)
        // ==================================================

        // 5s: 1 hàng dọc 5 con SwarmEnemy bám đuôi nhau bay từ mép trên bên Trái xuống
        // theo hình lượn sóng (Sine wave)
        if (elapsedSec >= 5.0 && !lvl3_spawned5s) {
            if (lvl3_queue5s_count < 5) {
                if (now - lvl3_last5s_time >= 300) {
                    SwarmEnemy swarm = new SwarmEnemy(SwarmEnemy.TrajectoryType.SINE_WAVE, 180.0, 0, 60.0, 0.025, 0);
                    swarm.spawnAt(swarm.getSizeX(), -swarm.getSizeY());
                    spawnEnemy(swarm);
                    lvl3_queue5s_count++;
                    lvl3_last5s_time = now;
                }
            } else {
                lvl3_spawned5s = true;
            }
        }

        // 12s: 1 hàng dọc 5 con SwarmEnemy bám đuôi nhau lượn sóng từ mép Phải sang
        if (elapsedSec >= 12.0 && !lvl3_spawned12s) {
            if (lvl3_queue12s_count < 5) {
                if (now - lvl3_last12s_time >= 300) {
                    SwarmEnemy swarm = new SwarmEnemy(SwarmEnemy.TrajectoryType.SINE_WAVE, 180.0, 0, 60.0, 0.025,
                            Math.PI);
                    swarm.spawnAt(Main.WIDTH - 120, -swarm.getSizeY());
                    spawnEnemy(swarm);
                    lvl3_queue12s_count++;
                    lvl3_last12s_time = now;
                }
            } else {
                lvl3_spawned12s = true;
            }
        }

        // 18s: 1 con quái Đỏ mang ItemUpgrade (PillPowerUp nâng cấp súng Cấp 2)
        if (elapsedSec >= 18.0 && !lvl3_spawned18s) {
            NormalEnemy eUpgradeRed = new NormalEnemy(150, false, false, true,
                    "enemy_normal_red");
            eUpgradeRed.setPos(Main.WIDTH / 2.0 - eUpgradeRed.getSizeX() / 2.0,
                    -eUpgradeRed.getSizeY());
            spawnEnemy(eUpgradeRed);
            lvl3_spawned18s = true;
        }

        // ==================================================
        // ☄️ GIAI ĐOẠN 2: CHƯỚNG NGẠI VẬT TỪ KHÔNG GIAN (Giây 25 - 45)
        // ==================================================

        // 25s: 1 Cục Thiên Thạch (Asteroid) khổng lồ trôi chầm chậm ở ngay giữa màn
        // hình
        // Cùng lúc đó, 2 hàng SwarmEnemy (mỗi hàng 7 con) bay chéo hình chữ X đan vào
        // nhau
        if (elapsedSec >= 25.0 && !lvl3_spawned25s) {
            if (lvl3_queue25s_count == 0) {
                Asteroid centerAsteroid = new Asteroid();
                double centerX = Main.WIDTH / 2.0 - centerAsteroid.getSizeX() / 2.0;
                centerAsteroid.setPos(centerX, -centerAsteroid.getSizeY());
                spawnEnemy(centerAsteroid);
            }

            if (lvl3_queue25s_count < 7) {
                if (now - lvl3_last25s_time >= 300) {
                    SwarmEnemy leftDiag = new SwarmEnemy(SwarmEnemy.TrajectoryType.DIAGONAL,
                            230.0, 160.0);
                    leftDiag.spawnAt(0, -leftDiag.getSizeY());

                    SwarmEnemy rightDiag = new SwarmEnemy(SwarmEnemy.TrajectoryType.DIAGONAL,
                            230.0, -160.0);
                    rightDiag.spawnAt(Main.WIDTH - rightDiag.getSizeX(), -rightDiag.getSizeY());

                    spawnEnemy(leftDiag);
                    spawnEnemy(rightDiag);
                    lvl3_queue25s_count++;
                    lvl3_last25s_time = now;
                }
            } else {
                lvl3_spawned25s = true;
            }
        }

        // 32s - 42s: 3 cục Thiên thạch trôi xuống tạo thành 2 "khe hẹp" trên màn hình.
        // Quái SwarmEnemy túa ra liên tục, trôi qua các khe hẹp này như nước chảy qua
        // khe đá.
        if (elapsedSec >= 32.0 && !lvl3_spawned32s_meteors) {
            Asteroid aLeft = new Asteroid();
            aLeft.setPos(0.0, -aLeft.getSizeY());

            Asteroid aCenter = new Asteroid();
            aCenter.setPos(Main.WIDTH / 2.0 - aCenter.getSizeX() / 2.0,
                    -aCenter.getSizeY() * 2);

            Asteroid aRight = new Asteroid();
            aRight.setPos(Main.WIDTH - aRight.getSizeX(), -aRight.getSizeY());

            spawnEnemy(aLeft);
            spawnEnemy(aCenter);
            spawnEnemy(aRight);
            lvl3_spawned32s_meteors = true;
        }

        if (elapsedSec >= 32.0 && elapsedSec <= 42.0) {
            if (now - lvl3_lastRainTime >= 450) {
                double gap1 = Main.WIDTH * 0.28;
                double gap2 = Main.WIDTH * 0.72;
                double spawnX = (random.nextBoolean()) ? gap1 : gap2;
                SwarmEnemy swarm = new SwarmEnemy(260.0,
                        0);
                swarm.spawnAt(spawnX, -swarm.getSizeY());
                spawnEnemy(swarm);
                lvl3_lastRainTime = now;
            }
        }

        // ==================================================
        // ⚠️ GIAI ĐOẠN 3: CHIẾN THUẬT NHIỄU LOẠN (Giây 50 - 65)
        // ==================================================

        // 50s: 2 Thiên thạch trôi xuống ở rìa Trái và rìa Phải + 1 đàn SwarmEnemy bay
        // ngang làm nhiễu loạn
        if (elapsedSec >= 50.0 && !lvl3_spawned50s) {
            Asteroid aLeft = new Asteroid();
            aLeft.setPos(aLeft.getSizeX(), -aLeft.getSizeY());

            Asteroid aRight = new Asteroid();
            aRight.setPos(Main.WIDTH - 2 * aRight.getSizeX(), -aRight.getSizeY());

            spawnEnemy(aLeft);
            spawnEnemy(aRight);

            for (int i = 0; i < 8; i++) {
                SwarmEnemy swarm = new SwarmEnemy();
                swarm.spawnAt(80.0 + (i * 35), -swarm.getSizeY() - (i * 25));
                spawnEnemy(swarm);
            }
            lvl3_spawned50s = true;
        }

        // 52s: 2 con SniperEnemy bay xuống và đứng nấp ngay sau lưng 2 cục Thiên thạch
        if (elapsedSec >= 52.0 && !lvl3_spawned52s) {
            SniperEnemy sLeft = new SniperEnemy(150.0, 1000, "AUTO");
            sLeft.setPos(sLeft.getSizeX(), -sLeft.getSizeY() * 2);

            SniperEnemy sRight = new SniperEnemy(150.0, 1000, "AUTO");
            sRight.setPos(Main.WIDTH - 2 * sRight.getSizeX(), -sRight.getSizeY() * 2);

            spawnEnemy(sLeft);
            spawnEnemy(sRight);
            lvl3_spawned52s = true;
        }

        // ==================================================
        // 🎆 GIAI ĐOẠN 4: MƯA SAO BĂNG (SURVIVAL WAVE) (Giây 70 - 80)
        // ==================================================

        // 70s - 78s: 5-6 cục Thiên thạch nhỏ trôi lác đác + 3 đội hình chữ V của
        // SwarmEnemy (21 con)
        if (elapsedSec >= 70.0 && !lvl3_spawned70s) {
            lvl3_finalWave.clear();

            for (int i = 0; i < 5; i++) {
                Asteroid a = new Asteroid();
                double meteorX = 30.0 + i * ((Main.WIDTH - 60 - a.getSizeX()) / 4.0);
                a.setPos(meteorX, -a.getSizeY() - (i % 2 * 60));
                spawnEnemy(a);
            }

            double center = Main.WIDTH / 2.0;
            createVFormation(center, 7);
            createVFormation(center - center / 2.0, 7);
            createVFormation(center + center / 2.0, 7);

            lvl3_spawned70s = true;
        }

        // 80s: Hoàn thành Level 3
        if (lvl3_spawned70s && !isVictory) {
            boolean allWaveDead = lvl3_finalWave.stream().allMatch(e -> !e.isAlive());
            if (allWaveDead || elapsedSec >= 80.0) {
                if (!lvl3_coinsSpawned) {
                    // Rớt đại tiệc 25 đồng Vàng!
                    for (int c = 0; c < 25; c++) {
                        double dropX = Main.WIDTH / 2.0 + (random.nextDouble() - 0.5) * 220;
                        double dropY = Main.HEIGHT * 0.35 + (random.nextDouble() - 0.5) * 120;
                        CoinPowerUp coin = new CoinPowerUp(dropX, dropY);
                        powerUps.add(coin);
                        gameLayoutPane.getChildren().add(coin.getView());
                    }
                    lvl3_coinsSpawned = true;
                    lvl3_clearTime = System.currentTimeMillis();
                }

                long elapsedSinceClear = System.currentTimeMillis() - lvl3_clearTime;
                if (elapsedSinceClear >= 500 && !lvl3_magneticActive) {
                    lvl3_magneticActive = true;
                }

                if (lvl3_magneticActive) {
                    for (PowerUp p : powerUps) {
                        if (p instanceof CoinPowerUp coin && !coin.isMagnetized()) {
                            coin.setMagnetized(true, player);
                        }
                    }
                }

                boolean hasCoinsRemaining = powerUps.stream().anyMatch(p -> p instanceof CoinPowerUp && p.isAlive());
                if (!hasCoinsRemaining) {
                    handleVictory();
                }
            }
        }
    }

    private void createVFormation(double apexX, int countPerV) {
        int half = countPerV / 2;
        for (int i = 0; i <= half; i++) {
            SwarmEnemy eCenterLeft = new SwarmEnemy(230.0, 0);
            eCenterLeft.spawnAt(apexX - (i * eCenterLeft.getSizeX()),
                    -eCenterLeft.getSizeY() - (i * eCenterLeft.getSizeY()));
            lvl3_finalWave.add(eCenterLeft);
            spawnEnemy(eCenterLeft);

            if (i > 0) {
                SwarmEnemy eCenterRight = new SwarmEnemy(230.0, 0);
                eCenterRight.spawnAt(apexX + (i * eCenterRight.getSizeX()),
                        -eCenterRight.getSizeY() - (i * eCenterRight.getSizeY()));
                lvl3_finalWave.add(eCenterRight);
                spawnEnemy(eCenterRight);
            }
        }
    }

    private void spawnLevel4Wave(long now, double elapsedSec) {
        // 🎬 Giai đoạn 1: Chạm trán Xe Tăng (Giây 0 - 20)
        // 5s: 1 con TankerEnemy to bự xuất hiện ở chính giữa
        if (elapsedSec >= 5.0 && !lvl4_spawned5s) {
            TankerEnemy t1 = new TankerEnemy(60.0, true);
            t1.setPos(Main.WIDTH / 2.0 - t1.getSizeX() / 2.0, -t1.getSizeY());
            spawnEnemy(t1);
            lvl4_spawned5s = true;
        }

        // 12s: 2 con TankerEnemy bay xuống ở 2 bên mép Trái và Phải
        if (elapsedSec >= 12.0 && !lvl4_spawned12s) {
            TankerEnemy tLeft = new TankerEnemy(65.0, true);
            tLeft.setPos(tLeft.getSizeX(), -tLeft.getSizeY());

            TankerEnemy tRight = new TankerEnemy(65.0, true);
            tRight.setPos(Main.WIDTH - 2 * tRight.getSizeX(), -tRight.getSizeY());

            spawnEnemy(tLeft);
            spawnEnemy(tRight);
            lvl4_spawned12s = true;
        }

        // 18s: Quái đỏ bay qua rớt Item Nâng Cấp (PillPowerUp)
        if (elapsedSec >= 18.0 && !lvl4_spawned18s) {
            NormalEnemy eUpgradeRed = new NormalEnemy(85.0, false, false, true, "enemy_normal_red");
            eUpgradeRed.setPos(Main.WIDTH / 2.0 - eUpgradeRed.getSizeX() / 2.0, -eUpgradeRed.getSizeY());
            spawnEnemy(eUpgradeRed);
            lvl4_spawned18s = true;
        }

        // 🛡️ Giai đoạn 2: Lá Chắn & Ngọn Giáo (Giây 25 - 45)
        // 25s: 1 TankerEnemy đi trước + 1 SniperEnemy nấp sau lưng (bắn 3 đạn)
        if (elapsedSec >= 25.0 && !lvl4_spawned25s) {
            TankerEnemy tShield = new TankerEnemy(60.0, true);
            tShield.setPos(Main.WIDTH / 2.0 - tShield.getSizeX() / 2.0, -tShield.getSizeY());

            SniperEnemy sSpear = new SniperEnemy(50.0, 1500, "AUTO");
            sSpear.setSpeedY(60.0);
            sSpear.setBurstMode(true);
            sSpear.setPos(Main.WIDTH / 2.0 - sSpear.getSizeX() / 2.0, -sSpear.getSizeY() - 100);

            spawnEnemy(tShield);
            spawnEnemy(sSpear);
            lvl4_spawned25s = true;
        }

        // 35s: 2 TankerEnemy đi song song + 2 SniperEnemy nấp sau (bắn 3 đạn) + 4
        // SwarmEnemy gây nhiễu
        if (elapsedSec >= 35.0 && !lvl4_spawned35s) {
            double centerX = Main.WIDTH / 2.0;

            TankerEnemy t1 = new TankerEnemy(60.0, true);
            t1.setPos(centerX - 110 - t1.getSizeX() / 2.0, -t1.getSizeY());

            TankerEnemy t2 = new TankerEnemy(60.0, true);
            t2.setPos(centerX + 110 - t2.getSizeX() / 2.0, -t2.getSizeY());

            SniperEnemy s1 = new SniperEnemy(50.0, 1500, "LEFT");
            s1.setSpeedY(60.0);
            s1.setBurstMode(true);
            s1.setPos(centerX - 110 - s1.getSizeX() / 2.0, -s1.getSizeY() - 80);

            SniperEnemy s2 = new SniperEnemy(50.0, 1500, "RIGHT");
            s2.setSpeedY(60.0);
            s2.setBurstMode(true);
            s2.setPos(centerX + 110 - s2.getSizeX() / 2.0, -s2.getSizeY() - 80);

            spawnEnemy(t1);
            spawnEnemy(t2);
            spawnEnemy(s1);
            spawnEnemy(s2);

            for (int i = 0; i < 4; i++) {
                SwarmEnemy swarm = new SwarmEnemy(SwarmEnemy.TrajectoryType.SINE_WAVE, 220.0, 0);
                swarm.spawnAt(60.0 + (i * 90), -swarm.getSizeY() - (i * 30));
                spawnEnemy(swarm);
            }
            lvl4_spawned35s = true;
        }

        // 🧱 Giai đoạn 3: Bức Tường Tuyệt Vọng (Giây 55 - 70)
        // 55s: 3 TankerEnemy dàn hàng ngang như bức tường thép
        if (elapsedSec >= 55.0 && !lvl4_spawned55s) {
            lvl4_wallTankers.clear();

            TankerEnemy sample = new TankerEnemy(55.0, true);
            double gap = (Main.WIDTH - (3 * sample.getSizeX())) / 4.0;

            TankerEnemy w1 = new TankerEnemy(55.0, true);
            w1.setPos(gap, -w1.getSizeY());

            TankerEnemy w2 = new TankerEnemy(55.0, true);
            w2.setPos(gap * 2 + w2.getSizeX(), -w2.getSizeY());

            TankerEnemy w3 = new TankerEnemy(55.0, true);
            w3.setPos(gap * 3 + w3.getSizeX() * 2, -w3.getSizeY());

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
            TankerEnemy sample = new TankerEnemy(55.0, true);
            double gap = (Main.WIDTH - (3 * sample.getSizeX())) / 4.0;
            double slot1 = gap + sample.getSizeX() / 2.0;
            double slot2 = gap * 2 + sample.getSizeX() * 1.5;

            for (int i = 0; i < 3; i++) {
                SwarmEnemy sw1 = new SwarmEnemy(SwarmEnemy.TrajectoryType.SINE_WAVE, 240.0, 0);
                sw1.spawnAt(slot1, -sw1.getSizeY() - (i * 40));

                SwarmEnemy sw2 = new SwarmEnemy(SwarmEnemy.TrajectoryType.SINE_WAVE, 240.0, 0);
                sw2.spawnAt(slot2, -sw2.getSizeY() - (i * 40));

                spawnEnemy(sw1);
                spawnEnemy(sw2);
            }
            lvl4_spawned60s = true;
        }

        // 🎆 Giai đoạn 4: Dọn dẹp chiến trường (Giây 75 - 85)
        if (lvl4_spawned55s && !isVictory) {
            boolean wallDead = lvl4_wallTankers.stream().allMatch(e -> !e.isAlive());
            if (wallDead || elapsedSec >= 85.0) {
                if (!lvl4_coinsSpawned) {
                    // Rớt đại tiệc 25 đồng Vàng lớn!
                    for (int c = 0; c < 25; c++) {
                        double dropX = Main.WIDTH / 2.0 + (random.nextDouble() - 0.5) * 240;
                        double dropY = Main.HEIGHT * 0.35 + (random.nextDouble() - 0.5) * 140;
                        CoinPowerUp coin = new CoinPowerUp(dropX, dropY);
                        powerUps.add(coin);
                        gameLayoutPane.getChildren().add(coin.getView());
                    }
                    lvl4_coinsSpawned = true;
                    lvl4_clearTime = System.currentTimeMillis();
                }

                long elapsedSinceClear = System.currentTimeMillis() - lvl4_clearTime;
                if (elapsedSinceClear >= 500 && !lvl4_magneticActive) {
                    lvl4_magneticActive = true;
                }

                if (lvl4_magneticActive) {
                    for (PowerUp p : powerUps) {
                        if (p instanceof CoinPowerUp coin && !coin.isMagnetized()) {
                            coin.setMagnetized(true, player);
                        }
                    }
                }

                boolean hasCoinsRemaining = powerUps.stream().anyMatch(p -> p instanceof CoinPowerUp && p.isAlive());
                if (!hasCoinsRemaining) {
                    handleVictory();
                }
            }
        }
    }

    private void spawnLevel5Wave(long now, double elapsedSec) {
        // 🎬 Giai đoạn 1: Giao hàng tiếp tế (Giây 0 - 15)
        if (elapsedSec >= 3.0 && !lvl5_spawnedSwarm) {
            for (int i = 0; i < 4; i++) {
                SwarmEnemy s1 = new SwarmEnemy(SwarmEnemy.TrajectoryType.SINE_WAVE, 240.0, 0, 60.0, 0.025, 0);
                s1.spawnAt(80.0 + (i * 40), -s1.getSizeY() - (i * 30));

                SwarmEnemy s2 = new SwarmEnemy(SwarmEnemy.TrajectoryType.SINE_WAVE, 240.0, 0, 60.0, 0.025, Math.PI);
                s2.spawnAt(Main.WIDTH - 80.0 - (i * 40), -s2.getSizeY() - (i * 30));

                spawnEnemy(s1);
                spawnEnemy(s2);
            }
            lvl5_spawnedSwarm = true;
        }

        // 12s: 2 quái Đỏ rớt 1 ShieldPowerUp và 1 PillPowerUp 100%
        if (elapsedSec >= 10.0 && !lvl5_spawnedRedEnemies) {
            double centerX = Main.WIDTH / 2.0;

            NormalEnemy rShield = new NormalEnemy(90.0, false, false, true, "enemy_normal_red");
            rShield.setPos(centerX - 90 - rShield.getSizeX() / 2.0, -rShield.getSizeY());

            NormalEnemy rPill = new NormalEnemy(90.0, false, false, true, "enemy_normal_red");
            rPill.setPos(centerX + 90 - rPill.getSizeX() / 2.0, -rPill.getSizeY());

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
            MidBoss boss = new MidBoss();
            boss.setPos(Main.WIDTH / 2.0 - boss.getSizeX() / 2.0, -boss.getSizeY());
            lvl5_midBoss = boss;
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
                        spawnEnemyBullet(b);
                    }
                } else {
                    // Phase 2: Tử Quang (2 Tia Laser 50 Dmg + 2 Đạn Tỉa Kim Cương 25 Dmg nhắm
                    // player)
                    vfxManager.spawnScreenEffect(true);
                    AudioManager.getInstance().playSound("sfx_zap");

                    // 2 Tia Laser chéo
                    EnemyBullet laser1 = new EnemyBullet(bossCenterX - 40, bossCenterY, -50.0,
                            380.0, 50,
                            "bullet_enemy_laser");
                    EnemyBullet laser2 = new EnemyBullet(bossCenterX + 40, bossCenterY, 50.0,
                            380.0, 50,
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
                    EnemyBullet diamond1 = new EnemyBullet(bossCenterX - 20, bossCenterY,
                            targetedVx, targetedVy, 25,
                            "bullet_enemy_diamond_yellow");
                    EnemyBullet diamond2 = new EnemyBullet(bossCenterX + 20, bossCenterY,
                            targetedVx, targetedVy, 25,
                            "bullet_enemy_diamond_yellow");

                    EnemyBullet[] phase2Bullets = { laser1, laser2, diamond1, diamond2 };
                    for (EnemyBullet b : phase2Bullets) {
                        spawnEnemyBullet(b);
                    }
                }
            }
        }

        // 🎆 Giai đoạn 5: Vụ Nổ Lịch Sử khi Boss chết
        if (lvl5_bossSpawned && lvl5_midBoss != null && !lvl5_midBoss.isAlive() && !lvl5_victoryTriggered) {
            if (!lvl5_coinsSpawned) {
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
                lvl5_coinsSpawned = true;
                lvl5_clearTime = System.currentTimeMillis();
            }

            long elapsedSinceClear = System.currentTimeMillis() - lvl5_clearTime;
            if (elapsedSinceClear >= 500 && !lvl5_magneticActive) {
                lvl5_magneticActive = true;
            }

            if (lvl5_magneticActive) {
                for (PowerUp p : powerUps) {
                    if (p instanceof CoinPowerUp coin && !coin.isMagnetized()) {
                        coin.setMagnetized(true, player);
                    }
                }
            }

            boolean hasCoinsRemaining = powerUps.stream().anyMatch(p -> p instanceof CoinPowerUp && p.isAlive());
            if (!hasCoinsRemaining) {
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

    public void spawnEnemyBullet(EnemyBullet bullet) {
        if (bullet == null)
            return;
        enemyBullets.add(bullet);
        if (bullet.getView() != null && !gameLayoutPane.getChildren().contains(bullet.getView())) {
            gameLayoutPane.getChildren().add(bullet.getView());
        }
        if (isDebug && bullet.getHitbox() != null) {
            bullet.getHitbox().setFill(Color.rgb(255, 0, 0, 0.3));
            bullet.getHitbox().setStroke(Color.YELLOW);
            bullet.getHitbox().setStrokeWidth(2);
            if (!gameLayoutPane.getChildren().contains(bullet.getHitbox())) {
                gameLayoutPane.getChildren().add(bullet.getHitbox());
            }
        }
    }

    // Xử lý va chạm
    private void handleCollisions() {
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
                        } else if (enemy instanceof MiniBoss miniBoss) {
                            handleMiniBossDefeated(miniBoss);
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
                    if (enemy instanceof BossObject) {
                        // Va chạm trực tiếp với Boss: Người chơi thua ngay lập tức (Game Over)!
                        vfxManager.spawnExplosionSpriteSheet(player.getX() + player.getSizeX() / 2.0,
                                player.getY() + player.getSizeY() / 2.0,
                                player.getSizeX() * 4.0,
                                player.getSizeY() * 4.0);
                        AudioManager.getInstance().playSound("sfx_explosion_enemy");
                        player.setHealth(0);
                        player.setAlive(false);
                        vfxManager.spawnScreenEffect(false);
                        break;
                    } else {
                        enemy.setAlive(false);
                        vfxManager.spawnExplosionSpriteSheet(enemy.getX() + enemy.getSizeX() / 2,
                                enemy.getY() + enemy.getSizeY() / 2,
                                enemy.getSizeX() * 3.0,
                                enemy.getSizeY() * 3.0);
                        AudioManager.getInstance().playSound("sfx_explosion_enemy");
                        player.takeDamage(enemy.getCollisionDamage());
                        vfxManager.applyPlayerGlow(player, "damaged");
                        vfxManager.spawnScreenEffect(false);
                    }
                }

            }
            // 3. Enemy Bullet và Player
            Iterator<EnemyBullet> eBulletIter = enemyBullets.iterator();
            while (eBulletIter.hasNext()) {
                EnemyBullet eBullet = eBulletIter.next();
                if (!eBullet.isAlive()) {
                    continue;
                }
                if (isColliding(eBullet, player)) {
                    eBullet.setAlive(false);
                    player.takeDamage(eBullet.getDamage());
                    vfxManager.applyPlayerGlow(player, "damaged");

                    if (eBullet.getView() != null) {
                        gameLayoutPane.getChildren().remove(eBullet.getView());
                    }
                    if (eBullet.getHitbox() != null) {
                        gameLayoutPane.getChildren().remove(eBullet.getHitbox());
                    }
                    eBulletIter.remove();
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
        gameLayoutPane.getChildren().clear();
        isGameOver = true;
        stopGame();
        PlayerDataManager.getInstance().addGold(goldCollected);
        PlayerDataManager.getInstance().checkAndUpdateHighScore(score,currentStageLevel);
        playScene.showGameOverMenu(score, goldCollected);
    }

    private void handleMiniBossDefeated(EnemyObject miniBoss) {
        if (lvl1_miniBossDefeated)
            return;
        lvl1_miniBossDefeated = true;
        lvl1_bossDeathTime = System.currentTimeMillis();
        lvl1_magneticActive = false;

        // MiniBoss nổ rớt 20 đồng Vàng xung quanh vị trí nổ (60s)
        for (int c = 0; c < 20; c++) {
            double dropX = miniBoss.getX() + miniBoss.getSizeX() / 2.0 + (random.nextDouble() - 0.5) * 180;
            double dropY = miniBoss.getY() + miniBoss.getSizeY() / 2.0 + (random.nextDouble() - 0.5) * 120;
            CoinPowerUp coin = new CoinPowerUp(dropX, dropY);
            powerUps.add(coin);
            gameLayoutPane.getChildren().add(coin.getView());
        }
    }

    private void handleVictory() {

        if (isVictory)
            return;
        isVictory = true;

        score += 500;
        goldCollected += 100;

        stopGame();
        PlayerDataManager.getInstance().addGold(goldCollected);
        PlayerDataManager.getInstance().checkAndUpdateHighScore(score,currentStageLevel);

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
        stopGame(); // Dừng vòng lặp cũ đang chạy ngầm trước khi setup lại
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
