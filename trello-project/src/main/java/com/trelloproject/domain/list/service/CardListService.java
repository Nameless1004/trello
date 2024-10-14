package com.trelloproject.domain.list.service;

import com.trelloproject.domain.list.repository.CardListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardListService {

    private final CardListRepository cardListRepository;
}
