package elderlycare.DAO.Repositories;

import elderlycare.DAO.Entities.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findBySenderIdAndRecipientIdOrSenderIdAndRecipientIdOrderByTimestampAsc(
            String senderId1, String recipientId1, String senderId2, String recipientId2);


// seen for sender
    List<ChatMessage> findByRecipientIdAndSenderIdAndSeen(String currentUserEmail, String senderEmail, boolean b);
}
