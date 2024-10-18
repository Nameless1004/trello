package com.trelloproject.domain.workspace.repository;

import com.trelloproject.domain.workspace.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {
    @Query("SELECT DISTINCT m.workspace FROM Member m WHERE m.user.id = :userId")
    List<Workspace> findAllUserWorkspaces(@Param("userId") Long userId);

    @Query("SELECT w FROM Workspace w LEFT JOIN FETCH w.members m LEFT JOIN FETCH m.user u WHERE w.id = :workspaceId")
    Optional<Workspace> findByIdWithMember(@Param("workspaceId") Long workspaceId);
}
