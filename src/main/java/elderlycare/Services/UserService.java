package elderlycare.Services;

import elderlycare.DAO.Entities.OurUsers;
import elderlycare.DAO.Repositories.OurUserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private OurUserRepo ourUserRepo;
    @Autowired
    private RoleStatistic roleStatistic;
    @Autowired
    public UserService(OurUserRepo userRepository) {
        this.ourUserRepo = userRepository;
    }


    public int ArchiveUser(Integer Id) {
        OurUsers ourUsers = ourUserRepo.findById(Id).get();
        if (ourUsers != null) {
            ourUsers.setArchive(true);
            ourUserRepo.save(ourUsers);
            updateStatisticsOnArchive(ourUsers);

            return 1;
        } else {
            return 0;
        }

    }

    public int ActivateUser(Integer Id){
        OurUsers ourUsers = ourUserRepo.findById(Id).get();
        if (ourUsers != null) {
            ourUsers.setArchive(false);
            ourUserRepo.save(ourUsers);
            updateStatisticsOnActivate(ourUsers);

            return 1;
        } else {
            return 0;
        }

    }

    private void updateStatisticsOnArchive(OurUsers ourUsers) {
        // Mise à jour des statistiques en fonction du type d'utilisateur archivé
        if (ourUsers.getRole().equals("doctor")) {
            roleStatistic.decrementDoctorCount();
        } else if (ourUsers.getRole().equals("nurse")) {
            roleStatistic.decrementNurseCount();
        } else if (ourUsers.getRole().equals("elderly")) {
            roleStatistic.decrementElderlyCount();
        } else if (ourUsers.getRole().equals("ambulanceDriver")) {
            roleStatistic.decrementAmbulanceDriverCount();
        } else if (ourUsers.getRole().equals("ambulanceOwner")) {
            roleStatistic.decrementAmbulanceOwnerCount();
        }
    }

    private void updateStatisticsOnActivate(OurUsers ourUsers){
        // Mise à jour des statistiques en fonction du type d'utilisateur activé
        if (ourUsers.getRole().equals("doctor")) {
            roleStatistic.incrementDoctorCount();
        } else if (ourUsers.getRole().equals("nurse")) {
            roleStatistic.incrementNurseCount();
        } else if (ourUsers.getRole().equals("elderly")) {
            roleStatistic.incrementElderlyCount();
        } else if (ourUsers.getRole().equals("ambulanceDriver")) {
            roleStatistic.incrementAmbulanceDriverCount();
        } else if (ourUsers.getRole().equals("ambulanceOwner")) {
            roleStatistic.incrementAmbulanceOwnerCount();
        }
    }
    public Optional<OurUsers> getUserDetailsByEmail(String email) {
        return ourUserRepo.findByEmail(email);
    }


//Mariem


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
    }

}



