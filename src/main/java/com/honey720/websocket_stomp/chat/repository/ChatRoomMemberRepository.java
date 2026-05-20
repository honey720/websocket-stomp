package com.honey720.websocket_stomp.chat.repository;

import com.honey720.websocket_stomp.chat.dto.ChatRoomResponse;
import com.honey720.websocket_stomp.chat.entity.ChatRoom;
import com.honey720.websocket_stomp.chat.entity.ChatRoomMember;
import com.honey720.websocket_stomp.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {

    @Query("SELECT new com.honey720.websocket_stomp.chat.dto.ChatRoomResponse(crm1.chatRoom.id, crm2.member.nickname) " +
           "FROM ChatRoomMember crm1 JOIN ChatRoomMember crm2 ON crm1.chatRoom = crm2.chatRoom " +
           "WHERE crm1.member = :me AND crm2.member != :me")
    List<ChatRoomResponse> findChatRoomsWithPartner(@Param("me") Member me);
}
