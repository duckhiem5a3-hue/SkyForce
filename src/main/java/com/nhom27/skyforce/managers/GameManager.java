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
import com.nhom27.skyforce.scenes.SceneManager;
import com.nhom27.skyforce.ui.buttons.CustomButton;
import com.nhom27.skyforce.utils.AssetManager;

import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Shape;

public class GameManager {
    private Pane gameLayoutPane;

    private Player player;
    private List<EnemyObject> enemies;
    private List<Bullet> playerBullets;
    private List<PowerUp> powerUps;

    private AnimationTimer gameLoop;
    private Random random;

    private int score;
    private int wave;
    private long frameCount; // đếm số frame trôi qua
    private boolean isPaused;
    private boolean isGameOver;

    private Label scoreLabel;
    private Label healthLabel;
    private ProgressBar healthBar;
    private Label waveLabel;
    private Label buffStatusLabel;
    private StackPane gameOverOverlay;

    public GameManager(Pane gameLayoutPane) {
        this.gameLayoutPane = gameLayoutPane;
        this.enemies = new ArrayList<>();
        this.playerBullets = new ArrayList<>();
        this.powerUps = new ArrayList<>();
        this.random = new Random();

        setupGame();
    }

    public void setupGame() {
        // Dọn dẹp và Thiết lập lại trạng thái màn chơi
        gameLayoutPane.getChildren().clear();
        enemies.clear();
        playerBullets.clear();
        powerUps.clear();

        score = 0;
        wave = 1;
        frameCount = 0;
        isPaused = false;
        isGameOver = false;

        // 1. Tạo đối tượng người chơi
        player = new Player(Main.WIDTH / 2.0, Main.HEIGHT * (3.0 / 4.0));
        if (player.getView() != null) {
            gameLayoutPane.getChildren().add(player.getView());
        }

        // setup chuột
        // gameLayoutPane.setOnMouseMoved(e -> movePlayer(e.getX(), e.getY()));
        gameLayoutPane.setOnMouseDragged(e -> movePlayer(e.getX(), e.getY()));

        // 2. Setup HUD
        setupHUD();

        // 3. Game Loop Setup
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!isPaused && !isGameOver) {
                    updateGame();
                }
            }
        };
    }

    private void movePlayer(double mouseX, double mouseY) {
        if (isPaused || isGameOver || player == null)
            return;
        // Đảm bảo máy bay người chơi luôn hiển thị trong khung hình
        double clampedX = Math.min(Math.max((Player.sizeX / 2), mouseX), Main.WIDTH - Player.sizeX / 2);
        double clampedY = Math.min(Math.max((Player.sizeY / 2), mouseY), Main.HEIGHT - Player.sizeY / 2);
        // Căn cho con trỏ chuột trỏ vào trọng tâm máy bay
        player.setPos(clampedX - (Player.sizeX / 2), clampedY - (Player.sizeY / 2));
    }

    private void setupHUD() {
        // Điểm số
        scoreLabel = new Label("SCORE: 0");
        scoreLabel.setFont(AssetManager.getFont("font_kenvector_future", 22));
        scoreLabel.setTextFill(Color.GOLD);
        scoreLabel.setEffect(new DropShadow(BlurType.GAUSSIAN, Color.BLACK, 5, 0.8, 0, 2));

        HBox scoreBox = new HBox(scoreLabel);
        scoreBox.setAlignment(Pos.TOP_RIGHT);
        scoreBox.setLayoutX(Main.WIDTH - 260);
        scoreBox.setLayoutY(20);
        scoreBox.setPrefWidth(240);

        // Thanh máu
        healthBar = new ProgressBar(1.0);
        healthBar.setPrefWidth(180);
        healthBar.setPrefHeight(20);
        healthBar.setStyle("-fx-accent: #2ecc71; -fx-control-inner-background: #34495e;");

        healthLabel = new Label("HP: 100 / 100");
        healthLabel.setFont(AssetManager.getFont("font_kenvector_future", 14));
        healthLabel.setTextFill(Color.WHITE);

        VBox healthBox = new VBox(5, healthLabel, healthBar);
        healthBox.setLayoutX(120);
        healthBox.setLayoutY(15);

        // Màn
        waveLabel = new Label("WAVE 1");
        waveLabel.setFont(AssetManager.getFont("font_kenvector_future", 24));
        waveLabel.setTextFill(Color.CYAN);
        waveLabel.setEffect(new DropShadow(BlurType.GAUSSIAN, Color.BLACK, 8, 0.8, 0, 2));

        HBox waveBox = new HBox(waveLabel);
        waveBox.setAlignment(Pos.CENTER);
        waveBox.setLayoutX((Main.WIDTH - 200) / 2.0);
        waveBox.setLayoutY(15);
        waveBox.setPrefWidth(200);

        // Trạng thái cường hóa
        buffStatusLabel = new Label("");
        buffStatusLabel.setFont(AssetManager.getFont("font_kenvector_future", 14));
        buffStatusLabel.setTextFill(Color.YELLOW);
        buffStatusLabel.setLayoutX(120);
        buffStatusLabel.setLayoutY(65);

        gameLayoutPane.getChildren().addAll(scoreBox, healthBox, waveBox, buffStatusLabel);
    }

    private void updateGame() {
        frameCount++;

        // Cập nhật logic người chơi
        if (player != null && player.isAlive()) {
            player.update();

            // Tự động bắn đạn của người chơi
            if (player.getTimeSinceLastBullet() >= 12) {
                firePlayerBullet();
                player.setTimeSinceLastBullet(0);
            }

            // Cập nhật thông báo cường hóa
            if (player.isGettingBuffed()) {
                buffStatusLabel.setText("TRIPLE SHOT (" + (player.getTimeInBuff() / 60 + 1) + "s)");
            } else {
                buffStatusLabel.setText("");
            }
        }

        // Cập nhật đạn của người chơi
        Iterator<Bullet> bulletIter = playerBullets.iterator();
        while (bulletIter.hasNext()) {
            Bullet b = bulletIter.next();
            b.update();
            if (!b.isAlive()) {
                gameLayoutPane.getChildren().remove(b.getView());
                bulletIter.remove();
            }
        }

        // Triệu hồi kẻ địch
        spawnEnemyWave();

        // Cập nhật kẻ địch (xóa an toàn nhờ vào Iterator)
        Iterator<EnemyObject> enemyIter = enemies.iterator();
        while (enemyIter.hasNext()) {
            EnemyObject e = enemyIter.next();
            e.update();
            if (!e.isAlive()) {
                gameLayoutPane.getChildren().remove(e.getView());
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
        updateHUD();

        // Check Game Over
        if (player != null && (!player.isAlive() || player.getHealth() <= 0)) {
            handleGameOver();
        }
    }

    private void firePlayerBullet() {
        if (player == null)
            return;
        double startX = player.getX() + 35;
        double startY = player.getY();

        if (player.isGettingBuffed()) {
            // Triple spread shot
            Bullet b1 = new Bullet("bullet_img", startX, startY, 0, -800, 12, 24);
            Bullet b2 = new Bullet("bullet_img", startX - 15, startY + 5, -250, -750, 12, 24);
            Bullet b3 = new Bullet("bullet_img", startX + 15, startY + 5, 250, -750, 12, 24);

            addBullet(b1);
            addBullet(b2);
            addBullet(b3);
        } else {
            // Single straight shot
            Bullet b = new Bullet("bullet_img", startX, startY, 0, -800, 12, 24);
            addBullet(b);
        }
    }

    private void addBullet(Bullet bullet) {
        playerBullets.add(bullet);
        if (bullet.getView() != null) {
            gameLayoutPane.getChildren().add(bullet.getView());
        }
    }

    private void spawnEnemyWave() {
        if (frameCount % Math.max(30, 90 - wave * 5) == 0) {
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
            }
        }

        // Tăng đợt tấn công lên sau mỗi 5000 điểm
        if (score % 5000 == 0) {
            wave++;
        }
    }

    // Xử lý va chạm
    private void handleCollisions() {
        // 1. Player Bullets và Enemies
        for (Bullet bullet : playerBullets) {
            if (!bullet.isAlive())
                continue;

            for (EnemyObject enemy : enemies) {
                if (!enemy.isAlive())
                    continue;

                if (isColliding(bullet, enemy)) {
                    bullet.setAlive(false);
                    enemy.takeDamage(40);

                    if (!enemy.isAlive()) {
                        score += 100;
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

    private void updateHUD() {
        if (scoreLabel != null) {
            scoreLabel.setText("SCORE: " + score);
        }
        if (waveLabel != null) {
            waveLabel.setText("WAVE " + wave);
        }
        if (player != null && healthBar != null && healthLabel != null) {
            double healthPercent = (double) player.getHealth() / player.getMaxHealth();
            healthBar.setProgress(Math.max(0, healthPercent));
            healthLabel.setText("HP: " + player.getHealth() + " / " + player.getMaxHealth());
        }
    }

    private void handleGameOver() {
        isGameOver = true;
        stopGame();

        gameOverOverlay = new StackPane();
        gameOverOverlay.setPrefSize(Main.WIDTH, Main.HEIGHT);
        gameOverOverlay.setBackground(
                new Background(new BackgroundFill(Color.rgb(0, 0, 0, 0.75), CornerRadii.EMPTY, Insets.EMPTY)));

        VBox card = new VBox(20);
        card.setAlignment(Pos.CENTER);
        card.setMaxSize(400, 350);

        LinearGradient cardGradient = new LinearGradient(
                0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#2c3e50")),
                new Stop(1, Color.web("#1a252f")));
        CornerRadii radii = new CornerRadii(16);
        card.setBackground(new Background(new BackgroundFill(cardGradient, radii, Insets.EMPTY)));
        card.setBorder(new Border(
                new BorderStroke(Color.rgb(231, 76, 60, 0.8), BorderStrokeStyle.SOLID, radii, new BorderWidths(2))));

        Label titleLabel = new Label("GAME OVER");
        titleLabel.setTextFill(Color.web("#e74c3c"));
        titleLabel.setFont(AssetManager.getFont("font_kenvector_future", 32));

        Label finalScoreLabel = new Label("FINAL SCORE: " + score);
        finalScoreLabel.setTextFill(Color.WHITE);
        finalScoreLabel.setFont(AssetManager.getFont("font_kenvector_future", 18));

        CustomButton btnRestart = new CustomButton("PLAY AGAIN", "button_blue", () -> {
            gameLayoutPane.getChildren().remove(gameOverOverlay);
            restartGame();
        });

        CustomButton btnMainMenu = new CustomButton("HOME MENU", "button_blue", () -> {
            SceneManager.getInstance().switchScene("MenuScene");
        });

        card.getChildren().addAll(titleLabel, finalScoreLabel, btnRestart, btnMainMenu);
        gameOverOverlay.getChildren().add(card);

        gameLayoutPane.getChildren().add(gameOverOverlay);
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
