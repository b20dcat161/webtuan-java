package com.example.demo8.DAO;

import com.example.demo8.model.Message;

import java.util.List;

import java.util.List;

public interface MessageDAO {
    void sendMessage(int senderId, int receiverId, String message);
    List<Message> getMessagesBetween(int userId, int otherUserId);
    void updateMessage(int messageId, String newContent);
    void deleteMessage(int messageId);
}
