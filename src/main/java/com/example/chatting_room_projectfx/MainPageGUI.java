package com.example.chatting_room_projectfx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainPageGUI extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Main Page");

        // Create VBox for layout
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-background-color: lightgray;"); // Background color

        // Login Button
        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: #303441; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
        loginButton.setOnAction(e -> {
            primaryStage.close();
            LoginGUI loginGUI = new LoginGUI();
            Stage loginStage = new Stage();
            loginGUI.start(loginStage);
            new LoginController(loginGUI); // Initialize LoginController after showing the stage
        });

        // Sign Up Button
        Button signupButton = new Button("Sign Up");
        signupButton.setStyle("-fx-background-color: #5b738f; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
        signupButton.setOnAction(e -> {
            primaryStage.close();
            SignupGUI signupGUI = new SignupGUI();
            Stage signupStage = new Stage();
            signupGUI.start(signupStage);
            new SignupController(signupGUI); // Initialize SignupController after showing the stage
        });

        // Add components to VBox
        vbox.getChildren().addAll(
                new javafx.scene.control.Label("Welcome to the Application"),
                loginButton,
                signupButton
        );

        // Set the scene
        Scene scene = new Scene(vbox, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
