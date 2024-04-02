package elderlycare.RestControllers;

import elderlycare.Services.RoleStatistic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/role-statistics")
public class RoleStatisticController {

    private final RoleStatistic roleStatistic;

    @Autowired
    public RoleStatisticController(RoleStatistic roleStatistic) {
        this.roleStatistic = roleStatistic;
    }

    @GetMapping
    public Map<String, Integer> getRoleStatistics() {
        return roleStatistic.getRoleStatistics();
    }
}
