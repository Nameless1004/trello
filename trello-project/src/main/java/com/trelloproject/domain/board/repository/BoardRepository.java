package com.trelloproject.domain.board.repository;

import com.trelloproject.domain.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findByWorkspaceId(long workspaceId);
}
