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

    // Méthode privée pour mettre à jour les statistiques lors de l'archivage d'un utilisateur
    private void updateStatisticsOnArchive(OurUsers ourUsers) {
        String role = ourUsers.getRole();
        roleStatistic.updateUserRemovedStatistics(role);
    }

    // Méthode privée pour mettre à jour les statistiques lors de l'activation d'un utilisateur
    private void updateStatisticsOnActivate(OurUsers ourUsers) {
        String role = ourUsers.getRole();
        roleStatistic.updateUserAddedStatistics(role);
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



