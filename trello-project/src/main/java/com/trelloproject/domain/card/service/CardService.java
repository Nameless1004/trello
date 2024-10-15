package com.trelloproject.domain.card.service;

import com.trelloproject.common.dto.ResponseDto;
import com.trelloproject.common.enums.ManagerRole;
import com.trelloproject.common.exceptions.AccessDeniedException;
import com.trelloproject.domain.card.dto.CardRequest;
import com.trelloproject.domain.card.dto.CardResponse;
import com.trelloproject.domain.card.entity.Card;
import com.trelloproject.domain.card.repository.CardRepository;
import com.trelloproject.domain.list.entity.CardList;
import com.trelloproject.domain.list.repository.CardListRepository;
import com.trelloproject.domain.manager.entity.Manager;
import com.trelloproject.domain.manager.repository.ManagerRepository;
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

    @Transactional
    public ResponseDto<CardResponse> createOrUpdateCard(Long listId, CardRequest request) {
        // ManagerRequest 리스트에서 Manager 엔티티로 변환
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
    public ResponseDto<CardResponse> updateCard(Long listId, Long cardId, CardRequest request) {
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
    public ResponseDto<Void> deleteCard(Long listId, Long cardId) {
        cardListRepository.findById(listId)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 card list ID 입니다."));

        if (!cardRepository.existsById(cardId)) {
            throw new IllegalArgumentException("잘못된 cardId 입니다.");
        }

        cardRepository.deleteById(cardId);
        return ResponseDto.of(HttpStatus.NO_CONTENT, "카드가 성공적으로 삭제되었습니다.");
    }
}
