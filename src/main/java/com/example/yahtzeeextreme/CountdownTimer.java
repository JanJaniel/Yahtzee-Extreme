package com.example.yahtzeeextreme;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class CountdownTimer {

    private final Label countdownLabel;
    private int countdownSeconds;
    private final Timeline timeline;
    private final Runnable onFinishCallback;

    public CountdownTimer(Label countdownLabel, int initialSeconds, Runnable onFinishCallback) {
        this.countdownLabel = countdownLabel;
        this.countdownSeconds = initialSeconds;
        this.onFinishCallback = onFinishCallback;

        // Initialize the timeline with a one-second interval
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), this::handleTimerEvent)
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    public void start() {
        // Start the countdown
        timeline.play();
    }

    public void stop() {
        // Stop the countdown
        timeline.stop();
    }

    private void handleTimerEvent(ActionEvent event) {
        // Update the label with the current countdown value
        updateCountdownLabel();

        // Decrement the countdown value
        countdownSeconds--;

        // Check if the countdown has reached zero
        if (countdownSeconds < -1) {
            // Stop the countdown when it reaches zero
            stop();
            // Execute the callback function
            onFinishCallback.run();
        }
    }

    private void updateCountdownLabel() {
        // Update the label with the current countdown value
        countdownLabel.setText(Integer.toString(countdownSeconds));
    }
}
