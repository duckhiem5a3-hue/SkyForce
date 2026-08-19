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

    private BossObject currentActiveBoss = null;

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

    public void handleKeyPressed(KeyCode code) {
        activeKeys.add(code);
    }

    public void handleKeyReleased(KeyCode code) {
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
        renderDebugHitbox(player);

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
        renderDebugHitbox(player);
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
                if (activeKeys.contains(KeyCode.W)
                        || activeKeys.contains(KeyCode.UP))
                    dy -= speed;
                if (activeKeys.contains(KeyCode.S)
                        || activeKeys.contains(KeyCode.DOWN))
                    dy += speed;
                if (activeKeys.contains(KeyCode.A)
                        || activeKeys.contains(KeyCode.LEFT))
                    dx -= speed;
                if (activeKeys.contains(KeyCode.D)
                        || activeKeys.contains(KeyCode.RIGHT))
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
                for (Bullet bullet : player.fireBullet(enemies)) {
                    gameLayoutPane.getChildren().add(bullet.getView());
                    renderDebugHitbox(bullet);
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
                if (b.getView() != null) {
                    gameLayoutPane.getChildren().remove(b.getView());
                }
                if (b.getHitbox() != null) {
                    gameLayoutPane.getChildren().remove(b.getHitbox());
                }
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

        // Cập nhật kẻ địch (Đa hình Polymorphism)
        Iterator<EnemyObject> enemyIter = enemies.iterator();
        while (enemyIter.hasNext()) {
            EnemyObject e = enemyIter.next();
            e.update();
            e.attack(this, now, player);
            if (!e.isAlive()) {
                if (e.getView() != null) {
                    gameLayoutPane.getChildren().remove(e.getView());
                }
                if (e.getHitbox() != null) {
                    gameLayoutPane.getChildren().remove(e.getHitbox());
                }
                enemyIter.remove();
            }
        }

        // Cập nhật PowerUps
        Iterator<PowerUp> powerUpIter = powerUps.iterator();
        while (powerUpIter.hasNext()) {
            PowerUp p = powerUpIter.next();
            p.update();
            if (!p.isAlive()) {
                if (p.getView() != null) {
                    gameLayoutPane.getChildren().remove(p.getView());
                }
                if (p.getHitbox() != null) {
                    gameLayoutPane.getChildren().remove(p.getHitbox());
                }
                powerUpIter.remove();
            }
        }

        // Xử lý va chạm
        handleCollisions();

        // Update HUD
        playScene.updateHUD(score, goldCollected, player);

        if (currentActiveBoss != null) {
            if (!currentActiveBoss.isAlive()) {
                currentActiveBoss = null;
            }
        }
        playScene.updateBossHUD(currentActiveBoss);

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
     * Phương thức phụ trợ hiển thị khung Hitbox debug cho bất kỳ GameObject nào.
     *
     * @param obj GameObject cần hiển thị khung hitbox
     */
    private void renderDebugHitbox(GameObject obj) {
        if (isDebug && obj != null && obj.getHitbox() != null) {
            obj.getHitbox().setFill(Color.rgb(255, 0, 0, 0.3));
            obj.getHitbox().setStroke(Color.YELLOW);
            obj.getHitbox().setStrokeWidth(2);
            if (!gameLayoutPane.getChildren().contains(obj.getHitbox())) {
                gameLayoutPane.getChildren().add(obj.getHitbox());
            }
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

            if (newEnemy instanceof BossObject boss) {
                this.currentActiveBoss = boss;
            }

            if (newEnemy.getView() != null) {
                gameLayoutPane.getChildren().add(newEnemy.getView());
            }
            renderDebugHitbox(newEnemy);
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
        renderDebugHitbox(bullet);
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
        renderDebugHitbox(powerUp);
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
                        vfxManager.spawnExplosionSpriteSheet(enemy.getX() + enemy.getSizeX() / 2,
                                enemy.getY() + enemy.getSizeY() / 2,
                                enemy.getSizeX() * 4.0,
                                enemy.getSizeY() * 4.0);
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
                        vfxManager.spawnScreenEffect("damaged");
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
                        vfxManager.spawnScreenEffect("damaged");
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
                    vfxManager.spawnScreenEffect("damaged");
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
