package elderlycare.DAO.Repositories;

import elderlycare.DAO.Entities.Tracking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackingRepository extends JpaRepository<Tracking,Long> {
}
