package elderlycare.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoiceMessageRequest {
    private String senderId;
    private String recipientId;
    private byte[] audioContent;

    // Getters and setters
}
