package elderlycare.DAO.Repositories;

<<<<<<< HEAD
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

=======
import elderlycare.DAO.Entities.Relative;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RelativeRepository extends JpaRepository<Relative,Long> {
>>>>>>> a91cccbc16c00c02dfa62d7def9d2a41298a99ae
}
