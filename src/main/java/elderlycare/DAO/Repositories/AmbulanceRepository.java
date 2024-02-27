package elderlycare.DAO.Repositories;

import elderlycare.DAO.Entities.Ambulance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AmbulanceRepository extends JpaRepository<Ambulance,Long> {
    List<Ambulance> findByArchiveTrue();
    Ambulance getAmbulanceByAmbulanceID(Long ambulanceID);
}
