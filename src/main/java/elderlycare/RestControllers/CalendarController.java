package elderlycare.RestControllers;

import elderlycare.DAO.Entities.Calendar;
import elderlycare.DAO.Entities.Elderly;
import elderlycare.DAO.Entities.OurUsers;
import elderlycare.Services.ICalendarService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = {"http://localhost:4200"} , allowedHeaders = {"Content-Type"})

@AllArgsConstructor

@RestController
@RequestMapping("/Calendar")

public class CalendarController {
    private  ICalendarService calendarService;
   // private UserService ourUsersService;

    @GetMapping("/settings/{id}")
    public Calendar getCalendarSettingsById(  @PathVariable Long id) {
        return calendarService.getCalendarSettingsById(id);
    }

    @PostMapping("/settings")
    public ResponseEntity<Calendar> createCalendarSettings(@RequestBody Calendar calendar) {
        Calendar createdCalendar = calendarService.createOrUpdateCalendarSettings(calendar);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCalendar);
    }
    /*  @PutMapping("/settings/{id}")
      public ResponseEntity<Calendar> updateCalendarSettings(@PathVariable Long id, @RequestBody Calendar updatedCalendar) {
          try {
              Calendar existingCalendar = calendarService.getCalendarSettingsById(id);

              // Update fields as needed
              existingCalendar.setBookingColor(updatedCalendar.getBookingColor());
              existingCalendar.setCalendarStart(updatedCalendar.getCalendarStart());
              existingCalendar.setCalendarEnd(updatedCalendar.getCalendarEnd());
              existingCalendar.setCurrentView(updatedCalendar.getCurrentView());
              existingCalendar.setIntervall(updatedCalendar.getIntervall());
              existingCalendar.setFirstDayOfWeek(updatedCalendar.getFirstDayOfWeek());

              Calendar savedCalendar = calendarService.createOrUpdateCalendarSettings(existingCalendar);
              return ResponseEntity.ok(savedCalendar);
          } catch (EntityNotFoundException e) {
              return ResponseEntity.notFound().build();
          }

      }*/
    @DeleteMapping("/settings/{id}")
    public ResponseEntity<String> deleteCalendarSettingsById(@PathVariable Long id) {
        try {
            calendarService.deleteCalendarSettingsById(id);
            return ResponseEntity.ok("Calendar settings with ID " + id + " deleted successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getCalendarIdByDoctor/{doctorId}")
    public ResponseEntity<Long> getCalendarIdByDoctorId(@PathVariable Long doctorId) {
        Long calendarId = calendarService.getCalendarIdByDoctorId(doctorId);

        if (calendarId != null) {
            return new ResponseEntity<>(calendarId, HttpStatus.OK);
        } else {
            // Return an appropriate response or handle the case where calendarId is not found
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }@GetMapping("/Elderly/{ElderlyId}")
    public ResponseEntity<Elderly> getElderlyById(@PathVariable Long ElderlyId) {
        Elderly elderly = calendarService.getElderlyById(ElderlyId);

        if (elderly != null) {
            return ResponseEntity.ok(elderly);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/{appointmentId}")
    public ResponseEntity<String> getElderlyNameByAppointmentId(@PathVariable Long appointmentId) {
        Optional<String> elderlyNameOptional = calendarService.getElderlyNameByAppointmentId(appointmentId);
        return elderlyNameOptional.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
/*
    @GetMapping("/details/{email}")
    public ResponseEntity<OurUsers> getUserDetailsByEmail(@PathVariable String email) {
        Optional<OurUsers> optionalUser = ourUsersService.getUserDetailsByEmail(email);
        if (optionalUser.isPresent()) {
            return ResponseEntity.ok(optionalUser.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }*/

}
