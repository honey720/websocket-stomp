package com.honey720.websocket_stomp.chat.dto;

import lombok.Getter;

@Getter
public class ChatRoomResponse {
    private final Long id;
    private final String partnerNickname;

    public ChatRoomResponse(Long chatRoomId, String partnerNickname) {
        this.id = chatRoomId;
        this.partnerNickname = partnerNickname;
    }
}
