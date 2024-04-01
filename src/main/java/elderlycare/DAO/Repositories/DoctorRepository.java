package elderlycare.DAO.Repositories;

<<<<<<< HEAD
import elderlycare.DAO.Entities.Appointment;
import elderlycare.DAO.Entities.Doctor;
import elderlycare.DAO.Entities.OurUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> , JpaSpecificationExecutor<Doctor> {
    Doctor findByUser(OurUsers user);
    List<OurUsers> findByRole(String role); // Ajoutez cette méthode pour rechercher des utilisateurs par rôle


    Doctor findById( long idDoctor);
    @Query("SELECT d.calendar.appointments FROM Doctor d WHERE d.idDoctor = :doctorId")
    List<Appointment> findAllAppointmentsForDoctor(@Param("doctorId") Long doctorId);

    List<Doctor> findDoctorByspecialization(String specialization);
    List<Doctor> findBySpecializationStartingWithIgnoreCase(String specialization);
    List<Doctor> findByAddressStartingWithIgnoreCase(String city);

    List<Doctor> findBySpecializationAndAddressIgnoreCase(String specialization, String city);
    List<Doctor> findBySpecializationContainingIgnoreCaseAndAddressContainingIgnoreCase(String specialty, String city);
    List<Doctor> findBySpecializationContainingIgnoreCase(String specialty);
    List<Doctor> findByAddressContainingIgnoreCase(String city);
    Doctor findDoctorByIdDoctor(long idDoctor);
    Long getIdDoctorByUserId(Long userId);

    Doctor findByUser_Id(Long userId);



    Doctor findDoctorByIdDoctor(Long idDoctor);

    Optional<Doctor> findByUserId(Integer userId);

=======
import elderlycare.DAO.Entities.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor,Long> {
>>>>>>> a91cccbc16c00c02dfa62d7def9d2a41298a99ae
}
