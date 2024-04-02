package elderlycare.Services;

import elderlycare.DAO.Entities.Appointment;
import elderlycare.DAO.Entities.Calendar;
import elderlycare.DAO.Entities.Doctor;
import elderlycare.DAO.Entities.Elderly;
import elderlycare.DAO.Repositories.CalendarRepository;
import elderlycare.DAO.Repositories.ElderlyRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class CalendarService implements ICalendarService{
    CalendarRepository  calendarRepository;
    private ElderlyRepository elderlyRepository;



    @Transactional
    @Override
    public Calendar getCalendarSettingsById(long id) {
        Optional<Calendar> calendarOptional = calendarRepository.findById(id);

        if (calendarOptional.isPresent()) {
            return calendarOptional.get();
        } else {
            // Handle the case when the appointment is not found
            throw new EntityNotFoundException("calendar not found with id: " + id);
        }
    }


    public Calendar createOrUpdateCalendarSettings(Calendar calendar) {
        return calendarRepository.save(calendar);
    }

    @Transactional
    public void deleteCalendarSettingsById(long id) {
        if (calendarRepository.existsById(id)) {
            calendarRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Calendar not found with id: " + id);
        }
    }
    @Override

    public Long getCalendarIdByDoctorId(Long doctorId) {
        Doctor doctor = new Doctor();
        doctor.setIdDoctor(doctorId); // Assuming Doctor has setId method, adjust accordingly

        Calendar calendar = calendarRepository.findCalendarIdByDoctor(doctor);

        if (calendar != null) {
            return calendar.getId(); // Assuming Calendar has getId method, adjust accordingly
        } else {
            // Return an appropriate value or throw an exception based on your requirements
            return null;
        }
    }
    @Override
    public Elderly getElderlyById(Long ElderlyId) {
        return elderlyRepository.findById(ElderlyId).orElse(null);
    }

    @Override
    public Optional<String> getElderlyNameByAppointmentId(Long appointmentId) {
        Iterable<Elderly> elderlyIterable = elderlyRepository.findAll();
        for (Elderly elderly : elderlyIterable) {
            for (Appointment appointment : elderly.getAppointments()) {
                if (appointment.getIdAppointment() == appointmentId) {
                    return Optional.of(elderly.getFirstName() + " " + elderly.getLastName());
                }
            }
        }
        return Optional.empty();
    }










}
