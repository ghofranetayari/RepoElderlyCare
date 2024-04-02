package elderlycare.Services;

import elderlycare.DAO.Entities.ArchiveUser;
import elderlycare.DAO.Entities.Doctor;
import elderlycare.DAO.Entities.OurUsers;
import elderlycare.DAO.Repositories.ArchiveUserRepository;
import elderlycare.DAO.Repositories.DoctorRepository;
import elderlycare.DAO.Repositories.OurUserRepo;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OurUserDetailsService implements UserDetailsService {

    @Autowired
    private OurUserRepo ourUserRepo;

    @Autowired
    ArchiveUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return ourUserRepo.findByEmail(username).orElseThrow();
    }

    public List<OurUsers> getAllUsers() {
        return ourUserRepo.findAll();
    }


    public void deleteUser(Integer userId) {
        ourUserRepo.deleteById(userId);
    }


}
