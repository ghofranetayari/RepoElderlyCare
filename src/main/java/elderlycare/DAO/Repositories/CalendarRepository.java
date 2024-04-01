package elderlycare.DAO.Repositories;
import elderlycare.DAO.Entities.Appointment;
import elderlycare.DAO.Entities.Calendar;
import elderlycare.DAO.Entities.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalendarRepository extends JpaRepository<Calendar,Long> {
    Calendar findCalendarIdByDoctor(Doctor doctorId);

}
