package com.trelloproject.domain.list.repository;

import com.trelloproject.domain.list.entity.CardList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardListRepository extends JpaRepository<CardList, Long> {

}
