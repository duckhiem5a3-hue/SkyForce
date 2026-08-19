package com.nhom27.skyforce.scenes;

import java.util.ArrayList;
import java.util.List;

import com.nhom27.skyforce.audio.AudioManager;
import com.nhom27.skyforce.main.Main;
import com.nhom27.skyforce.managers.PlayerDataManager;
import com.nhom27.skyforce.managers.SceneManager;
import com.nhom27.skyforce.ui.CustomButton;
import com.nhom27.skyforce.utils.AssetManager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class LevelScene {
    private static LevelScene instance;

    private Scene scene;
    private int selectedLevel = 1;

    // Lưu trữ các thanh UI (Row) và Tiêu đề để update trạng thái
    private final List<HBox> rowNodes = new ArrayList<>();
    private final List<Label> titleLabels = new ArrayList<>();

    // Tái sử dụng đối tượng hiệu ứng cho các thanh danh sách
    private CornerRadii rowRadii = new CornerRadii(15);
    private Background defaultBg = new Background(
            new BackgroundFill(Color.rgb(20, 30, 45, 0.8), rowRadii, Insets.EMPTY));
    private Background selectedBg = new Background(
            new BackgroundFill(Color.rgb(40, 60, 80, 0.9), rowRadii, Insets.EMPTY));

    private Border defaultBorder = new Border(
            new BorderStroke(Color.CYAN, BorderStrokeStyle.SOLID, rowRadii, new BorderWidths(2)));
    private Border selectedBorder = new Border(
            new BorderStroke(Color.GOLD, BorderStrokeStyle.SOLID, rowRadii, new BorderWidths(3)));
    private Border hoverBorder = new Border(
            new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, rowRadii, new BorderWidths(2)));

    private static class LevelInfo {
        int levelNumber;
        String name;

        LevelInfo(int levelNumber, String name) {
            this.levelNumber = levelNumber;
            this.name = name;
        }
    }

    private final List<LevelInfo> levelList = new ArrayList<>();

    private LevelScene() {
        initLevelData();
        createLevelScene();
    }

    public static LevelScene getInstance() {
        if (instance == null) {
            instance = new LevelScene();
        }
        return instance;
    }

    private void initLevelData() {
        // Đã xóa bỏ hoàn toàn tham số "imageName" không cần thiết
        levelList.add(new LevelInfo(1, "LEVEL 1 : THE BEGINNING"));
        levelList.add(new LevelInfo(2, "LEVEL 2 : RED SKY"));
        levelList.add(new LevelInfo(3, "LEVEL 3 : ASTEROID BELT"));
        levelList.add(new LevelInfo(4, "LEVEL 4 : IRON WALL"));
        levelList.add(new LevelInfo(5, "LEVEL 5 : FINAL STAND"));
        levelList.add(new LevelInfo(6, "LEVEL 6 : ENDLESS VOID"));
    }

    private void createLevelScene() {
        StackPane root = new StackPane();

        // 1. SETUP NỀN GAME CHUNG
        Image bgImage = AssetManager.getImage("background_home");
        if (bgImage != null) {
            ImageView bgImageView = new ImageView(bgImage);
            bgImageView.setFitWidth(Main.WIDTH);
            bgImageView.setFitHeight(Main.HEIGHT);
            root.getChildren().add(bgImageView);
        } else {
            root.setStyle("-fx-background-color: #0b131e;");
        }

        // 2. LAYOUT CHÍNH
        VBox mainLayout = new VBox(40);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(30));

        // Tiêu đề
        Label titleLabel = new Label("MISSION LOG");
        titleLabel.setFont(AssetManager.getFont("font_kenvector_future", 40));
        titleLabel.setTextFill(Color.CYAN);
        titleLabel.setEffect(new DropShadow(BlurType.GAUSSIAN, Color.rgb(0, 200, 255, 0.8), 12, 0.8, 0, 2));

        // 3. KHỞI TẠO DANH SÁCH (List VBox)
        VBox listContainer = new VBox(15); // Khoảng cách giữa các thanh
        listContainer.setAlignment(Pos.CENTER);
        listContainer.setMaxWidth(600); // Giới hạn độ rộng của danh sách

        initLevelListUI(listContainer);

        // 4. Tạo cụm nút dưới cùng
        HBox bottomButtons = setupBottomButtons();

        // Lắp ráp
        mainLayout.getChildren().addAll(titleLabel, listContainer, bottomButtons);
        root.getChildren().add(mainLayout);
        scene = new Scene(root, Main.WIDTH, Main.HEIGHT);
    }

    private void initLevelListUI(VBox container) {
        container.getChildren().clear();
        rowNodes.clear();
        titleLabels.clear();

        for (int i = 0; i < levelList.size(); i++) {
            LevelInfo level = levelList.get(i);

            // Tạo thanh ngang (Row) cho mỗi Level
            HBox row = new HBox();
            row.setAlignment(Pos.CENTER_LEFT);
            row.setPadding(new Insets(15, 30, 15, 30));
            row.setPrefHeight(60);
            row.setBackground(defaultBg);
            row.setBorder(defaultBorder);

            // Tên màn chơi
            Label nameLabel = new Label(level.name);
            nameLabel.setFont(AssetManager.getFont("font_kenvector_future", 22));
            nameLabel.setTextFill(Color.WHITE);

            // Một khoảng trống (Spacer) để đẩy Điểm kỷ lục sang tít mép bên phải
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            // Điểm kỷ lục
            int highScore = PlayerDataManager.getInstance().getHighScore(level.levelNumber);
            Label highScoreLabel = new Label("BEST: " + highScore);
            highScoreLabel.setFont(AssetManager.getFont("font_kenvector_future", 20));
            highScoreLabel.setTextFill(Color.web("#f39c12"));

            row.getChildren().addAll(nameLabel, spacer, highScoreLabel);

            // Sự kiện Click chọn
            row.setOnMouseClicked(e -> {
                AudioManager.getInstance().playSound("sfx_click");
                selectedLevel = level.levelNumber;
                updateLevelListState();
            });

            // Sự kiện Hover (Di chuột)
            row.setOnMouseEntered(e -> {
                if (selectedLevel != level.levelNumber) {
                    row.setBorder(hoverBorder);
                }
            });
            row.setOnMouseExited(e -> {
                if (selectedLevel != level.levelNumber) {
                    row.setBorder(defaultBorder);
                }
            });

            // Lưu trữ tham chiếu
            rowNodes.add(row);
            titleLabels.add(nameLabel);
            container.getChildren().add(row);
        }

        // Gọi hàm để tô màu dòng được chọn mặc định (Level 1)
        updateLevelListState();
    }

    private void updateLevelListState() {
        for (int i = 0; i < levelList.size(); i++) {
            LevelInfo level = levelList.get(i);
            HBox row = rowNodes.get(i);
            Label title = titleLabels.get(i);

            boolean isSelected = (level.levelNumber == selectedLevel);

            if (isSelected) {
                row.setBackground(selectedBg);
                row.setBorder(selectedBorder);
                row.setEffect(new DropShadow(BlurType.GAUSSIAN, Color.GOLD, 15, 0.5, 0, 0));
                title.setTextFill(Color.GOLD);
            } else {
                row.setBackground(defaultBg);
                row.setBorder(defaultBorder);
                row.setEffect(null);
                title.setTextFill(Color.WHITE);
            }
        }
    }

    private HBox setupBottomButtons() {
        HBox bottomButtons = new HBox(40);
        bottomButtons.setAlignment(Pos.CENTER);
        bottomButtons.setPadding(new Insets(20, 0, 0, 0));

        CustomButton btnBack = new CustomButton("BACK", "button_blue", () -> {
            SceneManager.getInstance().switchScene("MenuScene");
        });

        CustomButton btnPlay = new CustomButton("PLAY", "button_green", () -> {
            PlayScene playScene = new PlayScene(selectedLevel);
            SceneManager.getInstance().switchScene(playScene.getScene());
        });

        bottomButtons.getChildren().addAll(btnBack, btnPlay);
        return bottomButtons;
    }

    public Scene getScene() {
        return scene;
    }
}