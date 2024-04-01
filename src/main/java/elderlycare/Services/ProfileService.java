package elderlycare.Services;

import elderlycare.DAO.Entities.*;
import elderlycare.DAO.Repositories.*;
import elderlycare.dto.ReqRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class ProfileService {
    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private ElderlyRepository elderlyRepository;
    @Autowired
    private NurseRepository nurseRepository;
    @Autowired
    private OurUserRepo ourUserRepo;
    @Autowired
    private AmbulanceDriverRepository ambulanceDriverRepository;
    @Autowired
    private AmbulanceOwnerRepository ambulanceOwnerRepository;
@Autowired
private  RelativeRepository relativeRepository;
    public ReqRes getUserProfile(Long userId) {
        Doctor doctor = doctorRepository.findByUser_Id(userId);
        if (doctor != null) {
            return mapDoctorToReqRes(doctor);
        }

        Elderly elderly = elderlyRepository.findByUser_Id(userId);
        if (elderly != null) {
            return mapElderlyToReqRes(elderly);
        }
        Nurse nurse = nurseRepository.findByUser_Id(userId);
        if (nurse != null) {
            return mapNurseToReqRes(nurse);
        }
        AmbulanceDriver ambulanceDriver = ambulanceDriverRepository.findByUser_Id(userId);
        if (ambulanceDriver != null) {
            return mapAmbulanceDriverToReqRes(ambulanceDriver);
        }
        AmbulanceOwner ambulanceOwner = ambulanceOwnerRepository.findByUser_Id(userId);
        if (ambulanceOwner != null) {
            return mapAmbulanceOwnerToReqRes(ambulanceOwner);
        }
        Relative relative = relativeRepository.findByUser_Id(userId);
        if (relative != null) {
            return mapRelativeToReqRes(relative);
        }

        return null;
    }
    private ReqRes mapRelativeToReqRes(Relative relative) {
        ReqRes reqRes = new ReqRes();
        reqRes.setId(relative.getUser().getId());
        reqRes.setEmail(relative.getEmail());
        reqRes.setRole(relative.getRole());
        reqRes.setFirstName(relative.getFirstName());
        reqRes.setLastName(relative.getLastName());
        reqRes.setDateOfBirth(relative.getDateOfBirth());
        reqRes.setPhoneNumber(relative.getPhoneNumber());
        reqRes.setRelationship(relative.getRelationship());
        reqRes.setGender(relative.getGender());
        return reqRes;
    }
    private ReqRes mapDoctorToReqRes(Doctor doctor) {
        ReqRes reqRes = new ReqRes();
        reqRes.setId(doctor.getUser().getId());
        reqRes.setEmail(doctor.getEmail());
        reqRes.setRole(doctor.getRole());
        reqRes.setDoctorType(doctor.getDoctorType());
        reqRes.setFirstName(doctor.getFirstName());
        reqRes.setLastName(doctor.getLastName());
        reqRes.setDateOfBirth(doctor.getDateOfBirth());
        reqRes.setPhoneNumber(doctor.getPhoneNumber());
        reqRes.setGender(doctor.getGender());
        reqRes.setSpecialization(doctor.getSpecialization());
        reqRes.setSchedule(doctor.getSchedule());

        // Récupérer la liste des personnes âgées associées
        List<Elderly> elderlyList = doctor.getElderlyList();
        if (elderlyList != null && !elderlyList.isEmpty()) {
            List<Long> elderlyIds = new ArrayList<>();
            List<String> elderlyEmails = new ArrayList<>(); // Liste pour stocker les emails des personnes âgées
            for (Elderly elderly : elderlyList) {
                elderlyIds.add(elderly.getElderlyID());
                elderlyEmails.add(elderly.getEmail()); // Ajouter l'email à la liste
            }
            reqRes.setElderlyIds(elderlyIds);
            reqRes.setElderlyEmails(elderlyEmails); // Setter la liste d'emails dans ReqRes
        }


        // Mapping d'autres attributs spécifiques au médecin
        return reqRes;
    }

    private ReqRes mapElderlyToReqRes(Elderly elderly) {
        ReqRes reqRes = new ReqRes();
        reqRes.setId(elderly.getUser().getId());
        reqRes.setEmail(elderly.getEmail());
        reqRes.setRole(elderly.getRole());
        reqRes.setFirstName(elderly.getFirstName());
        reqRes.setLastName(elderly.getLastName());
        reqRes.setDateOfBirth(elderly.getDateOfBirth());
        reqRes.setPhoneNumber(elderly.getPhoneNumber());
        reqRes.setPreferences(elderly.getPreferences());
        reqRes.setHealthRecord(elderly.getHealthRecord());
        reqRes.setGender(elderly.getGender());
        return reqRes;
    }


    private ReqRes mapNurseToReqRes(Nurse nurse) {
        ReqRes reqRes = new ReqRes();
        reqRes.setId(nurse.getUser().getId());
        reqRes.setEmail(nurse.getEmail());
        reqRes.setRole(nurse.getRole());
        reqRes.setFirstName(nurse.getFirstName());
        reqRes.setLastName(nurse.getLastName());
        reqRes.setDateOfBirth(nurse.getDateOfBirth());
        reqRes.setPhoneNumber(nurse.getPhoneNumber());
        reqRes.setGender(nurse.getGender());
        reqRes.setResponsibilities(nurse.getResponsibilities());


        // Mapping d'autres attributs spécifiques à la personne âgée
        return reqRes;
    }

    private ReqRes mapAmbulanceDriverToReqRes(AmbulanceDriver ambulanceDriver) {
        ReqRes reqRes = new ReqRes();
        reqRes.setId(ambulanceDriver.getUser().getId());
        reqRes.setEmail(ambulanceDriver.getEmail());
        reqRes.setRole(ambulanceDriver.getRole());
        reqRes.setFirstName(ambulanceDriver.getFirstName());
        reqRes.setLastName(ambulanceDriver.getLastName());
        reqRes.setDateOfBirth(ambulanceDriver.getDateOfBirth());
        reqRes.setPhoneNumber(ambulanceDriver.getPhoneNumber());
        reqRes.setGender(ambulanceDriver.getGender());
        reqRes.setOnDuty(ambulanceDriver.getOnDuty());
        reqRes.setDrivingExperienceYears(ambulanceDriver.getDrivingExperienceYears());


        // Mapping d'autres attributs spécifiques à la personne âgée
        return reqRes;
    }

    private ReqRes mapAmbulanceOwnerToReqRes(AmbulanceOwner ambulanceOwner) {
        ReqRes reqRes = new ReqRes();
        reqRes.setId(ambulanceOwner.getUser().getId());
        reqRes.setEmail(ambulanceOwner.getEmail());
        reqRes.setRole(ambulanceOwner.getRole());
        reqRes.setFirstName(ambulanceOwner.getFirstName());
        reqRes.setLastName(ambulanceOwner.getLastName());
        reqRes.setDateOfBirth(ambulanceOwner.getDateOfBirth());
        reqRes.setPhoneNumber(ambulanceOwner.getPhoneNumber());
        reqRes.setGender(ambulanceOwner.getGender());
        reqRes.setYearsofexperience(ambulanceOwner.getYearsofexperience());

        return reqRes;
    }


    public ResponseEntity<?> updateUser(ReqRes updatedUser, Integer id) {
        try {
            OurUsers oldUser = ourUserRepo.findById(id).orElse(null);
            if (oldUser == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with id: " + id);
            }
            System.out.println("User is : "+oldUser);
            if ("doctor".equals(oldUser.getRole())) {
                Doctor doctor = doctorRepository.findByUser(oldUser);
                if (doctor != null) {
                    doctor.setLastName(updatedUser.getLastName());
                    doctor.setFirstName(updatedUser.getFirstName());
                    doctor.setPhoneNumber(updatedUser.getPhoneNumber());
                    doctor.setGender(updatedUser.getGender());
                    doctor.setDateOfBirth(updatedUser.getDateOfBirth());
                    doctor.setDoctorType(updatedUser.getDoctorType());
                    doctor.setSpecialization(updatedUser.getSpecialization());
                    doctor.setAddress(updatedUser.getAddress());
                    doctor.setEmail(updatedUser.getEmail());

                    doctorRepository.save(doctor);
                }
            } else if ("elderly".equals(oldUser.getRole())) {
                Elderly elderly = elderlyRepository.findByUser(oldUser);
                if (elderly != null) {
                    elderly.setLastName(updatedUser.getLastName());
                    elderly.setFirstName(updatedUser.getFirstName());
                    elderly.setPhoneNumber(updatedUser.getPhoneNumber());
                    elderly.setGender(updatedUser.getGender());
                    elderly.setDateOfBirth(updatedUser.getDateOfBirth());
                    elderly.setPreferences(updatedUser.getPreferences());
                    elderly.setHealthRecord(updatedUser.getHealthRecord());
                    elderly.setAddress(updatedUser.getAddress());
                    elderly.setEmail(updatedUser.getEmail());


                    elderlyRepository.save(elderly);
                }
            } else if ("nurse".equals(oldUser.getRole())) {
                Nurse nurse = nurseRepository.findByUser(oldUser);
                if (nurse != null) {
                    nurse.setLastName(updatedUser.getLastName());
                    nurse.setFirstName(updatedUser.getFirstName());
                    nurse.setPhoneNumber(updatedUser.getPhoneNumber());
                    nurse.setGender(updatedUser.getGender());
                    nurse.setDateOfBirth(updatedUser.getDateOfBirth());
                    nurse.setResponsibilities(updatedUser.getResponsibilities());
                    nurse.setAddress(updatedUser.getAddress());
                    nurse.setEmail(updatedUser.getEmail());
                    nurseRepository.save(nurse);
                }
            } else if ("ambulance-driver".equals(oldUser.getRole())) {
                AmbulanceDriver ambulanceDriver = ambulanceDriverRepository.findByUser(oldUser);
                if (ambulanceDriver != null) {
                    ambulanceDriver.setLastName(updatedUser.getLastName());
                    ambulanceDriver.setFirstName(updatedUser.getFirstName());
                    ambulanceDriver.setPhoneNumber(updatedUser.getPhoneNumber());
                    ambulanceDriver.setGender(updatedUser.getGender());
                    ambulanceDriver.setDateOfBirth(updatedUser.getDateOfBirth());
                    ambulanceDriver.setOnDuty(updatedUser.isOnDuty());
                    ambulanceDriver.setDrivingExperienceYears(updatedUser.getDrivingExperienceYears());

                    ambulanceDriver.setAddress(updatedUser.getAddress());
                    ambulanceDriver.setEmail(updatedUser.getEmail());
                    ambulanceDriverRepository.save(ambulanceDriver);
                }
            } else if ("ambulance-owner".equals(oldUser.getRole())) {
                AmbulanceOwner ambulanceOwner = ambulanceOwnerRepository.findByUser(oldUser);
                if (ambulanceOwner != null) {
                    ambulanceOwner.setLastName(updatedUser.getLastName());
                    ambulanceOwner.setFirstName(updatedUser.getFirstName());
                    ambulanceOwner.setPhoneNumber(updatedUser.getPhoneNumber());
                    ambulanceOwner.setGender(updatedUser.getGender());
                    ambulanceOwner.setDateOfBirth(updatedUser.getDateOfBirth());
                    ambulanceOwner.setYearsofexperience(updatedUser.getYearsofexperience());

                    ambulanceOwner.setAddress(updatedUser.getAddress());
                    ambulanceOwner.setEmail(updatedUser.getEmail());
                    ambulanceOwnerRepository.save(ambulanceOwner);}
            }
          else if ("relative".equals(oldUser.getRole())) {
            Relative relative = relativeRepository.findByUser(oldUser);
            if (relative != null) {
                relative.setLastName(updatedUser.getLastName());
                relative.setFirstName(updatedUser.getFirstName());
                relative.setPhoneNumber(updatedUser.getPhoneNumber());
                relative.setGender(updatedUser.getGender());
                relative.setDateOfBirth(updatedUser.getDateOfBirth());
                relative.setRelationship(updatedUser.getRelationship());

                relative.setAddress(updatedUser.getAddress());
                relative.setEmail(updatedUser.getEmail());
                relativeRepository.save(relative);}
        }


        // Mettez à jour l'e-mail s'il est fourni et valide
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
    }


}

