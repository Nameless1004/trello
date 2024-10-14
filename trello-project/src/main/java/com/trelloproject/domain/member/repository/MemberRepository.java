package com.trelloproject.domain.member.repository;

import com.trelloproject.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByWorkspace_IdAndUser_Id(Long workspace_id, Long user_id);
}
