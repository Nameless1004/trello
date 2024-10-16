package com.trelloproject.domain.card.dto;

import com.trelloproject.domain.manager.dto.ManagerRequest;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class CardRequest {
    private String title;
    private String description;
    private LocalDate deadline;
    private String status;
    private List<ManagerRequest> managers;
}

