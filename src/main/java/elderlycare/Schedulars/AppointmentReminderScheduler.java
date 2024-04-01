package elderlycare.Schedulars;

import elderlycare.Services.EmailSenderService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AppointmentReminderScheduler {

    @Autowired
    private EmailSenderService emailSenderService;

    @Transactional // Open a transaction for the scheduled task

    //
    @Scheduled(fixedRate = 3600000) // 3600000 milliseconds = 1 hour
    public void sendAppointmentReminders() {
        emailSenderService.sendAppointmentReminders();
    }


}