package com.trelloproject.domain.member.repository;

import com.trelloproject.domain.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("SELECT m FROM Member m LEFT JOIN FETCH m.user WHERE m.user.id=:userId")
    Optional<Member> findByUserId(@Param("userId") long userId);
}
