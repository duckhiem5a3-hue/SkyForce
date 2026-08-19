package com.nhom27.skyforce.scenes;

import com.nhom27.skyforce.audio.AudioManager;
import com.nhom27.skyforce.entities.player.Player;
import com.nhom27.skyforce.main.Main;
import com.nhom27.skyforce.managers.GameManager;
import com.nhom27.skyforce.managers.PlayerDataManager;
import com.nhom27.skyforce.managers.SceneManager;
import com.nhom27.skyforce.ui.CustomButton;
import com.nhom27.skyforce.utils.AssetManager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

public class PlayScene {
    private Scene scene;
    private StackPane root;
    private Pane gamePane;
    private StackPane pauseOverlay;
    private StackPane gameOverOverlay;

    private CustomButton btnSound;
    private GameManager gameManager;

    private Label scoreLabel;
    private Label goldLabel;
    private Label healthLabel;
    private ProgressBar healthBar;
    private Label levelLabel;
    private Label xpLabel;
    private ProgressBar xpBar;
    private Label buffStatusLabel;
    private HBox buffContainer;

    private VBox bossHealthBox;
    private Label bossNameLabel;
    private ProgressBar bossHealthBar;

    private StackPane warningBanner;
    private javafx.animation.FadeTransition warningAnimation;

    public PlayScene() {
        this(1);
    }

    public PlayScene(int level) {
        AudioManager.getInstance().playMusic("background_play_music");
        root = new StackPane();

        setupBackground(level);
        setupGameWorld(level);
        setupHUD();

        // Tạo Pause Overlay Menu
        createPauseOverlay();

        scene = new Scene(root, Main.WIDTH, Main.HEIGHT);
        scene.setOnKeyPressed(e -> {
            if (gameManager != null) {
                gameManager.handleKeyPressed(e.getCode());
            }
        });
        scene.setOnKeyReleased(e -> {
            if (gameManager != null) {
                gameManager.handleKeyReleased(e.getCode());
            }
        });
    }

    private void setupBackground(int level) {
        Image bgImage = AssetManager.getImage("background_level_" + level);
        if (bgImage == null) {
            bgImage = AssetManager.getImage("background_play");
        }
        ImageView bgImageView = null;

        if (bgImage != null) {
            bgImageView = new ImageView(bgImage);
            bgImageView.setFitWidth(Main.WIDTH);
            bgImageView.setFitHeight(Main.HEIGHT);
            root.getChildren().add(bgImageView);
        } else {
            System.out.println("Lỗi: Không tìm thấy ảnh nền play!");
            root.setStyle("-fx-background-color: black;");
        }
    }

    private void setupGameWorld(int level) {
        gamePane = new Pane();
        gamePane.setPrefSize(Main.WIDTH, Main.HEIGHT);
        root.getChildren().add(gamePane);

        gameManager = new GameManager(gamePane, this, level);
        gameManager.startGame();
    }

    private void setupHUD() {
        StackPane hudLayout = new StackPane();

        // NÚT PAUSE
        CustomButton btnPause = new CustomButton(50, 50, "button_pause_blue", () -> {
            if (gameManager != null) {
                gameManager.pauseGame(); // làm cho GameManager.isPaused = false
            }
            showPauseMenu(true);
        });

        // THANH MÁU
        healthBar = new ProgressBar(1.0);
        healthBar.setPrefWidth(150);
        healthBar.setPrefHeight(16);
        healthBar.setStyle("-fx-accent: #2ecc71; -fx-control-inner-background: #34495e;");

        healthLabel = new Label("HP: 100 / 100");
        healthLabel.setFont(AssetManager.getFont("font_kenvector_future", 12));
        healthLabel.setTextFill(Color.WHITE);

        VBox healthBox = new VBox(3, healthLabel, healthBar);
        healthBox.setPickOnBounds(false);

        // LEVEL & THANH XP
        levelLabel = new Label("LV 1");
        levelLabel.setFont(AssetManager.getFont("font_kenvector_future", 13));
        levelLabel.setTextFill(Color.GOLD);

        xpBar = new ProgressBar(0.0);
        xpBar.setPrefWidth(150);
        xpBar.setPrefHeight(16);
        xpBar.setStyle("-fx-accent: #00d2d3; -fx-control-inner-background: #34495e;");

        xpLabel = new Label("XP: 0 / 100");
        xpLabel.setFont(AssetManager.getFont("font_kenvector_future", 12));
        xpLabel.setTextFill(Color.CYAN);

        HBox xpHeader = new HBox(8, levelLabel, xpLabel);
        xpHeader.setAlignment(Pos.CENTER_LEFT);

        VBox xpBox = new VBox(3, xpHeader, xpBar);
        xpBox.setPickOnBounds(false);

        // HÀNG THÔNG TIN (Gồm Pause + Máu + XP)
        HBox playerInfoRow = new HBox(12, btnPause, healthBox, xpBox);
        playerInfoRow.setPickOnBounds(false);

        // TRẠNG THÁI CƯỜNG HÓA
        buffStatusLabel = new Label("");
        buffStatusLabel.setFont(AssetManager.getFont("font_kenvector_future", 14));
        buffStatusLabel.setTextFill(Color.YELLOW);

        buffContainer = new HBox(8);
        buffContainer.setAlignment(Pos.CENTER_LEFT);
        buffContainer.setPadding(new Insets(4, 0, 0, 0));
        buffContainer.setPickOnBounds(false);

        // ==========================================
        // LẮP RÁP CÁC PHÂN VÙNG CHÍNH LÊN MÀN HÌNH
        // ==========================================

        // 1. Góc trên bên trái
        VBox topLeftPanel = new VBox(playerInfoRow, buffContainer);
        topLeftPanel.setAlignment(Pos.TOP_LEFT);
        topLeftPanel.setPickOnBounds(false);

        // 2. Góc trên bên phải (Điểm số & Vàng)
        scoreLabel = new Label("SCORE: 0");
        scoreLabel.setFont(AssetManager.getFont("font_kenvector_future", 20));
        scoreLabel.setTextFill(Color.GOLD);
        scoreLabel.setEffect(new DropShadow(BlurType.GAUSSIAN, Color.BLACK, 5, 0.8, 0, 2));

        goldLabel = new Label("GOLD: 0");
        goldLabel.setFont(AssetManager.getFont("font_kenvector_future", 18));
        goldLabel.setTextFill(Color.YELLOW);
        goldLabel.setEffect(new DropShadow(BlurType.GAUSSIAN, Color.BLACK, 5, 0.8, 0, 2));

        VBox topRightPanel = new VBox(4, scoreLabel, goldLabel);
        topRightPanel.setAlignment(Pos.TOP_RIGHT);
        topRightPanel.setPickOnBounds(false);

        bossNameLabel = new Label("BOSS");
        bossNameLabel.setFont(AssetManager.getFont("font_kenvector_future", 14));
        bossNameLabel.setTextFill(Color.RED);
        bossNameLabel.setEffect(new DropShadow(BlurType.GAUSSIAN, Color.BLACK, 5, 0.8, 0, 1));

        bossHealthBar = new ProgressBar(1.0);
        bossHealthBar.setPrefWidth(300);
        bossHealthBar.setPrefHeight(16);
        bossHealthBar.setStyle("-fx-accent: red;");

        bossHealthBox = new VBox(4, bossNameLabel, bossHealthBar);
        bossHealthBox.setAlignment(Pos.CENTER);
        bossHealthBox.setVisible(false);
        bossHealthBox.setPickOnBounds(false);

        VBox topCenterVBox = new VBox(6, bossHealthBox);
        topCenterVBox.setAlignment(Pos.TOP_CENTER);
        topCenterVBox.setPickOnBounds(false);

        // 4. Gom tất cả vào layout tổng
        hudLayout.getChildren().addAll(topLeftPanel, topCenterVBox, topRightPanel);
        hudLayout.setPadding(new Insets(10));
        hudLayout.setPickOnBounds(false);

        root.getChildren().addAll(hudLayout);
    }

    private void createPauseOverlay() {
        pauseOverlay = new StackPane();
        pauseOverlay.setBackground(
                new Background(new BackgroundFill(Color.rgb(0, 0, 0, 0.65), CornerRadii.EMPTY, Insets.EMPTY)));
        pauseOverlay.setVisible(false);

        VBox card = new VBox(18);
        card.setAlignment(Pos.CENTER);
        card.setMaxSize(360, 420);

        LinearGradient cardGradient = new LinearGradient(
                0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#1e2c3a")),
                new Stop(1, Color.web("#121b24")));
        CornerRadii radii16 = new CornerRadii(16);
        card.setBackground(new Background(new BackgroundFill(cardGradient, radii16, Insets.EMPTY)));

        card.setBorder(new Border(new BorderStroke(
                Color.rgb(120, 210, 255, 0.3),
                BorderStrokeStyle.SOLID,
                radii16,
                new BorderWidths(2))));

        card.setEffect(new DropShadow(BlurType.GAUSSIAN, Color.rgb(0, 0, 0, 0.8), 20, 0.5, 0, 4));

        Label titleLabel = new Label("Game Paused");
        titleLabel.setTextFill(Color.WHITE);
        titleLabel.setFont(AssetManager.getFont("font_kenvector_future", 28));
        titleLabel.setEffect(new DropShadow(BlurType.GAUSSIAN, Color.rgb(77, 141, 182, 0.8), 10, 0.5, 0, 0));

        // 1. Nút Tiếp tục (Resume)
        CustomButton btnResume = new CustomButton("Resume", "button_blue", () -> {
            showPauseMenu(false);
            if (gameManager != null) {
                gameManager.resumeGame();
            }
        });

        // 2. Nút Chơi lại (Restart)
        CustomButton btnRestart = new CustomButton("PLAY AGAIN", "button_blue", () -> {
            showPauseMenu(false);
            AudioManager.getInstance().playMusic("background_play_music");
            if (gameManager != null) {
                gameManager.restartGame();
            }
        });

        // 3. Nút Âm thanh (Sound Toggle)
        String soundStatus = AudioManager.getInstance().isMuted() ? "Music: Off" : "Music: On";
        btnSound = new CustomButton(soundStatus, "button_blue", () -> {
            AudioManager.getInstance().toggleMute();
            if (AudioManager.getInstance().isMuted()) {
                btnSound.updateLabel("Music: Off");
            } else {
                btnSound.updateLabel("Music: On");
            }
        });

        // 4. Nút Về Trang Chủ (Main Menu)
        CustomButton btnMainMenu = new CustomButton("Home", "button_blue", () -> {
            if (gameManager != null) {
                gameManager.stopGame();
            }
            SceneManager.getInstance().switchScene("MenuScene");
        });

        card.getChildren().addAll(titleLabel, btnResume, btnRestart, btnSound, btnMainMenu);
        pauseOverlay.getChildren().add(card);

        root.getChildren().add(pauseOverlay);
    }

    private void showPauseMenu(boolean show) {
        pauseOverlay.setVisible(show);
        if (show) {
            pauseOverlay.toFront();
        }
    }

    public void updateHUD(int score, int gold, Player player) {
        if (goldLabel != null) {
            goldLabel.setText("GOLD: " + gold);
        }
        updateHUD(score, player);
    }

    public void updateHUD(int score, Player player) {
        if (scoreLabel != null) {
            scoreLabel.setText("SCORE: " + score);
        }
        if (player != null) {
            if (healthBar != null && healthLabel != null) {
                double healthPercent = (double) player.getHealth() / player.getMaxHealth();
                healthBar.setProgress(Math.max(0, healthPercent));
                healthLabel.setText("HP: " + player.getHealth() + " / " + player.getMaxHealth());
            }

            if (levelLabel != null && xpBar != null && xpLabel != null) {
                if (player.getLevel() >= player.getMaxLevel()) {
                    levelLabel.setText("LVL MAX");
                    xpBar.setProgress(1.0);
                    xpLabel.setText("XP: MAX");
                } else {
                    levelLabel.setText("LV " + player.getLevel());
                    double xpPercent = (double) player.getCurrentXp() / player.getXpToNextLevel();
                    xpBar.setProgress(Math.max(0, Math.min(1.0, xpPercent)));
                    xpLabel.setText("XP: " + player.getCurrentXp() + " / " + player.getXpToNextLevel());
                }
            }

            if (buffContainer != null) {
                buffContainer.getChildren().clear();
                long nowMs = System.currentTimeMillis();
                boolean blinkState = (nowMs / 250) % 2 == 0;

                if (player.isShieldActive()) {
                    Image iconImg = AssetManager.getImage("ui_icon_shield_active");
                    if (iconImg != null) {
                        ImageView iconView = new ImageView(iconImg);
                        iconView.setFitWidth(32);
                        iconView.setFitHeight(32);
                        iconView.setPreserveRatio(true);

                        long shieldMs = player.getShieldBuffTimeRemaining();
                        if (shieldMs <= 3000) {
                            iconView.setOpacity(blinkState ? 1.0 : 0.15);
                        } else {
                            iconView.setOpacity(1.0);
                        }
                        buffContainer.getChildren().add(iconView);
                    }
                }

                if (player.isSeekerActive()) {
                    Image iconImg = AssetManager.getImage("ui_icon_lightning_active");
                    if (iconImg != null) {
                        ImageView iconView = new ImageView(iconImg);
                        iconView.setFitWidth(32);
                        iconView.setFitHeight(32);
                        iconView.setPreserveRatio(true);

                        long seekerMs = player.getSeekerBuffTimeRemaining();
                        if (seekerMs <= 3000) {
                            iconView.setOpacity(blinkState ? 1.0 : 0.15);
                        } else {
                            iconView.setOpacity(1.0);
                        }
                        buffContainer.getChildren().add(iconView);
                    }
                }
            }
        }
    }

    public void updateBossHUD(com.nhom27.skyforce.entities.base.BossObject boss) {
        if (bossHealthBox == null)
            return;
        if (boss != null && boss.isAlive()) {
            bossHealthBox.setVisible(true);
            if (bossNameLabel != null) {
                bossNameLabel.setText(boss.getBossName());
            }
            if (bossHealthBar != null) {
                bossHealthBar.setProgress(boss.getHealthPercentage());
            }
        } else {
            bossHealthBox.setVisible(false);
        }
    }

    public void showGameOverMenu(int score) {
        showGameOverMenu(score, 0);
    }

    public void showGameOverMenu(int score, int goldGained) {
        AudioManager.getInstance().playMusicOnce("lose");
        gameOverOverlay = new StackPane();
        gameOverOverlay.setPrefSize(Main.WIDTH, Main.HEIGHT);
        gameOverOverlay.setBackground(
                new Background(new BackgroundFill(Color.rgb(0, 0, 0, 0.75), CornerRadii.EMPTY, Insets.EMPTY)));

        VBox card = new VBox(15);
        card.setAlignment(Pos.CENTER);
        card.setMaxSize(400, 400);

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

        Label goldGainedLabel = new Label("GOLD EARNED: +" + goldGained);
        goldGainedLabel.setTextFill(Color.GOLD);
        goldGainedLabel.setFont(AssetManager.getFont("font_kenvector_future", 16));

        int totalGold = PlayerDataManager.getInstance().getTotalGold();
        Label totalGoldLabel = new Label("TOTAL GOLD: " + totalGold);
        totalGoldLabel.setTextFill(Color.YELLOW);
        totalGoldLabel.setFont(AssetManager.getFont("font_kenvector_future", 14));

        CustomButton btnRestart = new CustomButton("PLAY AGAIN", "button_blue", () -> {
            gamePane.getChildren().remove(gameOverOverlay);
            AudioManager.getInstance().playMusic("background_play_music");
            gameManager.restartGame();
        });

        CustomButton btnMainMenu = new CustomButton("HOME MENU", "button_blue", () -> {
            SceneManager.getInstance().switchScene("MenuScene");
        });

        card.getChildren().addAll(titleLabel, finalScoreLabel, goldGainedLabel, totalGoldLabel, btnRestart,
                btnMainMenu);

        gameOverOverlay.getChildren().add(card);
        gamePane.getChildren().add(gameOverOverlay);

    }

    public void showWinMenu(int score, int goldGained) {
        StackPane winOverlay = new StackPane();
        winOverlay.setPrefSize(Main.WIDTH, Main.HEIGHT);
        winOverlay.setBackground(
                new Background(new BackgroundFill(Color.rgb(0, 0, 0, 0.75), CornerRadii.EMPTY, Insets.EMPTY)));

        VBox card = new VBox(15);
        card.setAlignment(Pos.CENTER);
        card.setMaxSize(400, 400);

        LinearGradient cardGradient = new LinearGradient(
                0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#1e3799")),
                new Stop(1, Color.web("#0c2461")));
        CornerRadii radii = new CornerRadii(16);
        card.setBackground(new Background(new BackgroundFill(cardGradient, radii, Insets.EMPTY)));
        card.setBorder(new Border(
                new BorderStroke(Color.web("#f6b93b"), BorderStrokeStyle.SOLID, radii, new BorderWidths(2))));

        int currentLvl = (gameManager != null) ? gameManager.getCurrentStageLevel() : 1;
        Label titleLabel = new Label("LEVEL " + currentLvl + " CLEARED!");
        titleLabel.setTextFill(Color.GOLD);
        titleLabel.setFont(AssetManager.getFont("font_kenvector_future", 26));

        Label finalScoreLabel = new Label("FINAL SCORE: " + score);
        finalScoreLabel.setTextFill(Color.WHITE);
        finalScoreLabel.setFont(AssetManager.getFont("font_kenvector_future", 18));

        Label goldGainedLabel = new Label("GOLD EARNED: +" + goldGained);
        goldGainedLabel.setTextFill(Color.YELLOW);
        goldGainedLabel.setFont(AssetManager.getFont("font_kenvector_future", 16));

        int totalGold = PlayerDataManager.getInstance().getTotalGold();
        Label totalGoldLabel = new Label("TOTAL GOLD: " + totalGold);
        totalGoldLabel.setTextFill(Color.CYAN);
        totalGoldLabel.setFont(AssetManager.getFont("font_kenvector_future", 14));

        CustomButton btnNextLevel;
        if (currentLvl < 9) {
            int nextLvl = currentLvl + 1;
            btnNextLevel = new CustomButton("NEXT LEVEL (LVL " + nextLvl + ")", "button_green", () -> {
                gamePane.getChildren().remove(winOverlay);
                AudioManager.getInstance().playMusic("background_play_music");
                if (gameManager != null) {
                    gameManager.setCurrentStageLevel(nextLvl);
                    gameManager.restartGame();
                }
            });
        } else {
            btnNextLevel = new CustomButton("PLAY AGAIN", "button_yellow", () -> {
                gamePane.getChildren().remove(winOverlay);
                AudioManager.getInstance().playMusic("background_play_music");
                if (gameManager != null) {
                    gameManager.setCurrentStageLevel(1);
                    gameManager.restartGame();
                }
            });
        }

        CustomButton btnMainMenu = new CustomButton("HOME MENU", "button_blue", () -> {
            if (gameManager != null) {
                gameManager.stopGame();
            }
            SceneManager.getInstance().switchScene("MenuScene");
        });

        card.getChildren().addAll(titleLabel, finalScoreLabel, goldGainedLabel, totalGoldLabel, btnNextLevel,
                btnMainMenu);

        winOverlay.getChildren().add(card);
        gamePane.getChildren().add(winOverlay);
    }

    public void showWarningBanner(boolean show) {
        if (warningBanner == null) {
            warningBanner = new StackPane();
            warningBanner.setMaxHeight(80); // Chiều cao của dải băng

            // 1. Tạo nền Gradient Đỏ - Đen mang cảm giác báo động
            LinearGradient bgGradient = new LinearGradient(
                    0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                    new Stop(0, Color.web("#3a0000", 0.7)), // Đỏ mờ ở viền trên
                    new Stop(0.5, Color.web("#ff0000", 0.4)), // Đỏ sáng ở giữa
                    new Stop(1, Color.web("#3a0000", 0.7)) // Đỏ mờ ở viền dưới
            );
            warningBanner
                    .setBackground(new Background(new BackgroundFill(bgGradient, CornerRadii.EMPTY, Insets.EMPTY)));

            // 2. Thêm viền đỏ rực ở cạnh trên và dưới của dải băng
            warningBanner.setBorder(new Border(new BorderStroke(
                    Color.RED, Color.RED, Color.RED, Color.RED,
                    BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE,
                    CornerRadii.EMPTY, new BorderWidths(2, 0, 2, 0), Insets.EMPTY)));

            // 3. Chữ cảnh báo (Thêm icon tam giác cảnh báo cho ngầu)
            Label textLabel = new Label("⚠ WARNING: BOSS APPROACHING ⚠");
            textLabel.setFont(AssetManager.getFont("font_kenvector_future", 32));
            textLabel.setTextFill(Color.WHITE);

            // Hiệu ứng chữ phát sáng đỏ kết hợp vàng
            DropShadow glow = new DropShadow(BlurType.GAUSSIAN, Color.RED, 20, 0.8, 0, 0);
            DropShadow innerGlow = new DropShadow(BlurType.GAUSSIAN, Color.YELLOW, 5, 0.5, 0, 0);
            glow.setInput(innerGlow);
            textLabel.setEffect(glow);

            // 4. Lắp ráp và Căn giữa
            warningBanner.getChildren().add(textLabel);
            warningBanner.setMouseTransparent(true);
            StackPane.setAlignment(warningBanner, Pos.CENTER);

            // 5. Tạo Animation nhấp nháy chớp tắt (Nhịp đập dồn dập)
            warningAnimation = new javafx.animation.FadeTransition(javafx.util.Duration.millis(350), warningBanner);
            warningAnimation.setFromValue(0.3); // Mờ 30%
            warningAnimation.setToValue(1.0); // Sáng 100%
            warningAnimation.setCycleCount(javafx.animation.Animation.INDEFINITE);
            warningAnimation.setAutoReverse(true); // Tự động đảo chiều (sáng -> mờ -> sáng)

            root.getChildren().add(warningBanner);
        }

        warningBanner.setVisible(show);
        if (show) {
            warningBanner.toFront();
            warningAnimation.play(); // Bật nhấp nháy khi hiện
        } else {
            warningAnimation.stop(); // Tắt nhấp nháy khi ẩn để tiết kiệm CPU
        }
    }

    public Scene getScene() {
        return scene;
    }

    public GameManager getGameManager() {
        return gameManager;
    }
}
