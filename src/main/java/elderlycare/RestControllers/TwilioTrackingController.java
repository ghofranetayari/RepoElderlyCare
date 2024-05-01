package elderlycare.RestControllers;

import elderlycare.Services.TwilioTrackingService;
import elderlycare.dto.TwilioTrackingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TwilioTrackingController {

    private final TwilioTrackingService smsService;

    @Autowired
    public TwilioTrackingController(TwilioTrackingService smsService) {
        this.smsService = smsService;
    }

    @PostMapping("/send-sms")
    public void sendSms(@RequestBody TwilioTrackingRequest request) {
        smsService.sendSms("+216" +request.getTo(), request.getBody());
    }



    @PostMapping("/make-call")
    public void makeVoiceCall(@RequestBody String phoneNumber) {
        smsService.makeVoiceCall(phoneNumber);
    }
}
