package elderlycare.DAO.Repositories;

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

}
