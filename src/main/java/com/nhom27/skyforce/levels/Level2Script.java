package com.nhom27.skyforce.levels;

import java.util.ArrayList;
import java.util.List;

import com.nhom27.skyforce.entities.enemies.NormalEnemy;
import com.nhom27.skyforce.entities.enemies.SniperEnemy;
import com.nhom27.skyforce.entities.items.CoinPowerUp;
import com.nhom27.skyforce.entities.items.PowerUp;
import com.nhom27.skyforce.main.Main;
import com.nhom27.skyforce.managers.GameManager;

/**
 * Class kịch bản dành riêng cho Level 2.
 * Chứa toàn bộ cờ trạng thái và kịch bản Wave xuất hiện kẻ địch của Level 2.
 */
public class Level2Script implements LevelScript {

    private boolean lvl2_spawned3s = false;
    private boolean lvl2_spawned8s = false;
    private boolean lvl2_spawned16s = false;
    private boolean lvl2_spawned22s = false;
    private boolean lvl2_spawned30s = false;
    private boolean lvl2_spawned35s = false;
    private long lvl2_lastFlyRainTime = 0;
    private boolean lvl2_spawned60s = false;
    private List<SniperEnemy> lvl2_deathSquad = new ArrayList<>();
    private boolean lvl2_coinsSpawned = false;
    private long lvl2_clearTime = 0;
    private boolean lvl2_magneticActive = false;
    private boolean isVictory = false;

    public Level2Script() {
        setup();
    }

    @Override
    public void setup() {
        lvl2_spawned3s = false;
        lvl2_spawned8s = false;
        lvl2_spawned16s = false;
        lvl2_spawned22s = false;
        lvl2_spawned30s = false;
        lvl2_spawned35s = false;
        lvl2_lastFlyRainTime = 0;
        lvl2_spawned60s = false;
        lvl2_deathSquad.clear();
        lvl2_coinsSpawned = false;
        lvl2_clearTime = 0;
        lvl2_magneticActive = false;
        isVictory = false;
    }

    @Override
    public void update(long now, double elapsedSec, GameManager gameManager) {
        // 🎬 Giai đoạn 1: Khởi động & Ôn bài (Giây 0 - 15)
        if (elapsedSec >= 3.0 && !lvl2_spawned3s) {
            NormalEnemy eLeft = new NormalEnemy(150.0, false, false, false, "enemy_normal_blue");
            eLeft.setPos(0, -eLeft.getSizeY());
            NormalEnemy eRight = new NormalEnemy(150.0, false, false, false, "enemy_normal_blue");
            eRight.setPos(Main.WIDTH - eRight.getSizeX(), -eRight.getSizeY());
            gameManager.spawnEnemy(eLeft);
            gameManager.spawnEnemy(eRight);
            lvl2_spawned3s = true;
        }

        if (elapsedSec >= 8.0 && !lvl2_spawned8s) {
            NormalEnemy eRow1 = new NormalEnemy(85.0, false, false, false, "enemy_normal_blue");
            eRow1.setPos(Main.WIDTH / 2.0 - eRow1.getSizeX() / 2.0, -eRow1.getSizeY());

            NormalEnemy eRow2 = new NormalEnemy(85.0, false, false, false, "enemy_normal_blue");
            eRow2.setPos(Main.WIDTH / 2.0 - eRow2.getSizeX() / 2.0 - eRow2.getSizeX(), -eRow2.getSizeY() * 2);

            NormalEnemy eRow3 = new NormalEnemy(85.0, false, false, false, "enemy_normal_blue");
            eRow3.setPos(Main.WIDTH / 2.0 - eRow3.getSizeX() / 2.0 + eRow3.getSizeX(), -eRow3.getSizeY() * 2);

            gameManager.spawnEnemy(eRow1);
            gameManager.spawnEnemy(eRow2);
            gameManager.spawnEnemy(eRow3);
            lvl2_spawned8s = true;
        }

        // ⚠️ Giai đoạn 2: Lần đầu chạm trán Sniper (Giây 15 - 30)
        if (elapsedSec >= 16.0 && !lvl2_spawned16s) {
            SniperEnemy s1 = new SniperEnemy(150.0, 1500, "UP");
            gameManager.spawnEnemy(s1);
            lvl2_spawned16s = true;
        }

        if (elapsedSec >= 22.0 && !lvl2_spawned22s) {
            SniperEnemy sLeft = new SniperEnemy(150.0, 1500, "LEFT");
            sLeft.setPos(0, -sLeft.getSizeY());
            SniperEnemy sRight = new SniperEnemy(150.0, 1500, "RIGHT");
            sRight.setPos(Main.WIDTH - sRight.getSizeX(), -sRight.getSizeY());
            gameManager.spawnEnemy(sLeft);
            gameManager.spawnEnemy(sRight);
            lvl2_spawned22s = true;
        }

        // ⚔️ Giai đoạn 3: Chiến trường hỗn loạn (Giây 35 - 55)
        if (elapsedSec >= 30.0 && !lvl2_spawned30s) {
            NormalEnemy eShieldRed = new NormalEnemy(120.0, false, false, true, "enemy_normal_red");
            eShieldRed.setPos(Main.WIDTH / 2.0 - eShieldRed.getSizeX(), -eShieldRed.getSizeY());
            gameManager.spawnEnemy(eShieldRed);
            lvl2_spawned30s = true;
        }

        if (elapsedSec >= 35.0 && !lvl2_spawned35s) {
            NormalEnemy n1 = new NormalEnemy(100.0, false, false, false, "enemy_normal_blue");
            n1.setPos(Main.WIDTH / 2.0 - 2 * n1.getSizeX(), -n1.getSizeY());
            NormalEnemy n2 = new NormalEnemy(100.0, false, false, false, "enemy_normal_blue");
            n2.setPos(Main.WIDTH / 2.0 - n2.getSizeX(), -n2.getSizeY());
            NormalEnemy n3 = new NormalEnemy(100.0, false, false, false, "enemy_normal_blue");
            n3.setPos(Main.WIDTH / 2.0, -n3.getSizeY());
            NormalEnemy n4 = new NormalEnemy(100.0, false, false, false, "enemy_normal_blue");
            n4.setPos(Main.WIDTH / 2.0 + n4.getSizeX(), -n4.getSizeY());
            SniperEnemy sBack = new SniperEnemy(80.0, 1500, "AUTO");
            sBack.setPos(Main.WIDTH / 2.0 - sBack.getSizeX() / 2.0, -2 * sBack.getSizeY());
            gameManager.spawnEnemy(n1);
            gameManager.spawnEnemy(n2);
            gameManager.spawnEnemy(n3);
            gameManager.spawnEnemy(n4);
            gameManager.spawnEnemy(sBack);
            lvl2_spawned35s = true;
        }

        if (elapsedSec >= 45.0 && elapsedSec <= 50.0) {
            if (now - lvl2_lastFlyRainTime >= 1500) {
                for (int i = 0; i < 3; i++) {
                    NormalEnemy e = new NormalEnemy(160.0, false, false, false, "enemy_normal_blue");
                    double spawnX = gameManager.getRandom().nextDouble() * (Main.WIDTH - e.getSizeX());
                    e.setPos(spawnX, -e.getSizeY() - (i * e.getSizeY()));
                    gameManager.spawnEnemy(e);
                }
                lvl2_lastFlyRainTime = now;
            }
        }

        // 💀 Giai đoạn 4: Bài Thi Cuối Cấp (Giây 60 - 75)
        if (elapsedSec >= 60.0 && !lvl2_spawned60s) {
            SniperEnemy death1 = new SniperEnemy(160.0, 1500, "AUTO");
            death1.setPos(Main.WIDTH / 2.0 - death1.getSizeX() / 2.0, -death1.getSizeY());
            SniperEnemy death2 = new SniperEnemy(140.0, 1500, "LEFT");
            death2.setPos(Main.WIDTH / 2.0 - death2.getSizeX() / 2.0 - death2.getSizeX(), -2 * death2.getSizeY());
            SniperEnemy death3 = new SniperEnemy(140.0, 1500, "RIGHT");
            death3.setPos(Main.WIDTH / 2.0 - death3.getSizeX() / 2.0 + death3.getSizeX(), -2 * death3.getSizeY());

            lvl2_deathSquad.clear();
            lvl2_deathSquad.add(death1);
            lvl2_deathSquad.add(death2);
            lvl2_deathSquad.add(death3);

            gameManager.spawnEnemy(death1);
            gameManager.spawnEnemy(death2);
            gameManager.spawnEnemy(death3);
            lvl2_spawned60s = true;
        }

        if (lvl2_spawned60s && !isVictory) {
            boolean allSquadDead = lvl2_deathSquad.stream().allMatch(e -> !e.isAlive());
            if (allSquadDead) {
                if (!lvl2_coinsSpawned) {
                    for (int c = 0; c < 15; c++) {
                        double dropX = Main.WIDTH / 2.0 + (gameManager.getRandom().nextDouble() - 0.5) * 160;
                        double dropY = Main.HEIGHT * 0.3 + (gameManager.getRandom().nextDouble() - 0.5) * 100;
                        CoinPowerUp coin = new CoinPowerUp(dropX, dropY);
                        gameManager.addPowerUp(coin);
                    }
                    lvl2_coinsSpawned = true;
                    lvl2_clearTime = System.currentTimeMillis();
                }

                long elapsedSinceClear = System.currentTimeMillis() - lvl2_clearTime;
                if (elapsedSinceClear >= 500 && !lvl2_magneticActive) {
                    lvl2_magneticActive = true;
                }

                if (lvl2_magneticActive) {
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
