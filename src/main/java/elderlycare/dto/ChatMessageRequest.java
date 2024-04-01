package elderlycare.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
// ChatMessageRequest.java
public class ChatMessageRequest {
    private String senderId;
    private String recipientId;
    private String textContent;

    // Getters and setters
}
