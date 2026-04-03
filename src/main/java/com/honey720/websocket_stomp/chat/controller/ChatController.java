package com.honey720.websocket_stomp.chat.controller;

import com.honey720.websocket_stomp.chat.dto.ChatMessageDto;
import com.honey720.websocket_stomp.chat.dto.ChatRoomResponse;
import com.honey720.websocket_stomp.chat.dto.CreateRoomRequest;
import com.honey720.websocket_stomp.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    // 채팅방 생성
    @PostMapping("/api/rooms")
    public ResponseEntity<ChatRoomResponse> createRoom(@RequestBody CreateRoomRequest request) {
        return ResponseEntity.ok(new ChatRoomResponse(chatService.createRoom(request)));
    }

    // 채팅방 목록 조회
    @GetMapping("/api/rooms")
    public ResponseEntity<List<ChatRoomResponse>> getRooms() {
        return ResponseEntity.ok(
                chatService.getAllRooms().stream()
                        .map(ChatRoomResponse::new)
                        .toList()
        );
    }

    // 채팅방 메시지 내역 조회
    @GetMapping("/api/rooms/{roomId}/messages")
    public ResponseEntity<List<ChatMessageDto>> getMessages(@PathVariable Long roomId) {
        return ResponseEntity.ok(chatService.getMessages(roomId));
    }

    /**
     * STOMP 메시지 수신 및 브로드캐스트
     * 클라이언트: stompClient.send('/app/chat/{roomId}', {}, JSON.stringify(payload))
     * 구독:       stompClient.subscribe('/topic/chat/{roomId}', callback)
     */
    @MessageMapping("/chat/{roomId}")
    public void handleMessage(@DestinationVariable Long roomId, @Payload ChatMessageDto messageDto) {
        messageDto.setRoomId(roomId);
        ChatMessageDto saved = chatService.saveMessage(roomId, messageDto);
        messagingTemplate.convertAndSend("/topic/chat/" + roomId, saved);
    }
}
