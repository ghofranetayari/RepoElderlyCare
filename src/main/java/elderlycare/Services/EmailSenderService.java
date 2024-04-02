package elderlycare.Services;


import elderlycare.DAO.Entities.Appointment;
import elderlycare.DAO.Entities.AppointmentStatus;
import elderlycare.DAO.Entities.Elderly;
import elderlycare.DAO.Repositories.AppointementRepository;
import elderlycare.DAO.Repositories.ElderlyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class EmailSenderService {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired

    private AppointementRepository  appointementRepository;
    @Autowired

    private ElderlyRepository elderlyRepository;
    public void sendSimpleEmail(String toEmail,
                                String subject,
                                String body
    ) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("loumema01@gmail.com");
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);
        mailSender.send(message);
        System.out.println("Mail Send...");


    }


    public void sendAppointmentReminders() {
        // Logic to retrieve upcoming appointments
        System.out.println("sendAppointmentReminders void:");


        List<Appointment> upcomingAppointments = retrieveUpcomingAppointments();
        System.out.println("upcomingAppointments :" +upcomingAppointments);

        // Loop through appointments and send reminders
        for (Appointment appointment : upcomingAppointments) {
            System.out.println("upcomingAppointments..."+ upcomingAppointments);

            sendReminder(appointment);
        }
    }
    private void sendReminder(Appointment appointment) {

        // Logic to get elder's email and send a reminder
        String elderEmail = getElderEmailForAppointment(appointment);
        String subject = "Reminder: Your Appointment is in an hour";
        String body = "Dear Elder, Your appointment is scheduled in an hour. Please be prepared.";
        System.out.println("Sending reminder email:");
        System.out.println("Recipient: " + elderEmail);
        System.out.println("Subject: " + subject);
        System.out.println("Body: " + body);

        sendSimpleEmail(elderEmail, subject, body);
    }

    private String getElderEmailForAppointment(Appointment appointment) {
        List<Elderly> elderlies = elderlyRepository.findAll(); // Implement your service method to get all elderly individuals

        for (Elderly elderly : elderlies) {
            List<Appointment> elderlyAppointments = elderly.getAppointments();
            for (Appointment elderlyAppointment : elderlyAppointments) {
                if (elderlyAppointment.getIdAppointment() == appointment.getIdAppointment()) {
                    // Found the elderly associated with the appointment
                    return elderly.getEmail();
                }
            }
        }

        // Handle the case when the associated elderly is not found
        throw new RuntimeException("No elderly found for the appointment: " + appointment.getIdAppointment());
    }
    private List<Appointment> retrieveUpcomingAppointments() {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneHourLater = now.plusHours(2);

        Date startDate = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(oneHourLater.atZone(ZoneId.systemDefault()).toInstant());

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        String startDateString = dateFormat.format(startDate);
        String endDateString = dateFormat.format(endDate);

        List<Appointment> appointments = appointementRepository.findByAppFromBetweenAndAppStatusAndArchiveApp(startDateString, endDateString, AppointmentStatus.APPROVED, "0");

        return appointments;
    }
    /*private List<Appointment> retrieveUpcomingAppointments() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime sixtyMinutesLater = now.plusMinutes(10);

        return appointementRepository.findByAppFromBetweenAndAppStatusAndArchiveAppIsFalse(
                now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                sixtyMinutesLater.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                AppointmentStatus.APPROVED);
    }*//*ttt
    private List<Appointment> retrieveUpcomingAppointments() {

        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime sixtyMinutesLater = now.plusMinutes(1400);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String startDateString = dateFormat.format(Date.from(now.toInstant()));
        String endDateString = dateFormat.format(Date.from(sixtyMinutesLater.toInstant()));

        Date startDate;
        Date endDate;
        try {
            startDate = dateFormat.parse(startDateString);
            endDate = dateFormat.parse(endDateString);
        } catch (ParseException e) {
            // Handle the parsing exception
            throw new RuntimeException("Error parsing date strings", e);
        }

        return appointementRepository.findByAppFromBetweenAndAppStatusAndArchiveApp(startDateString, endDateString, AppointmentStatus.APPROVED ,"false");
    }*/

}
