package com.trelloproject.domain.list.dto.response;

import com.trelloproject.domain.list.dto.response.ListResponse.Info;
import com.trelloproject.domain.list.entity.CardList;

public sealed  interface ListResponse permits Info {
    record Info(Long id, String title, int order) implements ListResponse{
        public Info(CardList cardList) {
            this(cardList.getId(), cardList.getTitle(), cardList.getOrderIndex());
        }
    }
}
