package elderlycare.DAO.Repositories;

import elderlycare.DAO.Entities.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProductRepository extends JpaRepository <Product,Long> {
  List<Product> findByProductNameStartingWithIgnoreCase(String searchTerm);
        @Query("SELECT p FROM Product p WHERE p.ArchProd = 'Available'")
        Page<Product> findAllAvailableProducts(Pageable pageable);


    @Transactional
    @Modifying
    @Query("UPDATE Product p SET p.price = p.price * 0.8, p.discounted = true WHERE p.discounted = false")
    void applyDiscount();

    @Transactional
    @Modifying
    @Query("UPDATE Product p SET p.price = p.price / 0.8, p.discounted = false WHERE p.discounted = true")
    void removeDiscount();

}
