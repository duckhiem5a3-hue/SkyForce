package com.nhom27.skyforce.managers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.nhom27.skyforce.entities.base.EnemyObject;
import com.nhom27.skyforce.entities.base.GameObject;
import com.nhom27.skyforce.entities.enemies.SineOrbitEnemy;
import com.nhom27.skyforce.entities.enemies.StraightEnemy;
import com.nhom27.skyforce.entities.items.PowerUp;
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

    private Player player;
    private List<EnemyObject> enemies;
    private List<PowerUp> powerUps;

    private AnimationTimer gameLoop;
    private Random random;

    private int score;
    private int wave;
    private boolean isPaused;
    private boolean isGameOver;

    private long lastEnemyWaveTime;

    private boolean isDebug = true;

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
        wave = 1;
        isPaused = false;
        isGameOver = false;

        // 1. Tạo đối tượng người chơi
        player = new Player(Main.WIDTH * 0.5, Main.HEIGHT * 0.75);

        if (player.getView() != null) {
            gameLayoutPane.getChildren().add(player.getView());
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
                System.out.println(now + "ms");
                if (!isPaused && !isGameOver) {
                    updateGame(now);
                }
            }
        };
    }

    private void updateGame(long now) {
        // Cập nhật logic người chơi
        if (player != null && player.isAlive()) {
            player.update();

            // Tự động bắn đạn của người chơi
            if (now - player.getTimeSinceLastBullet() >= player.getFireRate()) {
                for (Bullet bullet : player.fireBullet()) {
                    gameLayoutPane.getChildren().add(bullet.getView());
                    if (isDebug && bullet.getHitbox() != null) {
                        bullet.getHitbox().setFill(Color.rgb(255, 0, 0, 0.3)); // Nền đỏ mờ 30%
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

        // Cập nhật PowerUps
        Iterator<PowerUp> powerUpIter = powerUps.iterator();
        while (powerUpIter.hasNext()) {
            PowerUp p = powerUpIter.next();
            p.update();
            if (!p.isAlive()) {
                gameLayoutPane.getChildren().remove(p.getView());
                powerUpIter.remove();
            }
        }

        // Xử lý va chạm
        handleCollisions();

        // Update HUD
        playScene.updateHUD(score, wave, player);

        // Check Game Over
        if (player != null && (!player.isAlive() || player.getHealth() <= 0)) {
            handleGameOver();
        }
    }

    private void spawnEnemyWave(long now) {
        if (now - lastEnemyWaveTime >= 1000) {
            int spawnType = random.nextInt(2);
            EnemyObject newEnemy = null;

            if (spawnType == 0) {
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

        // Tăng đợt tấn công lên sau mỗi 5000 điểm
        if (score % 500 == 0 && score > 0) {
            wave++;
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
                    bullet.setAlive(false);
                    enemy.takeDamage(40);

                    if (!enemy.isAlive()) {
                        score += 5;
                        // tỉ lệ 30% rơi ra vật phẩm cường hóa
                        if (random.nextDouble() < 0.30) {
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
                    player.takeDamage(20);
                }
            }

            // 3. PowerUps vs Player
            for (PowerUp powerUp : powerUps) {
                if (!powerUp.isAlive())
                    continue;

                if (isColliding(player, powerUp)) {
                    powerUp.setAlive(false);
                    player.setGettingBuffed(true);
                    player.heal(20);
                    score += 50;
                }
            }
        }
    }

    private void spawnPowerUp(double x, double y) {
        PowerUp powerUp = new PowerUp("powerup", x, y);
        powerUps.add(powerUp);
        if (powerUp.getView() != null) {
            gameLayoutPane.getChildren().add(powerUp.getView());
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
        playScene.showGameOverMenu(score);
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
