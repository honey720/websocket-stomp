package com.honey720.websocket_stomp.chat.dto;

import com.honey720.websocket_stomp.chat.entity.ChatMessage;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageDto {

    private Long roomId;
    private Long memberId;
    private String nickname;
    private String content;
    private ChatMessage.MessageType messageType;
    private LocalDateTime createdAt;

    public static ChatMessageDto from(ChatMessage message) {
        return ChatMessageDto.builder()
                .roomId(message.getChatRoom().getId())
                .memberId(message.getMember().getId())
                .nickname(message.getMember().getNickname())
                .content(message.getContent())
                .messageType(message.getMessageType())
                .createdAt(message.getCreatedAt())
                .build();
    }
}
