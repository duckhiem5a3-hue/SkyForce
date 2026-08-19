package com.nhom27.skyforce.levels;

import com.nhom27.skyforce.audio.AudioManager;
import com.nhom27.skyforce.entities.base.EnemyObject;
import com.nhom27.skyforce.entities.enemies.MiniBoss;
import com.nhom27.skyforce.entities.enemies.NormalEnemy;
import com.nhom27.skyforce.entities.items.CoinPowerUp;
import com.nhom27.skyforce.entities.items.PowerUp;
import com.nhom27.skyforce.main.Main;
import com.nhom27.skyforce.managers.GameManager;

/**
 * Class kịch bản dành riêng cho Level 1.
 * Đóng gói toàn bộ biến trạng thái (cờ boolean, bộ đếm thời gian) và logic sinh quái vật của Level 1.
 */
public class Level1Script implements LevelScript {

    // Trạng thái các wave trong Level 1
    private boolean spawned3s = false;
    private boolean spawned7s = false;
    private boolean spawned12s = false;
    private boolean spawned18s = false;
    private long lastFlyRainTime = 0;
    private boolean warningTriggered = false;
    private boolean miniBossSpawned = false;
    private boolean isVictory = false;

    // Trạng thái tiêu diệt MiniBoss & Lực hút từ tính cuối màn
    private boolean lvl1_miniBossDefeated = false;
    private long lvl1_bossDeathTime = 0;
    private boolean lvl1_magneticActive = false;

    public Level1Script() {
        setup();
    }

    @Override
    public void setup() {
        spawned3s = false;
        spawned7s = false;
        spawned12s = false;
        spawned18s = false;
        lastFlyRainTime = 0;
        warningTriggered = false;
        miniBossSpawned = false;
        isVictory = false;

        lvl1_miniBossDefeated = false;
        lvl1_bossDeathTime = 0;
        lvl1_magneticActive = false;
    }

    @Override
    public void update(long now, double elapsedSec, GameManager gameManager) {
        // 🎬 Giai đoạn 1: Chào sân (Giây 0 - 15)
        if (elapsedSec >= 3.0 && !spawned3s) {
            NormalEnemy e1 = new NormalEnemy(60.0, false, false, false, "enemy_normal_blue");
            e1.setPos(Main.WIDTH / 2.0 - e1.getSizeX() / 2.0, -e1.getSizeY());
            gameManager.spawnEnemy(e1);
            spawned3s = true;
        }

        if (elapsedSec >= 7.0 && !spawned7s) {
            NormalEnemy eLeft = new NormalEnemy(80.0, false, false, false, "enemy_normal_blue");
            eLeft.setPos(0, -eLeft.getSizeY());
            NormalEnemy eRight = new NormalEnemy(80.0, false, false, false, "enemy_normal_blue");
            eRight.setPos(Main.WIDTH - eRight.getSizeX(), -eRight.getSizeY());
            gameManager.spawnEnemy(eLeft);
            gameManager.spawnEnemy(eRight);
            spawned7s = true;
        }

        if (elapsedSec >= 12.0 && !spawned12s) {
            NormalEnemy eV1 = new NormalEnemy(85.0, false, false, false, "enemy_normal_blue");
            eV1.setPos(Main.WIDTH / 2.0 - eV1.getSizeX() / 2.0, -eV1.getSizeY());

            NormalEnemy eV2 = new NormalEnemy(85.0, false, false, false, "enemy_normal_blue");
            eV2.setPos(Main.WIDTH / 2.0 - eV2.getSizeX() / 2.0 - eV2.getSizeX(), -eV2.getSizeY() * 2);

            NormalEnemy eV3 = new NormalEnemy(85.0, false, false, false, "enemy_normal_blue");
            eV3.setPos(Main.WIDTH / 2.0 - eV3.getSizeX() / 2.0 + eV3.getSizeX(), -eV3.getSizeY() * 2);

            gameManager.spawnEnemy(eV1);
            gameManager.spawnEnemy(eV2);
            gameManager.spawnEnemy(eV3);
            spawned12s = true;
        }

        // 🎁 Giai đoạn 2: Trải nghiệm Sức mạnh (Giây 18 - 35)
        if (elapsedSec >= 18.0 && !spawned18s) {
            NormalEnemy eRed = new NormalEnemy(90.0, false, false, true, "enemy_normal_red");
            eRed.setPos(Main.WIDTH / 2.0 - eRed.getSizeX() / 2.0, -eRed.getSizeY() * 2);
            gameManager.spawnEnemy(eRed);
            spawned18s = true;
        }

        if (elapsedSec >= 22.0 && elapsedSec <= 35.0) {
            if (now - lastFlyRainTime >= 2500) {
                int count = 2 + gameManager.getRandom().nextInt(2);
                for (int i = 0; i < count; i++) {
                    NormalEnemy e = new NormalEnemy(110.0, false, false, false, "enemy_normal_blue");
                    e.setPos(gameManager.getRandom().nextDouble() * (Main.WIDTH - e.getSizeX()), -e.getSizeY() - (i * e.getSizeY()));
                    gameManager.spawnEnemy(e);
                }
                lastFlyRainTime = now;
            }
        }

        // ⚠️ Giai đoạn 3: Cao trào & Kết thúc (Giây 40 - 60)
        if (elapsedSec >= 45.0 && elapsedSec < 48.0 && !warningTriggered) {
            if (gameManager.getPlayScene() != null) {
                gameManager.getPlayScene().showWarningBanner(true);
            }
            AudioManager.getInstance().playSound("sfx_zap");
            warningTriggered = true;
        }

        if (elapsedSec >= 48.0 && !miniBossSpawned) {
            if (gameManager.getPlayScene() != null) {
                gameManager.getPlayScene().showWarningBanner(false);
            }
            MiniBoss boss = new MiniBoss();
            boss.setPos(Main.WIDTH / 2.0 - boss.getSizeX() / 2.0, -boss.getSizeY());
            gameManager.spawnEnemy(boss);
            miniBossSpawned = true;
        }

        // 🏆 Giai đoạn 4: Kết thúc Màn 1 (61s: Lực hút từ tính -> 62s: Bảng Win)
        if (lvl1_miniBossDefeated && !isVictory) {
            long elapsedSinceBossDeath = System.currentTimeMillis() - lvl1_bossDeathTime;
            // 61s: Lực hút từ tính kích hoạt sau 1s từ khi Mini-Boss nổ
            if (elapsedSinceBossDeath >= 1000 && !lvl1_magneticActive) {
                lvl1_magneticActive = true;
            }

            if (lvl1_magneticActive) {
                for (PowerUp p : gameManager.getPowerUps()) {
                    if (p instanceof CoinPowerUp coin && !coin.isMagnetized()) {
                        coin.setMagnetized(true, gameManager.getPlayer());
                    }
                }
            }

            // 62s: Kết thúc - Sau 2s từ khi nổ hoặc khi toàn bộ vàng đã được hút hết
            boolean hasCoinsRemaining = gameManager.getPowerUps().stream()
                    .anyMatch(p -> p instanceof CoinPowerUp && p.isAlive());
            if (!hasCoinsRemaining) {
                isVictory = true;
                gameManager.handleVictory();
            }
        }
    }

    @Override
    public void onEnemyKilled(EnemyObject enemy, GameManager gameManager) {
        if (enemy instanceof MiniBoss miniBoss && !lvl1_miniBossDefeated) {
            lvl1_miniBossDefeated = true;
            lvl1_bossDeathTime = System.currentTimeMillis();
            lvl1_magneticActive = false;

            // MiniBoss nổ rớt 20 đồng Vàng xung quanh vị trí nổ
            for (int c = 0; c < 20; c++) {
                double dropX = miniBoss.getX() + miniBoss.getSizeX() / 2.0 + (gameManager.getRandom().nextDouble() - 0.5) * 180;
                double dropY = miniBoss.getY() + miniBoss.getSizeY() / 2.0 + (gameManager.getRandom().nextDouble() - 0.5) * 120;
                CoinPowerUp coin = new CoinPowerUp(dropX, dropY);
                gameManager.addPowerUp(coin);
            }
        }
    }

    @Override
    public boolean isCompleted() {
        return isVictory;
    }
}
