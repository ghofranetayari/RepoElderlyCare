package elderlycare.DAO.Repositories;

import elderlycare.DAO.Entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository <Order,Long> {
}
