package elderlycare.DAO.Repositories;

import elderlycare.DAO.Entities.Ambulance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AmbulanceRepository extends JpaRepository<Ambulance,Long> {
}
