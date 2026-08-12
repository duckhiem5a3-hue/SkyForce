package com.nhom27.skyforce.scenes;

import com.nhom27.skyforce.audio.AudioManager;
import com.nhom27.skyforce.entities.player.Player;
import com.nhom27.skyforce.main.Main;
import com.nhom27.skyforce.managers.GameManager;
import com.nhom27.skyforce.ui.buttons.CustomButton;
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
    private Label healthLabel;
    private ProgressBar healthBar;
    private Label levelLabel;
    private Label xpLabel;
    private ProgressBar xpBar;
    private Label waveLabel;
    private Label buffStatusLabel;

    public PlayScene() {
        AudioManager.getInstance().playMusic("background_play_music");
        root = new StackPane();

        setupBackground();
        setupGameWorld();
        setupHUD();

        // Tạo Pause Overlay Menu
        createPauseOverlay();

        scene = new Scene(root, com.nhom27.skyforce.main.Main.WIDTH, com.nhom27.skyforce.main.Main.HEIGHT);
    }

    private void setupBackground() {
        Image bgImage = AssetManager.getImage("background_play");
        ImageView bgImageView = null;

        if (bgImage != null) {
            bgImageView = new ImageView(bgImage);
            bgImageView.setFitWidth(com.nhom27.skyforce.main.Main.WIDTH);
            bgImageView.setFitHeight(com.nhom27.skyforce.main.Main.HEIGHT);
            root.getChildren().add(bgImageView);
        } else {
            System.out.println("Lỗi: Không tìm thấy ảnh nền play!");
            root.setStyle("-fx-background-color: black;");
        }
    }

    private void setupGameWorld() {
        gamePane = new Pane();
        gamePane.setPrefSize(com.nhom27.skyforce.main.Main.WIDTH, com.nhom27.skyforce.main.Main.HEIGHT);
        root.getChildren().add(gamePane);

        gameManager = new GameManager(gamePane, this);
        gameManager.startGame();

    }

    private void setupHUD() {
        StackPane hudLayout = new StackPane();

        // NÚT PAUSE
        CustomButton btnPause = new CustomButton(50, 50, "button_pause_blue", () -> {
            if (gameManager != null) {
                gameManager.pauseGame();
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

        // ==========================================
        // LẮP RÁP CÁC PHÂN VÙNG CHÍNH LÊN MÀN HÌNH
        // ==========================================

        // 1. Góc trên bên trái
        VBox topLeftPanel = new VBox(playerInfoRow, buffStatusLabel);
        topLeftPanel.setAlignment(Pos.TOP_LEFT);
        topLeftPanel.setPickOnBounds(false);

        // 2. Góc trên bên phải (Điểm số)
        scoreLabel = new Label("SCORE: 0");
        scoreLabel.setFont(AssetManager.getFont("font_kenvector_future", 22));
        scoreLabel.setTextFill(Color.GOLD);
        scoreLabel.setEffect(new DropShadow(BlurType.GAUSSIAN, Color.BLACK, 5, 0.8, 0, 2));

        HBox topRightPanel = new HBox(scoreLabel);
        topRightPanel.setAlignment(Pos.TOP_RIGHT);
        topRightPanel.setPickOnBounds(false);

        // 3. Phía trên ở giữa (Màn chơi)
        waveLabel = new Label("WAVE 1");
        waveLabel.setFont(AssetManager.getFont("font_kenvector_future", 24));
        waveLabel.setTextFill(Color.CYAN);
        waveLabel.setEffect(new DropShadow(BlurType.GAUSSIAN, Color.BLACK, 8, 0.8, 0, 2));

        HBox topCenterPanel = new HBox(waveLabel);
        topCenterPanel.setAlignment(Pos.TOP_CENTER);
        topCenterPanel.setPickOnBounds(false);

        // 4. Gom tất cả vào layout tổng
        hudLayout.getChildren().addAll(topLeftPanel, topCenterPanel, topRightPanel);
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

    public void updateHUD(int score, int wave, Player player) {
        if (scoreLabel != null) {
            scoreLabel.setText("SCORE: " + score);
        }
        if (waveLabel != null) {
            waveLabel.setText("WAVE " + wave);
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
        }
    }

    public void showGameOverMenu(int score) {
        AudioManager.getInstance().playMusicOnce("lose");
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
            gamePane.getChildren().remove(gameOverOverlay);
            AudioManager.getInstance().playMusic("background_play_music");
            gameManager.restartGame();
        });

        CustomButton btnMainMenu = new CustomButton("HOME MENU", "button_blue", () -> {
            SceneManager.getInstance().switchScene("MenuScene");
        });

        card.getChildren().addAll(titleLabel, finalScoreLabel, btnRestart, btnMainMenu);

        gameOverOverlay.getChildren().add(card);
        gamePane.getChildren().add(gameOverOverlay);
    }

    public Scene getScene() {
        return scene;
    }

    public GameManager getGameManager() {
        return gameManager;
    }
}
