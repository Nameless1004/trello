package com.trelloproject.domain.search.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.trelloproject.domain.board.entity.QBoard;
import com.trelloproject.domain.card.entity.QCard;
import com.trelloproject.domain.list.entity.QCardList;
import com.trelloproject.domain.manager.entity.QManager;
import com.trelloproject.domain.search.dto.CardSearchDto;
import com.trelloproject.domain.user.entitiy.QUser;
import com.trelloproject.domain.workspace.entity.QWorkspace;
import com.trelloproject.security.AuthUser;
import io.github.classgraph.TypeArgument;
import lombok.RequiredArgsConstructor;
import org.hibernate.type.descriptor.DateTimeUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.trelloproject.domain.board.entity.QBoard.board;
import static com.trelloproject.domain.card.entity.QCard.card;
import static com.trelloproject.domain.list.entity.QCardList.cardList;
import static com.trelloproject.domain.manager.entity.QManager.manager;

@RequiredArgsConstructor
public class CardSearchRepositoryImpl implements CardSearchRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<CardSearchDto.CardSearchResult> searchCards(CardSearchDto.CardSearch searchDto, Long boardId, Pageable pageable) {
        BooleanBuilder bb = createSearchCondition(searchDto, boardId);

        // 바디에 넣을거
        List<CardSearchDto.CardSearchResult> contents = queryFactory
                .select(
                    Projections.constructor(CardSearchDto.CardSearchResult.class,
                    card.title,
                    card.description,
                    card.deadline,
                    card.status,
                    card.viewCount
                ))
                .distinct() // 중복된 결과 제거
                .from(card)
                .join(card.cardListId, cardList)
                .join(cardList.board, board)
                .leftJoin(card.managers, manager)
                .where(bb)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 결과가 null일 경우 0으로 설정
        Long totalCount = getTotalCount(bb);
        totalCount = totalCount == null ? 0 : totalCount;

        // PageIml 객체를 반환하여 페이지네이션이 적용된 검색 결과와 총 개수를 포함한 페이지 객체 생성
        return new PageImpl<>(contents, pageable, totalCount);
    }

    private BooleanBuilder createSearchCondition(CardSearchDto.CardSearch searchDto, Long boardId) {
        // BooleanBuilder 초기화
        BooleanBuilder bb = new BooleanBuilder();

        // boardId가 null이 아닐 경우, boardId로 검색 조건 추가
        if(boardId != null) {
            bb.and(board.id.eq(boardId));
        }

        // title에 값이 있으면 카드 제목이 해당 값과 일치하는지 확인하는 조건
        if(StringUtils.hasText(searchDto.title())) {
            bb.and(card.title.containsIgnoreCase(searchDto.title()));
        }

        // description에 값이 있으면 카드 설명이 해당 값과 일치하는지 확인하는 조건
        if(StringUtils.hasText(searchDto.description())) {
            bb.and(card.description.containsIgnoreCase(searchDto.description()));
        }

        // deadline에 값이 있으면 카드 마감일이 해당 값과 일치하는지 확인하는 조건
        if(searchDto.deadline() != null) {
            bb.and(card.deadline.eq(searchDto.deadline()));
        }

        // manager에 값이 있으면 카드 마감일이 해당 값과 일치하는지 확인하는 조건
        if(StringUtils.hasText(searchDto.manager())) {
            bb.and(manager.name.containsIgnoreCase(searchDto.manager()));
        }

        return bb;
    }

    private Long getTotalCount(BooleanBuilder bb) {
        // 전체 검색 결과의 개수를 가져옴
        return queryFactory
                .select(Wildcard.count)
                .distinct()
                .from(card)
                .join(card.cardListId, cardList)
                .join(cardList.board, board)
                .leftJoin(card.managers, manager)
                .where(bb)
                .fetchOne();
    }
}
