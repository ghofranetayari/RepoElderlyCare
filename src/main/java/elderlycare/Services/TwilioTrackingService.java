package elderlycare.Services;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TwilioTrackingService {

    @Value("${twilio.accountSid}")
    private String accountSid;

    @Value("${twilio.authToken}")
    private String authToken;
    @Value("${twilio.phoneNumber}")
    private String twilioPhoneNumber;

    public void sendSms(String to, String body) {
        Twilio.init(accountSid, authToken);
        Message message = Message.creator(
                new PhoneNumber(to),
                new PhoneNumber(twilioPhoneNumber),
                body
        ).create();
        System.out.println("SMS sent successfully. Message SID: " + message.getSid());
    }



    public void makeVoiceCall(String to) {
        String cleanedPhoneNumber = to.replaceAll("\\D", "");
        String phoneNumber = "+216" + cleanedPhoneNumber;

        Twilio.init(accountSid, authToken);
        Call call = Call.creator(
                new PhoneNumber(phoneNumber),
                new PhoneNumber(twilioPhoneNumber),
                new com.twilio.type.Twiml("<Response><Say> Hello This is ElderlyCare Tracking Service.Your relative left home and they're supposed to be back now . Please check on them .</Say></Response>")
        ).create();
        System.out.println("Voice call initiated successfully. Call SID: " + call.getSid());
    }


}


