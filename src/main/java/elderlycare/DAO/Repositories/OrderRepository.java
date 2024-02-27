package elderlycare.DAO.Repositories;

import elderlycare.DAO.Entities.Orderr;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository <Orderr,Long> {
}
