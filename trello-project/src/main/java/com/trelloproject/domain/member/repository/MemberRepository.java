package com.trelloproject.domain.member.repository;

import com.trelloproject.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("SELECT m FROM Member m LEFT JOIN FETCH m.user WHERE m.user.id=:userId AND m.deleted=false")
    Optional<Member> findByUserId(@Param("userId") long userId);

    Optional<Member> findByWorkspace_IdAndUser_Id(Long workspace_id, Long user_id);

    boolean existsByWorkspace_IdAndUser_Id(Long workspace_id, Long user_id);

    boolean existsByWorkspace_IdAndId(Long workspace_id, Long id);

    Optional<Member> findByWorkspace_IdAndId(Long workspace_id, Long id);
}
