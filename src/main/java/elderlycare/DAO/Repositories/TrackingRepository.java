package elderlycare.DAO.Repositories;

import elderlycare.DAO.Entities.Tracking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrackingRepository extends JpaRepository<Tracking, Long> {
    Optional<Tracking> findByElderlyElderlyID(Long elderlyId); // Use the correct property name

}
