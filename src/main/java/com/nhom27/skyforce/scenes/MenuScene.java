package com.nhom27.skyforce.scenes;

import com.nhom27.skyforce.audio.AudioManager;

import com.nhom27.skyforce.ui.buttons.CustomButton;
import com.nhom27.skyforce.utils.AssetManager;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class MenuScene {
    private static MenuScene instance;
    private Scene scene; // Lưu trữ bản 2D
    private CustomButton btnSound;

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
            root.getChildren().addAll(bgImageView, menuBox);
        } else {
            root.getChildren().add(menuBox);
        }

        // root.getChildren().add(plane.getView());

        // Đóng gói "tường" thành "gói 2D"
        scene = new Scene(root, com.nhom27.skyforce.main.Main.WIDTH, com.nhom27.skyforce.main.Main.HEIGHT);
    }

    public void onShown() {
        updateSoundButton();
        // Cập nhật thêm các thông số khác (điểm cao, tài nguyên...) khi hiển thị lại Menu
    }

    public void updateSoundButton() {
        if (btnSound != null) {
            btnSound.updateLabel(AudioManager.getInstance().isMuted() ? "Music: Off" : "Music: On");
        }
    }

    public Scene getScene() {
        return scene;
    }
}
