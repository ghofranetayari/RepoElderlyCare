package elderlycare.RestControllers;
import elderlycare.DAO.Entities.OurUsers;
import elderlycare.DAO.Repositories.OurUserRepo;
import elderlycare.Services.UserService;
import elderlycare.dto.ChatMessageRequest;
import elderlycare.dto.VoiceMessageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import elderlycare.DAO.Entities.ChatMessage;
import elderlycare.Services.ChatMessageService;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/chat")
@CrossOrigin("http://localhost:4200")
public class ChatController {
    private final ChatMessageService chatService;
    private final UserService userService;
    private final OurUserRepo userRepo;


    @Autowired
    public ChatController(ChatMessageService chatService, UserService userService,OurUserRepo userRepo) {
        this.chatService = chatService;
        this.userService = userService;
        this.userRepo = userRepo;

    }

    @PostMapping("/send-text-message")
    public ResponseEntity<Object> sendMessage(@RequestBody ChatMessageRequest request) {
        chatService.sendMessage(request.getSenderId(), request.getRecipientId(), request.getTextContent());
        return ResponseEntity.ok().body(Map.of("message", "Message sent successfully"));
    }


    @PostMapping("/send-voice-message")
    public ResponseEntity<Object> sendVoiceMessage(@RequestBody VoiceMessageRequest request) {

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



    @GetMapping("/users/search")
    public ResponseEntity<List<OurUsers>>
    searchUsersByEmail(@RequestParam String email) {
        List<OurUsers> users =
                userRepo.findByEmailContaining(email);
        return ResponseEntity.ok(users);}

}
