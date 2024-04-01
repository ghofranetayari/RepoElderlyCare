package elderlycare.DAO.Repositories;

import elderlycare.DAO.Entities.AmbulanceDriver;
import elderlycare.DAO.Entities.Doctor;
import elderlycare.DAO.Entities.OurUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AmbulanceDriverRepository extends JpaRepository<AmbulanceDriver,Long> {
    AmbulanceDriver findByUser(OurUsers user);
    Optional<AmbulanceDriver> findByUserId(Integer userId); // Assurez-vous que le type de retour et le nom de la méthode correspondent à vos besoins

    AmbulanceDriver findByUser_Id(Long userId);

}
