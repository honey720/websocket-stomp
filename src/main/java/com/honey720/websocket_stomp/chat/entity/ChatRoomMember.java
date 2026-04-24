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
public class ChatRoomMember extends BaseEntity {

    @Column(nullable = false)
    private Long chatRoomId;

    @Column(nullable = false)
    private Long memberId;

    @Builder
    public ChatRoomMember(Long chatRoomId, Long memberId) {
        this.chatRoomId = chatRoomId;
        this.memberId = memberId;
    }

}
