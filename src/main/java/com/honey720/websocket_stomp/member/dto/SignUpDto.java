package com.honey720.websocket_stomp.member.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class SignUpDto {

    @Size(min = 4, max = 16)
    @NotEmpty
    private String username;

    @Size(min = 8, max = 16)
    @NotEmpty
    private String password;

    @Size(min = 4, max = 16)
    @NotEmpty
    private String nickname;
}
