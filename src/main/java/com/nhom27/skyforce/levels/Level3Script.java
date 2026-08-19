package com.nhom27.skyforce.levels;

import java.util.ArrayList;
import java.util.List;

import com.nhom27.skyforce.entities.enemies.NormalEnemy;
import com.nhom27.skyforce.entities.enemies.SniperEnemy;
import com.nhom27.skyforce.entities.enemies.SwarmEnemy;
import com.nhom27.skyforce.entities.items.CoinPowerUp;
import com.nhom27.skyforce.entities.items.PowerUp;
import com.nhom27.skyforce.entities.obstacles.Asteroid;
import com.nhom27.skyforce.main.Main;
import com.nhom27.skyforce.managers.GameManager;

/**
 * Class kịch bản dành riêng cho Level 3.
 * Chứa toàn bộ cờ trạng thái, các đợt sóng bầy ruồi (SwarmEnemy), chướng ngại vật thiên thạch (Asteroid),
 * đội hình chữ V (createVFormation), và logic kết thúc màn chơi.
 */
public class Level3Script implements LevelScript {

    private boolean lvl3_spawned5s = false;
    private int lvl3_queue5s_count = 0;
    private long lvl3_last5s_time = 0;

    private boolean lvl3_spawned12s = false;
    private int lvl3_queue12s_count = 0;
    private long lvl3_last12s_time = 0;

    private boolean lvl3_spawned18s = false;

    private boolean lvl3_spawned25s = false;
    private int lvl3_queue25s_count = 0;
    private long lvl3_last25s_time = 0;

    private boolean lvl3_spawned32s_meteors = false;
    private long lvl3_lastRainTime = 0;

    private boolean lvl3_spawned50s = false;
    private boolean lvl3_spawned52s = false;

    private boolean lvl3_spawned70s = false;
    private List<SwarmEnemy> lvl3_finalWave = new ArrayList<>();
    private boolean lvl3_coinsSpawned = false;
    private long lvl3_clearTime = 0;
    private boolean lvl3_magneticActive = false;
    private boolean isVictory = false;

    public Level3Script() {
        setup();
    }

    @Override
    public void setup() {
        lvl3_spawned5s = false;
        lvl3_queue5s_count = 0;
        lvl3_last5s_time = 0;

        lvl3_spawned12s = false;
        lvl3_queue12s_count = 0;
        lvl3_last12s_time = 0;

        lvl3_spawned18s = false;

        lvl3_spawned25s = false;
        lvl3_queue25s_count = 0;
        lvl3_last25s_time = 0;

        lvl3_spawned32s_meteors = false;
        lvl3_lastRainTime = 0;

        lvl3_spawned50s = false;
        lvl3_spawned52s = false;

        lvl3_spawned70s = false;
        lvl3_finalWave.clear();
        lvl3_coinsSpawned = false;
        lvl3_clearTime = 0;
        lvl3_magneticActive = false;
        isVictory = false;
    }

    @Override
    public void update(long now, double elapsedSec, GameManager gameManager) {
        // ==================================================
        // 🎬 GIAI ĐOẠN 1: ĐIỆU NHẢY CỦA BẦY RUỒI (Giây 0 - 20)
        // ==================================================

        // 5s: 1 hàng dọc 5 con SwarmEnemy bám đuôi nhau bay từ mép trên bên Trái xuống
        // theo hình lượn sóng (Sine wave)
        if (elapsedSec >= 5.0 && !lvl3_spawned5s) {
            if (lvl3_queue5s_count < 5) {
                if (now - lvl3_last5s_time >= 300) {
                    SwarmEnemy swarm = new SwarmEnemy(SwarmEnemy.TrajectoryType.SINE_WAVE, 180.0, 0, 60.0, 0.025, 0);
                    swarm.spawnAt(swarm.getSizeX(), -swarm.getSizeY());
                    gameManager.spawnEnemy(swarm);
                    lvl3_queue5s_count++;
                    lvl3_last5s_time = now;
                }
            } else {
                lvl3_spawned5s = true;
            }
        }

        // 12s: 1 hàng dọc 5 con SwarmEnemy bám đuôi nhau lượn sóng từ mép Phải sang
        if (elapsedSec >= 12.0 && !lvl3_spawned12s) {
            if (lvl3_queue12s_count < 5) {
                if (now - lvl3_last12s_time >= 300) {
                    SwarmEnemy swarm = new SwarmEnemy(SwarmEnemy.TrajectoryType.SINE_WAVE, 180.0, 0, 60.0, 0.025, Math.PI);
                    swarm.spawnAt(Main.WIDTH - 120, -swarm.getSizeY());
                    gameManager.spawnEnemy(swarm);
                    lvl3_queue12s_count++;
                    lvl3_last12s_time = now;
                }
            } else {
                lvl3_spawned12s = true;
            }
        }

        // 18s: 1 con quái Đỏ mang ItemUpgrade (PillPowerUp nâng cấp súng Cấp 2)
        if (elapsedSec >= 18.0 && !lvl3_spawned18s) {
            NormalEnemy eUpgradeRed = new NormalEnemy(150, false, false, true, "enemy_normal_red");
            eUpgradeRed.setPos(Main.WIDTH / 2.0 - eUpgradeRed.getSizeX() / 2.0, -eUpgradeRed.getSizeY());
            gameManager.spawnEnemy(eUpgradeRed);
            lvl3_spawned18s = true;
        }

        // ==================================================
        // ☄️ GIAI ĐOẠN 2: CHƯỚNG NGẠI VẬT TỪ KHÔNG GIAN (Giây 25 - 45)
        // ==================================================

        // 25s: 1 Cục Thiên Thạch (Asteroid) khổng lồ trôi chầm chậm ở ngay giữa màn hình
        // Cùng lúc đó, 2 hàng SwarmEnemy (mỗi hàng 7 con) bay chéo hình chữ X đan vào nhau
        if (elapsedSec >= 25.0 && !lvl3_spawned25s) {
            if (lvl3_queue25s_count == 0) {
                Asteroid centerAsteroid = new Asteroid();
                double centerX = Main.WIDTH / 2.0 - centerAsteroid.getSizeX() / 2.0;
                centerAsteroid.setPos(centerX, -centerAsteroid.getSizeY());
                gameManager.spawnEnemy(centerAsteroid);
            }

            if (lvl3_queue25s_count < 7) {
                if (now - lvl3_last25s_time >= 300) {
                    SwarmEnemy leftDiag = new SwarmEnemy(SwarmEnemy.TrajectoryType.DIAGONAL, 230.0, 160.0);
                    leftDiag.spawnAt(0, -leftDiag.getSizeY());

                    SwarmEnemy rightDiag = new SwarmEnemy(SwarmEnemy.TrajectoryType.DIAGONAL, 230.0, -160.0);
                    rightDiag.spawnAt(Main.WIDTH - rightDiag.getSizeX(), -rightDiag.getSizeY());

                    gameManager.spawnEnemy(leftDiag);
                    gameManager.spawnEnemy(rightDiag);
                    lvl3_queue25s_count++;
                    lvl3_last25s_time = now;
                }
            } else {
                lvl3_spawned25s = true;
            }
        }

        // 32s - 42s: 3 cục Thiên thạch trôi xuống tạo thành 2 "khe hẹp" trên màn hình.
        // Quái SwarmEnemy túa ra liên tục, trôi qua các khe hẹp này như nước chảy qua khe đá.
        if (elapsedSec >= 32.0 && !lvl3_spawned32s_meteors) {
            Asteroid aLeft = new Asteroid();
            aLeft.setPos(0.0, -aLeft.getSizeY());

            Asteroid aCenter = new Asteroid();
            aCenter.setPos(Main.WIDTH / 2.0 - aCenter.getSizeX() / 2.0, -aCenter.getSizeY() * 2);

            Asteroid aRight = new Asteroid();
            aRight.setPos(Main.WIDTH - aRight.getSizeX(), -aRight.getSizeY());

            gameManager.spawnEnemy(aLeft);
            gameManager.spawnEnemy(aCenter);
            gameManager.spawnEnemy(aRight);
            lvl3_spawned32s_meteors = true;
        }

        if (elapsedSec >= 32.0 && elapsedSec <= 42.0) {
            if (now - lvl3_lastRainTime >= 450) {
                double gap1 = Main.WIDTH * 0.28;
                double gap2 = Main.WIDTH * 0.72;
                double spawnX = (gameManager.getRandom().nextBoolean()) ? gap1 : gap2;
                SwarmEnemy swarm = new SwarmEnemy(260.0, 0);
                swarm.spawnAt(spawnX, -swarm.getSizeY());
                gameManager.spawnEnemy(swarm);
                lvl3_lastRainTime = now;
            }
        }

        // ==================================================
        // ⚠️ GIAI ĐOẠN 3: CHIẾN THUẬT NHIỄU LOẠN (Giây 50 - 65)
        // ==================================================

        // 50s: 2 Thiên thạch trôi xuống ở rìa Trái và rìa Phải + 1 đàn SwarmEnemy bay ngang làm nhiễu loạn
        if (elapsedSec >= 50.0 && !lvl3_spawned50s) {
            Asteroid aLeft = new Asteroid();
            aLeft.setPos(aLeft.getSizeX(), -aLeft.getSizeY());

            Asteroid aRight = new Asteroid();
            aRight.setPos(Main.WIDTH - 2 * aRight.getSizeX(), -aRight.getSizeY());

            gameManager.spawnEnemy(aLeft);
            gameManager.spawnEnemy(aRight);

            for (int i = 0; i < 8; i++) {
                SwarmEnemy swarm = new SwarmEnemy();
                swarm.spawnAt(80.0 + (i * 35), -swarm.getSizeY() - (i * 25));
                gameManager.spawnEnemy(swarm);
            }
            lvl3_spawned50s = true;
        }

        // 52s: 2 con SniperEnemy bay xuống và đứng nấp ngay sau lưng 2 cục Thiên thạch
        if (elapsedSec >= 52.0 && !lvl3_spawned52s) {
            SniperEnemy sLeft = new SniperEnemy(150.0, 1000, "AUTO");
            sLeft.setPos(sLeft.getSizeX(), -sLeft.getSizeY() * 2);

            SniperEnemy sRight = new SniperEnemy(150.0, 1000, "AUTO");
            sRight.setPos(Main.WIDTH - 2 * sRight.getSizeX(), -sRight.getSizeY() * 2);

            gameManager.spawnEnemy(sLeft);
            gameManager.spawnEnemy(sRight);
            lvl3_spawned52s = true;
        }

        // ==================================================
        // 🎆 GIAI ĐOẠN 4: MƯA SAO BĂNG (SURVIVAL WAVE) (Giây 70 - 80)
        // ==================================================

        // 70s - 78s: 5-6 cục Thiên thạch nhỏ trôi lác đác + 3 đội hình chữ V của SwarmEnemy (21 con)
        if (elapsedSec >= 70.0 && !lvl3_spawned70s) {
            lvl3_finalWave.clear();

            for (int i = 0; i < 5; i++) {
                Asteroid a = new Asteroid();
                double meteorX = 30.0 + i * ((Main.WIDTH - 60 - a.getSizeX()) / 4.0);
                a.setPos(meteorX, -a.getSizeY() - (i % 2 * 60));
                gameManager.spawnEnemy(a);
            }

            double center = Main.WIDTH / 2.0;
            createVFormation(center, 7, gameManager);
            createVFormation(center - center / 2.0, 7, gameManager);
            createVFormation(center + center / 2.0, 7, gameManager);

            lvl3_spawned70s = true;
        }

        // 80s: Hoàn thành Level 3
        if (lvl3_spawned70s && !isVictory) {
            boolean allWaveDead = lvl3_finalWave.stream().allMatch(e -> !e.isAlive());
            if (allWaveDead || elapsedSec >= 80.0) {
                if (!lvl3_coinsSpawned) {
                    // Rớt đại tiệc 25 đồng Vàng!
                    for (int c = 0; c < 25; c++) {
                        double dropX = Main.WIDTH / 2.0 + (gameManager.getRandom().nextDouble() - 0.5) * 220;
                        double dropY = Main.HEIGHT * 0.35 + (gameManager.getRandom().nextDouble() - 0.5) * 120;
                        CoinPowerUp coin = new CoinPowerUp(dropX, dropY);
                        gameManager.addPowerUp(coin);
                    }
                    lvl3_coinsSpawned = true;
                    lvl3_clearTime = System.currentTimeMillis();
                }

                long elapsedSinceClear = System.currentTimeMillis() - lvl3_clearTime;
                if (elapsedSinceClear >= 500 && !lvl3_magneticActive) {
                    lvl3_magneticActive = true;
                }

                if (lvl3_magneticActive) {
                    for (PowerUp p : gameManager.getPowerUps()) {
                        if (p instanceof CoinPowerUp coin && !coin.isMagnetized()) {
                            coin.setMagnetized(true, gameManager.getPlayer());
                        }
                    }
                }

                boolean hasCoinsRemaining = gameManager.getPowerUps().stream()
                        .anyMatch(p -> p instanceof CoinPowerUp && p.isAlive());
                if (!hasCoinsRemaining) {
                    isVictory = true;
                    gameManager.handleVictory();
                }
            }
        }
    }

    /**
     * Tạo đội hình bay hình chữ V cho các quái SwarmEnemy trong Level 3.
     *
     * @param apexX Đỉnh chữ V theo trục X
     * @param countPerV Số lượng quái trong đội hình chữ V
     * @param gameManager Tham chiếu GameManager để xuất hiện quái
     */
    private void createVFormation(double apexX, int countPerV, GameManager gameManager) {
        int half = countPerV / 2;
        for (int i = 0; i <= half; i++) {
            SwarmEnemy eCenterLeft = new SwarmEnemy(230.0, 0);
            eCenterLeft.spawnAt(apexX - (i * eCenterLeft.getSizeX()),
                    -eCenterLeft.getSizeY() - (i * eCenterLeft.getSizeY()));
            lvl3_finalWave.add(eCenterLeft);
            gameManager.spawnEnemy(eCenterLeft);

            if (i > 0) {
                SwarmEnemy eCenterRight = new SwarmEnemy(230.0, 0);
                eCenterRight.spawnAt(apexX + (i * eCenterRight.getSizeX()),
                        -eCenterRight.getSizeY() - (i * eCenterRight.getSizeY()));
                lvl3_finalWave.add(eCenterRight);
                gameManager.spawnEnemy(eCenterRight);
            }
        }
    }

    @Override
    public boolean isCompleted() {
        return isVictory;
    }
}
