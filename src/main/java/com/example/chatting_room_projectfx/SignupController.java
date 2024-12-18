package com.example.chatting_room_projectfx;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class SignupController {
    private final UserService userService = new UserService();
    private final SignupGUI signupGUI;

    public SignupController(SignupGUI signupGUI) {
        this.signupGUI = signupGUI;
        this.signupGUI.setController(this);
    }

    public void signup(String username, String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            showAlert(AlertType.ERROR, "Error", "Passwords do not match!");
            return;
        }
        if (userService.registerUser(username, password)) {
            showAlert(AlertType.INFORMATION, "Success", "Sign up successful!");
            signupGUI.close(); // Close the sign-up window

            // Open the login window
            LoginGUI loginGUI = new LoginGUI();
            Stage loginStage = new Stage();
            loginGUI.start(loginStage); // Assuming start() method of LoginGUI sets up and shows the stage
        } else {
            showAlert(AlertType.ERROR, "Error", "Sign up failed!");
        }
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
