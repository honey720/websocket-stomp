package com.honey720.websocket_stomp.chat.service;

import com.honey720.websocket_stomp.chat.dto.ChatMessageDto;
import com.honey720.websocket_stomp.chat.dto.CreateRoomRequest;
import com.honey720.websocket_stomp.chat.entity.ChatMessage;
import com.honey720.websocket_stomp.chat.entity.ChatRoom;
import com.honey720.websocket_stomp.chat.repository.ChatMessageRepository;
import com.honey720.websocket_stomp.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Transactional
    public ChatRoom createRoom(CreateRoomRequest request) {
        return chatRoomRepository.save(ChatRoom.builder()
                .name(request.getName())
                .build());
    }

    public List<ChatRoom> getAllRooms() {
        return chatRoomRepository.findAll();
    }

    @Transactional
    public ChatMessageDto saveMessage(Long roomId, ChatMessageDto dto) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다: " + roomId));

        ChatMessage saved = chatMessageRepository.save(ChatMessage.builder()
                .chatRoom(chatRoom)
                .sender(dto.getSender())
                .content(dto.getContent())
                .messageType(dto.getMessageType())
                .build());

        return ChatMessageDto.from(saved);
    }

    @Transactional(readOnly = true)
    public List<ChatMessageDto> getMessages(Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다: " + roomId));

        return chatMessageRepository.findByChatRoomOrderByCreatedAtAsc(chatRoom)
                .stream()
                .map(ChatMessageDto::from)
                .toList();
    }
}
