package com.honey720.websocket_stomp.security.service;

import com.honey720.websocket_stomp.member.entity.Member;
import com.honey720.websocket_stomp.member.repository.MemberRepository;
import com.honey720.websocket_stomp.security.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    /**
     * username을 이용해 사용자 정보를 조회
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> memberOptional = memberRepository.findByUsername(username);

        // 사용자가 존재하지 않을 경우 예외 throw
        Member member = memberOptional.orElseThrow(() -> {
            log.warn("사용자를 찾을 수 없습니다: username={}", username);
            return new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username);
        });

        return new CustomUserDetails(member);
    }
}
