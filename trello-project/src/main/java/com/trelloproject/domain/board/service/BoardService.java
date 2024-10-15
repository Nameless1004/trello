package com.trelloproject.domain.board.service;

import com.trelloproject.common.dto.ResponseDto;
import com.trelloproject.common.enums.MemberRole;
import com.trelloproject.common.exceptions.*;
import com.trelloproject.domain.board.dto.BoardRequest;
import com.trelloproject.domain.board.dto.BoardResponse;
import com.trelloproject.domain.board.entity.Board;
import com.trelloproject.domain.board.repository.BoardRepository;
import com.trelloproject.domain.card.entity.Card;
import com.trelloproject.domain.list.entity.CardList;
import com.trelloproject.domain.list.repository.CardListRepository;
import com.trelloproject.domain.member.entity.Member;
import com.trelloproject.domain.member.repository.MemberRepository;
import com.trelloproject.domain.user.repository.UserRepository;
import com.trelloproject.domain.workspace.entity.Workspace;
import com.trelloproject.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final CardListRepository cardListRepository;
    private final MemberRepository memberRepository;

    /**
     * 보드 생성
     */
    public ResponseDto<BoardResponse.CreatedBoard> createdBoard(AuthUser authUser, long workspaceId, BoardRequest.CreatedBoard request) {

        // 해당 워크스페이스가 존재하지 않을 때
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(WorkspaceNotFounException::new);

        // 로그인하지 않은 멤버가 생성하려는 경우
        if(authUser == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "로그인을 해주세요.");
        }

        // 읽기 전용 역할을 가진 멤버가 보드를 생성하려는 경우
        Member member = memberRepository.findByUserId(authUser.getUserId())
                .orElseThrow(MemberNotFoundException::new);

        if (member.getRole() == MemberRole.READ_ONLY) {
            throw new AccessDeniedException("읽기 전용 멤버는 생성할 수 없습니다.");
        }

        String title = request.title();
        String bgColor = request.bgColor();

        // 제목이 비어있는 경우
        if(title.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "제목을 입력해 주세요.");
        }

        Board board = new Board(title, bgColor);
        boardRepository.save(board);

        return ResponseDto.of(HttpStatus.CREATED, "보드 생성에 성공했습니다.", new BoardResponse.CreatedBoard(board.getId(), board.getTitle(), board.getBgColor()));
    }

    /**
     * 보드 수정
     */
    public ResponseDto<BoardResponse.CreatedBoard> updateBoard(AuthUser authUser, long workspaceId, long boardId, BoardRequest.UpdateBoard request) {

        // 해당 워크스페이스가 존재하지 않을 때
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(WorkspaceNotFounException::new);

        // 해당 보드가 존재하지 않을 때
        Board board = boardRepository.findById(boardId)
                .orElseThrow(BoardNotFoundException::new);

        // 로그인하지 않은 멤버가 수정하려는 경우
        if(authUser == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "로그인을 해주세요.");
        }

        // 읽기 전용 역할을 가진 멤버가 보드를 수정하려는 경우
        Member member = memberRepository.findByUserId(authUser.getUserId())
                .orElseThrow(MemberNotFoundException::new);

        if (member.getRole() == MemberRole.READ_ONLY) {
            throw new AccessDeniedException("읽기 전용 멤버는 수정할 수 없습니다.");
        }

        String title = request.title();
        String bgColor = request.bgColor();

        // 제목이 비어있는 경우
        if(title.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "제목을 입력해 주세요.");
        }

        board.update(title, bgColor);
        boardRepository.save(board);

        return ResponseDto.of(HttpStatus.OK, "보드 수정이 완료되었습니다.", new BoardResponse.CreatedBoard(board.getId(), board.getTitle(), bgColor));
    }

    /**
     * 보드 다건 조회
     */
    public ResponseDto<List<BoardResponse.GetBoard>> getBoards(long workspaceId) {

        // 해당 워크스페이스가 존재하지 않을 때
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(WorkspaceNotFounException::new);

        List<Board> boards = boardRepository.findByWorkspaceId(workspaceId);

        List<BoardResponse.GetBoard> response = boards.stream()
                .map(board -> new BoardResponse.GetBoard(board.getId(), board.getTitle(), board.getBgColor()))
                .toList();

        return ResponseDto.of(HttpStatus.OK, response);
    }

    /**
     * 보드 단건 조회
     */
    @Transactional(readOnly = true)
    public ResponseDto<BoardResponse.DetailBoard> getBoard(long workspaceId, long boardId) {

        // 해당 워크스페이스가 존재하지 않을 때
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(WorkspaceNotFounException::new);

        // 보드 조회 및 & 보드가 없을 때
        Board board = boardRepository.findById(boardId)
                .orElseThrow(BoardNotFoundException::new);

        List<CardList> cardList = cardListRepository.findByBoardId(boardId);
        List<Card> card = cardRepository.findByBoardId(boardId);

        return ResponseDto.of(HttpStatus.OK, "보드 단건 조회에 성공했습니다.", new BoardResponse.DetailBoard(board.getId(), board.getTitle(), board.getBgColor(), cardList, card));
    }

    /**
     * 보드 삭제
     */
    @Transactional
    public ResponseDto<Void> deleteBoard(AuthUser authUser, long workspaceId, long boardId) {

        // 해당 워크스페이스가 존재하지 않을 때
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(WorkspaceNotFounException::new);

        // 보드 존재 여부 확인
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "보드를 찾을 수 없습니다."));

        // 읽기 전용 역할을 가진 멤버가 보드를 삭제하려는 경우
        Member member = memberRepository.findByUserId(authUser.getUserId())
                .orElseThrow(MemberNotFoundException::new);

        if (member.getRole() == MemberRole.READ_ONLY) {
            throw new AccessDeniedException("읽기 전용 멤버는 삭제할 수 없습니다.");
        }

        // 보드 삭제
        boardRepository.delete(board);

        // 성공적으로 삭제된 경우 ResponseDto 반환
        return ResponseDto.of(HttpStatus.NO_CONTENT, "보드를 삭제 했습니다."); // 또는 다른 적절한 메시지
    }
}
