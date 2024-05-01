package elderlycare.RestControllers;

import elderlycare.Services.RoleStatistic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/role-statistics")
public class RoleStatisticController {

    private final RoleStatistic roleStatistic;

    @Autowired
    public RoleStatisticController(RoleStatistic roleStatistic) {
        this.roleStatistic = roleStatistic;
    }

    @GetMapping("/user-count-by-role")
    public Map<String, Double> getUserCountByRole() {
        Map<String, Integer> roleCounts = roleStatistic.getRoleCounts();
        int totalUserCount = roleStatistic.getTotalUserCount();

        Map<String, Double> userCountByRolePercentage = new HashMap<>();
        for (Map.Entry<String, Integer> entry : roleCounts.entrySet()) {
            String role = entry.getKey();
            int count = entry.getValue();
            double percentage = (count / (double) totalUserCount) * 100;
            userCountByRolePercentage.put(role, percentage);
        }

        return userCountByRolePercentage;
    }
}
