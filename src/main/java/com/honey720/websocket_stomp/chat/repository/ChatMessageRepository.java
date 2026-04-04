package com.honey720.websocket_stomp.chat.repository;

import com.honey720.websocket_stomp.chat.entity.ChatMessage;
import com.honey720.websocket_stomp.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByChatRoomOrderByCreatedAtAsc(ChatRoom chatRoom);
}
