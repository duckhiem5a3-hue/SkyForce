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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

public class ShopScene {
    private static ShopScene instance;
    private Scene scene;
    private Label totalGoldLabel;
    private VBox shopCardsContainer;

    private static class SkinInfo {
        String id;
        String name;
        int price;
        String previewKey;

        SkinInfo(String id, String name, int price, String previewKey) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.previewKey = previewKey;
        }
    }

    private final List<SkinInfo> skinList = new ArrayList<>();

    private ShopScene() {
        initSkinData();
        createShopScene();
    }

    public static ShopScene getInstance() {
        if (instance == null) {
            instance = new ShopScene();
        }
        return instance;
    }

    private void initSkinData() {
        skinList.add(new SkinInfo("blue", "Blue Falcon", 0, "player_ship_lv1_blue_idle"));
        skinList.add(new SkinInfo("green", "Green Emerald", 100, "player_ship_lv1_green_idle"));
        skinList.add(new SkinInfo("orange", "Orange Flame", 200, "player_ship_lv1_orange_idle"));
        skinList.add(new SkinInfo("red", "Crimson Red", 300, "player_ship_lv1_red_idle"));
    }

    private void createShopScene() {
        StackPane root = new StackPane();

        // Nền game
        Image bgImage = AssetManager.getImage("background_home");
        if (bgImage != null) {
            ImageView bgImageView = new ImageView(bgImage);
            bgImageView.setFitWidth(Main.WIDTH);
            bgImageView.setFitHeight(Main.HEIGHT);
            root.getChildren().add(bgImageView);
        } else {
            root.setStyle("-fx-background-color: #0b131e;");
        }

        // Layout tổng VBox
        VBox mainLayout = new VBox(25);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(20));

        // Tiêu đề SHOP
        Label titleLabel = new Label("SKIN SHOP");
        titleLabel.setFont(AssetManager.getFont("font_kenvector_future", 36));
        titleLabel.setTextFill(Color.CYAN);
        titleLabel.setEffect(new DropShadow(BlurType.GAUSSIAN, Color.rgb(0, 200, 255, 0.8), 12, 0.8, 0, 2));

        // Nút hiển thị tổng số vàng ở trên cùng
        totalGoldLabel = new Label("GOLD: " + PlayerDataManager.getInstance().getTotalGold());
        totalGoldLabel.setFont(AssetManager.getFont("font_kenvector_future", 22));
        totalGoldLabel.setTextFill(Color.GOLD);
        totalGoldLabel.setEffect(new DropShadow(BlurType.GAUSSIAN, Color.BLACK, 6, 0.8, 0, 2));

        HBox topBar = new HBox(totalGoldLabel);
        topBar.setAlignment(Pos.CENTER);

        // Khung chứa danh sách thẻ skin (HBox hiển thị 4 thẻ ngang)
        shopCardsContainer = new VBox(20);
        shopCardsContainer.setAlignment(Pos.CENTER);
        refreshSkinCards();

        // Nút BACK
        CustomButton btnBack = new CustomButton("BACK", "button_blue", () -> {
            SceneManager.getInstance().switchScene("MenuScene");
        });

        mainLayout.getChildren().addAll(titleLabel, topBar, shopCardsContainer, btnBack);
        root.getChildren().add(mainLayout);

        scene = new Scene(root, Main.WIDTH, Main.HEIGHT);
    }

    private void refreshSkinCards() {
        shopCardsContainer.getChildren().clear();

        HBox cardsRow = new HBox(20);
        cardsRow.setAlignment(Pos.CENTER);

        PlayerDataManager dataMgr = PlayerDataManager.getInstance();
        String equippedSkin = dataMgr.getEquippedSkin();
        int currentGold = dataMgr.getTotalGold();

        for (SkinInfo skin : skinList) {
            VBox card = new VBox(12);
            card.setAlignment(Pos.CENTER);
            card.setPrefSize(180, 260);
            card.setPadding(new Insets(15));

            // Nền thẻ card mờ sang trọng
            LinearGradient cardGradient = new LinearGradient(
                    0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                    new Stop(0, Color.web("#1e2c3a", 0.9)),
                    new Stop(1, Color.web("#0f1722", 0.9)));
            CornerRadii radii16 = new CornerRadii(16);
            card.setBackground(new Background(new BackgroundFill(cardGradient, radii16, Insets.EMPTY)));

            boolean isEquipped = skin.id.equalsIgnoreCase(equippedSkin);
            boolean isUnlocked = dataMgr.isSkinUnlocked(skin.id);

            // Viền card tùy theo trạng thái
            Color borderColor = isEquipped ? Color.LIGHTGREEN : (isUnlocked ? Color.CYAN : Color.GRAY);
            card.setBorder(new Border(new BorderStroke(
                    borderColor, BorderStrokeStyle.SOLID, radii16, new BorderWidths(isEquipped ? 3 : 2))));
            card.setEffect(new DropShadow(BlurType.GAUSSIAN, Color.rgb(0, 0, 0, 0.6), 10, 0.5, 0, 2));

            // Hình xem trước tàu
            Image shipImg = AssetManager.getImage(skin.previewKey);
            ImageView shipView = new ImageView(shipImg);
            shipView.setFitHeight(75);
            shipView.setPreserveRatio(true);

            // Tên skin
            Label nameLabel = new Label(skin.name);
            nameLabel.setFont(AssetManager.getFont("font_kenvector_future", 13));
            nameLabel.setTextFill(Color.WHITE);

            // Giá skin
            Label priceLabel;
            if (skin.price == 0 || isUnlocked) {
                priceLabel = new Label("UNLOCKED");
                priceLabel.setTextFill(Color.LIGHTGREEN);
            } else {
                priceLabel = new Label(skin.price + " GOLD");
                priceLabel.setTextFill(Color.GOLD);
            }
            priceLabel.setFont(AssetManager.getFont("font_kenvector_future_thin", 12));

            // Nút bấm tương ứng
            CustomButton actionBtn;
            if (isEquipped) {
                actionBtn = new CustomButton("EQUIPPED", 150, 42, "button_green", () -> {
                });
            } else if (isUnlocked) {
                actionBtn = new CustomButton("EQUIP", 150, 42, "button_blue", () -> {
                    dataMgr.setEquippedSkin(skin.id);
                    onShown();
                });
            } else {
                String btnBg = (currentGold >= skin.price) ? "button_yellow" : "button_red";
                actionBtn = new CustomButton("BUY", 150, 42, btnBg, () -> {
                    boolean success = dataMgr.buySkin(skin.id, skin.price);
                    if (success) {
                        AudioManager.getInstance().playSound("sfx_item_health_pickup");
                        onShown();
                    } else {
                        AudioManager.getInstance().playSound("sfx_zap");
                    }
                });
            }

            card.getChildren().addAll(shipView, nameLabel, priceLabel, actionBtn);
            cardsRow.getChildren().add(card);
        }

        shopCardsContainer.getChildren().add(cardsRow);
    }

    public void onShown() {
        if (totalGoldLabel != null) {
            totalGoldLabel.setText("GOLD: " + PlayerDataManager.getInstance().getTotalGold());
        }
        refreshSkinCards();
    }

    public Scene getScene() {
        return scene;
    }
}
