package elderlycare.Services;


import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import elderlycare.DAO.Entities.*;
import elderlycare.DAO.Repositories.*;
import elderlycare.dto.NurseReqRes;
import elderlycare.dto.ReqRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthService {


    @Autowired
    private OurUserRepo ourUserRepo;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
@Autowired
private  CalendarRepository calendarRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private RelativeRepository relativeRepository;
    @Autowired
    private ElderlyRepository elderlyRepository;
    @Autowired
    private AmbulanceOwnerRepository ambulanceOwnerRepository;
    @Autowired
    private AmbulanceDriverRepository ambulanceDriverRepository;

    @Autowired
    private  ArchiveUserRepository archiveUserRepository;

    @Autowired
    OurUserDetailsService userDetailsService;

    @Autowired
    private OurUserRepo userDao;

  /*  @Autowired
    private RestTemplate restTemplate; // Vous aurez besoin de cette dépendance pour appeler le service de géocodage
*/

    // Autres injections de dépendances...

    // Constants for Twilio API credentials
    private static final String ACCOUNT_SID = "AC87d1bb46078f04981d30e5b13b69a082";
    private static final String AUTH_TOKEN = "1c5e6d3a39d2493d23aa18bba27477b3";
    private static final String TWILIO_PHONE_NUMBER = "+14804053820";
    public ReqRes signUp(ReqRes registrationRequest){
        ReqRes resp = new ReqRes();
        try {
            OurUsers ourUsers = new OurUsers();

            // Vérifier si l'email existe déjà
            Optional<OurUsers> existingUser = ourUserRepo.findByEmail(registrationRequest.getEmail());
            if (existingUser.isPresent()) {
                resp.setStatusCode(333);
                resp.setMessage("Email already in use");
                return resp;
            }

            ourUsers.setEmail(registrationRequest.getEmail());
            ourUsers.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            ourUsers.setRole(registrationRequest.getRole());
            ourUsers.setPhoneNumber(registrationRequest.getPhoneNumber());
            ourUsers.setAddress(registrationRequest.getAddress());


            ourUsers.setArchive(false);

            // Vérification du rôle "doctor"
            if(registrationRequest.getRole().equals("doctor")) {
                // Création d'une entité Doctor et association avec l'utilisateur
                Doctor doctor = new Doctor();
                doctor.setUser(ourUsers);
                doctor.setFirstName(registrationRequest.getFirstName());
                doctor.setLastName(registrationRequest.getLastName());
                doctor.setGender(registrationRequest.getGender());
                doctor.setDateOfBirth(registrationRequest.getDateOfBirth());
                doctor.setAddress(registrationRequest.getAddress());
                doctor.setDoctorType(registrationRequest.getDoctorType());
                doctor.setSpecialization(registrationRequest.getSpecialization());
                doctor.setSchedule(registrationRequest.getSchedule());
                doctor.setUsername(registrationRequest.getUsername());
                doctor.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
                doctor.setPhoneNumber(registrationRequest.getPhoneNumber());
                doctor.setRole(registrationRequest.getRole());
                doctor.setEmail(registrationRequest.getEmail());
                doctor.setLanguage(registrationRequest.getLanguage());
                // Enregistrement de l'utilisateur et du médecin
                OurUsers ourUserResult = ourUserRepo.save(ourUsers);
                Doctor savedDoctor = doctorRepository.save(doctor);

                Calendar calendar = new Calendar();
                 calendar.setDoctor(savedDoctor);

                // Save the calendar entry
                Calendar savedCalendar = calendarRepository.save(calendar);




                // Vérification si l'utilisateur et le médecin sont sauvegardés avec succès
                if (ourUserResult != null && savedDoctor != null && ourUserResult.getId() > 0 && savedDoctor.getIdDoctor() > 0) {
                    resp.setOurUsers(ourUserResult);
                    resp.setMessage("User and Doctor saved Successfully");
                    resp.setStatusCode(200);
                }



            }

            // Vérification du rôle "nurse"
            else if(registrationRequest.getRole().equals("nurse")) {
                // Création d'une entité Nurse et association avec l'utilisateur
                Nurse nurse = new Nurse();
                nurse.setUser(ourUsers);
                nurse.setAddress(registrationRequest.getAddress());
                nurse.setFirstName(registrationRequest.getFirstName());
                nurse.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
                nurse.setLastName(registrationRequest.getLastName());
                nurse.setPhoneNumber(registrationRequest.getPhoneNumber());
                nurse.setResponsibilities(registrationRequest.getResponsibilities());
                nurse.setEmail(registrationRequest.getEmail());
                nurse.setDateOfBirth(registrationRequest.getDateOfBirth());
                nurse.setEmail(registrationRequest.getEmail());
                nurse.setGender(registrationRequest.getGender());
                nurse.setAddress(registrationRequest.getAddress());
                nurse.setRole(registrationRequest.getRole());







                // Vous pouvez ajouter d'autres attributs à l'infirmière ici si nécessaire

                // Enregistrement de l'utilisateur et de l'infirmière
                OurUsers ourUserResult = ourUserRepo.save(ourUsers);
                Nurse savedNurse = nurseRepository.save(nurse);

                // Vérification si l'utilisateur et l'infirmière sont sauvegardés avec succès
                if (ourUserResult != null && savedNurse != null && ourUserResult.getId() > 0 && savedNurse.getNurseID() > 0) {
                    resp.setOurUsers(ourUserResult);
                    resp.setMessage("User and Nurse saved Successfully");
                    resp.setStatusCode(200);
                }
            }

            // Vérification du rôle "ambulance driver"
            else if(registrationRequest.getRole().equals("ambulance-driver")) {
                // Création d'une entité AmBUlance Driver et association avec l'utilisateur
                AmbulanceDriver ambulanceDriver = new AmbulanceDriver();
                ambulanceDriver.setUser(ourUsers);
                ambulanceDriver.setAddress(registrationRequest.getAddress());
                ambulanceDriver.setFirstName(registrationRequest.getFirstName());
                ambulanceDriver.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
                ambulanceDriver.setLastName(registrationRequest.getLastName());
                ambulanceDriver.setPhoneNumber(registrationRequest.getPhoneNumber());
                ambulanceDriver.setEmail(registrationRequest.getEmail());
                ambulanceDriver.setDateOfBirth(registrationRequest.getDateOfBirth());
                ambulanceDriver.setRole(registrationRequest.getRole());
                ambulanceDriver.setGender(registrationRequest.getGender());
                ambulanceDriver.setDrivingExperienceYears(registrationRequest.getDrivingExperienceYears());
                ambulanceDriver.setAddress(registrationRequest.getAddress());




                // Vous pouvez ajouter d'autres attributs à l'infirmière ici si nécessaire

                // Enregistrement de l'utilisateur et de l'infirmière
                OurUsers ourUserResult = ourUserRepo.save(ourUsers);
                AmbulanceDriver savedAmbulanceDriver = ambulanceDriverRepository.save(ambulanceDriver);

                // Vérification si l'utilisateur et l'infirmière sont sauvegardés avec succès
                if (ourUserResult != null && savedAmbulanceDriver != null && ourUserResult.getId() > 0 && savedAmbulanceDriver.getAmbulanceDriverID() > 0) {
                    resp.setOurUsers(ourUserResult);
                    resp.setMessage("User and Ambulance Driver saved Successfully");
                    resp.setStatusCode(200);
                }
            }

            // Vérification du rôle "ambulance owner"
            else if(registrationRequest.getRole().equals("ambulance-owner")) {
                // Création d'une entité AmBUlance Driver et association avec l'utilisateur
                AmbulanceOwner ambulanceOwner = new AmbulanceOwner();
                ambulanceOwner.setUser(ourUsers);
                ambulanceOwner.setAddress(registrationRequest.getAddress());
                ambulanceOwner.setFirstName(registrationRequest.getFirstName());
                ambulanceOwner.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
                ambulanceOwner.setLastName(registrationRequest.getLastName());
                ambulanceOwner.setPhoneNumber(registrationRequest.getPhoneNumber());
                ambulanceOwner.setEmail(registrationRequest.getEmail());
                ambulanceOwner.setDateOfBirth(registrationRequest.getDateOfBirth());
                ambulanceOwner.setEmail(registrationRequest.getEmail());
                ambulanceOwner.setGender(registrationRequest.getGender());
                ambulanceOwner.setYearsofexperience(registrationRequest.getYearsofexperience());
                ambulanceOwner.setAddress(registrationRequest.getAddress());
                ambulanceOwner.setRole(registrationRequest.getRole());





                // Vous pouvez ajouter d'autres attributs à l'infirmière ici si nécessaire

                // Enregistrement de l'utilisateur et de l'infirmière
                OurUsers ourUserResult = ourUserRepo.save(ourUsers);
                AmbulanceOwner savedAmbulanceOwner = ambulanceOwnerRepository.save(ambulanceOwner);

                // Vérification si l'utilisateur et l'infirmière sont sauvegardés avec succès
                if (ourUserResult != null && savedAmbulanceOwner != null && ourUserResult.getId() > 0 && savedAmbulanceOwner.getAmbulanceOwnerID() > 0) {
                    resp.setOurUsers(ourUserResult);
                    resp.setMessage("User and Ambulance Owner saved Successfully");
                    resp.setStatusCode(200);
                }
            }

            // Vérification du rôle "relative"
            else if(registrationRequest.getRole().equals("relative")) {
                // Création d'une entité Relative et association avec l'utilisateur
                Relative relative = new Relative();
                relative.setUser(ourUsers);
                relative.setFirstName(registrationRequest.getFirstName());
                relative.setLastName(registrationRequest.getLastName());
                relative.setEmail(registrationRequest.getEmail());
                relative.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
                relative.setDateOfBirth(registrationRequest.getDateOfBirth());
                relative.setAddress(registrationRequest.getAddress());
                relative.setRelationship(registrationRequest.getRelationship());
                relative.setPhoneNumber(registrationRequest.getPhoneNumber());
                String elderlyEmail = registrationRequest.getElderlyEmail();
                relative.setRole(registrationRequest.getRole());

                Elderly elderly = elderlyRepository.findByEmail(elderlyEmail).orElse(null);

                if (elderly == null) {
                    resp.setStatusCode(404);
                    resp.setMessage("No Elderly found with the provided email");
                    return resp;
                } else {
                    relative.setElderly(elderly);
                }
                // Ajoutez d'autres attributs spécifiques à Relative ici si nécessaire

                // Enregistrement de l'utilisateur et du parent
                OurUsers ourUserResult = ourUserRepo.save(ourUsers);
                Relative savedRelative = relativeRepository.save(relative);

                // Vérification si l'utilisateur et le parent sont sauvegardés avec succès
                if (ourUserResult != null && savedRelative != null && ourUserResult.getId() > 0 && savedRelative.getIdRelative() > 0) {
                    resp.setOurUsers(ourUserResult);
                    resp.setMessage("User and Relative saved Successfully");
                    resp.setStatusCode(200);
                }
            }
            else if(registrationRequest.getRole().equals("elderly")) {
                // Création d'une entité Elderly et association avec l'utilisateur
                Elderly elderly = new Elderly();
                elderly.setUser(ourUsers);
                elderly.setFirstName(registrationRequest.getFirstName());
                elderly.setLastName(registrationRequest.getLastName());
                elderly.setEmail(registrationRequest.getEmail());
                elderly.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
                elderly.setDateOfBirth(registrationRequest.getDateOfBirth());
                elderly.setGender(registrationRequest.getGender());
                elderly.setAddress(registrationRequest.getAddress());

                elderly.setPreferences(registrationRequest.getPreferences());
                elderly.setPhoneNumber(registrationRequest.getPhoneNumber());
                elderly.setHealthRecord(registrationRequest.getHealthRecord());
                elderly.setAddress(registrationRequest.getAddress());


                // Fetch the list of nurses from the database
                List<Nurse> nurseList = nurseRepository.findAll();

                if (!nurseList.isEmpty()) {
                    Random random = new Random();
                    Nurse randomNurse = nurseList.get(random.nextInt(nurseList.size()));
                    elderly.setNurse(randomNurse);


                }
                List<Doctor> doctorList= doctorRepository.findAll();

                if(! doctorList.isEmpty()) {
                    Random random = new Random();
                    Doctor randomDoctor = doctorList.get(random.nextInt(doctorList.size()));
                    elderly.setDoctor(randomDoctor);
                    randomDoctor.getElderlyList().add(elderly);
                    doctorRepository.save(randomDoctor);
                }

                // Enregistrement de l'utilisateur et de l'ancien
                OurUsers ourUserResult = ourUserRepo.save(ourUsers);
                Elderly savedElderly = elderlyRepository.save(elderly);


                // Vérification si l'utilisateur et l'ancien sont sauvegardés avec succès
                if (ourUserResult != null && savedElderly != null && ourUserResult.getId() > 0 && savedElderly.getElderlyID() > 0) {
                    resp.setOurUsers(ourUserResult);
                    resp.setMessage("User and Elderly saved Successfully");
                    resp.setStatusCode(200);
                }
            }

            // Si le rôle n'est ni "doctor", ni "nurse", ni "relative", enregistrez simplement l'utilisateur
            else {
                OurUsers ourUserResult = ourUserRepo.save(ourUsers);
                if (ourUserResult != null && ourUserResult.getId() > 0) {
                    resp.setOurUsers(ourUserResult);
                    resp.setMessage("User Saved Successfully");
                    resp.setStatusCode(200);
                }
            }
// Appel de la méthode sendWelcomeSMS avec le numéro de téléphone de l'utilisateur
        /*    String phoneNumber = registrationRequest.getPhoneNumber(); // Récupérer le numéro de téléphone de l'objet registrationRequest
            String email = registrationRequest.getEmail(); // Récupérer l'email de l'objet registrationRequest
            String role = registrationRequest.getRole(); // Récupérer le rôle de l'objet registrationRequest

// Appel de la méthode sendWelcomeSMS avec les paramètres appropriés
            sendWelcomeSMS(phoneNumber, email, role);*/




        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;

    }

    public ReqRes signIn(ReqRes signinRequest) {

        try {

            Optional<OurUsers> userOptional = ourUserRepo.findByEmail(signinRequest.getEmail());
            if (userOptional.isPresent()) {
                OurUsers user = userOptional.get();
                // Vérifiez si l'utilisateur est archivé
                if (user.isArchive()) {
                    signinRequest.setStatusCode(403); // 403 Forbidden - L'utilisateur est archivé
                    signinRequest.setError("User is archived and cannot login.");
                    return signinRequest;
                }

                System.out.println("USER IS: " + user);
                signinRequest.setRole(user.getRole());
                signinRequest.setEmail(user.getEmail());
                signinRequest.setId(user.getId());

                // Ajoutez la récupération de l'ID spécifique en fonction du rôle de l'utilisateur
                switch (user.getRole()) {
                    case "elderly":
                        Optional<Elderly> elderlyOptional = elderlyRepository.findByUserId(user.getId());
                        if (elderlyOptional.isPresent()) {
                            signinRequest.setRoleId(elderlyOptional.get().getElderlyID());
                        }
                        break;
                    case "doctor":
                        Optional<Doctor> doctorOptional = doctorRepository.findByUserId(user.getId());
                        if (doctorOptional.isPresent()) {
                            signinRequest.setRoleId(doctorOptional.get().getIdDoctor());
                        }
                        break;
                    case "nurse":
                        Optional<Nurse> nurseOptional = nurseRepository.findByUserId(user.getId());
                        if (nurseOptional.isPresent()) {
                            signinRequest.setRoleId(nurseOptional.get().getNurseID());
                        }
                        break;
                    case "ambulance-driver":
                        Optional<AmbulanceDriver> ambulanceDriverOptional = ambulanceDriverRepository.findByUserId(user.getId());
                        if (ambulanceDriverOptional.isPresent()) {
                            signinRequest.setRoleId(ambulanceDriverOptional.get().getAmbulanceDriverID());
                        }
                        break;
                    case "ambulance-owner":
                        Optional<AmbulanceOwner> ambulanceOwnerOptional = ambulanceOwnerRepository.findByUserId(user.getId());
                        if (ambulanceOwnerOptional.isPresent()) {
                            signinRequest.setRoleId(ambulanceOwnerOptional.get().getAmbulanceOwnerID());
                        }
                        break;
                    case "relative":
                        Optional<Relative> relativeOptional = relativeRepository.findByUserId(user.getId());
                        if (relativeOptional.isPresent()) {
                            signinRequest.setRoleId(relativeOptional.get().getRelativeID());
                        }
                        break;
                    default:
                        // Traitement par défaut, peut-être une erreur ou une gestion spécifique
                        break;
                }



            } else {
                signinRequest.setStatusCode(404); // Or any other appropriate status code for "Not Found"
                signinRequest.setError("User with the provided email not found");
            }
        } catch (Exception e) {
            signinRequest.setStatusCode(500);
            signinRequest.setError(e.getMessage());
        }

        return signinRequest;
    }

    @Autowired
    private NurseRepository nurseRepository;

    public ReqRes signUpNurse(NurseReqRes registrationRequest) {
        ReqRes resp = new ReqRes();
        try {
            Nurse nurse = new Nurse();
            nurse.setFirstName(registrationRequest.getName());
            nurse.setEmail(registrationRequest.getEmail());
            nurse.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            Nurse nurseResult = nurseRepository.save(nurse);
            if (nurseResult != null && nurseResult.getNurseID() > 0) {
                resp.setMessage("Nurse Saved Successfully");
                resp.setStatusCode(200);
            }
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }
    public ReqRes refreshToken(ReqRes refreshTokenReqiest){
        ReqRes response = new ReqRes();
        String ourEmail = jwtUtils.extractUsername(refreshTokenReqiest.getToken());
        OurUsers users = ourUserRepo.findByEmail(ourEmail).orElseThrow();
        if (jwtUtils.isTokenValid(refreshTokenReqiest.getToken(), users)) {
            var jwt = jwtUtils.generateToken(users);
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRefreshToken(refreshTokenReqiest.getToken());
            response.setExpirationTime("24Hr");
            response.setMessage("Successfully Refreshed Token");
        }
        response.setStatusCode(500);
        return response;
    }


    /* public ResponseEntity<?> updateUser(ReqRes updatedUser, Integer id) {
         try {
             OurUsers oldUser = ourUserRepo.findById(id).orElse(null);
             Doctor doctor= doctorRepository.findByUser(oldUser);
             if (oldUser == null) {
                 return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with id: " + id);
             }
             if ("doctor".equals(oldUser.getRole())) {
                 doctor.setLastName(updatedUser.getLastName());
                 doctor.setFirstName(updatedUser.getFirstName());
                 doctor.setPhoneNumber(updatedUser.getPhoneNumber());
                 doctor.setGender(updatedUser.getGender());
                 doctor.setDateOfBirth(updatedUser.getDateOfBirth());

                 doctor.setDoctorType(updatedUser.getDoctorType());
                 doctor.setSpecialization(updatedUser.getSpecialization());
                 doctorRepository.save(doctor);

             }

             if (updatedUser.getEmail() != null) {
                 if (!Pattern.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", updatedUser.getEmail())) {
                     return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Please enter a valid email");
                 }
                 oldUser.setEmail(updatedUser.getEmail());
             }

             ourUserRepo.save(oldUser);


             return ResponseEntity.status(HttpStatus.OK).body(oldUser);
         } catch (Exception e) {
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating user: " + e.getMessage());
         }
     }*/
    @Transactional
    public ResponseEntity<?> deleteUser(Integer id) {
        Optional<OurUsers> user = ourUserRepo.findById(id);

        if (!user.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no user with this id: " + id);
        }
        ourUserRepo.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("User deleted");
    }



    public ResponseEntity<?> getDoctorById(Integer id) {
        try {
            Optional<OurUsers> userOptional = ourUserRepo.findById(id);

            if (userOptional.isPresent()) {
                OurUsers user = userOptional.get();

                // Vérifiez si l'utilisateur a le rôle "doctor"
                if (user.getRole().equals("doctor")) {
                    Doctor doctor = doctorRepository.findByUser(user);

                    if (doctor != null) {

                        return ResponseEntity.status(HttpStatus.OK).body(doctor);
                    } else {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found with id: " + id);
                    }
                } else {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied. User is not a doctor.");
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with id: " + id);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error getting doctor: " + e.getMessage());
        }
    }



    public OurUsers ResetPassword(String Email,String password) {
        Optional<OurUsers> optionalOurUsers = ourUserRepo.findByEmail(Email);
        if (optionalOurUsers.isPresent()) {
            OurUsers ourUsers = optionalOurUsers.get();
            ourUsers.setPassword(passwordEncoder.encode(password));
            return ourUserRepo.save(ourUsers);
        } else {
            // Handle the case where no user is found with the given email
            // You can throw an exception, return null, or handle it differently based on your requirements
            return null;
        }

    }
    public OurUsers ResetPasswordPhone(String PhoneNumber, String password) {
        OurUsers ourUsers = ourUserRepo.findByPhoneNumber(PhoneNumber);
        ourUsers.setPassword(passwordEncoder.encode(password));
        return ourUserRepo.save(ourUsers);

    }


    // Méthode pour envoyer un SMS de bienvenue
    public void sendWelcomeSMS(String phoneNumber, String email, String role) {
        try {
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

            Message message = Message.creator(
                    new PhoneNumber(phoneNumber),  // Destinataire
                    new PhoneNumber(TWILIO_PHONE_NUMBER),  // Expéditeur
                    "Welcome to our service! Your account has been successfully created with email: " + email + " and role: " + role
            ).create();

            // Log pour indiquer que le SMS a été envoyé avec succès
            System.out.println("SMS sent successfully. Message SID: " + message.getSid());
        } catch (Exception e) {
            // Log pour indiquer que l'envoi du SMS a échoué
            System.err.println("Failed to send SMS: " + e.getMessage());
            e.printStackTrace();
        }
    }


}
