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
                                System.out.println("Đang chuẩn bị load âm thanh: " + path); // path là tên cái biến chứa
                                                                                            // đường dẫn file
                                // Code cũ: AudioClip clip = new AudioClip(path);
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
                                System.out.println("Tải thành công font: " + font.getFamily() + " (" + font.getName()
                                                + ")");
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
                loadImage("background_level_1", "/com/nhom27/skyforce/textures/menu/background.png"); // tạm thời dùng
                                                                                                      // background menu
                loadImage("background_level_2", "/com/nhom27/skyforce/textures/menu/background.png");
                loadImage("background_level_3", "/com/nhom27/skyforce/textures/menu/background.png");
                loadImage("background_level_4", "/com/nhom27/skyforce/textures/menu/background.png");
                loadImage("background_level_5", "/com/nhom27/skyforce/textures/menu/background.png");
                loadImage("background_level_6", "/com/nhom27/skyforce/textures/menu/background.png");

                // Ảnh nút
                loadImage("button_blue", "/com/nhom27/skyforce/textures/menu/buttonBlue.png");
                loadImage("button_green", "/com/nhom27/skyforce/textures/menu/buttonGreen.png");
                loadImage("button_red", "/com/nhom27/skyforce/textures/menu/buttonRed.png");
                loadImage("button_yellow", "/com/nhom27/skyforce/textures/menu/buttonYellow.png");
                loadImage("button_square_blue", "/com/nhom27/skyforce/textures/menu/buttonSquareBlue.png");
                loadImage("button_pause_blue", "/com/nhom27/skyforce/textures/menu/buttonPauseBlue.png");

                // Thực thể
                loadSpriteInfo("player_ship_lv1_blue_idle",
                                "/com/nhom27/skyforce/textures/entities/player/lv1/player_ship_lv1_blue_idle.png",
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
                loadSpriteInfo("player_ship_lv1_green_idle",
                                "/com/nhom27/skyforce/textures/entities/player/lv1/player_ship_lv1_green_idle.png",
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
                loadSpriteInfo("player_ship_lv1_orange_idle",
                                "/com/nhom27/skyforce/textures/entities/player/lv1/player_ship_lv1_orange_idle.png",
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
                loadSpriteInfo("player_ship_lv1_red_idle",
                                "/com/nhom27/skyforce/textures/entities/player/lv1/player_ship_lv1_red_idle.png",
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

                loadSpriteInfo("player_ship_lv2_blue_idle",
                                "/com/nhom27/skyforce/textures/entities/player/lv2/player_ship_lv2_blue_idle.png",
                                new double[] {
                                                41, 0,
                                                57, 0,
                                                61, 26,
                                                85, 40,
                                                98, 32,
                                                95, 62,
                                                90, 59,
                                                62, 65,
                                                57, 74,
                                                41, 74,
                                                36, 65,
                                                8, 59,
                                                3, 62,
                                                0, 32,
                                                13, 40,
                                                37, 26
                                });
                loadSpriteInfo("player_ship_lv2_green_idle",
                                "/com/nhom27/skyforce/textures/entities/player/lv2/player_ship_lv2_green_idle.png",
                                new double[] {
                                                41, 0,
                                                57, 0,
                                                61, 26,
                                                85, 40,
                                                98, 32,
                                                95, 62,
                                                90, 59,
                                                62, 65,
                                                57, 74,
                                                41, 74,
                                                36, 65,
                                                8, 59,
                                                3, 62,
                                                0, 32,
                                                13, 40,
                                                37, 26
                                });
                loadSpriteInfo("player_ship_lv2_orange_idle",
                                "/com/nhom27/skyforce/textures/entities/player/lv2/player_ship_lv2_orange_idle.png",
                                new double[] {
                                                41, 0,
                                                57, 0,
                                                61, 26,
                                                85, 40,
                                                98, 32,
                                                95, 62,
                                                90, 59,
                                                62, 65,
                                                57, 74,
                                                41, 74,
                                                36, 65,
                                                8, 59,
                                                3, 62,
                                                0, 32,
                                                13, 40,
                                                37, 26
                                });
                loadSpriteInfo("player_ship_lv2_red_idle",
                                "/com/nhom27/skyforce/textures/entities/player/lv2/player_ship_lv2_red_idle.png",
                                new double[] {
                                                41, 0,
                                                57, 0,
                                                61, 26,
                                                85, 40,
                                                98, 32,
                                                95, 62,
                                                90, 59,
                                                62, 65,
                                                57, 74,
                                                41, 74,
                                                36, 65,
                                                8, 59,
                                                3, 62,
                                                0, 32,
                                                13, 40,
                                                37, 26
                                });

                loadSpriteInfo("player_ship_lv3_blue_idle",
                                "/com/nhom27/skyforce/textures/entities/player/lv3/player_ship_lv3_blue_idle.png",
                                new double[] {
                                                52, 0, // Mũi máy bay (Trái)
                                                60, 0, // Mũi máy bay (Phải)
                                                70, 20, // Khớp cánh trên (Phải)
                                                111, 41, // Mũi cánh ngoài (Phải) - Max Width
                                                94, 74, // Đuôi cánh ngoài (Phải)
                                                74, 66, // Khớp đuôi (Phải) - Phần lùi lõm vào
                                                68, 74, // Động cơ (Phải)
                                                44, 74, // Động cơ (Trái)
                                                38, 66, // Khớp đuôi (Trái) - Phần lùi lõm vào
                                                18, 74, // Đuôi cánh ngoài (Trái)
                                                1, 41, // Mũi cánh ngoài (Trái) - Min Width
                                                42, 20 // Khớp cánh trên (Trái)
                                });
                loadSpriteInfo("player_ship_lv3_green_idle",
                                "/com/nhom27/skyforce/textures/entities/player/lv3/player_ship_lv3_green_idle.png",
                                new double[] {
                                                52, 0, // Mũi máy bay (Trái)
                                                60, 0, // Mũi máy bay (Phải)
                                                70, 20, // Khớp cánh trên (Phải)
                                                111, 41, // Mũi cánh ngoài (Phải) - Max Width
                                                94, 74, // Đuôi cánh ngoài (Phải)
                                                74, 66, // Khớp đuôi (Phải) - Phần lùi lõm vào
                                                68, 74, // Động cơ (Phải)
                                                44, 74, // Động cơ (Trái)
                                                38, 66, // Khớp đuôi (Trái) - Phần lùi lõm vào
                                                18, 74, // Đuôi cánh ngoài (Trái)
                                                1, 41, // Mũi cánh ngoài (Trái) - Min Width
                                                42, 20 // Khớp cánh trên (Trái)
                                });
                loadSpriteInfo("player_ship_lv3_orange_idle",
                                "/com/nhom27/skyforce/textures/entities/player/lv3/player_ship_lv3_orange_idle.png",
                                new double[] {
                                                52, 0, // Mũi máy bay (Trái)
                                                60, 0, // Mũi máy bay (Phải)
                                                70, 20, // Khớp cánh trên (Phải)
                                                111, 41, // Mũi cánh ngoài (Phải) - Max Width
                                                94, 74, // Đuôi cánh ngoài (Phải)
                                                74, 66, // Khớp đuôi (Phải) - Phần lùi lõm vào
                                                68, 74, // Động cơ (Phải)
                                                44, 74, // Động cơ (Trái)
                                                38, 66, // Khớp đuôi (Trái) - Phần lùi lõm vào
                                                18, 74, // Đuôi cánh ngoài (Trái)
                                                1, 41, // Mũi cánh ngoài (Trái) - Min Width
                                                42, 20 // Khớp cánh trên (Trái)
                                });
                loadSpriteInfo("player_ship_lv3_red_idle",
                                "/com/nhom27/skyforce/textures/entities/player/lv3/player_ship_lv3_red_idle.png",
                                new double[] {
                                                52, 0, // Mũi máy bay (Trái)
                                                60, 0, // Mũi máy bay (Phải)
                                                70, 20, // Khớp cánh trên (Phải)
                                                111, 41, // Mũi cánh ngoài (Phải) - Max Width
                                                94, 74, // Đuôi cánh ngoài (Phải)
                                                74, 66, // Khớp đuôi (Phải) - Phần lùi lõm vào
                                                68, 74, // Động cơ (Phải)
                                                44, 74, // Động cơ (Trái)
                                                38, 66, // Khớp đuôi (Trái) - Phần lùi lõm vào
                                                18, 74, // Đuôi cánh ngoài (Trái)
                                                1, 41, // Mũi cánh ngoài (Trái) - Min Width
                                                42, 20 // Khớp cánh trên (Trái)
                                });
                loadSpriteInfo("player_ship_lv3_blue_idle",
                                "/com/nhom27/skyforce/textures/entities/player/lv3/player_ship_lv3_blue_idle.png",
                                new double[] {
                                                52, 0, // Mũi máy bay (Trái)
                                                60, 0, // Mũi máy bay (Phải)
                                                70, 20, // Khớp cánh trên (Phải)
                                                111, 41, // Mũi cánh ngoài (Phải) - Max Width
                                                94, 74, // Đuôi cánh ngoài (Phải)
                                                74, 66, // Khớp đuôi (Phải) - Phần lùi lõm vào
                                                68, 74, // Động cơ (Phải)
                                                44, 74, // Động cơ (Trái)
                                                38, 66, // Khớp đuôi (Trái) - Phần lùi lõm vào
                                                18, 74, // Đuôi cánh ngoài (Trái)
                                                1, 41, // Mũi cánh ngoài (Trái) - Min Width
                                                42, 20 // Khớp cánh trên (Trái)
                                });

                loadSpriteInfo("enemy_normal_blue",
                                "/com/nhom27/skyforce/textures/entities/enemy_normal_blue.png", new double[] {
                                                21, 0, // Mũi trái
                                                62, 0, // Mũi phải
                                                80, 13, // Đỉnh cánh phải
                                                81, 64, // Đáy cánh phải
                                                54, 82, // Đuôi phải
                                                29, 82, // Đuôi trái
                                                2, 64, // Đáy cánh trái
                                                3, 13 // Đỉnh cánh trái
                                });
                loadSpriteInfo("enemy_normal_red",
                                "/com/nhom27/skyforce/textures/entities/enemy_normal_red.png", new double[] {
                                                21, 0, // Mũi trái
                                                62, 0, // Mũi phải
                                                80, 13, // Đỉnh cánh phải
                                                81, 64, // Đáy cánh phải
                                                54, 82, // Đuôi phải
                                                29, 82, // Đuôi trái
                                                2, 64, // Đáy cánh trái
                                                3, 13 // Đỉnh cánh trái
                                });
                loadSpriteInfo("boss_mini_red", "/com/nhom27/skyforce/textures/entities/boss_mini_red.png",
                                new double[] {
                                                36, 1, // Mũi trái
                                                56, 1, // Mũi phải
                                                62, 9, // Cạnh mũi phải
                                                64, 61, // Thân phải
                                                88, 69, // Đỉnh cánh phải
                                                92, 112, // Mũi cánh phải
                                                64, 146, // Đuôi phải
                                                46, 124, // Lõm đuôi (Nằm chuẩn trên trục tâm)
                                                28, 146, // Đuôi trái
                                                0, 112, // Mũi cánh trái
                                                4, 69, // Đỉnh cánh trái
                                                28, 61, // Thân trái
                                                30, 9 // Cạnh mũi trái
                                });
                loadSpriteInfo("enemy_sniper_green", "/com/nhom27/skyforce/textures/entities/enemy_sniper_green.png",
                                new double[] { 1, 41, // Mép cánh trái
                                                24, 2, // Mũi trái
                                                80, 2, // Mũi phải
                                                103, 41, // Mép cánh phải
                                                89, 82, // Đuôi phải
                                                52, 76, // Điểm lõm giữa đuôi (Nằm chuẩn trên trục tâm X = 52)
                                                15, 82 // Đuôi trái
                                });
                loadSpriteInfo("enemy_swarm_black", "/com/nhom27/skyforce/textures/entities/enemy_swarm_black.png",
                                new double[] {
                                                4, 2, // Mép cánh trái
                                                16, 0, // Mũi trái
                                                36, 14, // Thân trái trên
                                                60, 14, // Thân phải trên
                                                80, 0, // Mũi phải
                                                92, 2, // Mép cánh phải
                                                72, 72, // Đuôi cánh phải ngoài (Đã tạo thêm để cân với bên trái)
                                                66, 76, // Đuôi cánh phải trong
                                                58, 82, // Đáy thân phải
                                                38, 82, // Đáy thân trái
                                                30, 76, // Đuôi cánh trái trong
                                                24, 72 // Đuôi cánh trái ngoài
                                });

                loadSpriteInfo("obstacle_asteroid_large",
                                "/com/nhom27/skyforce/textures/obstacles/obstacle_asteroid_large.png", new double[] {
                                                14, 13,
                                                66, 1,
                                                96, 37,
                                                80, 90,
                                                31, 95,
                                                1, 58
                                });
                loadImage("bullet_boss_mini_red", "/com/nhom27/skyforce/textures/projectiles/bullet_boss_mini_red.png");
                loadImage("enemy_ship_1", "/com/nhom27/skyforce/textures/Spaceship_01_RED.png");
                loadImage("enemy_ship_2", "/com/nhom27/skyforce/textures/Spaceship_02_RED.png");
                loadImage("enemy_ship_3", "/com/nhom27/skyforce/textures/Spaceship_03_RED.png");
                loadImage("bullet_enemy_1", "/com/nhom27/skyforce/textures/entities/weapons/laserRed12.png");
                loadImage("bullet_enemy_2", "/com/nhom27/skyforce/textures/entities/weapons/laserRed14.png");
                loadImage("bullet_enemy_3", "/com/nhom27/skyforce/textures/entities/weapons/laserRed16.png");

                loadImage("powerup", "/com/nhom27/skyforce/textures/Flame_01.png");
                loadImage("bullet_enemy_laser",
                                "/com/nhom27/skyforce/textures/projectiles/bullet_enemy_laser.png");
                loadImage("bullet_player_blue",
                                "/com/nhom27/skyforce/textures/projectiles/bullet_player_blue.png");
                loadImage("bullet_player_green",
                                "/com/nhom27/skyforce/textures/projectiles/bullet_player_green.png");
                loadImage("bullet_player_orange",
                                "/com/nhom27/skyforce/textures/projectiles/bullet_player_orange.png");
                loadImage("bullet_player_red",
                                "/com/nhom27/skyforce/textures/projectiles/bullet_player_red.png");
                loadImage("bullet_player_seeker_lv1",
                                "/com/nhom27/skyforce/textures/projectiles/bullet_player_seeker_lv1.png");
                loadImage("bullet_player_seeker_lv2",
                                "/com/nhom27/skyforce/textures/projectiles/bullet_player_seeker_lv2.png");
                loadImage("bullet_player_seeker_lv3",
                                "/com/nhom27/skyforce/textures/projectiles/bullet_player_seeker_lv3.png");
                loadImage("bullet_enemy_laser",
                                "/com/nhom27/skyforce/textures/projectiles/bullet_enemy_laser.png");
                loadImage("bullet_enemy_round_purple",
                                "/com/nhom27/skyforce/textures/projectiles/bullet_enemy_round_purple.png");
                loadImage("bullet_enemy_diamond_yellow",
                                "/com/nhom27/skyforce/textures/projectiles/bullet_enemy_diamond_yellow.png");

                loadImage("meteor_big",
                                "/com/nhom27/skyforce/textures/entities/obstacles/meteor_big.png");
                loadImage("obstacle_mine_red",
                                "/com/nhom27/skyforce/textures/entities/obstacles/obstacle_mine_red.png");
                loadSpriteInfo("enemy_straight",
                                "/com/nhom27/skyforce/textures/entities/enemies/enemy_straight_black.png",
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
                loadSpriteInfo("enemy_sine_orbit",
                                "/com/nhom27/skyforce/textures/entities/enemies/enemy_sine_orbit_black.png",
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

                loadImage("item_coin_gold", "/com/nhom27/skyforce/textures/powerups/item_coin_gold.png");
                loadSpriteInfo("enemy_shooter", "/com/nhom27/skyforce/textures/entities/enemies/enemy_shooter.png",
                                new double[] {
                                                27, 0,
                                                1, 28,
                                                6, 82,
                                                27, 84,
                                                30, 145,
                                                61, 143,
                                                60, 86,
                                                85, 79,
                                                92, 35,
                                                69, 1
                                });
                loadImage("item_pill_blue", "/com/nhom27/skyforce/textures/powerups/item_pill_blue.png");
                loadImage("item_powerup_lightning",
                                "/com/nhom27/skyforce/textures/powerups/item_powerup_lightning.png");
                loadImage("item_shield", "/com/nhom27/skyforce/textures/powerups/item_shield.png");
                loadImage("char_shield", "/com/nhom27/skyforce/textures/entities/player/char_shield.png");

                loadImage("ui_icon_shield_active", "/com/nhom27/skyforce/textures/ui/ui_icon_shield_active.png");
                loadImage("ui_icon_lightning_active", "/com/nhom27/skyforce/textures/ui/ui_icon_lightning_active.png");

                // Hiệu ứng
                loadImage("vfx_hit_player_blue_1", "/com/nhom27/skyforce/textures/vfx/vfx_hit_player_blue_1.png");
                loadImage("vfx_hit_player_blue_2", "/com/nhom27/skyforce/textures/vfx/vfx_hit_player_blue_2.png");
                loadImage("vfx_hit_player_green_1", "/com/nhom27/skyforce/textures/vfx/vfx_hit_player_green_1.png");
                loadImage("vfx_hit_player_green_2", "/com/nhom27/skyforce/textures/vfx/vfx_hit_player_green_2.png");
                loadImage("vfx_hit_player_orange_1", "/com/nhom27/skyforce/textures/vfx/vfx_hit_player_orange_1.png");
                loadImage("vfx_hit_player_orange_2", "/com/nhom27/skyforce/textures/vfx/vfx_hit_player_orange_2.png");
                loadImage("vfx_hit_player_red_1", "/com/nhom27/skyforce/textures/vfx/vfx_hit_player_red_1.png");
                loadImage("vfx_hit_player_red_2", "/com/nhom27/skyforce/textures/vfx/vfx_hit_player_red_2.png");
                loadImage("vfx_explosion_8x8_sheet", "/com/nhom27/skyforce/textures/vfx/vfx_explosion_8x8_sheet.png");

                System.out.println("Tải ảnh hoàn tất!");

                System.out.println("Đang tải các tài nguyên Âm Thanh: ");
                loadMusic("background_home_music", "/com/nhom27/skyforce/audio/music/background.wav");
                loadMusic("background_play_music", "/com/nhom27/skyforce/audio/music/background_play.wav");
                loadMusic("lose", "/com/nhom27/skyforce/audio/music/lose.wav");

                loadSound("sfx_laser", "/com/nhom27/skyforce/audio/sfx/sfx_laser.wav");
                loadSound("sfx_explosion_enemy", "/com/nhom27/skyforce/audio/sfx/sfx_explosion_enemy.wav");
                loadSound("sfx_zap", "/com/nhom27/skyforce/audio/sfx/sfx_zap.wav");
                loadSound("sfx_laser_impact", "/com/nhom27/skyforce/audio/sfx/sfx_laser_impact.wav");
                loadSound("sfx_item_health_pickup", "/com/nhom27/skyforce/audio/sfx/sfx_item_health_pickup.wav");
                loadSound("sfx_item_powerup_lightning",
                                "/com/nhom27/skyforce/audio/sfx/sfx_item_powerup_lightning.wav");
                loadSound("sfx_player_shield_break", "/com/nhom27/skyforce/audio/sfx/sfx_player_shield_break.wav");
                System.out.println("Tải nhạc hoàn tất!");

                System.out.println("Đang tải các tài nguyên Font: ");
                loadFont("font_kenvector_future", "/com/nhom27/skyforce/fonts/kenvector_future.ttf");
                loadFont("font_kenvector_future_thin", "/com/nhom27/skyforce/fonts/kenvector_future_thin.ttf");
                System.out.println("Tải Font hoàn tất!");
        }

}
