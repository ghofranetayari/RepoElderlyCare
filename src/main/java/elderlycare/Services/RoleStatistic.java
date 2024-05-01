package elderlycare.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RoleStatistic {
    private final JdbcTemplate jdbcTemplate;
    private int totalUserCount;
    private Map<String, Integer> roleCounts;
    private Map<String, Double> rolePercentages;


    @Autowired
    public RoleStatistic(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.roleCounts = new HashMap<>();
        this.rolePercentages = new HashMap<>();
        // Initialisez les statistiques des rôles
        updateRoleStatistics();
    }


    private void updateRoleStatistics() {
        String sql = "SELECT role, COUNT(*) AS count FROM ourusers WHERE archive = false GROUP BY role";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
        int totalUsers = 0;
        roleCounts.clear();

        // Calcul du nombre total d'utilisateurs et des décomptes par rôle
        for (Map<String, Object> row : rows) {
            String role = (String) row.get("role");
            int count = ((Number) row.get("count")).intValue();
            roleCounts.put(role, count);
            totalUsers += count;
        }

        // Calcul des pourcentages de chaque rôle par rapport au total
        for (Map.Entry<String, Integer> entry : roleCounts.entrySet()) {
            String role = entry.getKey();
            int count = entry.getValue();
            double percentage = (count / (double) totalUsers) * 100;
            rolePercentages.put(role, percentage);
        }

        totalUserCount = totalUsers;
    }

    // Mettre à jour les statistiques lorsqu'un utilisateur est ajouté
    public void updateUserAddedStatistics(String role) {
        int count = roleCounts.getOrDefault(role, 0);
        roleCounts.put(role, count + 1);
        updateRolePercentages();
    }

    // Mettre à jour les statistiques lorsqu'un utilisateur est supprimé
    public void updateUserRemovedStatistics(String role) {
        int count = roleCounts.getOrDefault(role, 0);
        if (count > 0) {
            roleCounts.put(role, count - 1);
            updateRolePercentages();
        }
    }

    // Mettre à jour les pourcentages après chaque mise à jour du décompte des rôles
    private void updateRolePercentages() {
        int totalUsers = roleCounts.values().stream().mapToInt(Integer::intValue).sum();
        for (Map.Entry<String, Integer> entry : roleCounts.entrySet()) {
            String role = entry.getKey();
            int count = entry.getValue();
            double percentage = (count / (double) totalUsers) * 100;
            rolePercentages.put(role, percentage);
        }
    }
    public int getTotalUserCount() {
        return totalUserCount;
    }

    public Map<String, Integer> getRoleCounts() {
        return roleCounts;
    }

    public Map<String, Double> getRolePercentages() {
        return rolePercentages;
    }


    private int doctorCount;
    private int nurseCount;
    private int elderlyCount;
    private int ambulanceDriverCount;
    private int ambulanceOwnerCount;
    private int relativeCount;

    public void incrementDoctorCount() {
        doctorCount++;
    }

    public void decrementDoctorCount() {
        doctorCount--;
    }

    public void incrementNurseCount() {
        nurseCount++;
    }

    public void decrementNurseCount() {
        nurseCount--;
    }

    public void incrementElderlyCount() {
        elderlyCount++;
    }

    public void decrementElderlyCount() {
        elderlyCount--;
    }

    public void incrementAmbulanceDriverCount() {
        ambulanceDriverCount++;
    }

    public void decrementAmbulanceDriverCount() {
        ambulanceDriverCount--;
    }

    public void incrementAmbulanceOwnerCount() {
        ambulanceOwnerCount++;
    }

    public void decrementAmbulanceOwnerCount() {
        ambulanceOwnerCount--;
    }
    public void incrementRelativeCount() {
        relativeCount++;
    }
    public void decrementRelativeCount() {
        relativeCount--;
    }
}
