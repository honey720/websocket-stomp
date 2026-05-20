package com.honey720.websocket_stomp.chat.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class CreateRoomRequest {
    private final Long targetMemberId;

    @JsonCreator
    public CreateRoomRequest(@JsonProperty("targetMemberId") Long targetMemberId) {
        this.targetMemberId = targetMemberId;
    }
}
