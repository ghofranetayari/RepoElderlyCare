package elderlycare.DAO.Repositories;

import elderlycare.DAO.Entities.Ambulance;
<<<<<<< HEAD
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
=======
import org.springframework.data.jpa.repository.JpaRepository;
>>>>>>> a91cccbc16c00c02dfa62d7def9d2a41298a99ae

import java.util.List;

public interface AmbulanceRepository extends JpaRepository<Ambulance,Long> {
    List<Ambulance> findByArchiveTrue();
    Ambulance getAmbulanceByAmbulanceID(Long ambulanceID);
<<<<<<< HEAD
    Ambulance findByLocation(String location);

    @Modifying
    @Transactional
    @Query("UPDATE Ambulance a SET a.status = CASE WHEN a.status = 'Available' THEN 'NotAvailable' ELSE 'Available' END WHERE a.ambulanceID = ?1")
    void updateAmbulanceStatus(long id);

    List<Ambulance> findByStatus(String status);
=======
>>>>>>> a91cccbc16c00c02dfa62d7def9d2a41298a99ae
}
