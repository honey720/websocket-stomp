package com.honey720.websocket_stomp.chat.repository;

import com.honey720.websocket_stomp.chat.entity.ChatRoomMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {

    @Query("SELECT crm.chatRoomId FROM ChatRoomMember crm WHERE crm.memberId = :memberId")
    List<Long> findChatRoomIdsByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT crm.memberId FROM ChatRoomMember crm WHERE crm.chatRoomId = :roomId AND crm.memberId != :myId")
    Optional<Long> findPartnerIdByRoomIdAndMyId(@Param("roomId") Long roomId, @Param("myId") Long myId);

    @Query("SELECT crm1.chatRoomId FROM ChatRoomMember crm1 WHERE crm1.memberId = :myId AND crm1.chatRoomId IN " +
           "(SELECT crm2.chatRoomId FROM ChatRoomMember crm2 WHERE crm2.memberId = :targetId)")
    Optional<Long> findExistingRoomId(@Param("myId") Long myId, @Param("targetId") Long targetId);
}
