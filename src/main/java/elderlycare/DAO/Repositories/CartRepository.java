package elderlycare.DAO.Repositories;

import elderlycare.DAO.Entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository <Cart,Long> {
}
