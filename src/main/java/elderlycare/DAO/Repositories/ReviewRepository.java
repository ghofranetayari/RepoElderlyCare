package elderlycare.DAO.Repositories;

import elderlycare.DAO.Entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Long> {
 List<Review> findByDoctorId(long doctorId);
 @Query("SELECT COUNT(r) FROM Review r WHERE r.doctorId = :doctorId")
 int getTotalRatingsForDoctor(Long doctorId);}
