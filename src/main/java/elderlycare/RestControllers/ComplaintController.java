package elderlycare.RestControllers;

import elderlycare.DAO.Entities.Complaint;
import elderlycare.Services.ComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/api/complaints")
public class ComplaintController {
    private final ComplaintService complaintService;
    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @Autowired
    public ComplaintController(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    @GetMapping
    public List<Complaint> getAllComplaints() {
        return complaintService.getAllComplaints();
    }


    @PostMapping("/elderly/{elderlyId}")
    public ResponseEntity<Complaint> createComplaintForElderly(
            @PathVariable Long elderlyId, @RequestBody Complaint complaint) {

        Complaint createdComplaint = complaintService.createComplaintForElderly(complaint, elderlyId);

        if (createdComplaint != null) {
            return new ResponseEntity<>(createdComplaint, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /* @PostMapping("/relative/{relativeId}")
     public ResponseEntity<Complaint> createComplaintForRelative(
             @PathVariable Long relativeId, @RequestBody Complaint complaint) {

         Complaint createdComplaint = complaintService.createComplaintForRelative(complaint, relativeId);

         if (createdComplaint != null) {
             return new ResponseEntity<>(createdComplaint, HttpStatus.CREATED);
         } else {
             return new ResponseEntity<>(HttpStatus.NOT_FOUND);
         }
     }*/
    @PostMapping("/relative/{relativeId}")
    public ResponseEntity<Complaint> createComplaintForRelative(@RequestBody Complaint complaint,
                                                                @PathVariable("relativeId") Long relativeId) {
        Complaint createdComplaint = complaintService.createComplaintForRelative(complaint, relativeId);
        return new ResponseEntity<>(createdComplaint, HttpStatus.CREATED);
    }


    @GetMapping("/elderly/{elderlyId}")
    public ResponseEntity<List<Complaint>> getComplaintsByElderlyId(@PathVariable Long elderlyId) {
        List<Complaint> complaints = complaintService.getComplaintsByElderlyId(elderlyId);
        if (complaints != null && !complaints.isEmpty()) {
            return new ResponseEntity<>(complaints, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/relative/{idRelative}")
    public ResponseEntity<List<Complaint>> getComplaintsByRelativeId(@PathVariable Long idRelative) {
        List<Complaint> complaints = complaintService.getComplaintsByRelativeId(idRelative);
        if (complaints != null && !complaints.isEmpty()) {
            return new ResponseEntity<>(complaints, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

   /* @PutMapping("/{id}")
    public Complaint updateComplaint(@PathVariable Long id, @RequestBody Complaint complaint) {
        return complaintService.updateComplaint(id, complaint);
    }*/

    @DeleteMapping("/{id}")
    public void deleteComplaint(@PathVariable Long id) {
        complaintService.deleteComplaint(id);
    }

    @PutMapping("/archive/{id}")
    public void archiveComplaint(@PathVariable Long id) {
        complaintService.archiveComplaint(id);
    }

    @GetMapping("/relatives/{relativeId}/elderlyEmail")
    public String getElderlyEmailByRelativeId(@PathVariable Long relativeId) {
        return complaintService.getElderlyEmailByRelativeId(relativeId);
    }

    @GetMapping("/doctor/{elderlyEmail}")
    public ResponseEntity<String> getDoctorEmailByElderlyEmail(@PathVariable String elderlyEmail) {
        String doctorEmail = complaintService.getDoctorEmailByElderlyEmail(elderlyEmail);
        return ResponseEntity.ok(doctorEmail);
    }

   /* @PostMapping("/sendNotificationToDoctor/{doctorEmail}")
    public ResponseEntity<String> sendComplaintNotificationToDoctor(@PathVariable String doctorEmail, @RequestBody Complaint complaint) {
        // Implémentez la logique pour envoyer la notification au médecin avec la plainte
        return ResponseEntity.ok("Notification sent to doctor");
    }*/

   /* @PutMapping("/{complaintId}/relative/{relativeId}")
    public ResponseEntity<Complaint> updateComplaintForRelative(@PathVariable Long complaintId,
                                                                @PathVariable Long relativeId,
                                                                @RequestBody Complaint updatedComplaint) {
        try {
            Complaint updatedComplaintResult = complaintService.updateComplaintForRelative(complaintId, relativeId, updatedComplaint);
            return ResponseEntity.ok(updatedComplaintResult);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(null);
        }
    }*/

    @GetMapping("/relativeComplaints/{relativeId}/{doctorEmail}")
    public List<Complaint> getRelativeComplaintsForDoctor(@PathVariable Long relativeId, @PathVariable String doctorEmail) {
        return complaintService.getRelativeComplaintsForDoctor(relativeId, doctorEmail);
    }

    @PostMapping("/send-to-doctor/{doctorEmail}")
    public ResponseEntity<?> sendComplaintToDoctor(@RequestBody Complaint complaint, @PathVariable String doctorEmail) {
        // Implémentez la logique pour envoyer la plainte au médecin
        complaintService.sendComplaintToDoctor(complaint, doctorEmail);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/complaint/doctor/{doctorEmail}")
    public ResponseEntity<List<Complaint>> getComplaintByDoctorEmail(@PathVariable String doctorEmail) {
        List<Complaint> complaints = complaintService.getComplaintByDoctorEmail(doctorEmail);
        if (complaints == null || complaints.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(complaints);
    }

    /*@PutMapping("/{complaintId}")
    public ResponseEntity<Complaint> updateComplaintForRelative(@PathVariable Long complaintId, @PathVariable Long relativeId, @RequestBody Complaint updatedComplaint) {
        Complaint updatedComplaintResult = complaintService.updateComplaintForRelative(complaintId, relativeId, updatedComplaint);
        return ResponseEntity.ok(updatedComplaintResult);
    }*/
    @GetMapping("/{id}/{relativeId}")
    public ResponseEntity<Complaint> getComplaintById(@PathVariable Long id) {
        Optional<Complaint> complaint = complaintService.getComplaintById(id);
        if (complaint.isPresent()) {
            return ResponseEntity.ok(complaint.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Complaint> updateComplaint(@PathVariable Long id, @RequestBody Complaint updatedData) {
        Optional<Complaint> existingComplaintOptional = complaintService.getComplaintById(id);

        if (existingComplaintOptional.isPresent()) {
            Complaint existingComplaint = existingComplaintOptional.get();
            existingComplaint.setCategory(updatedData.getCategory());
            existingComplaint.setType(updatedData.getType());
            existingComplaint.setDescription(updatedData.getDescription());
            existingComplaint.setPriority(updatedData.getPriority());
            existingComplaint.setNotes(updatedData.getNotes());

            Complaint updatedComplaint = complaintService.updateComplaint(id, existingComplaint);
            return ResponseEntity.ok(updatedComplaint);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{doctorEmail}")
    public List<Complaint> getComplaintsByDoctorEmail(@RequestParam("doctorEmail") String doctorEmail) {
        return complaintService.getComplaintsByDoctorEmail(doctorEmail);
    }

    @GetMapping("/{id}/email")
    public ResponseEntity<Map<String, String>> getDoctorEmailById(@PathVariable("id") Long id) {
        Map<String, String> response = new HashMap<>();
        String email = complaintService.getDoctorEmailById(id);
        if (email != null) {
            response.put("email", email);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{complaintId}/notes")
    public ResponseEntity<String> getNotesForComplaint(@PathVariable Long complaintId) {
        Optional<Complaint> complaintOptional = complaintService.getComplaintById(complaintId);
        if (complaintOptional.isPresent()) {
            String notes = complaintOptional.get().getNotes();
            return ResponseEntity.ok(notes);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}







