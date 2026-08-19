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
import com.nhom27.skyforce.entities.player.Player;
import com.nhom27.skyforce.entities.weapons.Bullet;
import com.nhom27.skyforce.entities.weapons.EnemyBullet;
import com.nhom27.skyforce.levels.LevelScript;
import com.nhom27.skyforce.levels.LevelScriptFactory;
import com.nhom27.skyforce.main.Main;
import com.nhom27.skyforce.scenes.PlayScene;

import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

public class GameManager {
    private Pane gameLayoutPane;
    private PlayScene playScene;

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
    private long levelStartTime = 0;

    /**
     * Strategy Pattern: Kịch bản màn chơi hiện tại.
     * GameManager không còn trực tiếp giữ hàng chục cờ boolean cồng kềnh của 5
     * level.
     */
    private LevelScript currentLevelScript;

    private Set<KeyCode> activeKeys = new HashSet<>();
    private boolean isDebug = true;

    public int getCurrentStageLevel() {
        return currentStageLevel;
    }

    public void setCurrentStageLevel(int currentStageLevel) {
        this.currentStageLevel = currentStageLevel;
        this.currentLevelScript = LevelScriptFactory.createLevelScript(currentStageLevel);
        if (this.currentLevelScript != null) {
            this.currentLevelScript.setup();
        }
    }

    public LevelScript getCurrentLevelScript() {
        return currentLevelScript;
    }

    public void handleKeyPressed(javafx.scene.input.KeyCode code) {
        activeKeys.add(code);
    }

    public void handleKeyReleased(javafx.scene.input.KeyCode code) {
        activeKeys.remove(code);
    }

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

    /**
     * Thiết lập lại toàn bộ trạng thái màn chơi và khởi tạo LevelScript tương ứng.
     */
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

        // Khởi tạo kịch bản màn chơi thông qua Factory Pattern
        levelStartTime = System.currentTimeMillis();
        currentLevelScript = LevelScriptFactory.createLevelScript(currentStageLevel);
        if (currentLevelScript != null) {
            currentLevelScript.setup();
        }

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

            // Di chuyển bằng phím
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

        // Triệu hồi kẻ địch theo Strategy Pattern
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
                    double bulletSpeed = 350.0;
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

                    double totalSpeed = 120.0;
                    double radLeft = Math.toRadians(-20);
                    double radRight = Math.toRadians(20);

                    EnemyBullet b1 = new EnemyBullet(startX, startY, 0, totalSpeed, 15, "bullet_boss_mini_red");
                    EnemyBullet b2 = new EnemyBullet(startX, startY, totalSpeed * Math.sin(radLeft),
                            totalSpeed * Math.cos(radLeft), 15, "bullet_boss_mini_red");
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

    /**
     * Ủy quyền logic kịch bản sinh quái vật cho {@link LevelScript} hiện tại
     * (Strategy Pattern).
     *
     * @param now Timestamp hiện tại
     */
    private void spawnEnemyWave(long now) {
        if (levelStartTime == 0) {
            levelStartTime = System.currentTimeMillis();
        }
        double elapsedSec = (System.currentTimeMillis() - levelStartTime) / 1000.0;

        if (currentLevelScript != null) {
            currentLevelScript.update(now, elapsedSec, this);
        }
    }

    /**
     * Thêm một kẻ địch vào màn chơi và hiển thị lên giao diện (public helper cho
     * LevelScript).
     *
     * @param newEnemy Đối tượng kẻ địch mới
     */
    public void spawnEnemy(EnemyObject newEnemy) {
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

    /**
     * Thêm đạn của kẻ địch vào game loop.
     *
     * @param bullet Đạn kẻ địch
     */
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

    /**
     * Thêm vật phẩm hỗ trợ PowerUp vào màn chơi.
     *
     * @param powerUp Vật phẩm hỗ trợ
     */
    public void addPowerUp(PowerUp powerUp) {
        if (powerUp == null)
            return;
        powerUps.add(powerUp);
        if (powerUp.getView() != null && !gameLayoutPane.getChildren().contains(powerUp.getView())) {
            gameLayoutPane.getChildren().add(powerUp.getView());
        }
        if (isDebug && powerUp.getHitbox() != null) {
            powerUp.getHitbox().setFill(Color.rgb(255, 0, 0, 0.3));
            powerUp.getHitbox().setStroke(Color.YELLOW);
            powerUp.getHitbox().setStrokeWidth(2);
            if (!gameLayoutPane.getChildren().contains(powerUp.getHitbox())) {
                gameLayoutPane.getChildren().add(powerUp.getHitbox());
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

                        // Thông báo tới currentLevelScript khi kẻ địch bị hạ (dùng cho MiniBoss /
                        // MidBoss drops)
                        if (currentLevelScript != null) {
                            currentLevelScript.onEnemyKilled(enemy, this);
                        }

                        if (enemy instanceof NormalEnemy normalEnemy && normalEnemy.isGuaranteedDrop()) {
                            PowerUp item;
                            if (currentStageLevel == 2) {
                                item = new ShieldPowerUp(enemy.getX(), enemy.getY());
                            } else {
                                item = new PillPowerUp(enemy.getX(), enemy.getY());
                            }
                            addPowerUp(item);
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
        addPowerUp(powerUp);
    }

    // Xử lý logic va chạm 2 đối tượng
    private boolean isColliding(GameObject a, GameObject b) {
        if (a == null || b == null)
            return false;

        if (a.getHitbox() != null && b.getHitbox() != null) {
            if (!a.getHitbox().getBoundsInParent().intersects(b.getHitbox().getBoundsInParent())) {
                return false;
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
        PlayerDataManager.getInstance().checkAndUpdateHighScore(score, currentStageLevel);
        playScene.showGameOverMenu(score, goldCollected);
    }

    /**
     * Xử lý khi người chơi chiến thắng màn hiện tại.
     */
    public void handleVictory() {
        score += 500;
        goldCollected += 100;

        stopGame();
        PlayerDataManager.getInstance().addGold(goldCollected);
        PlayerDataManager.getInstance().checkAndUpdateHighScore(score, currentStageLevel);

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

    // Getters cho các thành phần game
    public List<EnemyObject> getEnemies() {
        return enemies;
    }

    public List<PowerUp> getPowerUps() {
        return powerUps;
    }

    public List<EnemyBullet> getEnemyBullets() {
        return enemyBullets;
    }

    public Player getPlayer() {
        return player;
    }

    public PlayScene getPlayScene() {
        return playScene;
    }

    public VFXManager getVFXManager() {
        return vfxManager;
    }

    public Pane getGameLayoutPane() {
        return gameLayoutPane;
    }

    public Random getRandom() {
        return random;
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
        stopGame();
        setupGame();
        startGame();
    }

    public boolean isPaused() {
        return isPaused;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public int getScore() {
        return score;
    }
}
