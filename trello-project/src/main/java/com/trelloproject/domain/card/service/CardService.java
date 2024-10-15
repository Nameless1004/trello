package com.trelloproject.domain.card.service;

import com.trelloproject.common.dto.ResponseDto;
import com.trelloproject.common.enums.ManagerRole;
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
import com.trelloproject.domain.manager.repository.ManagerRepository;
import com.trelloproject.domain.member.entity.Member;
import com.trelloproject.domain.member.repository.MemberRepository;
import com.trelloproject.security.AuthUser;
import lombok.RequiredArgsConstructor;
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
    private final ManagerRepository managerRepository;
    private final CardListRepository cardListRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ResponseDto<CardResponse> createOrUpdateCard(AuthUser authUser, Long listId, CardRequest request) {
        Member member = memberRepository.findByUserId(authUser.getUserId())
                .orElseThrow(MemberNotFoundException::new);

        if(member.getRole() == MemberRole.READ_ONLY) {
            throw new org.springframework.security.access.AccessDeniedException("읽기 전용 멤버는 생성할 수 없습니다.");
        }

        List<Manager> managers = request.getManagers().stream()
                .map(managerRequest -> managerRepository.findById(managerRequest.getId())
                        .orElseThrow(() -> new IllegalArgumentException("잘못된 manager ID 입니다.")))
                .collect(Collectors.toList());

        if (managers.stream().anyMatch(manager -> ManagerRole.READ_ONLY.equals(manager.getRole()))) {
            throw new AccessDeniedException("읽기 전용 역할을 가진 멤버는 카드를 생성/수정할 수 없습니다.");
        }

        CardList cardList = cardListRepository.findById(listId)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 card list ID 입니다."));

        Card card = new Card(request, managers);
        card.setCardList(cardList);
        cardRepository.save(card);

        return ResponseDto.of(HttpStatus.CREATED, "카드가 성공적으로 생성되었습니다.", new CardResponse(card));
    }

    public ResponseDto<CardResponse> getCardDetails(Long listId, Long cardId) {
        cardListRepository.findById(listId)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 card list ID 입니다."));

        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 card ID 입니다."));

        return ResponseDto.of(HttpStatus.OK, "카드 조회를 성공했습니다", new CardResponse(card));
    }

    @Transactional
    public ResponseDto<CardResponse> updateCard(AuthUser authUser, Long listId, Long cardId, CardRequest request) {
        Member member = memberRepository.findByUserId(authUser.getUserId())
                .orElseThrow(MemberNotFoundException::new);

        if(member.getRole() == MemberRole.READ_ONLY) {
            throw new org.springframework.security.access.AccessDeniedException("읽기 전용 멤버는 수정할 수 없습니다.");
        }

        cardListRepository.findById(listId)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 card list ID 입니다."));

        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 card ID 입니다."));

        List<Manager> managers = request.getManagers().stream()
                .map(managerRequest -> managerRepository.findById(managerRequest.getId())
                        .orElseThrow(() -> new IllegalArgumentException("잘못된 manager ID 입니다.")))
                .collect(Collectors.toList());

        if (managers.stream().anyMatch(manager -> ManagerRole.READ_ONLY.equals(manager.getRole()))) {
            throw new AccessDeniedException("읽기 전용 역할을 가진 멤버는 카드를 수정할 수 없습니다.");
        }

        card.updateCard(request, managers);
        cardRepository.save(card);

        return ResponseDto.of(HttpStatus.OK, "카드가 성공적으로 수정되었습니다.", new CardResponse(card));
    }

    @Transactional
    public ResponseDto<Void> deleteCard(AuthUser authUser, Long listId, Long cardId) {
        Member member = memberRepository.findByUserId(authUser.getUserId())
                .orElseThrow(MemberNotFoundException::new);

        if(member.getRole() == MemberRole.READ_ONLY) {
            throw new org.springframework.security.access.AccessDeniedException("읽기 전용 멤버는 삭제할 수 없습니다.");
        }

        cardListRepository.findById(listId)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 card list ID 입니다."));

        if (!cardRepository.existsById(cardId)) {
            throw new IllegalArgumentException("잘못된 cardId 입니다.");
        }

        cardRepository.deleteById(cardId);
        return ResponseDto.of(HttpStatus.NO_CONTENT, "카드가 성공적으로 삭제되었습니다.");
    }
}
