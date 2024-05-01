package elderlycare.Services;

import com.google.maps.errors.NotFoundException;
import elderlycare.DAO.Entities.Complaint;
import elderlycare.DAO.Entities.Doctor;
import elderlycare.DAO.Entities.Elderly;
import elderlycare.DAO.Entities.Relative;
import elderlycare.DAO.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ComplaintService {

    private final ComplaintRepository complaintRepository;
    @Autowired
    ElderlyRepository elderlyRepository;
    @Autowired
    OurUserRepo ourUserRepo;
    @Autowired
    DoctorRepository doctorRepository;
    @Autowired
    RelativeRepository relativeRepository;


    @Autowired
    public ComplaintService(ComplaintRepository complaintRepository) {
        this.complaintRepository = complaintRepository;
    }

    public List<Complaint> getAllComplaints() {
        return complaintRepository.findAll();
    }


    public Complaint createComplaintForElderly(Complaint complaint, Long elderlyId) {
        Optional<Elderly> elderlyOptional = elderlyRepository.findById(elderlyId);

        if (elderlyOptional.isPresent()) {
            Elderly elderly = elderlyOptional.get();
            complaint.setElderly(elderly);


            complaint.setElderly(elderly);

            return complaintRepository.save(complaint);
        } else {
            // Gérer le cas où l'utilisateur ou la personne âgée n'existe pas
            return null;
        }
    }

    /*  public Complaint createComplaintForRelative(Complaint complaint, Long relativeId) {
          Optional<Relative> relativeOptional = relativeRepository.findById(relativeId);

          if (relativeOptional.isPresent()) {
              Relative relative = relativeOptional.get();
              complaint.setRelative(relative);


              complaint.setRelative(relative);

              return complaintRepository.save(complaint);
          } else {
              // Gérer le cas où l'utilisateur ou la personne âgée n'existe pas
              return null;
          }
      }
  */
    public List<Complaint> getComplaintsByElderlyId(Long elderlyId) {
        return complaintRepository.findByElderlyElderlyID(elderlyId);
    }

    public List<Complaint> getComplaintsByRelativeId(Long idRelative) {
        return complaintRepository.findByRelativeIdRelative(idRelative);
    }

    /* public Complaint updateComplaint(Long id, Complaint updatedComplaint) {
         Complaint existingComplaint = complaintRepository.findById(id).orElse(null);
         if (existingComplaint != null) {
             existingComplaint.setDescription(updatedComplaint.getDescription());
             existingComplaint.setCategory(updatedComplaint.getCategory());
             existingComplaint.setCreationDate(updatedComplaint.getCreationDate());
             existingComplaint.setPriority(updatedComplaint.getPriority());
             existingComplaint.setAssigneA(updatedComplaint.getAssigneA());
             existingComplaint.setClosingDate(updatedComplaint.getClosingDate());
             existingComplaint.setInternalNotes(updatedComplaint.getInternalNotes());

             // Mettez à jour d'autres champs si nécessaire
             return complaintRepository.save(existingComplaint);
         }
         return null;
     }
 */
    public void deleteComplaint(Long id) {
        complaintRepository.deleteById(id);
    }


    public void archiveComplaint(Long id) {
        Complaint complaint = complaintRepository.findById(id).orElse(null);
        if (complaint != null) {
            complaint.setArchived(true);
            complaintRepository.save(complaint);
        }
    }

    public String getElderlyEmailByRelativeId(Long relativeId) {
        Relative relative = relativeRepository.findById(relativeId)
                .orElseThrow(() -> new RuntimeException("Relative not found with id: " + relativeId));

        return relative.getElderly().getEmail();
    }

    public String getDoctorEmailByElderlyEmail(String elderlyEmail) {
        Elderly elderly = elderlyRepository.findByEmail(elderlyEmail)
                .orElseThrow(() -> new RuntimeException("Elderly not found with email: " + elderlyEmail));

        return elderly.getDoctor().getEmail();
    }

    public Complaint updateComplaintForRelative(Long complaintId, Long relativeId, Complaint updatedComplaint) {
        Optional<Complaint> complaintOptional = complaintRepository.findById(complaintId);
        Optional<Relative> relativeOptional = relativeRepository.findById(relativeId);

        if (complaintOptional.isPresent() && relativeOptional.isPresent()) {
            Complaint existingComplaint = complaintOptional.get();
            Relative relative = relativeOptional.get();

            if (existingComplaint.getRelative().getIdRelative() == relativeId) {
                existingComplaint.setDescription(updatedComplaint.getDescription());
                //existingComplaint.setCategory(updatedComplaint.getCategory());
                existingComplaint.setPriority(updatedComplaint.getPriority());
                existingComplaint.setAssigneA(updatedComplaint.getAssigneA());
                existingComplaint.setClosingDate(updatedComplaint.getClosingDate());
                existingComplaint.setInternalNotes(updatedComplaint.getInternalNotes());

                return complaintRepository.save(existingComplaint);
            } else {
                // Le relative n'est pas lié à cette plainte
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le relative fourni n'est pas lié à cette plainte.");
            }
        } else {
            // La plainte ou le relative n'existe pas
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La plainte ou le relative n'a pas été trouvé.");
        }
    }

    public Complaint updateComplaint(Long complaintId, Complaint updatedComplaint) {
        // Récupérer la plainte existante à partir de son ID
        Optional<Complaint> complaintOptional = complaintRepository.findById(complaintId);

        // Vérifier si la plainte existe
        if (complaintOptional.isPresent()) {
            // Récupérer la plainte existante depuis l'Optional
            Complaint existingComplaint = complaintOptional.get();


            // Mettre à jour les champs de la plainte existante avec les valeurs de la plainte mise à jour
            existingComplaint.setDescription(updatedComplaint.getDescription());
            existingComplaint.setPriority(updatedComplaint.getPriority());
            existingComplaint.setAssigneA(updatedComplaint.getAssigneA());
            existingComplaint.setClosingDate(updatedComplaint.getClosingDate());
            existingComplaint.setInternalNotes(updatedComplaint.getInternalNotes());
            existingComplaint.setNotes(updatedComplaint.getNotes());

            // Enregistrer la plainte mise à jour dans la base de données et la retourner
            return complaintRepository.save(existingComplaint);
        } else {
            // Si la plainte n'existe pas, lancer une exception appropriée ou retourner null selon votre logique
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La plainte avec l'ID " + complaintId + " n'a pas été trouvée.");
        }
    }

    public List<Complaint> getRelativeComplaintsForDoctor(Long elderlyId, String doctorEmail) {
        // Retrieve the elderly entity by ID
        Optional<Elderly> elderlyOptional = elderlyRepository.findById(elderlyId);
        if (elderlyOptional.isPresent()) {
            Elderly elderly = elderlyOptional.get();

            // Retrieve the doctor entity by email
            Doctor doctor = doctorRepository.findByEmail(doctorEmail)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Doctor not found with email: " + doctorEmail));

            // Check if the doctor is associated with the elderly
            if (elderly.getDoctor().equals(doctor)) {
                // Retrieve the complaints associated with the relative of the elderly
                return complaintRepository.findByRelativeIdRelative(elderly.getRelative().getIdRelative());
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "The doctor is not associated with the elderly.");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Elderly not found with ID: " + elderlyId);
        }
    }

    public void sendComplaintToDoctor(Complaint complaint, String doctorEmail) {
        // Implémentez la logique pour envoyer la plainte au médecin
        // Vous pouvez envoyer un e-mail, une notification, ou toute autre action requise
        System.out.println("Complaint sent to doctor: " + doctorEmail);
        System.out.println("Complaint details: " + complaint);
    }

    public List<Complaint> getComplaintByDoctorEmail(String doctorEmail) {
        return complaintRepository.findByDoctorEmail(doctorEmail);
    }

    public Optional<Complaint> getComplaintById(long id) {
        return complaintRepository.findById(id);
    }

    public List<Complaint> getComplaintsByDoctorEmail(String doctorEmail) {
        return complaintRepository.findByDoctorEmail(doctorEmail);
    }

    public Complaint createComplaintForRelative(Complaint complaint, Long relativeId) {
        Optional<Relative> relativeOptional = relativeRepository.findById(relativeId);

        if (relativeOptional.isPresent()) {
            Relative relative = relativeOptional.get();
            complaint.setRelative(relative);

            // Get the email of the doctor associated with the elderly of the relative
            String doctorEmail = relative.getElderly().getDoctor().getEmail();

            // Retrieve the doctor using their email
            Doctor doctor = doctorRepository.findByEmail(doctorEmail)
                    .orElseThrow(() -> new RuntimeException("Doctor not found with email: " + doctorEmail));

            // Set the responsible doctor for the complaint
            complaint.setDoctor(doctor);

            // Print the emails for verification
            System.out.println("Relative Email: " + relative.getEmail());
            System.out.println("Doctor Email: " + doctor.getEmail());

            // Save the complaint
            return complaintRepository.save(complaint);
        } else {
            throw new RuntimeException("Relative not found with id: " + relativeId);
        }
    }
    public String getDoctorEmailById(Long doctorId) {
        try {
            Doctor doctor = doctorRepository.findById(doctorId).orElse(null);
            if (doctor != null) {
                return doctor.getEmail();
            } else {
                throw new com.google.maps.errors.NotFoundException("Doctor not found");
            }
        } catch (com.google.maps.errors.NotFoundException e) {
            // Handle the exception
            // You can log an error message or take other actions
            e.printStackTrace(); // Example: Print the stack trace
            return null; // Or return a default value, depending on your requirements
        }
    }

}