package elderlycare.Services;

import elderlycare.DAO.Entities.Appointment;
import elderlycare.DAO.Entities.AppointmentStatus;
import elderlycare.DAO.Entities.Calendar;
import elderlycare.DAO.Entities.Elderly;
import elderlycare.DAO.Repositories.AppointementRepository;
import elderlycare.DAO.Repositories.CalendarRepository;
import elderlycare.DAO.Repositories.ElderlyRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class AppointmentService implements IAppointmentService {
    AppointementRepository appointmentRepository;
    ElderlyRepository     elderlyRepository;
    CalendarRepository calendarRepository;

    @Transactional

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }


    /*  @Transactional
      @Override
      public List<Appointment> getTodaysAppointments() {
          LocalDate today = LocalDate.now();
          LocalDateTime startOfDay = today.atStartOfDay();
          LocalDateTime endOfDay = today.atTime(23, 59, 59); // Set end time to 23:59:59

          return appointmentRepository.findByAppFromAfterAndAppToBefore(
                  Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant()),
                  Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant())
          );
      }*/
    @Transactional
    @Override
    public List<Appointment> getTodaysAppointments() {
        LocalDate today = LocalDate.now();
        String startOfDay = today.atStartOfDay().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'00:00:00"));
        String endOfDay = today.atTime(23, 59, 59).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));

        return appointmentRepository.findByAppFromAfterAndAppToBefore(startOfDay, endOfDay);
    }



    @Transactional
    @Override
    public Appointment getAppointmentById(long id) {
        Optional<Appointment> appointmentOptional = appointmentRepository.findById(id);

        if (appointmentOptional.isPresent()) {
            return appointmentOptional.get();
        } else {
            // Handle the case when the appointment is not found
            throw new EntityNotFoundException("Appointment not found with id: " + id);
        }
    }
    /*
        @Override
        public Appointment createAppointment(Appointment appointment) {
            return appointmentRepository.save(appointment);
        }*/
    @Override
    @Transactional
    public Appointment createAppointment(Appointment appointment, Long elderlyId, long calendarId) {
        Elderly elderly = elderlyRepository.findById(elderlyId).orElse(null);
        Calendar calendar = calendarRepository.findById(calendarId).orElse(null);

        if (elderly != null && calendar != null) {
            // Load the appointment entity within the current transactional context
            Appointment existingAppointment = appointmentRepository.findById(appointment.getIdAppointment()).orElse(null);

            if (existingAppointment == null) {
                // If the appointment doesn't exist, it's a new appointment, so set the relationship and save
                elderly.getAppointments().add(appointment);
                calendar.getAppointments().add(appointment);
                appointment.setArchiveApp("0");

                elderlyRepository.save(elderly);
                calendarRepository.save(calendar);
                appointmentRepository.save(appointment);
            } else {
                // If the appointment already exists, update its properties and save
                existingAppointment.setAppFrom(appointment.getAppFrom());
                existingAppointment.setAppTo(appointment.getAppTo());
                existingAppointment.setAppFirst(appointment.getAppFirst());
                existingAppointment.setSymptom(appointment.getSymptom());
                existingAppointment.setAppStatus(appointment.getAppStatus());

                elderlyRepository.save(elderly);
                calendarRepository.save(calendar);
                appointmentRepository.save(existingAppointment);
            }

            return appointment;
        } else {
            // Handle the case where the Elderly or Calendar entity with the given ID is not found
            return null;
        }
    }

    @Transactional
    public void cancelAppointment(long id) {
        Optional<Appointment> existingApp = appointmentRepository.findById(id);

        if (existingApp.isPresent()) {
            Appointment cancelledAppointment = existingApp.get();
            cancelledAppointment.setAppStatus(AppointmentStatus.CANCELLED);

            // Optionally, you can update other fields or perform additional actions

            appointmentRepository.save(cancelledAppointment);
        }
    }  @Transactional

    public Appointment updateAppointment(long id, Appointment updatedAppointment) {
        Optional<Appointment> existingApp = appointmentRepository.findById(id);

        if (existingApp.isPresent()) {
            Appointment Uapp = existingApp.get();
            Uapp.setPatientName(updatedAppointment.getPatientName());
            Uapp.setAppFirst(updatedAppointment.getAppFirst());
            Uapp.setAppFrom(updatedAppointment.getAppFrom());
            Uapp.setAppTo(updatedAppointment.getAppTo());
            Uapp.setSymptom(updatedAppointment.getSymptom());
            Uapp.setArchiveApp(updatedAppointment.getArchiveApp());
            Uapp.setAppStatus(updatedAppointment.getAppStatus());

            // Update other fields as needed

            return appointmentRepository.save(Uapp);
        }

        // Handle the case when the appointment with the given ID is not found
        return null;
    }



    @Override
    public Appointment markAppointmentDone(Long id) {
        Appointment appointment = getAppointmentById(id);
        appointment.setAppStatus(AppointmentStatus.APPROVED);
        appointmentRepository.save(appointment);
        return appointment; // Return the updated appointment
    }

   /* @Override
    public List<Appointment> getPendingAppointments(Long doctorId) {
        return appointmentRepository.findByDoctor2IdDoctorAndAppStatus(doctorId, AppointmentStatus.PENDING);
    }*/

    public List<Appointment> getElderlyAppointments(Long elderlyId) {
        Elderly elderly = elderlyRepository.findById(elderlyId)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found with ID: " + elderlyId));

        return elderly.getAppointments();
    }
    @Override
    public List<Appointment> getAppointmentsByCalendarId(Long calendarId) {
        Calendar calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new EntityNotFoundException("Calendar not found with id: " + calendarId));

        return calendar.getAppointments();
    }
    @Override
    public List<Appointment> getPendingAppointmentsByCalendarId(Long calendarId) {
        // Retrieve the calendar by ID
        Calendar calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new RuntimeException("Calendar not found with ID: " + calendarId));

        // Now, you can access the appointments of the calendar
        List<Appointment> appointments = calendar.getAppointments();

        // Filter appointments with status "PENDING"
        List<Appointment> pendingAppointments = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getAppStatus().equals(AppointmentStatus.PENDING)) {
                pendingAppointments.add(appointment);
            }
        }
        return pendingAppointments;
    }
    @Override
    public List<Appointment> getPendingAppointmentsByElderlyId(Long elderlyId) {
        // Implement the logic to fetch pending appointments based on the elderlyId
        Elderly   elderly = elderlyRepository.findById(elderlyId)
                .orElseThrow(() -> new RuntimeException("elderly not found with ID: " + elderlyId));
        List<Appointment> appointments = elderly.getAppointments();
        // Filter appointments with status "PENDING"
        List<Appointment> pendingAppointments = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getAppStatus().equals(AppointmentStatus.PENDING)) {
                pendingAppointments.add(appointment);
            }
        }
        return pendingAppointments;

    }
    @Override
    public List<Appointment> getCompletedAppointmentsByElderlyId(Long elderlyId) {
        // Implement the logic to fetch pending appointments based on the elderlyId
        Elderly   elderly = elderlyRepository.findById(elderlyId)
                .orElseThrow(() -> new RuntimeException("elderly not found with ID: " + elderlyId));
        List<Appointment> appointments = elderly.getAppointments();
        // Filter appointments with status "PENDING"
        List<Appointment> pendingAppointments = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getAppStatus().equals(AppointmentStatus.COMPLETED)) {
                pendingAppointments.add(appointment);
            }
        }
        return pendingAppointments;

    }




    @Override
    public List<Appointment> getRejectedAppointmentsByElderlyId(Long elderlyId) {
        // Implement the logic to fetch pending appointments based on the elderlyId
        Elderly   elderly = elderlyRepository.findById(elderlyId)
                .orElseThrow(() -> new RuntimeException("elderly not found with ID: " + elderlyId));
        List<Appointment> appointments = elderly.getAppointments();
        // Filter appointments with status "PENDING"
        List<Appointment> rejectedAppointments = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getAppStatus().equals(AppointmentStatus.REJECTED)) {
                rejectedAppointments.add(appointment);
            }
        }
        return rejectedAppointments;

    }







    @Override
    public List<Appointment> getCompletedAppointmentsByCalendarId(Long calendarId) {
        // Retrieve the calendar by ID
        Calendar calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new RuntimeException("Calendar not found with ID: " + calendarId));
        // Now, you can access the appointments of the calendar
        List<Appointment> appointments = calendar.getAppointments();
        // Filter appointments with status "COMPLETED"
        List<Appointment> completedAppointments = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getAppStatus().equals(AppointmentStatus.COMPLETED)) {
                completedAppointments.add(appointment);
            }
        }
        return completedAppointments;
    }
    @Override
    public List<Appointment> getApprovedAppointmentsByCalendarId(Long calendarId) {
        // Retrieve the calendar by ID
        Calendar calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new RuntimeException("Calendar not found with ID: " + calendarId));
        // Now, you can access the appointments of the calendar
        List<Appointment> appointments = calendar.getAppointments();
        // Filter appointments with status "Approved"
        List<Appointment> approvedAppointments = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getAppStatus().equals(AppointmentStatus.APPROVED)) {
                approvedAppointments.add(appointment);
            }
        }
        return approvedAppointments;
    }
    public void markAppointmentRejected(long id) {
        Appointment app = getAppointmentById(id);
        // Assuming you have a method to get the status 'REJECTED' from the enum
        app.setAppStatus(AppointmentStatus.REJECTED);
        appointmentRepository.save(app);

    }

}
