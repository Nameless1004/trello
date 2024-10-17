package com.trelloproject.domain.comment.repository;

import com.trelloproject.domain.comment.entity.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c JOIN FETCH c.card WHERE c.card.id=:cardId")
    List<Comment> findAllByCardId(@Param("cardId") Long cardId);
}
