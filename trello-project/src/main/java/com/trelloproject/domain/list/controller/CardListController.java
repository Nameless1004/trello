package com.trelloproject.domain.list.controller;

import com.trelloproject.domain.list.service.CardListService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CardListController {

    private final CardListService cardListService;
}
