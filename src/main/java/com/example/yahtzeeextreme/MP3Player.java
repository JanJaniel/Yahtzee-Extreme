package com.example.yahtzeeextreme;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class MP3Player {
    // Use a list or another data structure to store multiple MediaPlayers if needed
    private static MediaPlayer backgroundMediaPlayer;

    public static void playBackgroundMusic(String filePath) {
        Media sound = new Media(new File(filePath).toURI().toString());

        // Stop the previous background mediaPlayer if it's not null
        stopBackgroundMusic();

        backgroundMediaPlayer = new MediaPlayer(sound);
        backgroundMediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Repeat indefinitely for background music
        backgroundMediaPlayer.play();
    }

    public static void stopBackgroundMusic() {
        if (backgroundMediaPlayer != null) {
            backgroundMediaPlayer.stop();
        }
    }

    public static void playSound(String filePath) {
        Media sound = new Media(new File(filePath).toURI().toString());
        MediaPlayer soundMediaPlayer = new MediaPlayer(sound);
        soundMediaPlayer.play();
    }

    public static void setVolume(double volume) {
        if (backgroundMediaPlayer != null) {
            backgroundMediaPlayer.setVolume(volume);
        }
    }

}
