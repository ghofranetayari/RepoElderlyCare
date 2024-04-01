package elderlycare.DAO.Repositories;

import elderlycare.DAO.Entities.Appointment;
import elderlycare.DAO.Entities.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AppointementRepository extends JpaRepository<Appointment,Long> {
    List<Appointment> findByAppFromAfterAndAppToBefore(String startOfDay, String endOfDay);

  /*  @Query(value = "SELECT * FROM appointment a WHERE a.doctor_id = :doctorId AND STR_TO_DATE(a.app_from, '%Y-%m-%d %H:%i:%s') > CURRENT_TIMESTAMP", nativeQuery = true)
   List<Appointment> findUpcomingAppointmentsForDoctor(@Param("doctorId") Long doctorId);*/

    //  @Query(value = "SELECT * FROM appointment a WHERE a.doctor_id = :doctorId AND a.app_status = 'PENDING'", nativeQuery = true)
    // List<Appointment> findPendingAppointmentsForDoctor(@Param("doctorId") Long doctorId);
    //  List<Appointment> findByDoctor2IdDoctorAndAppStatus(Long doctorId, AppointmentStatus appStatus);
    List<Appointment> findByAppFromBetweenAndAppStatusAndArchiveApp(String startDate, String endDate, AppointmentStatus appStatus, String archiveApp);

    @Query("SELECT a FROM Appointment a WHERE a.appFrom >= :startDate AND a.appFrom <= :endDate AND a.appStatus = :appStatus AND a.archiveApp = '0'")
    List<Appointment> findByAppFromBetweenAndAppStatusAndArchiveAppIsFalse(String startDate, String endDate, AppointmentStatus appStatus);
}