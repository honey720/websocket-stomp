package com.honey720.websocket_stomp.member.service;

import com.honey720.websocket_stomp.member.dto.MemberDto;
import com.honey720.websocket_stomp.member.dto.SignUpDto;
import com.honey720.websocket_stomp.member.entity.Member;
import com.honey720.websocket_stomp.member.entity.MemberRole;
import com.honey720.websocket_stomp.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * 회원가입 기능
     * - 중복된 username 체크
     * - 비밀번호 암호화 후 저장
     */
    @Transactional
    public void signUp(SignUpDto signUpDto) {
        validateDuplicateUserName(signUpDto.getUsername());
        Member member = createMemberEntity(signUpDto);
        memberRepository.save(member);
    }

    public MemberDto getMe(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return new MemberDto(member);
    }

    public List<MemberDto> searchByUsername(String username) {
        return memberRepository.findByUsernameContainingIgnoreCase(username)
                .stream()
                .map(MemberDto::new)
                .toList();
    }

    /**
     * 중복된 username 체크
     */
    private void validateDuplicateUserName(String username) {
        if (memberRepository.existsByUsername(username)) {
            log.warn("중복된 아이디 입니다: {}", username);
            throw new IllegalArgumentException("이미 사용중인 아이디입니다.");
        }
    }

    /**
     * User 엔티티 생성 (비밀번호 암호화 적용)
     */
    private Member createMemberEntity(SignUpDto signUpDto) {
        return Member.builder()
                .username(signUpDto.getUsername())
                .password(bCryptPasswordEncoder.encode(signUpDto.getPassword())) // 비밀번호 암호화
                .nickname(signUpDto.getNickname())
                .role(MemberRole.USER)
                .build();
    }
}
