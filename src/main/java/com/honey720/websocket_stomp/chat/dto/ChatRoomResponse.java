package com.honey720.websocket_stomp.chat.dto;

import com.honey720.websocket_stomp.chat.entity.ChatRoom;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChatRoomResponse {
    private final Long id;
    private final String name;
    private final LocalDateTime createdAt;

    public ChatRoomResponse(ChatRoom chatRoom) {
        this.id = chatRoom.getId();
        this.name = chatRoom.getName();
        this.createdAt = chatRoom.getCreatedAt();
    }
}