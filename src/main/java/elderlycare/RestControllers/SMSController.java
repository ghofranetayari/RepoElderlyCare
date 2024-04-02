package elderlycare.RestControllers;

import elderlycare.Services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/SMS")
public class SMSController {

    @Value("${TWILIO_ACCOUNT_SID}")
    private String twilioAccountSid;

    @Value("${TWILIO_AUTH_TOKEN}")
    private String twilioAuthToken;

    @Value("${twilio.phone.number}")
    private String twilioPhoneNumber;
    @Autowired
    AuthService authService;


   /* @PostMapping(value = "/sendSMS")
    public ResponseEntity<String> sendSMS(@RequestBody EmailRequest emailRequest) {
        Twilio.init(twilioAccountSid, twilioAuthToken);
        Message.creator(
                new PhoneNumber("+216" + emailRequest.getTo()),
                new PhoneNumber(twilioPhoneNumber),
                emailRequest.getText() + " 📞"
        ).create();
        return new ResponseEntity<>("Message sent successfully", HttpStatus.OK);
    }*/

  /*  @PostMapping(value = "/send-smsWelcome")
    public ResponseEntity<?> sendWelcomeSMS(@RequestBody SmsRequest smsRequest) {
        String phoneNumber = smsRequest.getPhoneNumber();

        // Vérifier si le numéro commence par "+" pour s'assurer qu'il est au format international
        if (!phoneNumber.startsWith("+")) {
            // Ajouter le préfixe "+216" pour le numéro s'il ne commence pas par "+"
            phoneNumber = "+216" + phoneNumber;
        }

        authService.sendWelcomeSMS(phoneNumber, smsRequest.getEmail(), smsRequest.getRole());
        return ResponseEntity.ok().build();
    }*/
}
