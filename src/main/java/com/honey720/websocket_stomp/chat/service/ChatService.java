package com.honey720.websocket_stomp.chat.service;

import com.honey720.websocket_stomp.chat.entity.ChatRoom;
import com.honey720.websocket_stomp.chat.repository.ChatMessageRepository;
import com.honey720.websocket_stomp.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private static ChatRoomRepository chatRoomRepository;
    private static ChatMessageRepository chatMessageRepository;

    public void createRoom() {

    }

    public List<ChatRoom> getAllRooms() {
        return chatRoomRepository.findAll();
    }

    public void saveMessage(Long roomId, ChatMessageDto messageDto) {

    }
}
