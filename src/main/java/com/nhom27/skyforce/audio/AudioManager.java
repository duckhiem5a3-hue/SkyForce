package com.nhom27.skyforce.audio;

import com.nhom27.skyforce.utils.AssetManager;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class AudioManager {
    private static AudioManager instance;
    private boolean isMuted = false;
    private MediaPlayer mediaPlayer;
    private String currentMusicName = "";

    private AudioManager() {
        playMusic("background_home_music");
    }

    public static AudioManager getInstance() {
        if (instance == null)
            instance = new AudioManager();
        return instance;
    }

    public void playMusic(String musicName) {
        playMusic(musicName, true);
    }

    public void playMusicOnce(String musicName) {
        playMusic(musicName, false);
    }

    public void playMusic(String musicName, boolean loop) {
        if (musicName == null || musicName.isEmpty())
            return;

        // Nếu đang phát đúng bài nhạc này rồi thì không phát lại từ đầu
        if (musicName.equals(currentMusicName) && mediaPlayer != null
                && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            return;
        }

        stopMusic();

        Media media = AssetManager.getMusic(musicName);
        if (media != null) {
            mediaPlayer = new MediaPlayer(media);
            if (loop) {
                mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            } else {
                mediaPlayer.setCycleCount(1);
            }
            mediaPlayer.setMute(isMuted);
            mediaPlayer.play();
            currentMusicName = musicName;
        } else {
            System.out.println("Không tìm thấy nhạc: " + musicName);
        }
    }

    public void playSound(String soundName) {
        // Nếu đang tắt tiếng thì không phát
        if (isMuted || soundName == null || soundName.isEmpty())
            return;

        AudioClip clip = AssetManager.getSound(soundName);

        if (clip != null) {
            // Giảm âm lượng SFX xuống một chút để không lấn át nhạc nền
            clip.setVolume(0.2);
            clip.play();
        } else {
            System.out.println("Không tìm thấy âm thanh SFX: " + soundName);
        }
    }

    public void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
            currentMusicName = "";
        }
    }

    public void toggleMute() {
        isMuted = !isMuted;
        if (mediaPlayer != null) {
            mediaPlayer.setMute(isMuted);
        }
    }

    public boolean isMuted() {
        return isMuted;
    }

    public String getCurrentMusicName() {
        return currentMusicName;
    }
}
