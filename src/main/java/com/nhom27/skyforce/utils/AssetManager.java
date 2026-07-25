package com.nhom27.skyforce.utils;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.text.Font;

public class AssetManager {

    public static final Map<String, Image> images = new HashMap<>();
    public static final Map<String, Media> musics = new HashMap<>();
    public static final Map<String, String> stylesheets = new HashMap<>();
    public static final Map<String, Font> fonts = new HashMap<>();

    public static void loadImage(String name, String path) {
        if (!images.containsKey(name)) {
            URL imageUrl = AssetManager.class.getResource(path);
            if (imageUrl != null) {
                Image image = new Image(imageUrl.toString());
                images.put(name, image);
                System.out.println("Tải thành công ảnh: " + name);
            } else {
                System.out.println("Lỗi tải ảnh: " + name);
            }
        }
    }

    public static Image getImage(String name) {
        if (!images.containsKey(name)) {
            System.out.println("Ảnh " + name + " chưa được lưu vào kho!");
            return null;
        }
        return images.get(name);
    }

    public static void loadMusic(String name, String path) {
        if (!musics.containsKey(name)) {
            URL musicUrl = AssetManager.class.getResource(path);
            if (musicUrl != null) {
                Media media = new Media(musicUrl.toString());
                musics.put(name, media);
                System.out.println("Tải thành công nhạc: " + name);
            } else {
                System.out.println("Lỗi tải nhạc: " + name);
            }
        }
    }

    public static Media getMusic(String name) {
        if (!musics.containsKey(name)) {
            System.out.println("Nhạc " + name + " chưa được lưu vào kho!");
            return null;
        }
        return musics.get(name);
    }

    public static void loadCss(String name, String path) {
        if (!stylesheets.containsKey(name)) {
            URL cssUrl = AssetManager.class.getResource(path);
            if (cssUrl != null) {
                stylesheets.put(name, cssUrl.toExternalForm());
                System.out.println("Tải thành công CSS: " + name);
            } else {
                System.out.println("Lỗi tải CSS: " + name);
            }
        }
    }

    public static String getCss(String name) {
        if (!stylesheets.containsKey(name)) {
            System.out.println("CSS " + name + " chưa được lưu vào kho!");
            return null;
        }
        return stylesheets.get(name);
    }

    public static void loadFont(String name, String path) {
        try {
            Font font = Font.loadFont(AssetManager.class.getResourceAsStream(path), 12);
            if (font != null) {
                fonts.put(name, font);
                System.out.println("Tải thành công font: " + font.getFamily() + " (" + font.getName() + ")");
            } else {
                System.out.println("Lỗi: Không thể load font từ đường dẫn " + path);
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi tải font: " + e.getMessage());
        }
    }

    public static Font getFont(String name, double size) {
        if (!fonts.containsKey(name)) {
            System.out.println("Font " + name + " chưa được lưu vào kho!");
            return Font.font("System", size);
        }
        return Font.font(fonts.get(name).getFamily(), size);
    }

    public static void loadAllAsset() {
        System.out.println("Đang tải các tài nguyên Ảnh: ");
        loadImage("logo_game", "/com/nhom27/skyforce/textures/menu/logoGame.png");
        // Ảnh nền
        loadImage("background_home", "/com/nhom27/skyforce/textures/menu/background.png");
        loadImage("background_play", "/com/nhom27/skyforce/textures/play/play_background.png");
        // Ảnh nút
        loadImage("button_blue", "/com/nhom27/skyforce/textures/menu/buttonBlue.png");
        loadImage("button_green", "/com/nhom27/skyforce/textures/menu/buttonGreen.png");
        loadImage("button_red", "/com/nhom27/skyforce/textures/menu/buttonRed.png");
        loadImage("button_yellow", "/com/nhom27/skyforce/textures/menu/buttonYellow.png");
        loadImage("button_square_blue", "/com/nhom27/skyforce/textures/menu/buttonSquareBlue.png");
        loadImage("button_pause_blue", "/com/nhom27/skyforce/textures/menu/buttonPauseBlue.png");

        // Thực thể
        loadImage("player_ship_1", "/com/nhom27/skyforce/textures/entities/player/player_ship_1_blue.png");
        loadImage("Spaceship1Blue", "/com/nhom27/skyforce/textures/Spaceship_01_BLUE.png");
        loadImage("enemy_ship_1", "/com/nhom27/skyforce/textures/Spaceship_01_RED.png");
        loadImage("enemy_ship_2", "/com/nhom27/skyforce/textures/Spaceship_02_RED.png");
        loadImage("enemy_ship_3", "/com/nhom27/skyforce/textures/Spaceship_03_RED.png");
        loadImage("powerup", "/com/nhom27/skyforce/textures/Flame_01.png");
        loadImage("bullet_img", "/com/nhom27/skyforce/textures/Flame_01.png");
        loadImage("enemy_straight", "/com/nhom27/skyforce/textures/entities/enemies/enemy_straight_black.png");
        loadImage("enemy_sine_orbit", "/com/nhom27/skyforce/textures/entities/enemies/enemy_sine_orbit_black.png");

        System.out.println("Tải ảnh hoàn tất!");

        System.out.println("Đang tải các tài nguyên Âm Thanh: ");
        loadMusic("background_home_music", "/com/nhom27/skyforce/sounds/background.mp3");
        System.out.println("Tải nhạc hoàn tất!");

        System.out.println("Đang tải các tài nguyên CSS: ");
        loadCss("styleButton", "/com/nhom27/skyforce/ui/buttons/styleButton.css");
        System.out.println("Tải CSS hoàn tất!");

        System.out.println("Đang tải các tài nguyên Font: ");
        loadFont("font_kenvector_future", "/com/nhom27/skyforce/fonts/kenvector_future.ttf");
        loadFont("font_kenvector_future_thin", "/com/nhom27/skyforce/fonts/kenvector_future_thin.ttf");
        System.out.println("Tải Font hoàn tất!");
    }

}
