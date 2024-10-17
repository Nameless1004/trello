package com.trelloproject.domain.list.controller;

import com.trelloproject.common.dto.ResponseDto;
import com.trelloproject.domain.list.dto.request.ListRequest;
import com.trelloproject.domain.list.dto.response.ListResponse;
import com.trelloproject.domain.list.service.CardListService;
import com.trelloproject.security.AuthUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/workspaces/{workspaceId}/boards/{boardId}/lists")
public class CardListController {

    private final CardListService cardListService;

    @GetMapping
    public ResponseEntity<ResponseDto<List<ListResponse.Info>>> getLists(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable("workspaceId") long workspaceId,
            @PathVariable("boardId") long boardId) {

        return ResponseDto.toEntity(cardListService.getLists(workspaceId, authUser, boardId));
    }

    @PostMapping
    public ResponseEntity<ResponseDto<Long>> createList(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable("workspaceId") long workspaceId,
            @PathVariable("boardId") long boardId,
            @Valid @RequestBody ListRequest.Create request) {

        return ResponseDto.toEntity(cardListService.createList(authUser, workspaceId, boardId, request));
    }

    @PatchMapping("/{listId}")
    public ResponseEntity<ResponseDto<ListResponse.Info>> updateList(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable("workspaceId") long workspaceId,
            @PathVariable("boardId") long boardId,
            @PathVariable("listId") long listId,
            @Valid @RequestBody ListRequest.Update request) {

        return ResponseDto.toEntity(cardListService.updateList(authUser, workspaceId, boardId, listId, request));
    }

    @PatchMapping("/move")
    public ResponseEntity<ResponseDto<Void>> moveList(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable("workspaceId") long workspaceId,
            @PathVariable("boardId") long boardId,
            @Valid @RequestBody ListRequest.Move request) {

        return ResponseDto.toEntity(cardListService.moveList(authUser, workspaceId, boardId, request));
    }

    @DeleteMapping("/{listId}")
    public ResponseEntity<ResponseDto<Void>> deleteList(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable("workspaceId") long workspaceId,
            @PathVariable("boardId") long boardId,
            @PathVariable("listId") long listId) {

        return ResponseDto.toEntity(cardListService.deleteList(authUser, workspaceId, boardId, listId));
    }
}
