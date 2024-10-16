package com.trelloproject.domain.card.service;

import com.trelloproject.common.dto.ResponseDto;
import com.trelloproject.common.enums.MemberRole;
import com.trelloproject.common.exceptions.AccessDeniedException;
import com.trelloproject.common.exceptions.ListNotFoundException;
import com.trelloproject.common.exceptions.MemberNotFoundException;
import com.trelloproject.domain.card.dto.CardRequest;
import com.trelloproject.domain.card.dto.CardResponse;
import com.trelloproject.domain.card.entity.Card;
import com.trelloproject.domain.card.repository.CardRepository;
import com.trelloproject.domain.list.entity.CardList;
import com.trelloproject.domain.list.repository.CardListRepository;
import com.trelloproject.domain.manager.entity.Manager;
import com.trelloproject.domain.member.entity.Member;
import com.trelloproject.domain.member.repository.MemberRepository;
import com.trelloproject.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardService {

    private final CardRepository cardRepository;
    @Qualifier("sentinelRedisTemplate")  // Sentinel Redis를 사용
    private final RedisTemplate<String, Object> redisTemplate;
    private final CardListRepository cardListRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ResponseDto<CardResponse> createOrUpdateCard(AuthUser authUser, Long listId, CardRequest request) {
        Member member = validateMemberAndCheckPermissions(authUser, listId);
        CardList cardList = findByIdOrThrow(cardListRepository, listId, "card list");

        List<Manager> managers = request.getManagers().stream()
                .map(managerRequest -> new Manager(member))
                .collect(Collectors.toList());

        Card card = new Card(request, managers);
        card.setCardList(cardList);
        cardRepository.save(card);

        return ResponseDto.of(HttpStatus.CREATED, "카드가 성공적으로 생성되었습니다.", new CardResponse(card));
    }

    // 카드 조회수 증가 및 조회
    @Transactional
    public ResponseDto<CardResponse> getCardDetails(AuthUser authUser, Long listId, Long cardId) {
        findByIdOrThrow(cardListRepository, listId, "card list");
        Card card = findByIdOrThrow(cardRepository, cardId, "card");

        String redisKey = "card:viewcount:" + cardId;
        incrementViewCount(redisKey, authUser.getUserId().toString());

        Long viewCount = redisTemplate.opsForValue().increment(redisKey, 0);
        card.setViewCount(viewCount);
        CardResponse cardResponse = new CardResponse(card);
        return ResponseDto.of(HttpStatus.OK, "카드 조회를 성공했습니다", cardResponse);
    }

    private void incrementViewCount(String redisKey, String userId) {
        String userKey = redisKey + ":user:" + userId;

        if (Boolean.TRUE.equals(redisTemplate.hasKey(userKey))) {
            return;
        }

        redisTemplate.opsForValue().increment(redisKey);
        redisTemplate.opsForValue().set(userKey, "1", Duration.ofHours(24));
    }

    // 인기 카드 랭킹 관리
    public List<CardResponse> getTopCards() {
        String rankingKey = "card:ranking";

        // Redis에서 인기 카드 ID 리스트 가져오기
        List<Object> topCardIds = redisTemplate.opsForList().range(rankingKey, 0, 9);

        List<CardResponse> topCards = topCardIds.stream()
                .map(cardId -> {
                    Card card = findByIdOrThrow(cardRepository, (Long) cardId, "card");
                    return new CardResponse(card);
                })
                .collect(Collectors.toList());

        return topCards;
    }

    // 자정에 캐시 리셋
    @Scheduled(cron = "0 0 0 * * *")  // 매일 자정 실행
    @Transactional
    public void resetDailyViewCount() {
        // 조회수 캐시 리셋
        String pattern = "card:viewcount:*";
        Set<String> keys = redisTemplate.keys(pattern);

        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }

        // 랭킹 캐시 리셋
        redisTemplate.delete("card:ranking");
    }

    @Transactional
    public ResponseDto<CardResponse> updateCard(AuthUser authUser, Long listId, Long cardId, CardRequest request) {
        Member member = validateMemberAndCheckPermissions(authUser, listId);
        findByIdOrThrow(cardListRepository, listId, "card list");

        Card card = findByIdOrThrow(cardRepository, cardId, "card");

        List<Manager> managers = request.getManagers().stream()
                .map(managerRequest -> new Manager(member))
                .collect(Collectors.toList());

        card.updateCard(request, managers);  // 카드 수정 시 매니저 재할당
        cardRepository.save(card);

        return ResponseDto.of(HttpStatus.OK, "카드가 성공적으로 수정되었습니다.", new CardResponse(card));
    }

    @Transactional
    public ResponseDto<Void> deleteCard(AuthUser authUser, Long listId, Long cardId) {
        // 삭제 요청자의 권한 확인
        validateMemberAndCheckPermissions(authUser, listId);
        findByIdOrThrow(cardListRepository, listId, "card list");

        if (!cardRepository.existsById(cardId)) {
            throw new IllegalArgumentException("잘못된 cardId 입니다.");
        }

        cardRepository.deleteById(cardId);
        return ResponseDto.of(HttpStatus.OK, "카드가 성공적으로 삭제되었습니다.");
    }

    private Member validateMemberAndCheckPermissions(AuthUser authUser, Long listId) {
        // 카드 작성자의 권한을 확인하는 로직 (읽기 전용 멤버 제한)
        CardList workspaceId = cardListRepository.findWithBoardAndWorkspaceByCardListId(listId).orElseThrow(ListNotFoundException::new);
        Member member = memberRepository.findByWorkspace_IdAndUser_Id(workspaceId.getId(), authUser.getUserId())
                .orElseThrow(MemberNotFoundException::new);

        if (member.getRole().equals(MemberRole.ROLE_READ_ONLY)) {
            throw new AccessDeniedException("읽기 전용 멤버는 작업을 수행할 수 없습니다.");
        }

        return member;
    }

    private <T> T findByIdOrThrow(JpaRepository<T, Long> repository, Long id, String entityName) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 " + entityName + " ID 입니다."));
    }
}
