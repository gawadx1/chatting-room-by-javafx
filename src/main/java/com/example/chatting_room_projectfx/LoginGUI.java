package com.example.chatting_room_projectfx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class LoginGUI extends Application {
    private Stage stage;

    private TextField usernameField;
    private PasswordField passwordField;
    private Button loginButton;
    private Button cancelButton;
    private Button signupButton;
    private LoginController controller;

    @Override
    public void start(Stage primaryStage) {

        this.stage = primaryStage;
        this.controller = new LoginController(this); // Initialize the controller

        primaryStage.setTitle("Login");

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setAlignment(Pos.CENTER);

        Label usernameLabel = new Label("Username:");
        usernameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #0066cc;");
        gridPane.add(usernameLabel, 0, 0);

        usernameField = new TextField();
        usernameField.setPrefWidth(200);
        gridPane.add(usernameField, 1, 0);

        Label passwordLabel = new Label("Password:");
        passwordLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #0066cc;");
        gridPane.add(passwordLabel, 0, 1);

        passwordField = new PasswordField();
        passwordField.setPrefWidth(200);
        gridPane.add(passwordField, 1, 1);

        loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: #303441; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
        loginButton.setOnAction(e -> controller.login(usernameField.getText(), passwordField.getText()));
        gridPane.add(loginButton, 0, 2, 2, 1);
        GridPane.setHalignment(loginButton, javafx.geometry.HPos.CENTER);

        cancelButton = new Button("Cancel");
        cancelButton.setStyle("-fx-background-color: #ff4d4d; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
        cancelButton.setOnAction(e -> System.exit(0));
        gridPane.add(cancelButton, 0, 3, 1, 1);
        GridPane.setHalignment(cancelButton, javafx.geometry.HPos.CENTER);

        signupButton = new Button("Sign Up");
        signupButton.setStyle("-fx-background-color: #5b738f; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
        signupButton.setOnAction(e -> {
            primaryStage.close();
            SignupGUI signupGUI = new SignupGUI();
            new SignupController(signupGUI);
            signupGUI.start(new Stage());
        });
        gridPane.add(signupButton, 1, 3, 1, 1);
        GridPane.setHalignment(signupButton, javafx.geometry.HPos.CENTER);

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(loginButton, cancelButton, signupButton);

        HBox.setMargin(signupButton, new Insets(0, 0, 0, 0));
        HBox.setMargin(cancelButton, new Insets(0, 0, 0, 0));
        HBox.setMargin(loginButton, new Insets(0, 0, 0, 0));

        gridPane.add(buttonBox, 0, 3, 2, 1);
        GridPane.setHalignment(buttonBox, javafx.geometry.HPos.CENTER);
        GridPane.setMargin(buttonBox, new Insets(20, 0, 0, 0));

        Scene scene = new Scene(gridPane, 500, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public Stage getStage() {
        return stage;
    }

    public void setController(LoginController controller) {
        this.controller = controller;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
