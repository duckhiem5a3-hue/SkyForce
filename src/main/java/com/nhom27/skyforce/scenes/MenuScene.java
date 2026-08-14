package com.nhom27.skyforce.scenes;

import com.nhom27.skyforce.audio.AudioManager;
import com.nhom27.skyforce.managers.PlayerDataManager;
import com.nhom27.skyforce.ui.buttons.CustomButton;
import com.nhom27.skyforce.utils.AssetManager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class MenuScene {
    private static MenuScene instance;
    private Scene scene; // Lưu trữ bản 2D
    private CustomButton btnSound;
    private Label totalGoldLabel;
    private Label highScoreLabel;

    public MenuScene() {
        instance = this;
        AudioManager.getInstance().playMusic("background_home_music");
        createMenuScene();
    }

    public static MenuScene getInstance() {
        return instance;
    }

    private void createMenuScene() {
        // Tạo một "tường" để có thể xếp các khung ảnh, nút lên
        StackPane root = new StackPane();
        // Tải ảnh vào bộ nhớ
        Image bgImage = AssetManager.getImage("background_home");
        // Tạo khung ảnh
        ImageView bgImageView = null;
        if (bgImage != null) {
            // Đóng khung ảnh
            bgImageView = new ImageView(bgImage);
            bgImageView.setFitWidth(com.nhom27.skyforce.main.Main.WIDTH);
            bgImageView.setFitHeight(com.nhom27.skyforce.main.Main.HEIGHT);
        } else {
            System.out.println("Lỗi: Không tìm thấy ảnh nền menu!");
            root.setStyle("-fx-background-color: black;");
        }

        Image logoImage = AssetManager.getImage("logo_game");
        ImageView logoImageView = null;
        if (logoImage != null) {
            logoImageView = new ImageView(logoImage);
            // Tùy chỉnh kích thước logo (ví dụ: rộng 400px, giữ nguyên tỉ lệ)
            logoImageView.setFitWidth(700);
            logoImageView.setPreserveRatio(true);
        } else {
            System.out.println("Lỗi: Không tìm thấy ảnh logo!");
        }

        // Khung hiển thị điểm cao nhất ở góc trên bên trái
        highScoreLabel = new Label("HIGH SCORE: " + PlayerDataManager.getInstance().getHighScore());
        highScoreLabel.setFont(AssetManager.getFont("font_kenvector_future", 20));
        highScoreLabel.setTextFill(Color.CYAN);
        highScoreLabel.setEffect(new DropShadow(BlurType.GAUSSIAN, Color.BLACK, 5, 0.8, 0, 2));

        HBox highScoreBox = new HBox(highScoreLabel);
        highScoreBox.setAlignment(Pos.TOP_LEFT);
        highScoreBox.setPadding(new Insets(20));
        highScoreBox.setPickOnBounds(false);

        // Khung hiển thị tổng số vàng ở góc trên bên phải
        totalGoldLabel = new Label("GOLD: " + PlayerDataManager.getInstance().getTotalGold());
        totalGoldLabel.setFont(AssetManager.getFont("font_kenvector_future", 20));
        totalGoldLabel.setTextFill(Color.GOLD);
        totalGoldLabel.setEffect(new DropShadow(BlurType.GAUSSIAN, Color.BLACK, 5, 0.8, 0, 2));

        HBox goldBox = new HBox(totalGoldLabel);
        goldBox.setAlignment(Pos.TOP_RIGHT);
        goldBox.setPadding(new Insets(20));
        goldBox.setPickOnBounds(false);

        // Tạo giá đỡ dọc cách nhau 20px để hiển thị các nút
        VBox menuBox = new VBox(20);
        // Căn giữa màn hình
        menuBox.setAlignment(Pos.CENTER);

        CustomButton btnPlay = new CustomButton("Play Game", "button_blue", () -> {
            PlayScene playScene = new PlayScene();
            SceneManager.getInstance().switchScene(playScene.getScene());
        });
        String soundStatus = AudioManager.getInstance().isMuted() ? "Music: Off" : "Music: On";
        btnSound = new CustomButton(soundStatus, "button_blue", () -> {
            AudioManager.getInstance().toggleMute();
            updateSoundButton();
        });
        CustomButton btnExit = new CustomButton("Exit", "button_blue", () -> {
            System.exit(0);
        });

        // Đặt logo (nếu có) và các nút bấm lên giá đỡ VBox
        if (logoImageView != null) {
            menuBox.getChildren().add(logoImageView);
            // Tăng khoảng cách phía dưới logo thêm 50px (bạn có thể thay đổi số 50 này)
            VBox.setMargin(logoImageView, new javafx.geometry.Insets(0, 0, 50, 0));
        }
        // Đặt nút bấm lên giá đỡ
        menuBox.getChildren().addAll(btnPlay, btnSound, btnExit);

        // Gắn khung ảnh, các nút lên "tường"
        if (bgImageView != null) {
            root.getChildren().addAll(bgImageView, menuBox, highScoreBox, goldBox);
        } else {
            root.getChildren().addAll(menuBox, highScoreBox, goldBox);
        }

        // Đóng gói "tường" thành "gói 2D"
        scene = new Scene(root, com.nhom27.skyforce.main.Main.WIDTH, com.nhom27.skyforce.main.Main.HEIGHT);
    }

    public void onShown() {
        updateSoundButton();
        updateGoldDisplay();
        updateHighScoreDisplay();
    }

    public void updateSoundButton() {
        if (btnSound != null) {
            btnSound.updateLabel(AudioManager.getInstance().isMuted() ? "Music: Off" : "Music: On");
        }
    }

    public void updateGoldDisplay() {
        if (totalGoldLabel != null) {
            totalGoldLabel.setText("GOLD: " + PlayerDataManager.getInstance().getTotalGold());
        }
    }

    public void updateHighScoreDisplay() {
        if (highScoreLabel != null) {
            highScoreLabel.setText("HIGH SCORE: " + PlayerDataManager.getInstance().getHighScore());
        }
    }

    public Scene getScene() {
        return scene;
    }
}
