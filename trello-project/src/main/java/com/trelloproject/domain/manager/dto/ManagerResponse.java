package com.trelloproject.domain.manager.dto;
import com.trelloproject.domain.manager.entity.Manager;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ManagerResponse {
    private Long id;
    private String name;
    private String role;

    public ManagerResponse(Manager manager) {
        this.id = manager.getId();
        this.name = manager.getName();
        this.role = manager.getRole();
    }
}

