package com.trelloproject.domain.board.controller;

import com.trelloproject.common.dto.ResponseDto;
import com.trelloproject.domain.board.dto.BoardRequest;
import com.trelloproject.domain.board.dto.BoardResponse;
import com.trelloproject.domain.board.service.BoardService;
import com.trelloproject.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    /**
     * 보드 생성
     *
     * @param authUser
     * @param workspaceId
     * @param request
     * @return
     */
    @PostMapping("")
    public ResponseEntity<ResponseDto<BoardResponse.CreatedBoard>> createdBoard(@AuthenticationPrincipal AuthUser authUser,
                                                                                @RequestBody long workspaceId,
                                                                                @RequestPart("file") MultipartFile file,
                                                                                @RequestBody BoardRequest.CreatedBoard request) {
        return ResponseDto.toEntity(boardService.createdBoard(authUser, workspaceId, file, request));
    }

    /**
     * 보드 수정
     *
     * @param authUser
     * @param workspaceId
     * @param boardId
     * @param request
     * @return
     */
    @PutMapping("")
    public ResponseEntity<ResponseDto<BoardResponse.CreatedBoard>> updateBoard(@AuthenticationPrincipal AuthUser authUser,
                                                                                @RequestBody long workspaceId,
                                                                                @RequestBody long boardId,
                                                                                @RequestBody BoardRequest.UpdateBoard request) {
        return ResponseDto.toEntity(boardService.updateBoard(authUser, workspaceId, boardId, request));
    }

    /**
     * 보드 다건 조회
     * @param workspaceId
     * @return
     */
    @GetMapping("/workspace/{workspaceId}/board")
    public ResponseEntity<ResponseDto<List<BoardResponse.GetBoard>>> getBoards(@PathVariable long workspaceId) {
        return ResponseDto.toEntity(boardService.getBoards(workspaceId));
    }

    /**
     * 보드 단건 조회
     *
     * @param workspaceId
     * @param boardId
     * @return
     */
    @GetMapping("/workspaces/{workspaceId}/boards/{boardId}")
    public ResponseEntity<ResponseDto<BoardResponse.DetailBoard>> getBoard(@PathVariable long workspaceId,
                                                                         @PathVariable long boardId) {
        return ResponseDto.toEntity(boardService.getBoard(workspaceId, boardId));
    }

    /**
     * 보드 삭제
     *
     * @param authUser
     * @param workspaceId
     * @param boardId
     * @return
     */
    @DeleteMapping("/workspaces/{workspaceId}/boards/{boardId}")
    public ResponseEntity<ResponseDto<Void>> deleteBoard(@AuthenticationPrincipal AuthUser authUser,
                                                         @PathVariable long workspaceId,
                                                         @PathVariable long boardId) {

        ResponseDto<Void> response = boardService.deleteBoard(authUser, workspaceId, boardId);
        return ResponseDto.toEntity(response);
    }


}
