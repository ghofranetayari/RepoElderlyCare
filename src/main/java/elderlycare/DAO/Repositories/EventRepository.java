package elderlycare.DAO.Repositories;

import elderlycare.DAO.Entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event,Long> {
}
