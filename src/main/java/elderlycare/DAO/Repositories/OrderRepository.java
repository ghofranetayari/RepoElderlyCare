package elderlycare.DAO.Repositories;

import elderlycare.DAO.Entities.Orderr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository <Orderr,Long> {
    @Query("SELECT o.totalPrice FROM Orderr o WHERE o.orderId = ?1")
    Double findTotalPriceByOrderId(Long orderId);
}
