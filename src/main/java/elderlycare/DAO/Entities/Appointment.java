package elderlycare.DAO.Entities;
 import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;


import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Appointment implements Serializable{
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Id
    private long idAppointment;
    private String patientName;
    private String appFrom;

    private String appTo;
    private Boolean appFirst;
    private String symptom;

    @Column(name = "app_status", length = 20) // adjust length as needed
    @Enumerated(EnumType.STRING)
    private AppointmentStatus appStatus;
    //this
    private String archiveApp ;
 /*  @JsonManagedReference@JsonIgnoreProperties("appointments") // Replace "appointments" with the actual property name in the Doctor entity

   @ManyToOne
   @JoinColumn(name = "doctor_id")
    private Doctor doctor2;
  //  @JsonBackReference

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Elderly patient;*/
}
