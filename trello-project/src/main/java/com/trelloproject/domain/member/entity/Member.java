package com.trelloproject.domain.member.entity;

import com.trelloproject.common.enums.MemberRole;
import com.trelloproject.domain.user.entitiy.User;
import com.trelloproject.domain.workspace.entity.Workspace;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(nullable = false)
    @ManyToOne
    private Workspace workspace;

    @JoinColumn(nullable = false)
    @ManyToOne
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Setter
    private MemberRole role;

    @Builder
    public Member(Workspace workspace, User user, MemberRole role) {
        this.workspace = workspace;
        this.user = user;
        this.role = role;
    }
}
