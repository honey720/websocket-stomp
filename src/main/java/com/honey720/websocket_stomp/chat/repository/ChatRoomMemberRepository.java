package com.honey720.websocket_stomp.chat.repository;

import com.honey720.websocket_stomp.chat.entity.ChatRoomMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {

    @Query("SELECT crm.chatRoomId FROM ChatRoomMember crm WHERE crm.memberId = :memberId")
    List<Long> findChatRoomIdsByMemberId(@Param("memberId") Long memberId);
}
