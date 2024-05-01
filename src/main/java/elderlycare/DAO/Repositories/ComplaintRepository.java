package elderlycare.DAO.Repositories;

import elderlycare.DAO.Entities.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
 /*@Modifying
    @Query("UPDATE Complaint c SET c.archived = true WHERE c.id = :id")
    void archiveComplaint(@Param("id") Long id);*/

    List<Complaint> findByElderlyElderlyID(Long elderlyID);
    List<Complaint> findByRelativeIdRelative(Long idRelative);

    List<Complaint> findByDoctorEmail(String doctorEmail);
}
