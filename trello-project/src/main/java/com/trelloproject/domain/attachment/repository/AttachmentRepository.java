package com.trelloproject.domain.attachment.repository;

import com.trelloproject.domain.attachment.entity.Attachment;
import com.trelloproject.domain.card.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    List<Attachment> findAllByCard(Card card);
}
