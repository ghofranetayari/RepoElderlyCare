package elderlycare.DAO.Entities;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;


import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Doctor implements Serializable{
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Id
    private long idDoctor;
    private String doctorType;
    private String specialization; // Cardiology, Oncology, etc.
    private String schedule; // "Monday, Wednesday, Friday: 9am-5pm"
    private String username;
    private String password;
    private String phoneNumber;

    @OneToMany(cascade = CascadeType.ALL, mappedBy="doctor")
    private List<MedicalFolder> medicalFolders;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "doctor2")
    private List<Appointment> appointments;
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Message> Messages;
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Complaint> Complaints;
}