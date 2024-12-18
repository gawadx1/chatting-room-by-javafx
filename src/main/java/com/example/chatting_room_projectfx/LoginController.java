package com.example.chatting_room_projectfx;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginController {
    private final UserService userService = new UserService();
    private final LoginGUI loginGUI;
    private final UserDAO userDAO;
    private final ExecutorService executorService;

    public LoginController(LoginGUI loginGUI) {
        this.loginGUI = loginGUI;
        this.loginGUI.setController(this);
        this.userDAO = new UserDAO(DatabaseConnection.getConnection());
        this.executorService = Executors.newFixedThreadPool(5); // You can adjust the pool size as needed
    }

    private void updateUserStatus(String username, String status) {
        String query = "UPDATE users SET status = ? WHERE username = ?";
        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(query)) {
            stmt.setString(1, status);
            stmt.setString(2, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void login(String username, String password) {
        executorService.submit(() -> {
            boolean loginSuccess = userService.loginUser(username, password);

            Platform.runLater(() -> {
                if (loginSuccess) {
                    updateUserStatus(username, "active");
                    showAlert(AlertType.INFORMATION, "Login successful!");

                    try {
                        userDAO.setUserActive(username);
                        loginGUI.getStage().close();

                        // Start Chat Room GUIs
                        ChatRoomGUI chatRoomGUI = new ChatRoomGUI(username);
                        new ChatRoomController(chatRoomGUI);
                        chatRoomGUI.start(new Stage());



                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    showAlert(AlertType.ERROR, "Invalid username or password!");
                }
            });
        });
    }

    private void showAlert(AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(alertType == AlertType.ERROR ? "Error" : "Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
