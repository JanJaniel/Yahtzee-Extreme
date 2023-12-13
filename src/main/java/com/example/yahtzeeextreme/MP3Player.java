package com.example.yahtzeeextreme;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class MP3Player {
    private static MediaPlayer mediaPlayer;

    public static void playMP3(String filePath) {
        Media sound = new Media(new File(filePath).toURI().toString());

        // Stop the previous mediaPlayer if it's not null
        stopMP3();

        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }

    public static void stopMP3() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }


}