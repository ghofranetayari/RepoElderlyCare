package elderlycare.RestControllers;

import elderlycare.Services.UserService;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @DeleteMapping("DeleteUser/{id}")
    public void DeleteUser(@PathVariable("id") Integer id){
        userService.ArchiveUser(id);
    }

    @DeleteMapping("ActivateUser/{id}")
    public void ActivateUser(@PathVariable("id") Integer id){
        userService.ActivateUser(id);
    }
}
