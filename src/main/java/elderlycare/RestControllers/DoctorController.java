package elderlycare.RestControllers;

import elderlycare.DAO.Entities.*;
import elderlycare.DAO.Repositories.DoctorRepository;
import elderlycare.DAO.Repositories.ElderlyRepository;
import elderlycare.Services.IDoctorService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = {"http://localhost:4200"} , allowedHeaders = {"Content-Type"})

@AllArgsConstructor

@RestController
@RequestMapping("/Doctor")
public class DoctorController {
    private IDoctorService iDoctorService;
    private DoctorRepository doctorRepository;
    private ElderlyRepository elderlyRepository;
    @GetMapping("/{doctorId}")
    public ResponseEntity<Doctor> getDoctorProfile(@PathVariable Long doctorId) {
        Doctor doctor = iDoctorService.getDoctorProfile(doctorId);
        return ResponseEntity.ok(doctor);
    }

    /*@GetMapping("/{doctorId}")
    public ResponseEntity<Doctor> getDoctorProfile(@PathVariable Long doctorId) {
        Doctor doctor = iDoctorService.getDoctorProfile(doctorId);
        if (doctor != null) {
            String imageUrl = iDoctorService.getDoctorProfileImage(doctorId);
            doctor.setImagedoc(imageUrl);
            return ResponseEntity.ok(doctor);
        } else {
            return ResponseEntity.notFound().build(); // Or handle as needed
        }
    }
*/






/*
    @GetMapping("/a/{doctorId}")
    public ResponseEntity<Doctor> getDoctorProfileImage(@PathVariable Long doctorId) {
        try {
            // Retrieve the doctor entity from the service
            Doctor doctor = iDoctorService.getDoctorProfile(doctorId);

            // Construct the image URL
            String fileName = doctor.getImagedoc(); // Retrieve the image file name from the doctor entity
            String uploadDir = "C:/xamppp/htdocs/hazemimage"; // Path to your upload directory
            String imageUrl = "http://localhost:8081" + fileName; // Construct the full image URL

            // Set the image URL in the doctor entity
            doctor.setImagedoc(imageUrl);

            return ResponseEntity.ok(doctor);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }*/













    /*  @GetMapping("/{doctorId}/PendingAppointments")
      public ResponseEntity<List<Appointment>> getUpcomingAppointments(@PathVariable Long doctorId) {
          List<Appointment> upcomingAppointments = iDoctorService.findPendingAppointmentsForDoctor(
                  doctorId);
          return ResponseEntity.ok(upcomingAppointments);
      }*/
    @PutMapping("/approve-appointment/{appointmentId}")
    public ResponseEntity<Appointment> approveAppointment(@PathVariable Long appointmentId) {
        Appointment approvedAppointment = iDoctorService.approveAppointment(appointmentId);
        return ResponseEntity.ok(approvedAppointment);
    }
    @GetMapping("/{doctorId}/AllAppointments")
    public ResponseEntity<List<Appointment>> getAllAppointmentsForDoctor(@PathVariable Long doctorId) {
        List<Appointment> allAppointments = iDoctorService.findAllAppointmentsForDoctor(doctorId);
        return ResponseEntity.ok(allAppointments);
    }
    @GetMapping("/Elderly/{elderlyId}/Appointments")
    public ResponseEntity<List<Appointment>> getAllAppointmentsForElderly(@PathVariable Long elderlyId) {
        List<Appointment> allAppointments = iDoctorService.findAllAppointmentsForElderly(elderlyId);
        return ResponseEntity.ok(allAppointments);
    }
    @PutMapping("/reject-appointment/{appointmentId}")
    public ResponseEntity<String> rejectAppointment(@PathVariable Long appointmentId) {
        iDoctorService.rejectAppointment(appointmentId);
        return ResponseEntity.ok("Appointment rejected successfully");
    }
    @PutMapping("/complete-appointment/{appointmentId}")
    public ResponseEntity<String> completeAppointment(@PathVariable Long appointmentId) {
        iDoctorService.completeAppointment(appointmentId);
        return ResponseEntity.ok("Appointment completed successfully");
    }

   /* @GetMapping("/search/{specialty}")
    public ResponseEntity<List<Doctor>> searchDoctorsBySpecialty(@PathVariable String specialty) {
        List<Doctor> doctors = iDoctorService.searchDoctorsBySpecialty(specialty);
        return ResponseEntity.ok(doctors); }*/

    @GetMapping("/{doctorId}/{elderlyId}/calendar")
    public ResponseEntity<DoctorCalendarDTO> getDoctorCalendar(@PathVariable Long doctorId, @PathVariable Long elderlyId) {
        try {
            DoctorCalendarDTO doctorCalendarDTO = iDoctorService.getDoctorCalendarWithAppointments(doctorId , elderlyId);
            return ResponseEntity.ok(doctorCalendarDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{doctorId}/appointments")
    public List<Appointment> getDoctorAppointments(@PathVariable Long doctorId) {
        return iDoctorService.getDoctorAppointments(doctorId);
    }










/*

    @GetMapping
    public ResponseEntity<List<Doctor>> getAllDoctorsWithImage() {
        try {
            // Retrieve all doctors from the service layer
            List<Doctor> doctors = iDoctorService.getAllDoctors();

            // Iterate through the list of doctors to construct and set image URLs
            for (Doctor doctor : doctors) {
                String fileName = doctor.getImagedoc(); // Retrieve the image file name from the doctor entity
                String uploadDir = "C:/xamppp/htdocs/hazemimage"; // Path to your upload directory
                String imageUrl =  fileName; // Construct the full image URL

                // Set the image URL in the doctor entity
                doctor.setImagedoc(imageUrl);
            }

            // Return the list of doctors with image URLs in the response
            return ResponseEntity.ok(doctors);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
*/








    @GetMapping("/search/{specialty}")
    public ResponseEntity<List<Doctor> >searchDoctorsBySpecialty(@PathVariable String specialty) {
        List<Doctor> doctors = iDoctorService.searchDoctorsBySpecialty(specialty);

        return new ResponseEntity<>(doctors, HttpStatus.OK);

    }
    @GetMapping("/searchCity/{doctorCity}")
    public ResponseEntity<List<Doctor> >searchDoctorsByCity(@PathVariable String doctorCity) {
        List<Doctor> doctors = iDoctorService.searchDoctorsByCity(doctorCity);

        return new ResponseEntity<>(doctors, HttpStatus.OK);

    }





    @GetMapping("/searchCitySpeciality/{specialty}/{city}")
    public ResponseEntity<List<Doctor>> searchDoctorsBySpecialtyAndCity(
            @PathVariable(required = false) String specialty,
            @PathVariable(required = false) String city
    ) {
        List<Doctor> doctors;
        if (specialty != null && !specialty.isEmpty() && city != null && !city.isEmpty()) {
            // Search by both specialty and city
            doctors = iDoctorService.searchDoctorsBySpecialtyAndCity(specialty, city);
        } else if (specialty != null && !specialty.isEmpty()) {
            // Search by specialty only
            doctors = iDoctorService.searchDoctorsBySpecialty(specialty);
        } else if (city != null && !city.isEmpty()) {
            // Search by city only
            doctors = iDoctorService.searchDoctorsByCity(city);
        } else {
            // If both specialty and city are empty or null, return all doctors
            doctors = iDoctorService.getAllDoctors();
        }
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }



    @GetMapping("/{id}/email")
    public String getDoctorEmail(@PathVariable("id") Long doctorId) {
        String email = iDoctorService.getDoctorEmailById(doctorId);
        if (email != null) {
            return email;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Doctor email not found");
        }
    }


    @GetMapping("/{id}/name")
    public String getDoctorName(@PathVariable("id") Long doctorId) {
        String name = iDoctorService.getDoctorNameById(doctorId);
        if (name != null) {
            return name;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Doctor name not found");
        }
    }

    @PostMapping("/upload-cabinet-pictures/{doctorId}")
    public ResponseEntity<FileResponseMessage> uploadCabinetPictures(
            @PathVariable Long doctorId,
            @RequestParam("files") MultipartFile[] files
    ) {
        String message;
        try {
            List<String> fileNames = new ArrayList<>();
            Arrays.stream(files).forEach(file -> {
                iDoctorService.save(doctorId, file);
                fileNames.add(file.getOriginalFilename());
            });
            message = "Cabinet picture(s) uploaded successfully: " + fileNames;
            return ResponseEntity.status(HttpStatus.OK).body(new FileResponseMessage(message));
        } catch (Exception e) {
            message = "Failed to upload cabinet picture(s)";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new FileResponseMessage(message));
        }
    }
    @GetMapping("/{doctorId}/cabinetPictures")
    public ResponseEntity<List<String>> getCabinetPicturesByDoctorId(@PathVariable Long doctorId) {
        Optional<Doctor> optionalDoctor = doctorRepository.findById(doctorId);
        if (optionalDoctor.isPresent()) {
            Doctor doctor = optionalDoctor.get();
            Set<String> cabinetPictures = doctor.getCabinetPictures();

            // Update URLs to use forward slashes
            List<String> updatedUrls = cabinetPictures.stream()
                    .map(url -> "http://localhost:9091/assets/FrontOffice/images/" + url.replace("\\", "/"))
                    .collect(Collectors.toList());

            return new ResponseEntity<>(updatedUrls, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/{doctorId}/profilePicture")
    public ResponseEntity<List<String>> getProfilePictureByDoctorId(@PathVariable Long doctorId) {
        Optional<Doctor> optionalDoctor = doctorRepository.findById(doctorId);
        if (optionalDoctor.isPresent()) {
            Doctor doctor = optionalDoctor.get();
            Set<String> profilePicture = Collections.singleton(doctor.getImagedoc());

            // Update URLs to use forward slashes
            List<String> updatedUrls = profilePicture.stream()
                    .map(url -> "http://localhost:9091/assets/FrontOffice/images/" + url.replace("\\", "/"))
                    .collect(Collectors.toList());

            return new ResponseEntity<>(updatedUrls, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/{doctorId}/cabinetPictures/{pictureName}")
    public ResponseEntity<String> deleteCabinetPicture(
            @PathVariable Long doctorId,
            @PathVariable String pictureName) {
        Optional<Doctor> optionalDoctor = doctorRepository.findById(doctorId);
        if (optionalDoctor.isPresent()) {
            Doctor doctor = optionalDoctor.get();
            Set<String> cabinetPictures = doctor.getCabinetPictures();
            if (cabinetPictures.contains(pictureName)) {
                // Delete the picture from the doctor's cabinet
                cabinetPictures.remove(pictureName);
                doctor.setCabinetPictures(cabinetPictures);
                doctorRepository.save(doctor);
                return new ResponseEntity<>("Picture deleted successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Picture not found in cabinet", HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>("Doctor not found", HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/review/All")
    public List<Review> getAllReviews() {
        return iDoctorService.getAllReviews();
    }

    @GetMapping("/review/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable Long id) {
        Review review = iDoctorService.getReviewById(id);
        return ResponseEntity.ok(review);
    }



    @PostMapping("/create")
    public ResponseEntity<Review> createReview(@RequestBody Review review, @RequestParam Long elderlyId, @RequestParam Long doctorId) {
        Review createdReview = iDoctorService.createReview(review, elderlyId, doctorId);
        if (createdReview != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdReview);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/review/{id}")
    public ResponseEntity<Review> updateReview(@PathVariable Long id, @RequestBody Review updatedReview) {
        Review updated = iDoctorService.updateReview(id, updatedReview);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/review/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        iDoctorService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/{doctorId}/reviews")
    public ResponseEntity<List<Review>> getDoctorReviews(@PathVariable Long doctorId) {
        List<Review> reviews = iDoctorService.getDoctorReviews(doctorId);
        return ResponseEntity.ok(reviews);
    }
    @GetMapping("/{doctorId}/average-rating")
    public ResponseEntity<Double> getAverageRatingForDoctor(@PathVariable Long doctorId) {
        double averageRating = iDoctorService.getAverageRatingForDoctor(doctorId);
        return ResponseEntity.ok(averageRating);
    }
    @GetMapping("/{doctorId}/total-ratings")
    public ResponseEntity<Integer> getTotalRatingsForDoctor(@PathVariable Long doctorId) {
        Integer totalRatings = iDoctorService.getTotalRatingsForDoctor(doctorId);
        if (totalRatings != null) {
            return ResponseEntity.ok(totalRatings);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    @PutMapping("/{reviewId}")
    public ResponseEntity<Review> editReview(@PathVariable Long reviewId, @RequestBody Review updatedReview, @RequestParam Long elderlyId) {
        Review editedReview = iDoctorService.editReview(reviewId, updatedReview, elderlyId);
        if (editedReview != null) {
            return ResponseEntity.ok(editedReview);
        } else {
            return ResponseEntity.notFound().build(); // Review not found or unauthorized
        }
    }

    @GetMapping("/elderly/{elderlyId}/banned-status")
    public ResponseEntity<Boolean> getElderlyBannedStatus(@PathVariable Long elderlyId) {
        boolean isBanned = iDoctorService.getElderlyBannedStatus(elderlyId);
        return ResponseEntity.ok(isBanned);
    }

    @DeleteMapping("/del/{doctorId}/{reviewId}/{elderlyId}")
    public ResponseEntity<String> deleteReview(
            @PathVariable Long doctorId,
            @PathVariable Long reviewId,
            @PathVariable Long elderlyId
    ) {
        try {
            iDoctorService.deleteReview(doctorId, reviewId, elderlyId);
            return ResponseEntity.ok("Review deleted successfully.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
        }
    }
    @GetMapping("/{doctorId}/address")
    public ResponseEntity<String> getDoctorAddress(@PathVariable Long doctorId) {
        String doctorAddress = iDoctorService.getDoctorAddressById(doctorId);
        if (doctorAddress != null) {
            return ResponseEntity.ok(doctorAddress);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/filter")
    public ResponseEntity<List<Doctor>> filterDoctors(@ModelAttribute DoctorFilterCriteria criteria) {
        List<Doctor> filteredDoctors = iDoctorService.filterDoctors(criteria);
        return ResponseEntity.ok(filteredDoctors);
    }

    @GetMapping("/patients/{doctorId}")
    public ResponseEntity<List<Elderly>> getPatientsByDoctorId(@PathVariable Long doctorId) {
        List<Elderly> patients = iDoctorService.getPatientsByDoctorId(doctorId);
        return ResponseEntity.ok(patients);
    }
    @GetMapping("/getDoctorIdByUserId/{userId}")
    public ResponseEntity<Long> getDoctorIdByUserId(@PathVariable Integer userId) {
        Long doctorId = iDoctorService.getDoctorIdByUserId(userId);
        if (doctorId != null) {
            return ResponseEntity.ok(doctorId);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/getElderlyIdByUserId/{userId}")
    public ResponseEntity<Long> getElderlyIdByUserId(@PathVariable Integer userId) {
        Long elderlyId = iDoctorService.getElderlyIdByUserId(userId);
        if (elderlyId != null) {
            return ResponseEntity.ok(elderlyId);
        } else {
            return ResponseEntity.notFound().build();
        }
    }



   /* @PostMapping("/{doctorId}/uploadImage")
    public ResponseEntity<String> uploadDoctorImage(@PathVariable Long doctorId, @RequestParam("file") MultipartFile file) {
        try {
            // Retrieve the doctor entity from the database
            Doctor doctor = doctorRepository.findById(doctorId)
                    .orElseThrow(() -> new EntityNotFoundException("Doctor not found with id: " + doctorId));

            // Save the uploaded image to a directory
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            String uploadDir = "C:/xamppp/htdocs/hazemimage";
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            try (InputStream inputStream = file.getInputStream()) {
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                doctor.setImagedoc(fileName); // Set the image file name to the doctor entity
                doctorRepository.save(doctor); // Save the updated doctor entity
            }

            return ResponseEntity.ok("Image uploaded successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image: " + e.getMessage());
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }*/



    @PostMapping("/{doctorId}/uploadImage")
    public ResponseEntity<FileResponseMessage> uploadDoctorImage(
            @PathVariable Long doctorId,
            @RequestParam("files") MultipartFile[] files
    ) {
        String message;
        try {
            List<String> fileNames = new ArrayList<>();
            Arrays.stream(files).forEach(file -> {
                iDoctorService.save2(doctorId, file);
                fileNames.add(file.getOriginalFilename());
            });
            message = "doc picture(s) uploaded successfully: " + fileNames;
            return ResponseEntity.status(HttpStatus.OK).body(new FileResponseMessage(message));
        } catch (Exception e) {
            message = "Failed to upload doc picture(s)";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new FileResponseMessage(message));
        }
    }/*
    @PostMapping("/{doctorId}/uploadImage")
    public ResponseEntity<String> uploadDoctorImage(@PathVariable Long doctorId, @RequestParam("file") MultipartFile file) {
        try {
            // Retrieve the doctor entity from the database
            Doctor doctor = doctorRepository.findById(doctorId)
                    .orElseThrow(() -> new EntityNotFoundException("Doctor not found with id: " + doctorId));

            // Generate a unique file name
            String originalFilename = file.getOriginalFilename();
            String fileName = UUID.randomUUID().toString() + "_" + originalFilename;

            // Save the uploaded image to a directory
            String uploadDir = "src/main/resources/static/assets/images";
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            try (InputStream inputStream = file.getInputStream()) {
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                doctor.setImagedoc(fileName); // Set the image file name to the doctor entity
                doctorRepository.save(doctor); // Save the updated doctor entity
            }

            return ResponseEntity.ok("Image uploaded successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image: " + e.getMessage());
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
     }   }*/
    @GetMapping
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        List<Doctor> doctors = iDoctorService
                .getAllDoctors();
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }


    /*@GetMapping
    public ResponseEntity<List<Doctor>> getAllDoctorsWithImage() {
        List<Doctor> doctors = iDoctorService.getAllDoctors();

        // Create a list to hold DoctorDTO objects
        List<Doctor> doctorDTOs = new ArrayList<>();

        // Iterate through the list of doctors
        for (Doctor doctor : doctors) {
            // Create a DoctorDTO object to hold doctor information
            Doctor doctorDTO = new Doctor();

            // Set doctor information
            doctorDTO.setIdDoctor(doctor.getIdDoctor());
            doctorDTO.setDoctorType(doctor.getDoctorType());
            doctorDTO.setSpecialization(doctor.getSpecialization());
            // Set other doctor fields as needed

            // Get the image URL using the imagedoc field
            String imageUrl = getImageUrl(doctor.getImagedoc());
            doctorDTO.setImagedoc(imageUrl);

            // Add the DoctorDTO object to the list
            doctorDTOs.add(doctorDTO);
        }

        return new ResponseEntity<>(doctorDTOs, HttpStatus.OK);
    }

    // Method to construct the image URL based on the file name
    private String getImageUrl(String fileName) {
        // Construct the full URL of the image using the file name and your base URL
        return "http://localhost:8081/" + fileName;




    }
*/

    @DeleteMapping("/delete-cabinet-pictures/{doctorId}")
    public ResponseEntity<?> deleteCabinetPictures(@PathVariable Long doctorId) {
        try {
            iDoctorService.deleteCabinetPicturesByDoctorId(doctorId);
            return ResponseEntity.ok().body("Cabinet pictures deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete cabinet pictures");
        }
    }
    @GetMapping("/{elderlyid}/checkArchivedStatus")
    public boolean checkArchivedStatus(@PathVariable Long elderlyid) {
        Elderly elderly = elderlyRepository.findById(elderlyid).orElse(null);
        if (elderly != null) {
            return elderly.getUser().isArchive();
        }
        return false; // Return false if elderly not found or archive status is false
    }
    @GetMapping("/{elderlyid}/bannedUntil")
    public ResponseEntity<Date> getElderlyBannedUntil(@PathVariable Long elderlyid) {
        Date bannedUntil = iDoctorService.getElderlyBannedUntil(elderlyid);
        return ResponseEntity.ok(bannedUntil);
    }

}
