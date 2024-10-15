package com.trelloproject.domain.workspace.entity;

import com.trelloproject.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Workspace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    String description;

    @OneToMany(mappedBy = "workspace", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Member> members;

    @Builder
    public Workspace(String name, String description, List<Member> members) {
        this.name = name;
        this.description = description;
        this.members = members;
    }
}
