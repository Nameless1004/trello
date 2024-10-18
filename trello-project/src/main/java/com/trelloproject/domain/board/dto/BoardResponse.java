package com.trelloproject.domain.board.dto;

import com.trelloproject.domain.card.entity.Card;
import com.trelloproject.domain.list.entity.CardList;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public sealed interface BoardResponse permits BoardResponse.CreatedBoard, BoardResponse.GetBoard, BoardResponse.DetailBoard {

    record CreatedBoard(Long boardId, String title, String bgColor, String imageUrl) implements BoardResponse { }

    record GetBoard(Long boardId, String title, String bgColor) implements BoardResponse { }

    record DetailBoard(Long boardId, String title, String bgColor, List<CardList> cardList, List<Card> card) implements BoardResponse {}
}
