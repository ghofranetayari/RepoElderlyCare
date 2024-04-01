package elderlycare.Services;

import elderlycare.DAO.Entities.OurUsers;
import elderlycare.DAO.Repositories.OurUserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class UserServiceM {
//Mariem
@Autowired
private OurUserRepo ourUserRepo;

    @Autowired
    public UserServiceM(OurUserRepo userRepository) {
        this.ourUserRepo = userRepository;
    }

    public List<OurUsers> getAllUsers() {
        return ourUserRepo.findAll();
    }

    public Map<String, Boolean> getOnlineStatus() {
        List<OurUsers> users = getAllUsers();
        Map<String, Boolean> onlineStatusMap = new HashMap<>();
        for (OurUsers user : users) {
            onlineStatusMap.put(user.getEmail(), user.isOnline());
        }
        return onlineStatusMap;
    }
    @Transactional
    public void updateUserOnlineStatus(String email, boolean online) {
        OurUsers user = ourUserRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        user.setOnline(online);
        ourUserRepo.save(user);
    }}
