package com.honey720.websocket_stomp.member.dto;

import com.honey720.websocket_stomp.member.entity.Member;
import lombok.Getter;

@Getter
public class MemberDto {
    private final Long id;
    private final String username;
    private final String nickname;

    public MemberDto(Member member) {
        this.id = member.getId();
        this.username = member.getUsername();
        this.nickname = member.getNickname();
    }
}
