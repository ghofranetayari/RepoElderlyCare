package elderlycare.DAO.Repositories;

import elderlycare.DAO.Entities.Nurse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface NurseRepository extends JpaRepository<Nurse, Long> {
    // You can add custom query methods here if needed
}
