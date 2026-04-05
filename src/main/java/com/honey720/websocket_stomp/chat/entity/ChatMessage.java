package com.honey720.websocket_stomp.chat.entity;

import com.honey720.websocket_stomp.entity.BaseEntity;
import com.honey720.websocket_stomp.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageType messageType;

    @Builder
    public ChatMessage(ChatRoom chatRoom, Member member, String content, MessageType messageType) {
        this.chatRoom = chatRoom;
        this.member = member;
        this.content = content;
        this.messageType = messageType;
    }

    public enum MessageType {
        ENTER, TALK, LEAVE
    }
}
