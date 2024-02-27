package elderlycare.DAO.Repositories;

import elderlycare.DAO.Entities.Elderly;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ElderlyRepository extends JpaRepository<Elderly, Long> {
    // You can add custom query methods here if needed
}