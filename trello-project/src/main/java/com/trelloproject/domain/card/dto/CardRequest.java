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
    private List<ManagerRequest> managers;  // 매니저 리스트를 ManagerRequest로 처리

    // test 후 삭제 하기
    public CardRequest(String title, String description, LocalDate deadline, String status, List<ManagerRequest> managers) {
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.status = status;
        this.managers = managers;
    }
}

