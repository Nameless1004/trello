package com.trelloproject.domain.board.controller;

import com.trelloproject.common.dto.ResponseDto;
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
@RequestMapping("/workspaces/{workspaceId}/boards")
public class BoardController {

    private final BoardService boardService;

    /**
     * 보드 생성
     *
     * @param authUser
     * @param workspaceId
     * @return
     */
    @PostMapping("")
    public ResponseEntity<ResponseDto<BoardResponse.CreatedBoard>> createdBoard(@AuthenticationPrincipal AuthUser authUser,
                                                                                @PathVariable long workspaceId,
                                                                                @RequestPart("file") MultipartFile file,
                                                                                @RequestPart("title") String title,
                                                                                @RequestPart(value = "bgColor", required = false) String bgColor) {
        return ResponseDto.toEntity(boardService.createdBoard(authUser, workspaceId, file, title, bgColor));
    }

    /**
     * 보드 수정
     *
     * @param authUser
     * @param workspaceId
     * @param boardId
     * @return
     */
    @PutMapping("/{boardId}")
    public ResponseEntity<ResponseDto<BoardResponse.CreatedBoard>> updateBoard(@AuthenticationPrincipal AuthUser authUser,
                                                                               @PathVariable long workspaceId,
                                                                               @PathVariable long boardId,
                                                                               @RequestPart("file") MultipartFile file,
                                                                               @RequestPart("title") String title,
                                                                               @RequestPart(value = "bgColor", required = false) String bgColor) {
        return ResponseDto.toEntity(boardService.updateBoard(authUser, workspaceId, boardId, file, title, bgColor));
    }

    /**
     * 보드 다건 조회
     *
     * @param workspaceId
     * @return
     */
    @GetMapping("")
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
    @GetMapping("/{boardId}")
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
    @DeleteMapping("/{boardId}")
    public ResponseEntity<ResponseDto<Void>> deleteBoard(@AuthenticationPrincipal AuthUser authUser,
                                                         @PathVariable long workspaceId,
                                                         @PathVariable long boardId) {

        ResponseDto<Void> response = boardService.deleteBoard(authUser, workspaceId, boardId);
        return ResponseDto.toEntity(response);
    }


}
