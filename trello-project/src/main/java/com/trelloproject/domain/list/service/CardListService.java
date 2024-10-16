package com.trelloproject.domain.list.service;

import com.trelloproject.common.dto.ResponseDto;
import com.trelloproject.common.enums.MemberRole;
import com.trelloproject.common.exceptions.AccessDeniedException;
import com.trelloproject.common.exceptions.BoardNotFoundException;
import com.trelloproject.common.exceptions.ListNotFoundException;
import com.trelloproject.common.exceptions.MemberNotFoundException;
import com.trelloproject.domain.board.entity.Board;
import com.trelloproject.domain.board.repository.BoardRepository;
import com.trelloproject.domain.list.dto.request.ListRequest.Create;
import com.trelloproject.domain.list.dto.request.ListRequest.Move;
import com.trelloproject.domain.list.dto.request.ListRequest.Update;
import com.trelloproject.domain.list.dto.response.ListResponse;
import com.trelloproject.domain.list.dto.response.ListResponse.Info;
import com.trelloproject.domain.list.entity.CardList;
import com.trelloproject.domain.list.repository.CardListRepository;
import com.trelloproject.domain.member.entity.Member;
import com.trelloproject.domain.member.repository.MemberRepository;
import com.trelloproject.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CardListService {

    private final CardListRepository cardListRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    /**
     * 리스트 다건 조회
     *
     * @param workspaceId
     * @param boardId
     * @return
     */
    public ResponseDto<List<ListResponse.Info>> getLists(long workspaceId, AuthUser authUser, long boardId) {

        if (!memberRepository.existsByWorkspace_IdAndUser_Id(workspaceId, authUser.getUserId())) {
            throw new AccessDeniedException("접근 권한이 없습니다.");
        }

        Board board = boardRepository.findById(boardId)
                .orElseThrow(BoardNotFoundException::new);

        List<Info> result = cardListRepository.findAllByBoardOrderByOrder(board)
                .stream()
                .map(Info::new)
                .toList();

        return ResponseDto.of(HttpStatus.OK, result);
    }

    /**
     * 리스트 생성
     */
    public ResponseDto<Long> createList(AuthUser authUser, long workspaceId, long boardId, Create request) {

        Member member = memberRepository.findByWorkspace_IdAndUser_Id(workspaceId, authUser.getUserId())
                .orElseThrow(MemberNotFoundException::new);

        Board board = boardRepository.findById(boardId)
                .orElseThrow(BoardNotFoundException::new);

        if (member.getRole() == MemberRole.ROLE_READ_ONLY) {
            throw new AccessDeniedException("읽기 전용 멤버는 수정할 수 없습니다.");
        }

        Integer max = cardListRepository.maxOrderIndex(board);
        max = max == null ? -1 : max;

        CardList res = CardList.builder()
                .board(board)
                .title(request.title())
                .order(max + 1)
                .build();

        res = cardListRepository.save(res);
        return ResponseDto.of(HttpStatus.CREATED, res.getId());
    }

    /**
     * 리스트 수정
     */
    public ResponseDto<ListResponse.Info> updateList(AuthUser authUser, long workspaceId, long boardId, long listId, Update request) {

        Member member = memberRepository.findByWorkspace_IdAndUser_Id(workspaceId, authUser.getUserId())
                .orElseThrow(() -> new AccessDeniedException("접근 권한이 없습니다."));

        if (member.getRole() == MemberRole.ROLE_READ_ONLY) {
            throw new AccessDeniedException("읽기 전용 멤버는 수정할 수 없습니다.");
        }

        Board board = boardRepository.findById(boardId)
                .orElseThrow(BoardNotFoundException::new);

        CardList cardList = cardListRepository.findById(listId)
                .orElseThrow(ListNotFoundException::new);

        cardList.updateTitle(request.title());
        return ResponseDto.of(HttpStatus.OK, new ListResponse.Info(cardList));
    }

    /**
     * 리스트 위치 변경
     */
    public ResponseDto<Void> moveList(AuthUser authUser, long workspaceId, long boardId, Move request) {

        Member member = memberRepository.findByWorkspace_IdAndUser_Id(workspaceId, authUser.getUserId())
                .orElseThrow(() -> new AccessDeniedException("접근 권한이 없습니다."));

        if (member.getRole() == MemberRole.ROLE_READ_ONLY) {
            throw new AccessDeniedException("읽기 전용 멤버는 수정할 수 없습니다.");
        }

        Board board = boardRepository.findById(boardId)
                .orElseThrow(BoardNotFoundException::new);

        // 보드에 있는 list 가져오기
        List<CardList> find = cardListRepository.findAllByBoardOrderByOrderWithWriteLock(board);

        int startIndex = clamp(request.startIndex(), 0, find.size() - 1);
        int destIndex = clamp(request.destIndex(), 0, find.size() - 1);

        CardList target = find.get(startIndex);

        if (startIndex == destIndex) {
            return ResponseDto.of(HttpStatus.OK, "같은 위치 입니다.");
        }

        int max = Math.max(startIndex, destIndex);
        int min = Math.min(startIndex, destIndex);

        // 자기 자신 제거
        find.remove(target);

        List<Long> slice = find.stream()
                .filter(x -> min <= x.getOrderIndex() && x.getOrderIndex() <= max)
                .map(CardList::getId)
                .toList();

        if (startIndex < destIndex) {
            cardListRepository.updateOrderInIds(slice, -1);
        } else {
            cardListRepository.updateOrderInIds(slice, 1);
        }

        target.updateOrder(destIndex);
        cardListRepository.save(target);

        return ResponseDto.of(HttpStatus.OK, "성공적으로 변경 되었습니다.");
    }

    /**
     * 리스트 삭제
     */
    public ResponseDto<Void> deleteList(AuthUser authUser, long workspaceId, long boardId, long listId) {

        Member member = memberRepository.findByWorkspace_IdAndUser_Id(workspaceId, authUser.getUserId())
                .orElseThrow(() -> new AccessDeniedException("접근 권한이 없습니다."));

        if (member.getRole() == MemberRole.ROLE_READ_ONLY) {
            throw new AccessDeniedException("읽기 전용 멤버는 수정할 수 없습니다.");
        }

        Board board = boardRepository.findById(boardId)
                .orElseThrow(BoardNotFoundException::new);

        List<CardList> find = cardListRepository.findAllByBoardOrderByOrderWithWriteLock(board);

        CardList cardList = find.stream()
                .filter(x -> x.getId() == listId)
                .findFirst()
                .orElseThrow(ListNotFoundException::new);

        int start = cardList.getOrderIndex();

        List<Long> slice = find.stream()
                .filter(x -> start + 1 <= x.getOrderIndex())
                .map(CardList::getId)
                .toList();

        if (!slice.isEmpty()) {
            cardListRepository.updateOrderInIds(slice, -1);
        }

        cardListRepository.delete(cardList);
        return ResponseDto.of(HttpStatus.OK, "성공적으로 삭제됐습니다.");
    }

    private int clamp(int value, int min, int max) {
        if (value <= min) return min;
        else if (value >= max) return max;
        return value;
    }
}

