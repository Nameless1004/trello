package com.trelloproject.domain.workspace.dto;

import com.trelloproject.domain.member.dto.MemberResponse;
import com.trelloproject.domain.member.entity.Member;
import com.trelloproject.domain.workspace.dto.WorkspaceResponse.Workspace;
import com.trelloproject.domain.workspace.dto.WorkspaceResponse.WorkspaceWithMember;

import java.util.List;


public sealed interface WorkspaceResponse permits WorkspaceWithMember, Workspace {

    record WorkspaceWithMember(Long id, String name, String description,
                               List<MemberResponse.MemberWithUser> members) implements WorkspaceResponse {
        public WorkspaceWithMember(List<Member> _members, Long _id, String _name, String _description) {
            this(_id, _name, _description, _members.stream().map(MemberResponse.MemberWithUser::new).toList());
        }
    }

    record Workspace(Long id, String name, String description) implements WorkspaceResponse {
    }
}
