package elderlycare.DAO.Repositories;

import elderlycare.DAO.Entities.OurUsers;
import elderlycare.DAO.Entities.Relative;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RelativeRepository extends JpaRepository<Relative,Long> {

    Relative findByUser_Id(Long userId);
    List<Relative> findByEtats(long etats);


    Relative findByUser(OurUsers user);

    Optional<Relative> findByUserId(Integer userId);
    Relative findByIdRelative(Long relativeId);

    List<Relative> findByElderlyElderlyID(Long elderlyId);
}
