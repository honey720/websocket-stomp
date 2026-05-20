package com.honey720.websocket_stomp.member.entity;

import com.honey720.websocket_stomp.chat.entity.ChatRoom;
import com.honey720.websocket_stomp.entity.BaseEntity;
import com.honey720.websocket_stomp.chat.entity.ChatMessage;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Column(nullable = false, unique = true, length = 16)
    private String username;

    @Column(nullable = false, length = 60)
    private String password;

    @Column(nullable = false, unique = true, length = 16)
    private String nickname;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @Column(nullable = false)
    private boolean enabled;

    private LocalDateTime updatedAt;

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void banMember() {
        this.enabled = false;
    }

    public void unBanMember() {
        this.enabled = true;
    }

    @Builder
    public Member(String username, String password, String nickname, MemberRole role) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.role = role != null ? role : MemberRole.USER;
        enabled = true;
    }
}
