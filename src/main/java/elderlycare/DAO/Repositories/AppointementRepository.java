package elderlycare.DAO.Repositories;

import elderlycare.DAO.Entities.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointementRepository extends JpaRepository<Appointment,Long> {
}
