package elderlycare.DAO.Repositories;

<<<<<<< HEAD
import elderlycare.DAO.Entities.Orderr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository <Orderr,Long> {
    @Query("SELECT o.totalPrice FROM Orderr o WHERE o.orderId = ?1")
    Double findTotalPriceByOrderId(Long orderId);
=======
import elderlycare.DAO.Entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository <Order,Long> {
>>>>>>> a91cccbc16c00c02dfa62d7def9d2a41298a99ae
}
