package com.nhom27.skyforce.scenes;

import java.util.ArrayList;
import java.util.List;

import com.nhom27.skyforce.audio.AudioManager;
import com.nhom27.skyforce.main.Main;
import com.nhom27.skyforce.ui.buttons.CustomButton;
import com.nhom27.skyforce.utils.AssetManager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class LevelScene {
    private static LevelScene instance;
    private Scene scene;
    private HBox levelCardsContainer;
    
    private int selectedLevel = 1; // Mặc định chọn Level 1

    private static class LevelInfo {
        int levelNumber;
        String name;
        String imageName;

        LevelInfo(int levelNumber, String name, String imageName) {
            this.levelNumber = levelNumber;
            this.name = name;
            this.imageName = imageName;
        }
    }

    private final List<LevelInfo> levelList = new ArrayList<>();

    public LevelScene() {
        instance = this;
        initLevelData();
        createLevelScene();
    }

    public static LevelScene getInstance() {
        return instance;
    }

    private void initLevelData() {
        // Tên các key hình ảnh nền cần đảm bảo đã được load trong AssetManager
        levelList.add(new LevelInfo(1, "LEVEL 1", "background_level_1"));
        levelList.add(new LevelInfo(2, "LEVEL 2", "background_level_2"));
        levelList.add(new LevelInfo(3, "LEVEL 3", "background_level_3"));
    }

    private void createLevelScene() {
        StackPane root = new StackPane();

        // 1. Nền game (Lấy nền chung của menu)
        Image bgImage = AssetManager.getImage("background_home");
        if (bgImage != null) {
            ImageView bgImageView = new ImageView(bgImage);
            bgImageView.setFitWidth(Main.WIDTH);
            bgImageView.setFitHeight(Main.HEIGHT);
            root.getChildren().add(bgImageView);
        } else {
            root.setStyle("-fx-background-color: #0b131e;");
        }

        // 2. Layout tổng VBox
        VBox mainLayout = new VBox(30);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(20));

        // Tiêu đề
        Label titleLabel = new Label("CHOOSE LEVEL");
        titleLabel.setFont(AssetManager.getFont("font_kenvector_future", 36));
        titleLabel.setTextFill(Color.CYAN);
        titleLabel.setEffect(new DropShadow(BlurType.GAUSSIAN, Color.rgb(0, 200, 255, 0.8), 12, 0.8, 0, 2));

        // Khung chứa danh sách thẻ level (HBox hiển thị các thẻ level nằm ngang nhau)
        levelCardsContainer = new HBox(20);
        levelCardsContainer.setAlignment(Pos.CENTER);
        refreshLevelCards();

        // 3. Cụm nút bấm phía dưới (BACK và PLAY)
        HBox bottomButtons = new HBox(40);
        bottomButtons.setAlignment(Pos.CENTER);

        CustomButton btnBack = new CustomButton("BACK", "button_blue", () -> {
            SceneManager.getInstance().switchScene("MenuScene");
        });

        // Nút PLAY với hiệu ứng hover sáng rực
        CustomButton btnPlay = new CustomButton("PLAY", "button_green", () -> {
            // Khởi tạo PlayScene và truyền selectedLevel vào
            PlayScene playScene = new PlayScene();  //rồi chỗ này sẽ phải truyền level 
            SceneManager.getInstance().switchScene(playScene.getScene());
        });
        
        // Thêm hiệu ứng Hover cho 2 button
        btnBack.setOnMouseEntered(e -> btnPlay.setEffect(new Glow(0.8)));
        btnBack.setOnMouseExited(e -> btnPlay.setEffect(null));
        btnPlay.setOnMouseEntered(e -> btnPlay.setEffect(new Glow(0.8)));
        btnPlay.setOnMouseExited(e -> btnPlay.setEffect(null));

        bottomButtons.getChildren().addAll(btnBack, btnPlay);

        mainLayout.getChildren().addAll(titleLabel, levelCardsContainer, bottomButtons);
        root.getChildren().add(mainLayout);

        scene = new Scene(root, Main.WIDTH, Main.HEIGHT);
    }

    private void refreshLevelCards() {
        levelCardsContainer.getChildren().clear();

        for (LevelInfo level : levelList) {
            // Sử dụng StackPane làm thẻ Card để hình nền nằm dưới, chữ và nút đè lên trên
            StackPane card = new StackPane();
            card.setPrefSize(180, 260);
            
            // Cắt góc tròn cho viền card
            CornerRadii radii16 = new CornerRadii(16);
            
            // 1. Cắt (Clip) hình nền level cho vừa thẻ Card và bo góc
            Image levelBgImg = AssetManager.getImage(level.imageName);
            if (levelBgImg != null) {
                ImageView bgView = new ImageView(levelBgImg);
                // Ép kích thước img nền vừa khung card
                bgView.setFitWidth(180);
                bgView.setFitHeight(260);
                
                // Crop góc bo tròn cho ảnh để không tràn viền
                Rectangle clip = new Rectangle(180, 260);
                clip.setArcWidth(32);
                clip.setArcHeight(32);
                bgView.setClip(clip);
                
                card.getChildren().add(bgView);
            } else {
                // Nếu không load được ảnh, dùng nền xám đen fallback
                card.setBackground(new Background(new BackgroundFill(Color.web("#1e2c3a"), radii16, Insets.EMPTY)));
            }

            // 2. Viền Card (Vàng nếu đang chọn, Xanh nếu chưa chọn)
            boolean isSelected = (level.levelNumber == selectedLevel);
            Color borderColor = isSelected ? Color.GOLD : Color.CYAN;
            
            Border defaultBorder = new Border(new BorderStroke(
                    borderColor, BorderStrokeStyle.SOLID, radii16, new BorderWidths(isSelected ? 3 : 2)));  //viền dày hơn xíu với card đc chọn
            Border hoverBorder = new Border(new BorderStroke(
                    Color.GOLD, BorderStrokeStyle.SOLID, radii16, new BorderWidths(3)));     //

            card.setBorder(defaultBorder);
            card.setEffect(new DropShadow(BlurType.GAUSSIAN, Color.rgb(0, 0, 0, 0.8), 10, 0.5, 0, 2));

            // Bố cục phần bên trên của card (Tiêu đề Level và Select Button nằm bên dưới)
            VBox cardOverlay = new VBox(15);
            cardOverlay.setAlignment(Pos.CENTER);
            
            // Phủ một lớp bóng đen mờ đằng sau chữ để chữ không bị chìm vào nền ảnh sáng
            cardOverlay.setStyle("-fx-background-color: rgba(0,0,0, 0.4); -fx-background-radius: 16;");

            Label nameLabel = new Label(level.name);
            nameLabel.setFont(AssetManager.getFont("font_kenvector_future", 16));
            nameLabel.setTextFill(isSelected ? Color.GOLD : Color.WHITE);
            nameLabel.setEffect(new DropShadow(BlurType.GAUSSIAN, Color.BLACK, 4, 1.0, 0, 1));

            CustomButton btnSelect = new CustomButton(isSelected ? "SELECTED" : "SELECT", 
                                                      130, 40, 
                                                      isSelected ? "button_yellow" : "button_blue", 
                                                      () -> {
                // Khi bấm nút select -> cập nhật biến và vẽ lại toàn bộ thẻ
                AudioManager.getInstance().playSound("sfx_click");
                selectedLevel = level.levelNumber;
                refreshLevelCards();
            });

            // 4. XỬ LÝ HOVER THAY ĐỔI VIỀN
            // Khi di chuột vào nút, thẻ card đổi viền vàng (nếu chưa select)
            btnSelect.setOnMouseEntered(e -> {
                if (!isSelected) card.setBorder(hoverBorder);
            });
            btnSelect.setOnMouseExited(e -> {
                if (!isSelected) card.setBorder(defaultBorder);
            });

            cardOverlay.getChildren().addAll(nameLabel, btnSelect);
            card.getChildren().add(cardOverlay);
            
            levelCardsContainer.getChildren().add(card);
        }
    }

    public Scene getScene() {
        return scene;
    }
}