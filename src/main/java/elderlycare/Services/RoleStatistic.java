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

    @Autowired
    public RoleStatistic(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Map<String, Integer> getRoleStatistics() {
        String sql = "SELECT role, COUNT(*) AS count FROM ourusers WHERE archive = false GROUP BY role";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
        Map<String, Integer> roleStatistics = new HashMap<>();

        for (Map<String, Object> row : rows) {
            String role = (String) row.get("role");
            int count = ((Number) row.get("count")).intValue();
            roleStatistics.put(role, count);
        }

        return roleStatistics;
    }

    private int doctorCount;
    private int nurseCount;
    private int elderlyCount;
    private int ambulanceDriverCount;
    private int ambulanceOwnerCount;
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

}
