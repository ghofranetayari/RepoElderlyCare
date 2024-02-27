package elderlycare.DAO.Repositories;

import elderlycare.DAO.Entities.MedicalFolder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicalFolderRepository extends JpaRepository<MedicalFolder,Long> {
}
