package elderlycare.Services;

import elderlycare.DAO.Entities.OurUsers;
import elderlycare.DAO.Repositories.ArchiveUserRepository;
import elderlycare.DAO.Repositories.OurUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

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
