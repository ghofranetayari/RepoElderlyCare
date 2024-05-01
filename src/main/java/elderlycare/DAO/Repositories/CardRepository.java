package elderlycare.DAO.Repositories;

import elderlycare.DAO.Entities.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends  JpaRepository<Card, Long> {
}
