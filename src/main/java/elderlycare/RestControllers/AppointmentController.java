package elderlycare.RestControllers;

import elderlycare.DAO.Entities.Appointment;
import elderlycare.Services.EmailSenderService;
import elderlycare.Services.IAppointmentService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@CrossOrigin(origins = {"http://localhost:4200"} , allowedHeaders = {"Content-Type"})

@AllArgsConstructor

@RestController
@RequestMapping("/appointments")

public class AppointmentController {
    IAppointmentService iAppointmentService;


    @GetMapping
    public List<Appointment> getAllAppointments() {
        return iAppointmentService.getAllAppointments();
    }


    @PutMapping("AddApp/{elderlyId}/{calendarId}")
    public Appointment createAppointment(@RequestBody Appointment appointment, @PathVariable long elderlyId,@PathVariable long calendarId){
        return iAppointmentService.createAppointment(appointment,elderlyId,calendarId);
    }
    @GetMapping("GetAppDetails/{id}")
    public Appointment getAppointmentById(@PathVariable long id) {
        return iAppointmentService.getAppointmentById(id);

    }
    /* @PutMapping("/UpdateApp/{id}")
     public Appointment updateAppointment(@PathVariable long id, @RequestBody Appointment updatedAppointment) {
        return  iAppointmentService.updateAppointment(id, updatedAppointment);


     }*/
    @DeleteMapping("/DeleteApp/{id}")
    public ResponseEntity<Void> cancelAppointment(@PathVariable long id) {
        iAppointmentService.cancelAppointment(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/today")
    public List<Appointment> getTodayAppointments() {
        return iAppointmentService.getTodaysAppointments();
    }
    @GetMapping("/markDone")
    public ResponseEntity<Object> markAppointmentDone(@RequestParam long appointmentId) {
        try {
            iAppointmentService.markAppointmentDone(appointmentId);
            return ResponseEntity.ok().build();  // Respond with 200 OK if successful
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Appointment not found with ID: " + appointmentId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error marking appointment as done.");
        }
    }
  /*  @GetMapping("/pending/{doctorId}")
    public ResponseEntity<List<Appointment>> getPendingAppointments(@PathVariable Long doctorId) {
        try {
            List<Appointment> pendingAppointments = iAppointmentService.getPendingAppointments(doctorId);
            return ResponseEntity.ok(pendingAppointments);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }*/

    @GetMapping("/{elderlyId}/appointments")
    public List<Appointment> getElderlyAppointments(@PathVariable Long elderlyId) {
        return iAppointmentService.getElderlyAppointments(elderlyId);
    }
    @GetMapping("/{calendarId}/cal" +
            "appointments")
    public List<Appointment> getAppointmentsByCalendarId(@PathVariable Long calendarId) {
        return iAppointmentService.getAppointmentsByCalendarId(calendarId);
    }

    @GetMapping("/{calendarId}/pending-appointments")
    public ResponseEntity<List<Appointment>> getPendingAppointmentsByCalendarId(
            @PathVariable Long calendarId) {
        try {
            List<Appointment> pendingAppointments = iAppointmentService.getPendingAppointmentsByCalendarId(calendarId);
            return new ResponseEntity<>(pendingAppointments, HttpStatus.OK);
        } catch (RuntimeException e) {
            // Handle the case when the calendar is not found
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/{elderlyId}/pending-appointmentsElderly")
    public ResponseEntity<List<Appointment>> getPendingAppointmentsByElderlyId(@PathVariable Long elderlyId) {
        try {
            List<Appointment> pendingAppointments = iAppointmentService.getPendingAppointmentsByElderlyId(elderlyId);
            return new ResponseEntity<>(pendingAppointments, HttpStatus.OK);
        } catch (RuntimeException e) {
            // Handle the case when the calendar is not found
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{elderlyId}/completed-appointmentsElderly")
    public ResponseEntity<List<Appointment>> getCompletedAppointmentsByElderlyId(@PathVariable Long elderlyId) {
        try {
            List<Appointment> completedAppointments = iAppointmentService.getCompletedAppointmentsByElderlyId(elderlyId);
            return new ResponseEntity<>(completedAppointments, HttpStatus.OK);
        } catch (RuntimeException e) {
            // Handle the case when the calendar is not found
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/{elderlyId}/rejected-appointmentsElderly")
    public ResponseEntity<List<Appointment>> getRejectedAppointmentsByElderlyId(@PathVariable Long elderlyId) {
        try {
            List<Appointment> rejectedAppointments = iAppointmentService.getRejectedAppointmentsByElderlyId(elderlyId);
            return new ResponseEntity<>(rejectedAppointments, HttpStatus.OK);
        } catch (RuntimeException e) {
            // Handle the case when the calendar is not found
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }




    @Autowired
    private EmailSenderService emailSenderService;

    @GetMapping("/send-appointment-reminders")
    public String sendAppointmentReminders() {
        System.out.println("Sending reminder email:");

        emailSenderService.sendAppointmentReminders();
        return "Appointment reminders sent successfully!";
    }




    @GetMapping("/{calendarId}/completed-appointments")
    public ResponseEntity<List<Appointment>> getCompletedAppointmentsByCalendarId(
            @PathVariable Long calendarId) {
        try {
            List<Appointment> completedAppointments = iAppointmentService.getCompletedAppointmentsByCalendarId(calendarId);
            return new ResponseEntity<>(completedAppointments, HttpStatus.OK);
        } catch (RuntimeException e) {
            // Handle the case when the calendar is not found
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/{calendarId}/approved-appointments")
    public ResponseEntity<List<Appointment>> getApprovedAppointmentsByCalendarId(
            @PathVariable Long calendarId) {
        try {
            List<Appointment> approvedAppointments = iAppointmentService.getApprovedAppointmentsByCalendarId(calendarId);
            return new ResponseEntity<>(approvedAppointments, HttpStatus.OK);
        } catch (RuntimeException e) {
            // Handle the case when the calendar is not found
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{calendarId}/online-appointments")
    public ResponseEntity<List<Appointment>> getApprovedOnlineAppointmentsByCalendarId(
            @PathVariable Long calendarId) {
        try {
            List<Appointment> approvedOnlineAppointments = iAppointmentService.getApprovedOnlineAppointmentsByCalendarId(calendarId);
            return new ResponseEntity<>(approvedOnlineAppointments, HttpStatus.OK);
        } catch (RuntimeException e) {
            // Handle the case when the calendar is not found
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/markRejected")
    public ResponseEntity<Object> markAppointmentRejected(@RequestParam long appointmentId) {
        try {
            iAppointmentService.markAppointmentRejected(appointmentId);
            return ResponseEntity.ok().build();  // Respond with 200 OK if successful
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Appointment not found with ID: " + appointmentId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error marking appointment as rejected.");
        }
    }



}
