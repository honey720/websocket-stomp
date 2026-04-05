package com.honey720.websocket_stomp.member.repository;

import com.honey720.websocket_stomp.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Boolean existsByUsername(String username);

    Optional<Member> findByUsername(String username);

    List<Member> findByUsernameContainingIgnoreCase(String username);
}
