package elderlycare.Services;


import elderlycare.DAO.Entities.Appointment;
import elderlycare.DAO.Entities.AppointmentStatus;
import elderlycare.DAO.Entities.Elderly;
import elderlycare.DAO.Repositories.AppointementRepository;
import elderlycare.DAO.Repositories.ElderlyRepository;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import freemarker.template.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.FileCopyUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
@Service
public class EmailSenderService {
    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private Configuration freemarkerConfig; // Autowire FreeMarker configuration

    @Autowired
    private JavaMailSender mailSender;
    @Autowired

    private AppointementRepository  appointementRepository;
    @Autowired

    private ElderlyRepository elderlyRepository;



    public void sendHtmlEmail(String toEmail, String subject, String templateName, Context context) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            // Set the recipients, subject, and content using FreeMarker processed HTML
            helper.setTo(toEmail);
            helper.setSubject(subject);

            // Use FreeMarker to process the template content
            Template template = freemarkerConfig.getTemplate(templateName + ".ftl"); // Load FreeMarker template
            String htmlContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, context);

            // Set HTML content with the processed template
            helper.setText(htmlContent, true);

            // Add the image as an inline attachment
            InputStreamSource imageSource = new ClassPathResource("static/images/logo.png");
            helper.addInline("image_cid", imageSource, "image/png");

            // Send the email
            mailSender.send(message);
            System.out.println("HTML Email Sent Successfully!");
        } catch (MessagingException | IOException | TemplateException e) {
            System.err.println("Error sending HTML email: " + e.getMessage());
            // Handle or log the exception as needed
        }
    }


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
    /* private void sendReminder(Appointment appointment) {

         // Logic to get elder's email and send a reminder
         String elderEmail = getElderEmailForAppointment(appointment);
         String subject = "Reminder: Your Appointment is in an hour";
         String body = "Dear Elder, Your appointment is scheduled in an hour. Please be prepared.";
         System.out.println("Sending reminder email:");
         System.out.println("Recipient: " + elderEmail);
         System.out.println("Subject: " + subject);
         System.out.println("Body: " + body);

         sendSimpleEmail(elderEmail, subject, body);
     }*/
    public void sendReminder(Appointment appointment) {
        String elderEmail = getElderEmailForAppointment(appointment);
        String subject = "Reminder: Your Appointment is in an hour";

        // Set the message variable in the FreeMarker model
        Context context = new Context();
        context.setVariable("message", "Dear Elder, Your appointment is scheduled in an hour. Please be prepared.");

        sendHtmlEmail(elderEmail, subject, "apptemplate", context);
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
