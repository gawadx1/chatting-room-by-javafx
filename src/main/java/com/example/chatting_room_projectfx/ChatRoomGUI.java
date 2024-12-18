package com.example.chatting_room_projectfx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.function.Consumer;

public class ChatRoomGUI extends Application {

    private final String username;
    private TextArea chatHistoryArea;
    private TextField messageField;
    private Button sendButton;
    private Button saveButton;
    private Button closeButton;
    private ComboBox<String> statusComboBox;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Consumer<Void> sendMessageAction;


    ChatHistoryDAO chatHistoryDAO;

    public ChatRoomGUI(String username) {
        this.username = username;
     }

    @Override
    public void start(Stage primaryStage) {
        setupGUI(primaryStage);
        connectToServer();
        startListeningForMessages();
    }

    private void setupGUI(Stage primaryStage) {
        primaryStage.setTitle("Chat Room - " + username);

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // Header
        Label headerLabel = new Label("Welcome to the Chat Room, " + username + "!");
        headerLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        headerLabel.setPadding(new Insets(10));
        headerLabel.setStyle("-fx-background-color: white;");
        root.setTop(headerLabel);

        // Chat History Area
        chatHistoryArea = new TextArea();
        chatHistoryArea.setEditable(false);
        chatHistoryArea.setWrapText(true);
        root.setCenter(new ScrollPane(chatHistoryArea));

        // Input Panel
        HBox inputPanel = new HBox(10);
        inputPanel.setPadding(new Insets(10));
        inputPanel.setAlignment(Pos.CENTER);

        messageField = new TextField();
        messageField.setPromptText("Enter your message here...");

        sendButton = new Button("Send");
        sendButton.setStyle("-fx-background-color: rgb(48, 52, 65); -fx-text-fill: white;");
        sendButton.setOnAction(e -> sendMessage(messageField.getText()));

        saveButton = new Button("Save Chat");
        saveButton.setStyle("-fx-background-color: rgb(68, 111, 136); -fx-text-fill: white;");
        saveButton.setOnAction(e -> saveChatHistoryToFile());

        closeButton = new Button("Close");
        closeButton.setStyle("-fx-background-color: rgb(255, 77, 77); -fx-text-fill: white;");
        closeButton.setOnAction(e -> handleClose(primaryStage));

        // Status ComboBox
        statusComboBox = new ComboBox<>();
        statusComboBox.getItems().addAll("Available", "Busy");
        statusComboBox.setValue("Available");

        inputPanel.getChildren().addAll(statusComboBox, messageField, sendButton, saveButton, closeButton);
        root.setBottom(inputPanel);

        Scene scene = new Scene(root, 600, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleClose(Stage primaryStage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Confirmation");
        alert.setHeaderText("Are you sure you want to exit?");
        alert.setContentText("Your chat history will not be saved.");
        if (alert.showAndWait().get() == ButtonType.OK) {
            sendMessage("User " + username + " has left the chat.");
            primaryStage.close();
            System.exit(0);
        }
    }

    private void connectToServer() {
        try {
            socket = new Socket("localhost", 12346); // Connect to server on localhost and port 12346
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out.println(username);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startListeningForMessages() {
        new Thread(() -> {
            String message;
            try {
                while ((message = in.readLine()) != null) {
                    chatHistoryArea.appendText(message + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }


    public void saveMessage(String sender, String message) {
        String sql = "INSERT INTO chat_history (sender_username, message) VALUES (?, ?)";
        try (PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(sql)) {
            statement.setString(1, sender);
            statement.setString(2, message);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }}
    private void sendMessage(String message) {
        if (message != null && !message.trim().isEmpty()) {
            String status = statusComboBox.getValue();
            String formattedMessage = username + " [" + status + "]: " + message;
            out.println(formattedMessage);

            chatHistoryArea.appendText("You [" + status + "]: " + message + "\n");
          saveMessage(username,message);
            messageField.clear();
            if (sendMessageAction != null) {
                sendMessageAction.accept(null);
            }
        }
    }

    public void setSendMessageAction(Consumer<Void> action) {
        this.sendMessageAction = action;
    }

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return messageField.getText();
    }

    public void addMessageToChatHistory(String sender, String message) {
        chatHistoryArea.appendText(sender + ": " + message + "\n");
    }

    private void saveChatHistoryToFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Chat History");
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(chatHistoryArea.getText());
                new Alert(Alert.AlertType.INFORMATION, "Chat history saved successfully!").showAndWait();
            } catch (IOException e) {
                new Alert(Alert.AlertType.ERROR, "Failed to save chat history.").showAndWait();
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
