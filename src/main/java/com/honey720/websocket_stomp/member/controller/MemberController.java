package com.honey720.websocket_stomp.member.controller;

import com.honey720.websocket_stomp.member.dto.MemberDto;
import com.honey720.websocket_stomp.member.dto.SignUpDto;
import com.honey720.websocket_stomp.member.service.MemberService;
import com.honey720.websocket_stomp.security.dto.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /**
     * 회원가입 API
     * - JSON 데이터를 받아 UserService에서 회원가입 처리
     */
    @PostMapping("/signUp")
    public ResponseEntity<String> signUp(@RequestBody @Valid SignUpDto signUpDto) {
        memberService.signUp(signUpDto);
        return ResponseEntity.ok("회원가입 성공");
    }

    @GetMapping("/api/members/me")
    public ResponseEntity<MemberDto> getMe(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return ResponseEntity.ok(memberService.getMe(userDetails.getUsername()));
    }

    @GetMapping("/api/members/search")
    public ResponseEntity<List<MemberDto>> search(@RequestParam String username) {
        return ResponseEntity.ok(memberService.searchByUsername(username));
    }
}
