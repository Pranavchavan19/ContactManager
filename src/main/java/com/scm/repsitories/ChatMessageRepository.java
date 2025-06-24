
package com.scm.repsitories;

import com.scm.entities.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    
    // Fetch messages between two users
    List<ChatMessage> findByFromUserAndToUserOrToUserAndFromUserOrderByTimestampAsc(
        String fromUser, String toUser, String toUser2, String fromUser2
    );
    
    // Fetch messages sent to a user that haven't been read
    List<ChatMessage> findByToUserAndIsReadFalse(String toUser);

    @Query("SELECT m FROM ChatMessage m WHERE (m.fromUser = :user1 AND m.toUser = :user2) OR (m.fromUser = :user2 AND m.toUser = :user1) ORDER BY m.timestamp ASC")
    List<ChatMessage> getChatHistory(@Param("user1") String user1, @Param("user2") String user2);
}
