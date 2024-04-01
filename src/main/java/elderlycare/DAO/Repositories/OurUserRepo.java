package elderlycare.DAO.Repositories;

 import elderlycare.DAO.Entities.Doctor;
 import elderlycare.DAO.Entities.OurUsers;
 import org.springframework.data.jpa.repository.JpaRepository;
 import org.springframework.stereotype.Repository;

 import java.util.List;
 import java.util.Optional;

 @Repository
public interface OurUserRepo extends JpaRepository<OurUsers, Integer> {
     Optional<OurUsers> findByEmail(String email);

     List<OurUsers> findAll();
     Optional<OurUsers> findById(Integer id);


     List<OurUsers> findByEmailContaining(String email);
     List<OurUsers> findByRole(String role);

     OurUsers findByPhoneNumber(String PhoneNumber);


 }
