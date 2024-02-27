package elderlycare.DAO.Repositories;

import elderlycare.DAO.Entities.Medication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicationRepository extends JpaRepository<Medication,Long> {
}
