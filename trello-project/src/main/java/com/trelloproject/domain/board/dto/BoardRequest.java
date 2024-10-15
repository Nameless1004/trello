package com.trelloproject.domain.board.dto;

import jakarta.validation.constraints.NotBlank;

public sealed interface BoardRequest permits BoardRequest.CreatedBoard, BoardRequest.UpdateBoard {

    record CreatedBoard (@NotBlank String title, String bgColor) implements BoardRequest {}

    record UpdateBoard (@NotBlank String title, String bgColor) implements BoardRequest {}

}
