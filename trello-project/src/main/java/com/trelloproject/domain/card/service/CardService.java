package com.trelloproject.domain.card.service;

import com.trelloproject.common.dto.ResponseDto;
import com.trelloproject.common.enums.MemberRole;
import com.trelloproject.common.exceptions.AccessDeniedException;
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
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardService {

    private final CardRepository cardRepository;
    private final CardListRepository cardListRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ResponseDto<CardResponse> createOrUpdateCard(AuthUser authUser, Long listId, CardRequest request) {
        // 카드 작성자의 권한 확인 (매니저 ID 확인 없이 진행)
        Member member = validateMemberAndCheckPermissions(authUser);
        CardList cardList = findByIdOrThrow(cardListRepository, listId, "card list");

        List<Manager> managers = request.getManagers().stream()
                .map(managerRequest -> new Manager(member))
                .collect(Collectors.toList());

        Card card = new Card(request, managers);  // 카드 생성 시 매니저 할당
        card.setCardList(cardList);
        cardRepository.save(card);

        return ResponseDto.of(HttpStatus.CREATED, "카드가 성공적으로 생성되었습니다.", new CardResponse(card));
    }

    public ResponseDto<CardResponse> getCardDetails(Long listId, Long cardId) {
        findByIdOrThrow(cardListRepository, listId, "card list");

        Card card = findByIdOrThrow(cardRepository, cardId, "card");

        return ResponseDto.of(HttpStatus.OK, "카드 조회를 성공했습니다", new CardResponse(card));
    }

    @Transactional
    public ResponseDto<CardResponse> updateCard(AuthUser authUser, Long listId, Long cardId, CardRequest request) {
        // 수정 요청자의 권한 확인 (매니저 ID 확인 없이 진행)
        Member member = validateMemberAndCheckPermissions(authUser);
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
        validateMemberAndCheckPermissions(authUser);
        findByIdOrThrow(cardListRepository, listId, "card list");

        if (!cardRepository.existsById(cardId)) {
            throw new IllegalArgumentException("잘못된 cardId 입니다.");
        }

        cardRepository.deleteById(cardId);
        return ResponseDto.of(HttpStatus.NO_CONTENT, "카드가 성공적으로 삭제되었습니다.");
    }

    private Member validateMemberAndCheckPermissions(AuthUser authUser) {
        // 카드 작성자의 권한을 확인하는 로직 (읽기 전용 멤버 제한)
        Member member = memberRepository.findByUserId(authUser.getUserId())
                .orElseThrow(MemberNotFoundException::new);

        if (member.getRole() == MemberRole.READ_ONLY) {
            throw new AccessDeniedException("읽기 전용 멤버는 작업을 수행할 수 없습니다.");
        }

        return member;
    }

    private <T> T findByIdOrThrow(JpaRepository<T, Long> repository, Long id, String entityName) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 " + entityName + " ID 입니다."));
    }
}
