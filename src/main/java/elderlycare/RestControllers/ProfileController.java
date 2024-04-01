package elderlycare.RestControllers;

import elderlycare.Services.ProfileService;
import elderlycare.dto.ReqRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {
    @Autowired
    private ProfileService profileService;



    @Autowired
    private ProfileService userProfileService;

    @GetMapping("/{userId}")
    public ResponseEntity<ReqRes> getUserProfile(@PathVariable Long userId) {
        ReqRes userProfile = userProfileService.getUserProfile(userId);
        if (userProfile != null) {
            return ResponseEntity.ok(userProfile);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


   /* @PutMapping("/{userId}")
    public ResponseEntity<ReqRes> updateUserProfile(@PathVariable Long userId, @RequestBody ReqRes updatedProfile) {
        ReqRes userProfile = profileService.updateUserProfile(userId, updatedProfile);
        if (userProfile != null) {
            return new ResponseEntity<>(userProfile, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }*/


  /*  @PutMapping({"/update/{id}"})
    public ResponseEntity<?> updateUser(@RequestBody ReqRes user,@PathVariable("id") Integer id)
    {
        return profileService.updateUser(user,id);
    }*/



    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUserProfile(@RequestBody ReqRes updatedUser, @PathVariable("userId") Long userId) {
        ResponseEntity<?> responseEntity = profileService.updateUser(updatedUser, userId.intValue());
        return responseEntity;
    }
}

