package com.honey720.websocket_stomp.chat.service;

import com.honey720.websocket_stomp.chat.dto.ChatMessageDto;
import com.honey720.websocket_stomp.chat.dto.ChatRoomResponse;
import com.honey720.websocket_stomp.chat.dto.CreateRoomRequest;
import com.honey720.websocket_stomp.chat.entity.ChatMessage;
import com.honey720.websocket_stomp.chat.entity.ChatRoom;
import com.honey720.websocket_stomp.chat.entity.ChatRoomMember;
import com.honey720.websocket_stomp.chat.repository.ChatRoomMemberRepository;
import com.honey720.websocket_stomp.member.entity.Member;
import com.honey720.websocket_stomp.chat.repository.ChatMessageRepository;
import com.honey720.websocket_stomp.chat.repository.ChatRoomRepository;
import com.honey720.websocket_stomp.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ChatRoom createRoom(Long myId, CreateRoomRequest request) {
        Member me = findMyData(myId);
        Long targetId = request.getTargetMemberId();
        Member target = memberRepository.findById(targetId)
                .orElseThrow(() -> new IllegalArgumentException("해당 멤버를 찾을 수 없습니다: " + targetId));

        ChatRoom createdRoom = chatRoomRepository.save(ChatRoom.builder().build());

        ChatRoomMember myData = ChatRoomMember.builder()
                .memberId(me.getId())
                .chatRoomId(createdRoom.getId())
                .build();
        ChatRoomMember targetData = ChatRoomMember.builder()
                .memberId(target.getId())
                .chatRoomId(createdRoom.getId())
                .build();
        chatRoomMemberRepository.saveAll(List.of(myData, targetData));

        return createdRoom;
    }

    public List<ChatRoomResponse> getMyRooms(Long myId) {
        Member me = findMyData(myId);
        List<Long> chatRoomIds = chatRoomMemberRepository.findChatRoomIdsByMemberId(me.getId());
        return chatRoomIds.stream()
                .map(ChatRoomResponse::new)
                .toList();
    }

    private Member findMyData(Long myId) {
        return memberRepository.findById(myId)
                .orElseThrow(() -> new IllegalArgumentException("내 정보를 찾을 수 없습니다: " + myId));
    }

    @Transactional
    public ChatMessageDto saveMessage(Long roomId, ChatMessageDto dto) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다: " + roomId));

        Member member = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("멤버를 찾을 수 없습니다: " + dto.getMemberId()));

        ChatMessage saved = chatMessageRepository.save(ChatMessage.builder()
                .chatRoom(chatRoom)
                .member(member)
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
