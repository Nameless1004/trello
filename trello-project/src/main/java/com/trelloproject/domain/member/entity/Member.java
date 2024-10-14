package com.trelloproject.domain.member.entity;

import com.trelloproject.domain.user.entitiy.User;
import com.trelloproject.domain.workspace.entity.Workspace;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @JoinColumn
    @ManyToOne
    Workspace workspace;

    @JoinColumn
    @ManyToOne
    User user;

    @Builder
    public Member(Long id, Workspace workspace, User user) {
        this.id = id;
        this.workspace = workspace;
        this.user = user;
    }
}
