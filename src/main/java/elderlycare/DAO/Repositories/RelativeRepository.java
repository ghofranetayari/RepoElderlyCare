package elderlycare.DAO.Repositories;

import elderlycare.DAO.Entities.OurUsers;
import elderlycare.DAO.Entities.Relative;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RelativeRepository extends JpaRepository<Relative,Long> {

    Relative findByUser_Id(Long userId);
    List<Relative> findByEtats(long etats);


    Relative findByUser(OurUsers user);

    Optional<Relative> findByUserId(Integer userId);
    Relative findByIdRelative(Long relativeId);

    List<Relative> findByElderlyElderlyID(Long elderlyId);
   //oumayma
    @Query("SELECT r FROM Relative r WHERE r.elderly.elderlyID = :elderlyId")
    List<Relative> findByElderlyId(Long elderlyId);


    @Query("SELECT r.elderly.elderlyID FROM Relative r WHERE r.idRelative = :relativeId")
    Long getElderlyIdByRelativeId(@Param("relativeId") Long relativeId);

}

