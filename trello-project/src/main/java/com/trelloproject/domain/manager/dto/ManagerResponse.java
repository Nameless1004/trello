package com.trelloproject.domain.manager.dto;
import com.trelloproject.domain.manager.entity.Manager;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ManagerResponse {
    private Long id;
    private Long managerId;

    public ManagerResponse(Manager manager) {
        this.id = manager.getId();
        this.managerId = manager.getMember().getId();
    }
}

