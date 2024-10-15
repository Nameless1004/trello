package com.templateproject.domain.card;

import com.trelloproject.common.dto.ResponseDto;
import com.trelloproject.domain.board.entity.Board;
import com.trelloproject.domain.board.repository.BoardRepository;
import com.trelloproject.domain.card.dto.CardRequest;
import com.trelloproject.domain.card.dto.CardResponse;
import com.trelloproject.domain.card.entity.Card;
import com.trelloproject.domain.card.repository.CardRepository;
import com.trelloproject.domain.card.service.CardService;
import com.trelloproject.domain.list.entity.CardList;
import com.trelloproject.domain.list.repository.CardListRepository;
import com.trelloproject.domain.manager.dto.ManagerRequest;
import com.trelloproject.domain.manager.entity.Manager;
import com.trelloproject.domain.manager.repository.ManagerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private ManagerRepository managerRepository;

    @Mock
    private CardListRepository cardListRepository;

    @Mock
    private BoardRepository boardRepository;

    @InjectMocks
    private CardService cardService;

    // Common test data
    private Long boardId;
    private Long listId;
    private Long cardId;
    private Manager manager;
    private CardList cardList;
    private CardRequest cardRequest;

    @BeforeEach
    public void setUp() {
        boardId = 1L;
        listId = 1L;
        cardId = 1L;

        // Initialize common objects
        manager = new Manager(1L, "John", "EDITOR");
        cardList = new CardList(listId, "Test List", 1);
        cardRequest = new CardRequest(
                "Card Title",
                "Description",
                LocalDate.now(),
                "OPEN",
                Arrays.asList(new ManagerRequest(1L, "John", "EDITOR", 1L))
        );
    }

    // 카드 생성 테스트
    @Test
    public void createCard_Success() {
        // Given
        Long boardId = 1L;
        Long listId = 1L;
        CardRequest cardRequest = new CardRequest("Card Title", "Description", LocalDate.now(), "OPEN", Arrays.asList(new ManagerRequest(1L, "John", "EDITOR", 1L)));

        // Mock 데이터 준비
        Board board = new Board(boardId, "TITLE", "Test Board");
        CardList cardList = new CardList(listId, "Test List", 1);
        Manager manager = new Manager(1L, "John", "EDITOR");

        // Mock repository responses
        Mockito.when(boardRepository.findById(boardId)).thenReturn(Optional.of(board));
        Mockito.when(cardListRepository.findByIdAndBoardId(listId, boardId)).thenReturn(Optional.of(cardList));
        Mockito.when(managerRepository.findById(1L)).thenReturn(Optional.of(manager));

        // When
        ResponseDto<CardResponse> response = cardService.createOrUpdateCard(boardId, listId, cardRequest);

        // Then
        assertNotNull(response);
        assertEquals(201, response.getStatusCode()); // HttpStatus.CREATED의 int 값 확인
        assertEquals("Card Title", response.getData().getTitle());
        Mockito.verify(cardRepository, times(1)).save(Mockito.any(Card.class)); // Verify save is called once
    }

    // 카드 조회 테스트
    @Test
    public void getCardDetails_Success() {
        // Given
        Card card = new Card(cardRequest, Arrays.asList(manager));

        // Mock repository responses
        Mockito.when(cardListRepository.findByIdAndBoardId(listId, boardId)).thenReturn(Optional.of(cardList));
        Mockito.when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));

        // When
        ResponseDto<CardResponse> response = cardService.getCardDetails(boardId, listId, cardId);

        // Then
        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        assertEquals("Card Title", response.getData().getTitle());
    }

    @Test
    public void updateCard_Success() {
        // Given
        Long boardId = 1L;
        Long listId = 1L;
        Long cardId = 1L;

        CardRequest updateRequest = new CardRequest(
                "Updated Card Title",
                "Updated Description",
                LocalDate.now().plusDays(5),
                "DONE",
                Arrays.asList(new ManagerRequest(1L, "John", "LEAD", 1L)) // 1L에 해당하는 관리자를 Mock
        );

        // Mock 데이터 준비
        Board board = new Board(boardId, "TITLE", "Test Board");
        CardList cardList = new CardList(listId, "Test List", 1);
        Manager manager = new Manager(1L, "John", "LEAD");
        Card card = new Card(cardRequest, Arrays.asList(manager));

        // Mock repository responses
        Mockito.when(cardListRepository.findByIdAndBoardId(listId, boardId)).thenReturn(Optional.of(cardList));
        Mockito.when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));
        Mockito.when(managerRepository.findById(1L)).thenReturn(Optional.of(manager)); // 유효한 관리자를 Mock

        // When
        ResponseDto<CardResponse> response = cardService.updateCard(boardId, listId, cardId, updateRequest);

        // Then
        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        assertEquals("Updated Card Title", response.getData().getTitle());
        Mockito.verify(cardRepository, times(1)).save(Mockito.any(Card.class)); // Verify save is called once
    }


    // 카드 삭제 테스트
    @Test
    public void deleteCard_Success() {
        // Given
        Card card = new Card(cardRequest, Arrays.asList(manager));

        // Mock repository responses
        Mockito.when(cardListRepository.findByIdAndBoardId(listId, boardId)).thenReturn(Optional.of(cardList));
        Mockito.when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));

        // When
        ResponseDto<Void> response = cardService.deleteCard(boardId, listId, cardId);

        // Then
        assertEquals(204, response.getStatusCode());
        Mockito.verify(cardRepository, times(1)).deleteById(cardId); // Verify delete is called once
    }
}
