package com.example.chatting_room_projectfx;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServer {
    private static final int PORT = 12346;
    private static ConcurrentHashMap<String, PrintWriter> clientWriters = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, String> clientStatuses = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        System.out.println("Chat server started...");

        // Start the chat server in a separate thread
        new Thread(ChatServer::startServer).start();

        // Start the GUI to display user statuses
        SwingUtilities.invokeLater(() -> {
            UserStatusGUI userStatusGUI = new UserStatusGUI();
            userStatusGUI.setVisible(true);
        });
    }

    private static void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread {
        private final Socket clientSocket;
        private String username;
        private PrintWriter out;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                out = new PrintWriter(clientSocket.getOutputStream(), true);

                // Initial setup: get username
                out.println("Enter your username:");
                username = in.readLine();
                if (username == null) {
                    return;
                }

                synchronized (clientWriters) {
                    clientWriters.put(username, out);
                    clientStatuses.put(username, "Available"); // Default status is "Available"
                }

                // Notify other users of the new user
                broadcast("Server: " + username + " has joined the chat.");

                String message;
                while ((message = in.readLine()) != null) {
                    if (message.toLowerCase().startsWith("/quit")) {
                        break;
                    }

                    if (message.startsWith(username + " [")) {
                        String status = message.substring(message.indexOf("[") + 1, message.indexOf("]"));
                        clientStatuses.put(username, status); // Update the status
                    }

                    broadcast(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (username != null) {
                    clientWriters.remove(username);
                    clientStatuses.remove(username);
                    broadcast("Server: " + username + " has left the chat.");
                }
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void broadcast(String message) {
            for (PrintWriter writer : clientWriters.values()) {
                writer.println(message);
            }
        }
    }

    private static class UserStatusGUI extends JFrame {
        private JTable statusTable;
        private DefaultTableModel tableModel;

        public UserStatusGUI() {
            setTitle("User Statuses");
            setSize(400, 300);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLayout(new BorderLayout());

            // Create a table to display the statuses
            String[] columnNames = {"Username", "Status"};
            tableModel = new DefaultTableModel(columnNames, 0);
            statusTable = new JTable(tableModel);

            // Center align the status column
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            statusTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);

            add(new JScrollPane(statusTable), BorderLayout.CENTER);

            // Update the table periodically
            new Timer(1000, e -> updateStatusTable()).start();
        }

        private void updateStatusTable() {
            tableModel.setRowCount(0); // Clear the table
            for (String user : clientStatuses.keySet()) {
                String status = clientStatuses.get(user);
                tableModel.addRow(new Object[]{user, status});
            }
        }
    }
}
