package elderlycare.RestControllers;


import elderlycare.DAO.Entities.*;
import elderlycare.DAO.Repositories.*;
import elderlycare.Services.AuthService;
import elderlycare.Services.OurUserDetailsService;
import elderlycare.Services.UserService;
import elderlycare.dto.NurseReqRes;
import elderlycare.dto.ReqRes;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:4200")
@PermitAll

@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;
@Autowired
private RelativeRepository relativeRepository;
    @Autowired
    private OurUserDetailsService userService;


    @Autowired
    private UserService userServiceM;


    @Autowired
    ArchiveUserRepository archiveUserRepository;
    @Autowired
    DoctorRepository doctorRepository;
    @Autowired
    ElderlyRepository elderlyRepository;
    @Autowired
    AmbulanceDriverRepository ambulanceDriverRepository;
    @Autowired
    AmbulanceOwnerRepository ambulanceOwnerRepository;
    @Autowired
    NurseRepository nurseRepository;
    @Autowired
    OurUserRepo ourUserRepo;


    @PostMapping("/signup")
    public ResponseEntity<ReqRes> signUp(@RequestBody ReqRes signUpRequest) {
        return ResponseEntity.ok(authService.signUp(signUpRequest));
    }

    @PostMapping("/signup/nurse")
    public ResponseEntity<ReqRes> signUpNurse(@RequestBody NurseReqRes signUpRequest) {
        return ResponseEntity.ok(authService.signUpNurse(signUpRequest));
    }

    @PostMapping("/signin")
    public ResponseEntity<ReqRes> signIn(@RequestBody ReqRes signInRequest) {
        return ResponseEntity.ok(authService.signIn(signInRequest));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ReqRes> refreshToken(@RequestBody ReqRes refreshTokenRequest) {
        return ResponseEntity.ok(authService.refreshToken(refreshTokenRequest));
    }
    @PutMapping("/updateRelative/{relativeId}")

    public ResponseEntity<?> updateRelative(@RequestBody Relative relativeData, @PathVariable Long relativeId) {
        try {
            Relative existingRelative = relativeRepository.findById(relativeId)
                    .orElseThrow(() -> new Exception("Relative not found"));

            // Mettez à jour les champs du elderly avec les données fournies
            existingRelative.setFirstName(relativeData.getFirstName());
            existingRelative.setLastName(relativeData.getLastName());
            existingRelative.setPhoneNumber(relativeData.getPhoneNumber());
            existingRelative.setEmail(relativeData.getEmail());
            existingRelative.setRelationship(relativeData.getRelationship());
            existingRelative.setGender(relativeData.getGender());

            // Ajoutez les autres champs à mettre à jour selon vos besoins

            Relative updatedRelative = relativeRepository.save(existingRelative);
            return ResponseEntity.ok(updatedRelative);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating Relative: " + e.getMessage());
        }
    }
/*    @PutMapping({"/update/{id}"})
    public ResponseEntity<?> updateUser(@RequestBody ReqRes user,@PathVariable("id") Integer id)
    {
        return authService.updateUser(user,id);
    } */
/*
    @DeleteMapping({"/delete/{id}"})
    @PreAuthorize("hasRole('HeadSupervisor') ")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Integer id)
    {
        return authService.deleteUser(id);
    }*/


    @GetMapping("/all")
    public ResponseEntity<List<OurUsers>> getAllUsers() {
        List<OurUsers> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    //k beha k blech
    @GetMapping("listArchive")
    public List<ArchiveUser> getAllArchivedUsers() {
        return archiveUserRepository.findAll();
    }



   /* @DeleteMapping("/deleteDoctor/{doctorId}")
    public ResponseEntity<String> deleteDoctor(@PathVariable Long doctorId) {
        try {
            doctorRepository.deleteById(doctorId);
            return ResponseEntity.ok("Doctor deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting doctor: " + e.getMessage());
        }
    }*/

    @GetMapping("/doctors/{doctorId}")
    public ResponseEntity<?> getDoctorById(@PathVariable Integer doctorId) {
        try {
            ResponseEntity<?> response = authService.getDoctorById(doctorId);
            return response;
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error getting doctor: " + e.getMessage());
        }
    }


    //shiha tebaa back
    @PutMapping("/updateDoctor/{doctorId}")

    public ResponseEntity<?> updateDoctor(@RequestBody Doctor doctorData, @PathVariable Long doctorId) {
        try {
            Doctor existingDoctor = doctorRepository.findById(doctorId)
                    .orElseThrow(() -> new Exception("Doctor not found"));

            // Mettez à jour les champs du médecin avec les données fournies
            existingDoctor.setFirstName(doctorData.getFirstName());
            existingDoctor.setLastName(doctorData.getLastName());
            existingDoctor.setPhoneNumber(doctorData.getPhoneNumber());
            existingDoctor.setEmail(doctorData.getEmail());
            existingDoctor.setSchedule(doctorData.getSchedule());
            existingDoctor.setSpecialization(doctorData.getSpecialization());
            existingDoctor.setDoctorType(doctorData.getDoctorType());

            existingDoctor.setGender(doctorData.getGender());

            // Ajoutez les autres champs à mettre à jour selon vos besoins

            Doctor updatedDoctor = doctorRepository.save(existingDoctor);
            return ResponseEntity.ok(updatedDoctor);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating doctor: " + e.getMessage());
        }
    }

    //shiha
    @GetMapping("/users/search")
    public ResponseEntity<List<OurUsers>> searchUsersByEmail(@RequestParam String email) {
        List<OurUsers> users = ourUserRepo.findByEmailContaining(email);
        return ResponseEntity.ok(users);
    }
    @GetMapping("listRelative")
    public List<Relative> getAllRelatives()
    {
        return  relativeRepository.findAll();
    }
    //
    @GetMapping("/searchByRole")
    public List<OurUsers> searchByRole(@RequestParam("role") String role) {
        return ourUserRepo.findByRole(role);
    }

    @PermitAll
    @GetMapping("listDoctor")
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    @GetMapping("listElderly")
    public List<Elderly> getAllElderlys() {
        return elderlyRepository.findAll();
    }

    @GetMapping("listAmbulanceDriver")
    public List<AmbulanceDriver> getAllAmbulanceDrivers() {
        return ambulanceDriverRepository.findAll();
    }

    @GetMapping("listAmbulanceOwner")
    public List<AmbulanceOwner> getAllAmbulanceOwners() {
        return ambulanceOwnerRepository.findAll();
    }

    @GetMapping("listNurse")
    public List<Nurse> getAllNurses() {
        return nurseRepository.findAll();
    }





  /*  @PutMapping("/resetPasswordPhone")
    public OurUsers resetPasswordPhone(@RequestBody ResetPasswordRequest request) {
        return authService.ResetPasswordPhone(request.getPhoneNumber(), request.getPassword());

    }*/


    @PutMapping("/updateElderly/{elderlyId}")

    public ResponseEntity<?> updateElderly(@RequestBody Elderly elderlyData, @PathVariable Long elderlyId) {
        try {
            Elderly existingElderly = elderlyRepository.findById(elderlyId)
                    .orElseThrow(() -> new Exception("Elderly not found"));

            // Mettez à jour les champs du elderly avec les données fournies
            existingElderly.setFirstName(elderlyData.getFirstName());
            existingElderly.setLastName(elderlyData.getLastName());
            existingElderly.setPhoneNumber(elderlyData.getPhoneNumber());
            //existingDoctor.setEmail(doctorData.getEmail());
            existingElderly.setPreferences(elderlyData.getPreferences());
            existingElderly.setHealthRecord(elderlyData.getHealthRecord());
            existingElderly.setGender(elderlyData.getGender());

            // Ajoutez les autres champs à mettre à jour selon vos besoins

            Elderly updatedElderly = elderlyRepository.save(existingElderly);
            return ResponseEntity.ok(updatedElderly);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating elderly: " + e.getMessage());
        }
    }

    @PutMapping("/updateNurse/{nurseId}")

    public ResponseEntity<?> updateNurse(@RequestBody Nurse nurseData, @PathVariable Long nurseId) {
        try {
            Nurse existingNurse = nurseRepository.findById(nurseId)
                    .orElseThrow(() -> new Exception("Nurse not found"));

            // Mettez à jour les champs du elderly avec les données fournies
            existingNurse.setFirstName(nurseData.getFirstName());
            existingNurse.setLastName(nurseData.getLastName());
            existingNurse.setPhoneNumber(nurseData.getPhoneNumber());
            //existingDoctor.setEmail(doctorData.getEmail());
            existingNurse.setResponsibilities(nurseData.getResponsibilities());

            existingNurse.setGender(nurseData.getGender());

            // Ajoutez les autres champs à mettre à jour selon vos besoins

            Nurse updatedNurse = nurseRepository.save(existingNurse);
            return ResponseEntity.ok(updatedNurse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating nurse: " + e.getMessage());
        }
    }

    @PutMapping("/updateAmbulanceDriver/{ambulanceDriverId}")

    public ResponseEntity<?> updateAmbulanceDriver(@RequestBody AmbulanceDriver ambulanceDriverData, @PathVariable Long ambulanceDriverId) {
        try {
            AmbulanceDriver existingAmbulanceDriver = ambulanceDriverRepository.findById(ambulanceDriverId)
                    .orElseThrow(() -> new Exception("AmbulanceDriver not found"));

            // Mettez à jour les champs du AmbulanceDriver avec les données fournies
            existingAmbulanceDriver.setFirstName(ambulanceDriverData.getFirstName());
            existingAmbulanceDriver.setLastName(ambulanceDriverData.getLastName());
            existingAmbulanceDriver.setPhoneNumber(ambulanceDriverData.getPhoneNumber());
            existingAmbulanceDriver.setEmail(ambulanceDriverData.getEmail());
            existingAmbulanceDriver.setDrivingExperienceYears(ambulanceDriverData.getDrivingExperienceYears());
            existingAmbulanceDriver.setOnDuty(ambulanceDriverData.getOnDuty());
            existingAmbulanceDriver.setDateOfBirth(ambulanceDriverData.getDateOfBirth());

            existingAmbulanceDriver.setGender(ambulanceDriverData.getGender());

            // Ajoutez les autres champs à mettre à jour selon vos besoins

            AmbulanceDriver updatedAmbulanceDriver = ambulanceDriverRepository.save(existingAmbulanceDriver);
            return ResponseEntity.ok(updatedAmbulanceDriver);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating ambulanceDriver: " + e.getMessage());
        }

    }


    @PutMapping("/updateAmbulanceOwner/{ambulanceOwnerId}")

    public ResponseEntity<?> updateAmbulanceOwner(@RequestBody AmbulanceOwner ambulanceOwnerData, @PathVariable Long ambulanceOwnerId) {
        try {
            AmbulanceOwner existingAmbulanceOwner = ambulanceOwnerRepository.findById(ambulanceOwnerId)
                    .orElseThrow(() -> new Exception("AmbulanceOwner not found"));

            // Mettez à jour les champs du AmbulanceDriver avec les données fournies
            existingAmbulanceOwner.setFirstName(ambulanceOwnerData.getFirstName());
            existingAmbulanceOwner.setLastName(ambulanceOwnerData.getLastName());
            existingAmbulanceOwner.setPhoneNumber(ambulanceOwnerData.getPhoneNumber());
            existingAmbulanceOwner.setEmail(ambulanceOwnerData.getEmail());
            existingAmbulanceOwner.setYearsofexperience(ambulanceOwnerData.getYearsofexperience());
            existingAmbulanceOwner.setDateOfBirth(ambulanceOwnerData.getDateOfBirth());

            existingAmbulanceOwner.setGender(ambulanceOwnerData.getGender());

            // Ajoutez les autres champs à mettre à jour selon vos besoins

            AmbulanceOwner updatedAmbulanceOwner = ambulanceOwnerRepository.save(existingAmbulanceOwner);
            return ResponseEntity.ok(updatedAmbulanceOwner);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating ambulanceOwner: " + e.getMessage());
        }

    }





}








