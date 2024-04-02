package elderlycare.Services;

import elderlycare.DAO.Entities.ChatMessage;
import elderlycare.DAO.Entities.Elderly;
import elderlycare.DAO.Entities.Nurse;
import elderlycare.DAO.Repositories.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;

    public void sendMessage(String senderId, String recipientId, String message) {
        ChatMessage chatMessage = ChatMessage.builder()
                .senderId(senderId)
                .recipientId(recipientId)
                .textContent(message)
                .timestamp(LocalDateTime.now())
                .build();
        chatMessageRepository.save(chatMessage);
    }

    public void saveVoiceMessage(String senderId, String recipientId, byte[] audioData) {
        ChatMessage message = ChatMessage.builder()
                .senderId(senderId)
                .recipientId(recipientId)
                .audioContent(audioData)
                .timestamp(LocalDateTime.now())
                .build();
        chatMessageRepository.save(message);
    }





        public void updateMessage(Long messageId, String updatedText) {
            Optional<ChatMessage> optionalMessage = chatMessageRepository.findById(messageId);
            if (optionalMessage.isPresent()) {
                ChatMessage message = optionalMessage.get();
                message.setTextContent(updatedText);
                chatMessageRepository.save(message);
            } else {throw new NoSuchElementException("Message not found");}
        }



    public void deleteMessage(Long messageId) {
        chatMessageRepository.deleteById(messageId);
    }


    public List<ChatMessage> getMessages(String userId1, String userId2) {
        List<ChatMessage> messages = chatMessageRepository
                .findBySenderIdAndRecipientIdOrSenderIdAndRecipientIdOrderByTimestampAsc(
                        userId1, userId2, userId2, userId1);

        for (ChatMessage message : messages) {
            if (message.getRecipientId().equals(userId1)) {
                message.setSeen(true);
                chatMessageRepository.save(message);
            }
        }
        return messages != null ? messages : new ArrayList<>();
    }


    public List<ChatMessage> getUnseenMessages(String currentUserEmail, String senderEmail) {
        return chatMessageRepository.findByRecipientIdAndSenderIdAndSeen(currentUserEmail, senderEmail, false);
    }




    //SCHEDULAR for nurse
    public void sendMorningMessageToElderly(Nurse nurse, Elderly elderly) {
        sendMessage(nurse.getEmail(), elderly.getEmail(), "Good morning! Have a wonderful day! don't forget your meds let me know if you need anything ");
    }

    public void sendEveningMessageToElderly(Nurse nurse, Elderly elderly) {
        sendMessage(nurse.getEmail(), elderly.getEmail(), "Good evening! Hope you had a great day!don't forget your meds,let me know if you need anything");
    }

}
