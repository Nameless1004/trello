package com.trelloproject.domain.list.dto.request;

import com.trelloproject.domain.list.dto.request.ListRequest.Create;
import com.trelloproject.domain.list.dto.request.ListRequest.Move;
import com.trelloproject.domain.list.dto.request.ListRequest.Update;
import jakarta.validation.constraints.NotBlank;

public sealed interface ListRequest permits Create, Update, Move {
    record Create(@NotBlank String title) implements ListRequest { }
    record Update(@NotBlank String title) implements ListRequest { }
    record Move(int startIndex, int destIndex) implements ListRequest {}
}
