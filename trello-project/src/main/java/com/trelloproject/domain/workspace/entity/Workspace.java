package com.trelloproject.domain.workspace.entity;

import com.trelloproject.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE workspace SET deleted = true WHERE id = ?")
@SQLRestriction("deleted = false")
public class Workspace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false)
    private String name;

    @Setter
    @Column(nullable = false)
    private String description;

    @OneToMany(mappedBy = "workspace", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Member> members;

    @Column(nullable = false)
    private Boolean deleted;

    @Builder
    public Workspace(String name, String description, List<Member> members, Boolean deleted) {
        this.name = name;
        this.description = description;
        this.members = members;
        this.deleted = deleted;
    }
}
