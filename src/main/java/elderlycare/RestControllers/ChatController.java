package elderlycare.RestControllers;
import elderlycare.DAO.Entities.ChatMessage;
import elderlycare.DAO.Entities.OurUsers;
import elderlycare.Services.ChatMessageService;
import elderlycare.Services.UserService;
import elderlycare.dto.ChatMessageRequest;
import elderlycare.dto.VoiceMessageRequest;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@PermitAll
public class ChatController {
    private final ChatMessageService chatService;
    private final UserService userService;

    @Autowired
    public ChatController(ChatMessageService chatService, UserService userService) {
        this.chatService = chatService;
        this.userService = userService;
    }

    @PostMapping("/send-text-message")
    public ResponseEntity<Object> sendMessage(@RequestBody ChatMessageRequest request) {
        chatService.sendMessage(request.getSenderId(), request.getRecipientId(), request.getTextContent());
        return ResponseEntity.ok().body(Map.of("message", "Message sent successfully"));
    }


    @PostMapping("/send-voice-message")
    public ResponseEntity<Object> sendVoiceMessage(@RequestBody VoiceMessageRequest request) {

            byte[] audioData = request.getAudioContent();
            chatService.saveVoiceMessage(request.getSenderId(), request.getRecipientId(), audioData);
            return ResponseEntity.ok().body(Map.of("message", "Voice message saved successfully"));
        byte[] audioData = request.getAudioContent(); // Get audio data from request
        chatService.saveVoiceMessage(request.getSenderId(), request.getRecipientId(), audioData);
        return ResponseEntity.ok().body(Map.of("message", "Voice message saved successfully"));
    }

    // ChatController.java
    @PutMapping("/update-message/{messageId}")
    public ResponseEntity<Object> updateMessage(@PathVariable Long messageId, @RequestBody String updatedText) {
        chatService.updateMessage(messageId, updatedText);
        return ResponseEntity.ok().body(Map.of("message", "Message updated successfully"));
    }


    // ChatController.java
    @DeleteMapping("/delete-message/{messageId}")
    public ResponseEntity<Object> deleteMessage(@PathVariable Long messageId) {
        chatService.deleteMessage(messageId);
        return ResponseEntity.ok().body(Map.of("message", "Message deleted successfully"));
    }




    @GetMapping("/messages")
    public ResponseEntity<List<ChatMessage>> getMessages(@RequestParam String userId1, @RequestParam String userId2) {
        List<ChatMessage> messages = chatService.getMessages(userId1, userId2);
        return ResponseEntity.ok(messages);
    }


    @GetMapping("/unseen-messages")
    public ResponseEntity<List<ChatMessage>> getUnseenMessages(
            @RequestParam("currentUserEmail") String currentUserEmail,
            @RequestParam("senderEmail") String senderEmail) {

        List<ChatMessage> unseenMessages = chatService.getUnseenMessages(currentUserEmail, senderEmail);
        return ResponseEntity.ok(unseenMessages);
    }


    @GetMapping("/all")
    public ResponseEntity<List<OurUsers>> getAllUsers() {
        List<OurUsers> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }


    //mariem
    @GetMapping("/online-status")
    public ResponseEntity<?> getOnlineStatus() {
        try {
            Map<String, Boolean> onlineStatus = userService.getOnlineStatus();
            return ResponseEntity.ok(onlineStatus);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching online status");
        }
    }

    //mariem
    @PutMapping("/update-online-status/{email}")
    public void updateUserOnlineStatus(@PathVariable String email, @RequestBody boolean online) {
        userService.updateUserOnlineStatus(email, online);
    }


}
