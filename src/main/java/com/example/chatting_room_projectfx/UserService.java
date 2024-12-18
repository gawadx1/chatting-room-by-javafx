package com.example.chatting_room_projectfx;

public class UserService {
    private final UserDAO userDAO = new UserDAO();

    public boolean registerUser(String username, String password) {
        return userDAO.registerUser(username, password);
    }

    public boolean loginUser(String username, String password) {
        return userDAO.loginUser(username, password);
    }
}
