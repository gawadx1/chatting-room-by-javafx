package com.example.chatting_room_projectfx;

import com.example.chatting_room_projectfx.*;


public class ChatRoomController {
    private final ChatRoomGUI chatRoomGUI;
    private final ChatService chatService;

    public ChatRoomController(ChatRoomGUI chatRoomGUI) {
        this.chatRoomGUI = chatRoomGUI;
        this.chatService = new ChatService(new ChatHistoryDAO(DatabaseConnection.getConnection()));

        this.chatRoomGUI.setSendMessageAction(e -> sendMessage());
    }

    private void sendMessage() {
        String sender = chatRoomGUI.getUsername();
        String message = chatRoomGUI.getMessage();
         chatRoomGUI.addMessageToChatHistory(sender, message);
    }
}
