package elderlycare.Services;

import elderlycare.DAO.Entities.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface IDoctorService {
  public Doctor getDoctorProfile(Long doctorId) ;
  //  public List<Appointment> findPendingAppointmentsForDoctor(Long doctorId) ;
  public Appointment approveAppointment(Long appointmentId) ;

  List<Appointment> findAllAppointmentsForDoctor(Long doctorId);

  List<Appointment> findAllAppointmentsForElderly(Long elderlyId);

  void rejectAppointment(Long appointmentId);
  // public List<Doctor> searchDoctorsBySpecialty(String specialty) ;
  public DoctorCalendarDTO getDoctorCalendarWithAppointments(Long doctorId, Long elderlyId) ;
  public List<Appointment> getDoctorAppointments(Long doctorId) ;

  public List<Doctor> getAllDoctors() ;
  public List<Doctor> searchDoctorsBySpecialty(String specialty) ;
  public List<Doctor> searchDoctorsByCity(String doctorCity) ;

  public String getDoctorEmailById(Long doctorId) ;
  public String getDoctorNameById(Long doctorId) ;

  public void completeAppointment(Long appointmentId) ;
  public List<Doctor> searchDoctorsBySpecialtyAndCity(String specialty, String city) ;


  public void save(Long doctorId, MultipartFile file) ;
  public void save2(Long doctorId, MultipartFile file) ;

    public void init() ;
  public List<Review> getAllReviews();

  public Review getReviewById(Long id);


  Review createReview(Review review, Long elderlyId, Long doctorId);
  public Review updateReview(Long id, Review updatedReview);



  public void deleteReview(Long id);
  public List<Review> getDoctorReviews(Long doctorId) ;
  double getAverageRatingForDoctor(Long doctorId);
  public Integer getTotalRatingsForDoctor(Long doctorId) ;
  public Review editReview(Long reviewId, Review updatedReview, Long elderlyId) ;
  public void deleteReview(Long doctorId, Long reviewId, Long elderlyId) ;
  public String getDoctorAddressById(Long doctorId) ;

  public Map<Doctor, Integer> calculateMonthlyRatings(List<Doctor> doctors, LocalDate month) ;

  public Doctor findDoctorWithHighestRating(Map<Doctor, Integer> monthlyRatings) ;
  public List<Doctor> filterDoctors(DoctorFilterCriteria criteria) ;
  List<Elderly> getPatientsByDoctorId(Long doctorId);







  public Long getDoctorIdByUserId(Integer userId) ;

  public Long getElderlyIdByUserId(Integer userId) ;

  public String getDoctorProfileImage(Long doctorId) ;
  void deleteCabinetPicturesByDoctorId(Long doctorId);

}