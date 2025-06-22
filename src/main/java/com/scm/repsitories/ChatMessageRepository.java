package com.scm.repsitories;

import com.scm.model.ChatMessage;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    @Query("SELECT m FROM ChatMessage m WHERE " +
           "(m.fromUser = :fromUser AND m.toUser = :toUser) OR " +
           "(m.fromUser = :toUser AND m.toUser = :fromUser) " +
           "ORDER BY m.timestamp ASC")
    List<ChatMessage> findConversation(@Param("fromUser") String fromUser,
                                       @Param("toUser") String toUser);
}
