package com.trelloproject.domain.workspace.repository;

import com.trelloproject.domain.workspace.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {
    @Query("SELECT w FROM Workspace w LEFT JOIN FETCH Member m ON w.id = m.workspace.id WHERE m.user.id = :userId")
    List<Workspace> findAllUserWorkspaces(@Param("userId") Long userId);
}
