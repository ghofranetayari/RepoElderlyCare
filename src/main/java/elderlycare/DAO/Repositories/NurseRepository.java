package elderlycare.DAO.Repositories;

<<<<<<< HEAD
import elderlycare.DAO.Entities.Doctor;
import elderlycare.DAO.Entities.Nurse;
import elderlycare.DAO.Entities.OurUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface NurseRepository extends JpaRepository<Nurse, Long> {
    Nurse findByUser(OurUsers user);
    Nurse findByUser_Id(Long userId);
    Optional<Nurse> findByUserId(Integer userId);

=======
import elderlycare.DAO.Entities.Nurse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface NurseRepository extends JpaRepository<Nurse, Long> {
    // You can add custom query methods here if needed
>>>>>>> a91cccbc16c00c02dfa62d7def9d2a41298a99ae
}
