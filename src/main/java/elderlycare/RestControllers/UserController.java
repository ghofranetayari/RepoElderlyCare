package elderlycare.RestControllers;

import elderlycare.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
