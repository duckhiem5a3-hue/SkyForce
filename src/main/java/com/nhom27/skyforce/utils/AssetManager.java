package com.nhom27.skyforce.utils;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.text.Font;

public class AssetManager {

    public static final Map<String, SpriteInfo> spriteRegistry = new HashMap<>();
    public static final Map<String, Media> musics = new HashMap<>();
    public static final Map<String, AudioClip> sounds = new HashMap<>();
    public static final Map<String, String> stylesheets = new HashMap<>();
    public static final Map<String, Font> fonts = new HashMap<>();

    public static void loadImage(String name, String path) {
        if (!spriteRegistry.containsKey(name)) {
            URL imageUrl = AssetManager.class.getResource(path);
            if (imageUrl != null) {
                Image image = new Image(imageUrl.toString());
                SpriteInfo spriteInfo = new SpriteInfo(image);
                spriteRegistry.put(name, spriteInfo);
                System.out.println("Tải thành công ảnh: " + name);
            } else {
                System.out.println("Lỗi tải ảnh: " + name);
            }
        }
    }

    public static Image getImage(String name) {
        if (!spriteRegistry.containsKey(name)) {
            System.out.println("Ảnh " + name + " chưa được lưu vào kho!");
            return null;
        }
        return spriteRegistry.get(name).getImage();
    }

    public static void loadSpriteInfo(String name, String path, double[] hitbox) {
        if (!spriteRegistry.containsKey(name)) {
            URL imageUrl = AssetManager.class.getResource(path);
            if (imageUrl != null) {
                Image image = new Image(imageUrl.toString());
                SpriteInfo spriteInfo = new SpriteInfo(image, hitbox);
                spriteRegistry.put(name, spriteInfo);
                System.out.println("Tải thành công ảnh: " + name + " và hitbox.");
            } else {
                System.out.println("Lỗi tải ảnh: " + name + "và hitbox");
            }
        }
    }

    public static SpriteInfo getSpriteInfo(String name) {
        if (!spriteRegistry.containsKey(name)) {
            System.out.println("Ảnh " + name + " chưa được lưu vào kho!");
            return null;
        }
        return spriteRegistry.get(name);
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

    public static void loadSound(String name, String path) {
        if (!sounds.containsKey(name)) {
            URL musicUrl = AssetManager.class.getResource(path);
            if (musicUrl != null) {
                AudioClip sound = new AudioClip(musicUrl.toString());
                sounds.put(name, sound);
                System.out.println("Tải thành công âm thanh: " + name);
            } else {
                System.out.println("Lỗi tải nhạc: " + name);
            }
        }
    }

    public static AudioClip getSound(String name) {
        if (!sounds.containsKey(name)) {
            System.out.println("Âm thanh " + name + " chưa được lưu vào kho!");
            return null;
        }
        return sounds.get(name);
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
        loadSpriteInfo("player_ship_1", "/com/nhom27/skyforce/textures/entities/player/player_ship_1_blue.png",
                new double[] {
                        46, 0,
                        52, 0,
                        98, 60,
                        98, 65,
                        75, 65,
                        62, 75,
                        36, 75,
                        23, 65,
                        0, 65,
                        0, 60,
                        46, 0
                });
        loadImage("Spaceship1Blue", "/com/nhom27/skyforce/textures/Spaceship_01_BLUE.png");
        loadImage("enemy_ship_1", "/com/nhom27/skyforce/textures/Spaceship_01_RED.png");
        loadImage("enemy_ship_2", "/com/nhom27/skyforce/textures/Spaceship_02_RED.png");
        loadImage("enemy_ship_3", "/com/nhom27/skyforce/textures/Spaceship_03_RED.png");
        loadImage("powerup", "/com/nhom27/skyforce/textures/Flame_01.png");
        loadImage("bullet_player_1", "/com/nhom27/skyforce/textures/entities/weapons/laserBlue01.png");
        loadImage("bullet_player_2", "/com/nhom27/skyforce/textures/entities/weapons/laserBlue02.png");
        loadSpriteInfo("enemy_straight", "/com/nhom27/skyforce/textures/entities/enemies/enemy_straight_black.png",
                new double[] {
                        16, 0,
                        36, 8,
                        38, 17,
                        55, 17,
                        57, 8,
                        76, 0,
                        92, 25,
                        71, 83,
                        57, 78,
                        62, 58,
                        29, 58,
                        34, 78,
                        22, 83,
                        0, 25
                });
        loadSpriteInfo("enemy_sine_orbit", "/com/nhom27/skyforce/textures/entities/enemies/enemy_sine_orbit_black.png",
                new double[] {
                        2, 2,
                        17, 0,
                        37, 13,
                        61, 13,
                        78, 0,
                        93, 2,
                        76, 74,
                        66, 74,
                        66, 78,
                        60, 83,
                        36, 83,
                        30, 78,
                        30, 74,
                        20, 74
                });

        loadImage("item_pill_blue", "/com/nhom27/skyforce/textures/powerups/item_pill_blue.png");

        // Hiệu ứng
        loadImage("vfx_impact_blue_01", "/com/nhom27/skyforce/textures/vfx/vfx_impact_blue_01.png");
        loadImage("vfx_impact_blue_02", "/com/nhom27/skyforce/textures/vfx/vfx_impact_blue_02.png");
        loadImage("vfx_explosion_8x8_sheet", "/com/nhom27/skyforce/textures/vfx/vfx_explosion_8x8_sheet.png");

        System.out.println("Tải ảnh hoàn tất!");

        System.out.println("Đang tải các tài nguyên Âm Thanh: ");
        loadMusic("background_home_music", "/com/nhom27/skyforce/audio/music/background.mp3");
        loadMusic("background_play_music", "/com/nhom27/skyforce/audio/music/background_play.mp3");
        loadMusic("lose", "/com/nhom27/skyforce/audio/music/lose.mp3");

        loadSound("sfx_laser", "/com/nhom27/skyforce/audio/sfx/sfx_laser.mp3");
        loadSound("sfx_explosion_enemy", "/com/nhom27/skyforce/audio/sfx/sfx_explosion_enemy.mp3");
        loadSound("sfx_zap", "/com/nhom27/skyforce/audio/sfx/sfx_zap.mp3");
        loadSound("sfx_laser_impact", "/com/nhom27/skyforce/audio/sfx/sfx_laser_impact.mp3");
        loadSound("sfx_item_health_pickup", "/com/nhom27/skyforce/audio/sfx/sfx_item_health_pickup.mp3");
        System.out.println("Tải nhạc hoàn tất!");

        System.out.println("Đang tải các tài nguyên Font: ");
        loadFont("font_kenvector_future", "/com/nhom27/skyforce/fonts/kenvector_future.ttf");
        loadFont("font_kenvector_future_thin", "/com/nhom27/skyforce/fonts/kenvector_future_thin.ttf");
        System.out.println("Tải Font hoàn tất!");
    }

}
