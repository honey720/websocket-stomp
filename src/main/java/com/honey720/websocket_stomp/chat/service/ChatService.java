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
    public ChatRoomResponse createRoom(String myUsername, CreateRoomRequest request) {
        Member me = findMemberByUsername(myUsername);
        Long targetId = request.getTargetMemberId();
        if (me.getId().equals(targetId)) {
            throw new IllegalArgumentException("자기 자신과 대화할 수 없습니다.");
        }
        Member target = memberRepository.findById(targetId)
                .orElseThrow(() -> new IllegalArgumentException("해당 멤버를 찾을 수 없습니다: " + targetId));

        //Optional<Long> existingRoomId = chatRoomMemberRepository.findExistingRoomId(me.getId(), targetId);
        //if (existingRoomId.isPresent()) {
        //    return new ChatRoomResponse(existingRoomId.get(), target.getNickname());
        //}
//
        ChatRoom createdRoom = chatRoomRepository.save(ChatRoom.builder().build());

        chatRoomMemberRepository.saveAll(List.of(
                ChatRoomMember.builder().member(me).chatRoom(createdRoom).build(),
                ChatRoomMember.builder().member(target).chatRoom(createdRoom).build()
        ));

        return new ChatRoomResponse(createdRoom.getId(), target.getNickname());
    }

    public List<ChatRoomResponse> getMyRooms(String myUsername) {
        Member me = findMemberByUsername(myUsername);
        return chatRoomMemberRepository.findChatRoomsWithPartner(me);
    }

    private Member findMemberByUsername(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("멤버를 찾을 수 없습니다: " + username));
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
