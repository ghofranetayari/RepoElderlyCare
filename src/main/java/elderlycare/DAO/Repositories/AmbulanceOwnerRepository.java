package elderlycare.DAO.Repositories;

import elderlycare.DAO.Entities.AmbulanceOwner;
import elderlycare.DAO.Entities.Doctor;
import elderlycare.DAO.Entities.OurUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AmbulanceOwnerRepository extends JpaRepository<AmbulanceOwner,Long> {
    AmbulanceOwner findByUser(OurUsers user);

    Optional<AmbulanceOwner> findByUserId(Integer userId); // Assurez-vous que le type de retour et le nom de la méthode correspondent à vos besoins

    AmbulanceOwner findByUser_Id(Long userId);

}
