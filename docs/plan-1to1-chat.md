# 1:1 채팅 기능 기획

## 현황 및 문제점

### 현재 구조
- `ChatRoom`은 `name`(방 제목)만 가지며, 참여자 정보가 없음
- 채팅방 생성 시 방 이름만 입력하면 생성됨 (상대방 선택 없음)
- `GET /api/rooms` 는 **전체 채팅방 목록**을 반환 → 모든 유저가 모든 방을 볼 수 있음
- 채팅방 메시지 조회(`GET /api/rooms/{roomId}/messages`) 에 접근 권한 검증 없음

### 문제점 요약
| 문제 | 영향 |
|------|------|
| 참여자 개념 없음 | A-B 간 채팅방이 C에게도 노출됨 |
| 채팅방 생성 시 상대방 미지정 | 방 제목만으로 채팅방 생성 가능, 1:1 의미 없음 |
| 권한 검증 없음 | 참여하지 않은 채팅방 메시지도 조회 가능 |

---

## 목표

- **1:1 채팅**: A가 B에게 대화를 요청하면 A와 B만 해당 채팅방을 볼 수 있어야 함
- **참여자 기반 채팅방 생성**: 채팅방 생성 시 상대방 유저를 선택해서 생성
- **채팅방 목록 필터링**: 본인이 참여한 채팅방만 조회
- **접근 권한 보호**: 본인이 참여하지 않은 채팅방 접근 차단

---

## 기능 명세

### 1. 채팅방 생성 (`POST /api/rooms`)

**변경 전**
```json
Request: { "name": "방 제목" }
```

**변경 후**
```json
Request: { "targetMemberId": 2 }
```

- 요청자(JWT 토큰에서 추출)와 `targetMemberId` 두 명을 참여자로 등록
- 이미 두 사람 사이의 채팅방이 존재하면 **기존 방을 반환** (중복 생성 방지)
- 방 이름 필드 제거 (표시 이름은 상대방 닉네임으로 대체)

**비즈니스 규칙**
- 자기 자신에게 채팅 요청 불가 (`targetMemberId == 요청자 id` 시 400 반환)
- 존재하지 않는 유저에게 채팅 요청 시 404 반환

---

### 2. 채팅방 목록 조회 (`GET /api/rooms`)

**변경 전**
- 전체 채팅방 반환

**변경 후**
- JWT 토큰에서 요청자 식별 → **본인이 참여한 채팅방만** 반환
- 응답에 상대방 닉네임 포함

**응답 예시**
```json
[
  {
    "roomId": 1,
    "partnerNickname": "hong",
    "lastMessage": "안녕하세요",
    "lastMessageAt": "2026-04-08T10:00:00"
  }
]
```

---

### 3. 채팅방 메시지 조회 (`GET /api/rooms/{roomId}/messages`)

**변경 후**
- 요청자가 해당 채팅방 참여자인지 검증
- 참여자가 아니면 403 반환

---

### 4. 유저 목록 조회 (`GET /api/members`) — 신규

채팅 상대방 선택을 위해 가입된 유저 목록을 조회하는 API 필요

**응답 예시**
```json
[
  { "memberId": 2, "nickname": "hong" },
  { "memberId": 3, "nickname": "kim" }
]
```
- 본인은 목록에서 제외

---

## 변경이 필요한 도메인 모델

### 신규: `ChatRoomMember` (채팅방 참여자 연결 테이블)

```
ChatRoomMember
├── id (PK)
├── chatRoom (FK → ChatRoom)
└── member (FK → Member)
```

- `ChatRoom` ↔ `Member` 다대다 관계를 중간 테이블로 풀어냄
- 1:1 채팅이므로 한 방에 항상 참여자 2명

### 변경: `ChatRoom`

| 필드 | 변경 내용 |
|------|-----------|
| `name` | 제거 (상대방 닉네임으로 대체) |
| `members` | 추가 (`List<ChatRoomMember>`) |

---

## API 변경 요약

| 메서드 | 경로 | 변경 내용 |
|--------|------|-----------|
| POST | `/api/rooms` | `name` 제거, `targetMemberId` 추가, 중복 방 반환 로직 |
| GET | `/api/rooms` | 전체 조회 → 본인 참여 방만 조회 |
| GET | `/api/rooms/{roomId}/messages` | 참여자 권한 검증 추가 |
| GET | `/api/members` | 신규 — 채팅 상대 선택용 유저 목록 |

---

## 구현 순서 (개발 계획)

1. `ChatRoomMember` 엔티티 및 리포지토리 생성
2. `ChatRoom` 엔티티에서 `name` 제거, `members` 관계 추가
3. `CreateRoomRequest` 변경 (`targetMemberId` 필드)
4. `ChatService.createRoom()` — 참여자 등록 + 중복 방 조회 로직
5. `ChatService.getMyRooms()` — 본인 참여 방만 필터링
6. `ChatService.getMessages()` — 참여자 권한 검증 추가
7. `MemberController` — 유저 목록 조회 API 추가
8. `ChatRoomResponse` DTO에 상대방 닉네임 추가
