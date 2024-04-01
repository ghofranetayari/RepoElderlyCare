package elderlycare.DAO.Repositories;

import elderlycare.DAO.Entities.AmbulanceDriver;
<<<<<<< HEAD
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

=======
import org.springframework.data.jpa.repository.JpaRepository;

public interface AmbulanceDriverRepository extends JpaRepository<AmbulanceDriver,Long> {
>>>>>>> a91cccbc16c00c02dfa62d7def9d2a41298a99ae
}
