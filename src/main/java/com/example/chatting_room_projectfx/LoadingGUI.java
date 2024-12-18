package com.example.chatting_room_projectfx;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LoadingGUI extends Application {

    private final String[] spinnerSymbols = {"|", "/", "-", "\\"};
    private int spinnerIndex = 0;
    private Label spinnerLabel;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Loading");

        // Create a VBox for layout
        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-background-color: white;");

        // Loading message
        Label loadingLabel = new Label("Loading...");
        loadingLabel.setStyle("-fx-font-size: 16px;");

        // Spinner label for animation
        spinnerLabel = new Label("|");
        spinnerLabel.setStyle("-fx-font-size: 30px; -fx-font-family: 'Monospaced';");

        // Add components to VBox
        vbox.getChildren().addAll(loadingLabel, spinnerLabel);

        // Set the scene
        Scene scene = new Scene(vbox, 300, 150);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Start spinner animation
        Timeline spinnerTimeline = new Timeline(new KeyFrame(Duration.millis(100), e -> updateSpinner()));
        spinnerTimeline.setCycleCount(Timeline.INDEFINITE);
        spinnerTimeline.play();

        // Timer for loading simulation
        PauseTransition loadingPause = new PauseTransition(Duration.seconds(3));
        loadingPause.setOnFinished(e -> {
            spinnerTimeline.stop(); // Stop the spinner animation
            Platform.runLater(() -> {
                primaryStage.close(); // Close the LoadingGUI
                MainPageGUI mainGUI = new MainPageGUI(); // Replace with your actual main GUI class
                Stage mainStage = new Stage();
                mainGUI.start(mainStage);
            });
        });
        loadingPause.play();
    }

    // Updates the spinner animation
    private void updateSpinner() {
        spinnerIndex = (spinnerIndex + 1) % spinnerSymbols.length;
        spinnerLabel.setText(spinnerSymbols[spinnerIndex]);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
