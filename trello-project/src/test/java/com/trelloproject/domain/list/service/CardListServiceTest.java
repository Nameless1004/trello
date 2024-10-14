package com.trelloproject.domain.list.service;

import com.trelloproject.common.enums.UserRole;
import com.trelloproject.domain.board.repository.BoardRepository;
import com.trelloproject.domain.list.dto.request.ListRequest.Move;
import com.trelloproject.domain.list.dto.response.ListResponse.Info;
import com.trelloproject.domain.list.entity.CardList;
import com.trelloproject.domain.list.repository.CardListRepository;
import com.trelloproject.domain.member.repository.MemberRepository;
import com.trelloproject.domain.user.repository.UserRepository;
import com.trelloproject.security.AuthUser;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class CardListServiceTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    CardListRepository cardListRepository;
    @Autowired
    BoardRepository boardRepository;

    @Autowired
    CardListService cardListService;
    @Autowired
    UserRepository userRepository;

    @Test
    @Rollback(false)
    public void t() throws Exception {
//        Board b = new Board("title", "color");
//        b = boardRepository.saveAndFlush(b);
//
//        User user = new User("n","p","e", "mo", UserRole.ROLE_USER);
//        user = userRepository.saveAndFlush(user);
//
//        Member member = new Member(null, user, MemberRole.BOARD);
//        member = memberRepository.saveAndFlush(member);
//
//        List<CardList> l = new ArrayList<CardList>();
//        for(int i = 0; i <= 9; i++) {
//            CardList cardList = new CardList("title_" + i+1, i);
//            ReflectionTestUtils.setField(cardList, "board", b);
//            l.add(cardList);
//        }
//
//        cardListRepository.saveAllAndFlush(l);
//
//        int i = cardListRepository.maxOrderIndex();
//        assertThat(i).isEqualTo(9);

        int start = 9;
        int dest = 1;

        cardListService.moveList(new AuthUser(1L, null, UserRole.ROLE_USER), 1L, new Move(start, dest)).getData();

//        List<Long> orderIndicies = allByBoardOrderByOrder.stream()
//            .map(x -> x.getOrderIndex())
//            .toList();
//
//        List<CardList>r = cardListRepository.findAllByBoardOrderByOrder(b);
//        r.stream().map(Info::new).toList().stream().forEach(x-> System.out.println(x.toString()));
//        assertThat(orderIndicies).containsExactly(0L, 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
    }


    @Test
    public void tddddd() throws Exception {
        List<CardList> allByBoardOrderByOrder = cardListRepository.findAllByBoardOrderByOrder(
            boardRepository.findById(1L)
                .get());
        allByBoardOrderByOrder.stream().map(Info::new).toList().stream().forEach(x-> System.out.println(x.toString()));
        // given
        // then
    }
}