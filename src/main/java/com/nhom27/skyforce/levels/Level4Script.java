package com.nhom27.skyforce.levels;

import java.util.ArrayList;
import java.util.List;

import com.nhom27.skyforce.entities.enemies.NormalEnemy;
import com.nhom27.skyforce.entities.enemies.SniperEnemy;
import com.nhom27.skyforce.entities.enemies.SwarmEnemy;
import com.nhom27.skyforce.entities.enemies.TankerEnemy;
import com.nhom27.skyforce.entities.items.CoinPowerUp;
import com.nhom27.skyforce.entities.items.PowerUp;
import com.nhom27.skyforce.main.Main;
import com.nhom27.skyforce.managers.GameManager;

/**
 * Class kịch bản dành riêng cho Level 4.
 * Chứa toàn bộ cờ trạng thái và kịch bản Wave xuất hiện kẻ địch của Level 4 (Tập trung vào TankerEnemy & SniperEnemy).
 */
public class Level4Script implements LevelScript {

    private boolean lvl4_spawned5s = false;
    private boolean lvl4_spawned12s = false;
    private boolean lvl4_spawned18s = false;
    private boolean lvl4_spawned25s = false;
    private boolean lvl4_spawned35s = false;
    private boolean lvl4_spawned55s = false;
    private boolean lvl4_spawned60s = false;
    private List<TankerEnemy> lvl4_wallTankers = new ArrayList<>();
    private boolean lvl4_coinsSpawned = false;
    private long lvl4_clearTime = 0;
    private boolean lvl4_magneticActive = false;
    private boolean isVictory = false;

    public Level4Script() {
        setup();
    }

    @Override
    public void setup() {
        lvl4_spawned5s = false;
        lvl4_spawned12s = false;
        lvl4_spawned18s = false;
        lvl4_spawned25s = false;
        lvl4_spawned35s = false;
        lvl4_spawned55s = false;
        lvl4_spawned60s = false;
        lvl4_wallTankers.clear();
        lvl4_coinsSpawned = false;
        lvl4_clearTime = 0;
        lvl4_magneticActive = false;
        isVictory = false;
    }

    @Override
    public void update(long now, double elapsedSec, GameManager gameManager) {
        // 🎬 Giai đoạn 1: Chạm trán Xe Tăng (Giây 0 - 20)
        // 5s: 1 con TankerEnemy to bự xuất hiện ở chính giữa
        if (elapsedSec >= 5.0 && !lvl4_spawned5s) {
            TankerEnemy t1 = new TankerEnemy(60.0, true);
            t1.setPos(Main.WIDTH / 2.0 - t1.getSizeX() / 2.0, -t1.getSizeY());
            gameManager.spawnEnemy(t1);
            lvl4_spawned5s = true;
        }

        // 12s: 2 con TankerEnemy bay xuống ở 2 bên mép Trái và Phải
        if (elapsedSec >= 12.0 && !lvl4_spawned12s) {
            TankerEnemy tLeft = new TankerEnemy(65.0, true);
            tLeft.setPos(tLeft.getSizeX(), -tLeft.getSizeY());

            TankerEnemy tRight = new TankerEnemy(65.0, true);
            tRight.setPos(Main.WIDTH - 2 * tRight.getSizeX(), -tRight.getSizeY());

            gameManager.spawnEnemy(tLeft);
            gameManager.spawnEnemy(tRight);
            lvl4_spawned12s = true;
        }

        // 18s: Quái đỏ bay qua rớt Item Nâng Cấp (PillPowerUp)
        if (elapsedSec >= 18.0 && !lvl4_spawned18s) {
            NormalEnemy eUpgradeRed = new NormalEnemy(85.0, false, false, true, "enemy_normal_red");
            eUpgradeRed.setPos(Main.WIDTH / 2.0 - eUpgradeRed.getSizeX() / 2.0, -eUpgradeRed.getSizeY());
            gameManager.spawnEnemy(eUpgradeRed);
            lvl4_spawned18s = true;
        }

        // 🛡️ Giai đoạn 2: Lá Chắn & Ngọn Giáo (Giây 25 - 45)
        // 25s: 1 TankerEnemy đi trước + 1 SniperEnemy nấp sau lưng (bắn 3 đạn)
        if (elapsedSec >= 25.0 && !lvl4_spawned25s) {
            TankerEnemy tShield = new TankerEnemy(60.0, true);
            tShield.setPos(Main.WIDTH / 2.0 - tShield.getSizeX() / 2.0, -tShield.getSizeY());

            SniperEnemy sSpear = new SniperEnemy(50.0, 1500, "AUTO");
            sSpear.setSpeedY(60.0);
            sSpear.setBurstMode(true);
            sSpear.setPos(Main.WIDTH / 2.0 - sSpear.getSizeX() / 2.0, -sSpear.getSizeY() - 100);

            gameManager.spawnEnemy(tShield);
            gameManager.spawnEnemy(sSpear);
            lvl4_spawned25s = true;
        }

        // 35s: 2 TankerEnemy đi song song + 2 SniperEnemy nấp sau (bắn 3 đạn) + 4 SwarmEnemy gây nhiễu
        if (elapsedSec >= 35.0 && !lvl4_spawned35s) {
            double centerX = Main.WIDTH / 2.0;

            TankerEnemy t1 = new TankerEnemy(60.0, true);
            t1.setPos(centerX - 110 - t1.getSizeX() / 2.0, -t1.getSizeY());

            TankerEnemy t2 = new TankerEnemy(60.0, true);
            t2.setPos(centerX + 110 - t2.getSizeX() / 2.0, -t2.getSizeY());

            SniperEnemy s1 = new SniperEnemy(50.0, 1500, "LEFT");
            s1.setSpeedY(60.0);
            s1.setBurstMode(true);
            s1.setPos(centerX - 110 - s1.getSizeX() / 2.0, -s1.getSizeY() - 80);

            SniperEnemy s2 = new SniperEnemy(50.0, 1500, "RIGHT");
            s2.setSpeedY(60.0);
            s2.setBurstMode(true);
            s2.setPos(centerX + 110 - s2.getSizeX() / 2.0, -s2.getSizeY() - 80);

            gameManager.spawnEnemy(t1);
            gameManager.spawnEnemy(t2);
            gameManager.spawnEnemy(s1);
            gameManager.spawnEnemy(s2);

            for (int i = 0; i < 4; i++) {
                SwarmEnemy swarm = new SwarmEnemy(SwarmEnemy.TrajectoryType.SINE_WAVE, 220.0, 0);
                swarm.spawnAt(60.0 + (i * 90), -swarm.getSizeY() - (i * 30));
                gameManager.spawnEnemy(swarm);
            }
            lvl4_spawned35s = true;
        }

        // 🧱 Giai đoạn 3: Bức Tường Tuyệt Vọng (Giây 55 - 70)
        // 55s: 3 TankerEnemy dàn hàng ngang như bức tường thép
        if (elapsedSec >= 55.0 && !lvl4_spawned55s) {
            lvl4_wallTankers.clear();

            TankerEnemy sample = new TankerEnemy(55.0, true);
            double gap = (Main.WIDTH - (3 * sample.getSizeX())) / 4.0;

            TankerEnemy w1 = new TankerEnemy(55.0, true);
            w1.setPos(gap, -w1.getSizeY());

            TankerEnemy w2 = new TankerEnemy(55.0, true);
            w2.setPos(gap * 2 + w2.getSizeX(), -w2.getSizeY());

            TankerEnemy w3 = new TankerEnemy(55.0, true);
            w3.setPos(gap * 3 + w3.getSizeX() * 2, -w3.getSizeY());

            lvl4_wallTankers.add(w1);
            lvl4_wallTankers.add(w2);
            lvl4_wallTankers.add(w3);

            gameManager.spawnEnemy(w1);
            gameManager.spawnEnemy(w2);
            gameManager.spawnEnemy(w3);
            lvl4_spawned55s = true;
        }

        // 60s: Bầy SwarmEnemy lượn sóng chui qua các khe hở
        if (elapsedSec >= 60.0 && !lvl4_spawned60s) {
            TankerEnemy sample = new TankerEnemy(55.0, true);
            double gap = (Main.WIDTH - (3 * sample.getSizeX())) / 4.0;
            double slot1 = gap + sample.getSizeX() / 2.0;
            double slot2 = gap * 2 + sample.getSizeX() * 1.5;

            for (int i = 0; i < 3; i++) {
                SwarmEnemy sw1 = new SwarmEnemy(SwarmEnemy.TrajectoryType.SINE_WAVE, 240.0, 0);
                sw1.spawnAt(slot1, -sw1.getSizeY() - (i * 40));

                SwarmEnemy sw2 = new SwarmEnemy(SwarmEnemy.TrajectoryType.SINE_WAVE, 240.0, 0);
                sw2.spawnAt(slot2, -sw2.getSizeY() - (i * 40));

                gameManager.spawnEnemy(sw1);
                gameManager.spawnEnemy(sw2);
            }
            lvl4_spawned60s = true;
        }

        // 🎆 Giai đoạn 4: Dọn dẹp chiến trường (Giây 75 - 85)
        if (lvl4_spawned55s && !isVictory) {
            boolean wallDead = lvl4_wallTankers.stream().allMatch(e -> !e.isAlive());
            if (wallDead || elapsedSec >= 85.0) {
                if (!lvl4_coinsSpawned) {
                    // Rớt đại tiệc 25 đồng Vàng lớn!
                    for (int c = 0; c < 25; c++) {
                        double dropX = Main.WIDTH / 2.0 + (gameManager.getRandom().nextDouble() - 0.5) * 240;
                        double dropY = Main.HEIGHT * 0.35 + (gameManager.getRandom().nextDouble() - 0.5) * 140;
                        CoinPowerUp coin = new CoinPowerUp(dropX, dropY);
                        gameManager.addPowerUp(coin);
                    }
                    lvl4_coinsSpawned = true;
                    lvl4_clearTime = System.currentTimeMillis();
                }

                long elapsedSinceClear = System.currentTimeMillis() - lvl4_clearTime;
                if (elapsedSinceClear >= 500 && !lvl4_magneticActive) {
                    lvl4_magneticActive = true;
                }

                if (lvl4_magneticActive) {
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

    @Override
    public boolean isCompleted() {
        return isVictory;
    }
}
