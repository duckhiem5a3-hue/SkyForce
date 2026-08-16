package com.nhom27.skyforce.managers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.nhom27.skyforce.audio.AudioManager;
import com.nhom27.skyforce.entities.base.EnemyObject;
import com.nhom27.skyforce.entities.base.GameObject;
import com.nhom27.skyforce.entities.enemies.ShooterEnemy;
import com.nhom27.skyforce.entities.enemies.SineOrbitEnemy;
import com.nhom27.skyforce.entities.enemies.StraightEnemy;
import com.nhom27.skyforce.entities.items.CoinPowerUp;
import com.nhom27.skyforce.entities.items.PillPowerUp;
import com.nhom27.skyforce.entities.items.PowerUp;
import com.nhom27.skyforce.entities.items.SeekerPowerUp;
import com.nhom27.skyforce.entities.items.ShieldPowerUp;
import com.nhom27.skyforce.entities.player.Player;
import com.nhom27.skyforce.entities.weapons.Bullet;
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
    private ShooterEnemy shooterEnemy; // chỉ có nhiều nhất 1 shooter tại mọi thời điểm xác định, vậy nên gán nó với
                                       // GameManager để kiểm tra riêng sự tồn tại của nó
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
        // gameLayoutPane.setOnMouseMoved(e -> movePlayer(e.getX(), e.getY()));
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
        // gameLayoutPane.setOnMouseMoved(e -> player.movePlayer(e.getX(), e.getY()));
        // gameLayoutPane.setOnMouseDragged(e -> player.movePlayer(e.getX(), e.getY()));
    }

    private void updateGame(long now) {
        // Cập nhật logic người chơi
        if (player != null && player.isAlive()) {
            player.update();

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

            // // Cập nhật thông báo cường hóa
            // if (player.isGettingBuffed()) {
            // buffStatusLabel.setText("TRIPLE SHOT (" + (player.getTimeInBuff() / 60 + 1) +
            // "s)");
            // } else {
            // buffStatusLabel.setText("");
            // }
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

        // Triệu hồi kẻ địch
        spawnEnemyWave(now);

        // Cập nhật kẻ địch (xóa an toàn nhờ vào Iterator)
        Iterator<EnemyObject> enemyIter = enemies.iterator();
        while (enemyIter.hasNext()) {
            EnemyObject e = enemyIter.next();
            e.update();
            if (!e.isAlive()) {
                gameLayoutPane.getChildren().remove(e.getView());
                if (e.getHitbox() != null) {
                    gameLayoutPane.getChildren().remove(e.getHitbox());
                }
                enemyIter.remove();
            }
        }
        // kiểm tra và cập nhật cái chết của kẻ địch shooterEnemy
        if (shooterEnemy != null && !shooterEnemy.isAlive()) {
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

        // Check Game Over
        if (player != null && (!player.isAlive() || player.getHealth() <= 0)) {
            handleGameOver();
        }
    }

    private void spawnEnemyWave(long now) {
        if (now - lastEnemyWaveTime >= 1000) {// attempt to spawn one enemy every second. "now" got real time update
            int spawnType = random.nextInt(2);
            EnemyObject newEnemy = null;

            // shooter spawning is prioritized
            if (shooterEnemy == null && System.currentTimeMillis() - lastShooterDeathTime >= 15000) {
                double spawnX = Main.WIDTH / 2 - ShooterEnemy.sizeX / 2;
                shooterEnemy = new ShooterEnemy(spawnX, 100);
                newEnemy = shooterEnemy;
            } else if (spawnType == 0) {
                double spawnX = random.nextDouble() * (Main.WIDTH - StraightEnemy.sizeX);
                newEnemy = new StraightEnemy(spawnX, -StraightEnemy.sizeY, 100 + wave * 2);
            } else if (spawnType == 1) {
                double startY = random.nextDouble() * Main.HEIGHT / 2;
                newEnemy = new SineOrbitEnemy(startY);
            }

            if (newEnemy != null) {
                enemies.add(newEnemy);

                if (newEnemy.getView() != null) {
                    gameLayoutPane.getChildren().add(newEnemy.getView());
                }
                if (isDebug && newEnemy.getHitbox() != null) {
                    // Hiển thị hitbox: Nền đỏ mờ 30%, viền vàng sắc nét
                    newEnemy.getHitbox().setFill(Color.rgb(255, 0, 0, 0.3));
                    newEnemy.getHitbox().setStroke(Color.YELLOW);
                    newEnemy.getHitbox().setStrokeWidth(2);
                    gameLayoutPane.getChildren().add(newEnemy.getHitbox());
                }
            }
            lastEnemyWaveTime = now;
        }

        // Tăng đợt tấn công lên sau mỗi 500 điểm
        if (score >= nextWaveScore) {
            wave++;
            nextWaveScore += 500;
        }
    }

    // Xử lý va chạm
    private void handleCollisions() {
        // 1. Player Bullets và Enemies
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
                        // tỉ lệ 10% rơi ra vật phẩm cường hóa
                        if (random.nextDouble() < 1) {
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
                    player.takeDamage(20);
                    vfxManager.applyPlayerGlow(player, "damaged");
                    vfxManager.spawnScreenEffect(false);
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
                powerUp.getHitbox().setFill(Color.rgb(255, 0, 0, 0.3)); // Nền đỏ mờ 30%
                powerUp.getHitbox().setStroke(Color.YELLOW); // Viền vàng
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
