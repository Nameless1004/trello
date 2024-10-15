package com.trelloproject.domain.manager.repository;

import com.trelloproject.domain.manager.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerRepository extends JpaRepository<Manager, Long> {
}