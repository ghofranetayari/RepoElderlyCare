package elderlycare.DAO.Repositories;

import elderlycare.DAO.Entities.Notificationshop;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface Notificationshoprepo extends JpaRepository<Notificationshop,Long>
{
    List<Notificationshop> findByProduct_ProductId(Long productId);
    @Transactional
    @Modifying
    @Query("DELETE FROM Notificationshop n WHERE n.product.productId = :productId")
    void deleteByProductId(@Param("productId") Long productId);

}
