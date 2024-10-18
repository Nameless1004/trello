package com.trelloproject.domain.member.entity;

import com.trelloproject.common.enums.MemberRole;
import com.trelloproject.domain.user.entity.User;
import com.trelloproject.domain.workspace.entity.Workspace;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE member SET deleted = true WHERE id = ?")
@SQLRestriction("deleted = false")
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

    @Column(nullable = false)
    private Boolean deleted;

    @Builder
    public Member(Workspace workspace, User user, MemberRole role, Boolean deleted) {
        this.workspace = workspace;
        this.user = user;
        this.role = role;
        this.deleted = deleted;
    }
}
