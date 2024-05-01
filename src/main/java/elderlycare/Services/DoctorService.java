package elderlycare.Services;

import elderlycare.DAO.Entities.Calendar;
import elderlycare.DAO.Entities.*;
import elderlycare.DAO.Repositories.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
@Slf4j
public class DoctorService implements IDoctorService{
    @Autowired
    private DoctorRepository doctorRepository;
    private ReviewRepository reviewRepository;
    private CalendarRepository calendarRepository;
    private AppointementRepository appointementRepository;
    private ElderlyRepository elderlyRepository;
    @Autowired
    private AppointmentService appointmentService;
    public Doctor getDoctorProfile(Long doctorId) {
        return doctorRepository.findById(doctorId).orElse(null);
    }



    // Other methods...

    public boolean getElderlyBannedStatus(Long elderlyId) {
        Elderly elderly = elderlyRepository.findById(elderlyId)
                .orElseThrow(() -> new EntityNotFoundException("Elderly not found with ID: " + elderlyId));

        return elderly.isBanned();
    }

    /* @Override
     public List<Appointment> findPendingAppointmentsForDoctor(Long doctorId) {
         return appointementRepository.findPendingAppointmentsForDoctor(doctorId);
     }*/
    public Appointment approveAppointment(Long appointmentId) {
        // Assume you want to mark the appointment as 'COMPLETED' when approved
        Appointment appointment = appointmentService.markAppointmentDone(appointmentId);
        return appointment;
    }
    @Override
    public List<Appointment> findAllAppointmentsForDoctor(Long doctorId) {
        return doctorRepository.findAllAppointmentsForDoctor(doctorId);
    }
    @Override
    public List<Appointment> findAllAppointmentsForElderly(Long elderlyId) {
        return elderlyRepository.findAllAppointmentsForElderly(elderlyId);
    }

    @Override
    public void rejectAppointment(Long appointmentId) {
        Appointment appointment = appointementRepository.findById(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found with ID: " + appointmentId));

        // Implement logic to reject the appointment
        appointment.setAppStatus(AppointmentStatus.REJECTED);
        appointementRepository.save(appointment);
    }

    /*@Override
    public List<Doctor> searchDoctorsBySpecialty(String specialty) {
        return doctorRepository.findDoctorByspecialization(specialty);
    }*/
    @Override
    public DoctorCalendarDTO getDoctorCalendarWithAppointments(Long doctorId, Long elderlyId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found with ID: " + doctorId));

        Elderly elderly = elderlyRepository.findById(elderlyId)
                .orElseThrow(() -> new EntityNotFoundException("Elderly not found with ID: " + elderlyId));

        // Assuming each doctor has only one calendar, adjust if needed
        Calendar calendar = doctor.getCalendar();

        if (calendar != null) {
            DoctorCalendarDTO doctorCalendarDTO = new DoctorCalendarDTO();
            doctorCalendarDTO.setCalendarId(calendar.getId());
            doctorCalendarDTO.setAppointments(calendar.getDoctor().getCalendar().getAppointments());

            // You might want to filter appointments based on the elderly's id
            // doctorCalendarDTO.setAppointments(filterAppointmentsByElderlyId(calendar.getDoctor().getAppointments(), elderlyId));

            return doctorCalendarDTO;
        } else {
            // Handle the case when the doctor has no calendar
            throw new EntityNotFoundException("Calendar not found for doctor with ID: " + doctorId);
        }
    }


    /*
    @Override
    public DoctorCalendarDTO getDoctorCalendarWithAppointments(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found with ID: " + doctorId));

        // Assuming each doctor has only one calendar, adjust if needed
        Calendar calendar = doctor.getCalendar();

        if (calendar != null) {
            DoctorCalendarDTO doctorCalendarDTO = new DoctorCalendarDTO();
            doctorCalendarDTO.setCalendarId(calendar.getId());
            doctorCalendarDTO.setAppointments(calendar.getDoctor().getAppointments());
            return doctorCalendarDTO;
        } else {
            // Handle the case when the doctor has no calendar
            throw new EntityNotFoundException("Calendar not found for doctor with ID: " + doctorId);
        }
    }

*/
    public List<Appointment> getDoctorAppointments(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found with ID: " + doctorId));

        return doctor.getCalendar().getAppointments();
    }

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Doctor> getAllDoctors() {
        return entityManager.createQuery("SELECT d FROM Doctor d", Doctor.class)
                .getResultList();    }
    @Override
    public List<Doctor> searchDoctorsBySpecialty(String specialty) {
        if (StringUtils.hasText(specialty) && specialty.length() >= 1) {
            return doctorRepository.findBySpecializationStartingWithIgnoreCase(specialty);
        } else {

            return doctorRepository.findAll();
        }
    }
    @Override
    public List<Doctor> searchDoctorsByCity(String doctorCity) {
        if (StringUtils.hasText(doctorCity) && doctorCity.length() >= 1) {
            return doctorRepository.findByAddressStartingWithIgnoreCase(doctorCity);
        } else {

            return doctorRepository.findAll();
        }
    }

    public String getDoctorEmailById(Long doctorId) {
        Optional<Doctor> optionalDoctor = doctorRepository.findById(doctorId);
        return optionalDoctor.map(Doctor::getEmail).orElse(null);
    }
    public String getDoctorNameById(Long doctorId) {
        Optional<Doctor> optionalDoctor = doctorRepository.findById(doctorId);
        return optionalDoctor.map(Doctor::getLastName).orElse(null);
    }
    @Override
    public void completeAppointment(Long appointmentId) {
        Appointment appointment = appointementRepository.findById(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found with ID: " + appointmentId));

        // Implement logic to complete the appointment
        appointment.setAppStatus(AppointmentStatus.COMPLETED);
        appointementRepository.save(appointment);
    }
    @Override
    public List<Doctor> searchDoctorsBySpecialtyAndCity(String specialty, String city) {
        if (specialty != null && !specialty.isEmpty() && city != null && !city.isEmpty()) {
            // Search by both specialty and city
            return doctorRepository.findBySpecializationContainingIgnoreCaseAndAddressContainingIgnoreCase(specialty, city);
        } else if (specialty != null && !specialty.isEmpty()) {
            // Search by specialty only
            return doctorRepository.findBySpecializationContainingIgnoreCase(specialty);
        } else if (city != null && !city.isEmpty()) {
            // Search by city only
            return doctorRepository.findByAddressContainingIgnoreCase(city);
        } else {
            // If both specialty and city are empty or null, return all doctors
            return doctorRepository.findAll();
        }
    }


    private static final Path UPLOADS_DIR = Paths.get("uploads");
    @Override
    public void init() {
        try {
            Files.createDirectories(UPLOADS_DIR);
        } catch (IOException e) {
            throw new RuntimeException("Error creating uploads directory", e);
        }
    }
    @Override
    public void save(Long doctorId, MultipartFile file) {
        try {
            Doctor doctor = doctorRepository.findById(doctorId)
                    .orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + doctorId));

            Path doctorDir = UPLOADS_DIR.resolve(String.valueOf(doctorId));
            Files.createDirectories(doctorDir);

            Path filePath = doctorDir.resolve(file.getOriginalFilename());
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Add the file path to the doctor's cabinet pictures list
            doctor.getCabinetPictures().add(filePath.toString());
            doctorRepository.save(doctor);
        } catch (IOException e) {
            throw new RuntimeException("Error saving cabinet picture", e);
        }
    }

    @Override
    public void save2(Long doctorId, MultipartFile file) {
        try {
            Doctor doctor = doctorRepository.findById(doctorId)
                    .orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + doctorId));

            Path doctorDir = UPLOADS_DIR.resolve(String.valueOf(doctorId));
            Files.createDirectories(doctorDir);

            Path filePath = doctorDir.resolve(file.getOriginalFilename());
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);



            // Add the file path to the doctor's cabinet pictures list
            String existingImageDoc = doctor.getImagedoc();

// Filter out "null" if it exists in the existing image doc string
            existingImageDoc = existingImageDoc != null ? existingImageDoc.replace("null,", "") : "";

// Concatenate the new image path with the filtered image doc string
            String newImagePath = filePath.toString();
            String updatedImageDoc = existingImageDoc.isEmpty() ? newImagePath : existingImageDoc  + newImagePath;

// Update the doctor entity with the updated image doc string
            doctor.setImagedoc(updatedImageDoc);

            doctorRepository.save(doctor);
        } catch (IOException e) {
            throw new RuntimeException("Error saving cabinet picture", e);
        }
    }

    public Stream<Path> loadAllFiles() {
        try {
            return Files.walk(UPLOADS_DIR, 1)
                    .filter(path -> !path.equals(UPLOADS_DIR))
                    .map(UPLOADS_DIR::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Error loading cabinet pictures", e);
        }
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public Review getReviewById(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Review not found"));
    }

    @Transactional
    public Review createReview(Review review, Long elderlyId, Long idDoctor) {
        Elderly elderly = elderlyRepository.findElderlyByElderlyID(elderlyId);
        Doctor doctor = doctorRepository.findDoctorByIdDoctor(idDoctor);

        if (elderly != null && doctor != null) {
            // Check for bad words in the comment
            if (containsBadWords(review.getComment())) {
                elderly.incrementBadWordCount(); // Increment bad word count for the elderly
                elderlyRepository.save(elderly); // Save the updated elderly entity
                if (elderly.getBadWordCount() >= 6) {
                    elderly.getUser().setArchive(true); // Archive the user account
                }

                return null; // Do not save the review with bad words
            }
            Review existingReview = review.getId() == null ? null : reviewRepository.findById(review.getId()).orElse(null);

            if (existingReview == null) {
                review.setCreationTime(LocalDateTime.now());
                review.setElderlyUsername(elderly.getFirstName());
                review.setElderlyId(elderly.getElderlyID());
                review.setDoctorId(doctor.getIdDoctor());

                // Save the review
                elderly.getReviewsE().add(review);
                doctor.getReviewsD().add(review);

                elderlyRepository.save(elderly);
                doctorRepository.save(doctor);
                return reviewRepository.save(review);
            } else {
                // Update existing review properties
                existingReview.setElderlyUsername(elderly.getFirstName());
                existingReview.setElderlyId(elderly.getElderlyID());
                existingReview.setDoctorId(doctor.getIdDoctor());
                existingReview.setComment(review.getComment());
                existingReview.setRating(review.getRating());

                // Save the updated review
                elderlyRepository.save(elderly);
                doctorRepository.save(doctor);
                return reviewRepository.save(existingReview);
            }
        } else {
            // Handle the case where the Elderly or doctor entity with the given ID is not found
            return null;
        }
    }

    private boolean containsBadWords(String text) {
        List<String> badWords = Arrays.asList("badword1", "badword2", "badword3"); // List of bad words
        for (String badWord : badWords) {
            if (text.toLowerCase().contains(badWord.toLowerCase())) {
                return true; // Found a bad word
            }
        }
        return false; // No bad words found
    }
/*
    @Transactional
    public Review createReview(Review review, Long elderlyId, Long idDoctor) {


        Elderly elderly = elderlyRepository.findElderlyByElderlyID(elderlyId);
        Doctor doctor = doctorRepository.findDoctorByIdDoctor(idDoctor);

        if (elderly != null && doctor != null) {
            Review existingReview = review.getId() == null ? null : reviewRepository.findById(review.getId()).orElse(null);

            if (existingReview == null) {
                review.setCreationTime(LocalDateTime.now());

                review.setElderlyUsername(elderly.getFirstName());
                review.setElderlyId(elderly.getElderlyID());
                review.setDoctorId(doctor.getIdDoctor());
                //   a new review,  set relationship and save
                elderly.getReviewsE().add(review);
                doctor.getReviewsD().add(review);

                elderlyRepository.save(elderly);
                doctorRepository.save(doctor);
                return reviewRepository.save(review);
            } else {
                existingReview.setElderlyUsername(elderly.getFirstName());
                existingReview.setElderlyId(elderly.getElderlyID());
                existingReview.setDoctorId(doctor.getIdDoctor());
                //   review already exists, update properties and save

                existingReview.setComment(review.getComment());
                existingReview.setRating(review.getRating());

                elderlyRepository.save(elderly);
                doctorRepository.save(doctor);
                return reviewRepository.save(existingReview);
            }
        } else {
            // Handle the case where the Elderly or doctor entity with the given ID is not found
            return null;
        }
    }*/

    public Review updateReview(Long id, Review updatedReview) {
        Review existingReview = getReviewById(id);
        existingReview.setComment(updatedReview.getComment());
        existingReview.setRating(updatedReview.getRating());
        return reviewRepository.save(existingReview);
    }

    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }

    @Override
    public List<Review> getDoctorReviews(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found with id: " + doctorId));

        List<Review> reviews = doctor.getReviewsD();
        if (reviews.isEmpty()) {
            throw new EntityNotFoundException("No reviews found for doctor with id: " + doctorId);
        }

        return reviews;
    }
    @Override
    public double getAverageRatingForDoctor(Long doctorId) {
        List<Review> reviews = reviewRepository.findByDoctorId(doctorId);
        if (reviews.isEmpty()) {
            return 0.0; // Return 0 if there are no reviews
        }

        int sumRatings = reviews.stream().mapToInt(Review::getRating).sum();
        return (double) sumRatings / reviews.size();
    }
    @Override

    public Integer getTotalRatingsForDoctor(Long doctorId) {
        return reviewRepository.getTotalRatingsForDoctor(doctorId);
    }
    @Override
    public Review editReview(Long reviewId, Review updatedReview, Long elderlyId) {
        Review existingReview = reviewRepository.findById(reviewId).orElse(null);
        Elderly elderly = elderlyRepository.findElderlyByElderlyID(elderlyId);

        if (existingReview != null && existingReview.getElderlyId().equals(elderlyId)) {
            existingReview.setElderlyUsername(elderly.getFirstName());
            existingReview.setElderlyId(elderly.getElderlyID());
            //   review already exists, update properties and save

            String comment = updatedReview.getComment();
            if (containsBadWords(comment)) {
                elderly.incrementBadWordCount(); // Increment bad word count for the elderly
                elderlyRepository.save(elderly); // Save the updated elderly entity
                return null; // Do not save the review with bad words
            }

            existingReview.setComment(comment);
            existingReview.setRating(updatedReview.getRating());

            elderlyRepository.save(elderly);
            // Update other fields as needed
            return reviewRepository.save(existingReview);
        }
        return null; // Review not found or unauthorized
    }
    @Override
    public void deleteReview(Long doctorId, Long reviewId, Long elderlyId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found with ID: " + doctorId));

        Elderly elderly = elderlyRepository.findById(elderlyId)
                .orElseThrow(() -> new EntityNotFoundException("Elderly not found with ID: " + elderlyId));

        List<Review> doctorReviews = doctor.getReviewsD();
        List<Review> elderlyReviews = elderly.getReviewsE();

        if (elderlyReviews.stream().anyMatch(review -> review.getId().equals(reviewId))) {
            removeReviewFromList(doctorReviews, reviewId);
            removeReviewFromList(elderlyReviews, reviewId);
            doctorRepository.save(doctor);
            elderlyRepository.save(elderly);
        } else {
            throw new IllegalStateException("Review with ID " + reviewId + " does not belong to the elderly.");
        }
    } private void removeReviewFromList(List<Review> reviews, Long reviewId) {
        Iterator<Review> iterator = reviews.iterator();
        while (iterator.hasNext()) {
            Review review = iterator.next();
            if (review.getId().equals(reviewId)) {
                iterator.remove(); // Remove the review from the list
                break; // Exit the loop since we found the review
            }
        }
    }
    @Override
    public String getDoctorAddressById(Long doctorId) {
        Optional<Doctor> doctorOptional = doctorRepository.findById(doctorId);
        return doctorOptional.map(Doctor::getAddress).orElse(null);
    }


    public Map<Doctor, Integer> calculateMonthlyRatings(List<Doctor> doctors, LocalDate month) {
        return doctors.stream()
                .collect(Collectors.toMap(
                        doctor -> doctor,
                        doctor -> doctor.getReviewsD().stream()
                                .filter(review -> review.getCreationTime() != null && review.getCreationTime().getMonth().equals(month.getMonth()))
                                .mapToInt(Review::getRating)
                                .sum()
                ));
    }
    @Transactional

    public Doctor findDoctorWithHighestRating(Map<Doctor, Integer> monthlyRatings) {

        return monthlyRatings.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    public List<Doctor> filterDoctors(DoctorFilterCriteria criteria) {
        // Build a specification based on the provided criteria
        Specification<Doctor> spec = Specification.where(null);
        if (criteria.getGender() != null) {
            spec = spec.and((root, query, builder) -> builder.equal(root.get("gender"), criteria.getGender()));
        }
        if (criteria.getSpecialization() != null) {
            spec = spec.and((root, query, builder) -> builder.equal(root.get("specialization"), criteria.getSpecialization()));
        }
        if (criteria.getLanguage() != null) {
            spec = spec.and((root, query, builder) -> builder.equal(root.get("language"), criteria.getLanguage()));
        }
        if (criteria.getCity() != null) {
            spec = spec.and((root, query, builder) -> builder.equal(root.get("address"), criteria.getCity()));
        }

        // Use the specification to fetch filtered doctors from the repository
        return doctorRepository.findAll(spec);
    }

    @Override
    public List<Elderly> getPatientsByDoctorId(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId).orElse(null);
        List<Elderly> patients = new ArrayList<>();

        if (doctor != null) {
            Set<Appointment> doctorAppointments = new HashSet<>();
            Calendar calendar = calendarRepository.findCalendarIdByDoctor(doctor);


            doctorAppointments.addAll(calendar.getAppointments());


            List<Elderly> elderlies = elderlyRepository.findAll();

            for (Elderly elderly : elderlies) {
                Set<Appointment> elderlyAppointments = new HashSet<>(elderly.getAppointments());
                elderlyAppointments.retainAll(doctorAppointments); // Find common appointments
                if (!elderlyAppointments.isEmpty()) {
                    patients.add(elderly);
                }
            }
        }

        return patients;
    }


    @Override
    public Long getDoctorIdByUserId(Integer userId) {
        OurUsers user = new OurUsers();
        user.setId(userId);

        Doctor doctor = doctorRepository.findByUser(user);
        if (doctor != null) {
            return doctor.getIdDoctor();
        } else {
            return null; // or handle the case as per your requirements
        }
    }
    @Override
    public Long getElderlyIdByUserId(Integer userId) {
        OurUsers user = new OurUsers();
        user.setId(userId);

        Elderly elderly = elderlyRepository.findByUser(user);
        if (elderly != null) {
            return elderly.getElderlyID();
        } else {
            return null; // or handle the case as per your requirements
        }
    }


    @Override
    public String getDoctorProfileImage(Long doctorId) {
        Optional<Doctor> optionalDoctor = doctorRepository.findById(doctorId);
        if (optionalDoctor.isPresent()) {
            Doctor doctor = optionalDoctor.get();
            // Assuming 'imagedoc' is the field that stores the image file name
            String fileName = doctor.getImagedoc();
            // Assuming the image directory path
            String uploadDir = "C:/xamppp/htdocs/hazemimage";
            // Construct the full image URL based on the directory and file name
            String imageUrl = uploadDir + "/" + fileName;
            return imageUrl;
        } else {
            // Return a default image URL or handle as needed
            return "default-image-url.jpg";
        }
    }

    @Override
    public void deleteCabinetPicturesByDoctorId(Long doctorId) {
        Optional<Doctor> optionalDoctor = doctorRepository.findById(doctorId);
        optionalDoctor.ifPresent(doctor -> {
            doctor.getCabinetPictures().clear(); // Assuming cabinetPictures is a Set in your Doctor entity
            doctorRepository.save(doctor);
        });
    }

    public Date getElderlyBannedUntil(Long id) {
        Optional<Elderly> optionalElderly = elderlyRepository.findById(id);
        if (optionalElderly.isPresent()) {
            Elderly elderly = optionalElderly.get();
            LocalDateTime bannedUntilLocalDateTime = elderly.getBannedUntil();
            if (bannedUntilLocalDateTime != null) {
                return Date.from(bannedUntilLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());
            } else {
                return null; // Or handle the case where bannedUntil is null
            }
        } else {
            throw new RuntimeException("Elderly not found with id: " + id);
            // Or handle the exception in a different way if needed
        }}
}