package elderlycare.DAO.Repositories;

import elderlycare.DAO.Entities.Appointment;
import elderlycare.DAO.Entities.Doctor;
import elderlycare.DAO.Entities.Elderly;
import elderlycare.DAO.Entities.OurUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ElderlyRepository extends JpaRepository<Elderly, Long> {
    // You can add custom query methods here if needed

    Optional<Elderly> findById(Long id);

    Elderly findByUser(OurUsers user);
    @Query("SELECT e.appointments FROM Elderly e WHERE e.elderlyID = :elderlyId")
    List<Appointment> findAllAppointmentsForElderly(@Param("elderlyId") Long elderlyId);
    Elderly findElderlyByElderlyID (long elderlyId);

    Elderly findByUser_Id(Long userId);

    // Elderly findElderlyById(Long userId);


    Optional<Elderly> findByUserId(Integer userId);

    Optional<Elderly> findByEmail(String email);


}