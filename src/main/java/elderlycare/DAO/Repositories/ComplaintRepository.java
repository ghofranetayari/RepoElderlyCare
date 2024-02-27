package elderlycare.DAO.Repositories;

import elderlycare.DAO.Entities.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    // You can add custom query methods here if needed
}
