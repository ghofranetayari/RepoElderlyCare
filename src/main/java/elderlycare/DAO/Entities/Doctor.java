package elderlycare.DAO.Entities;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;


import java.io.Serializable;
import java.time.LocalDate;
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
    private String imagedoc;
    private String doctorType;
    private String specialization; // Cardiology, Oncology, etc.
    private String schedule; // "Monday, Wednesday, Friday: 9am-5pm"
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private String phoneNumber;
    private String role;
    private String gender;
    private LocalDate dateOfBirth;
    private String email;
    private String address;
    private double longitude;
    private double latitude;


    private boolean favoriteOfTheMonth; // New field to track favorite status
    private String language;

    @OneToMany(cascade = CascadeType.ALL, mappedBy="doctor")
    private List<MedicalFolder> medicalFolders;


    @OneToMany(cascade = CascadeType.ALL)
    private Set<Complaint> Complaints;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id") // Assurez-vous que le nom correspond à la colonne appropriée dans votre table
    private OurUsers user;





    @OneToMany(mappedBy = "doctor", cascade = CascadeType.REMOVE)
    @JsonIgnoreProperties("doctor")
    private List<Elderly> elderlyList;



    @ElementCollection
    private Set<String> cabinetPictures = new HashSet<>(); // Assuming each path is a string

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Review> reviewsD;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "doctor")
    @JoinColumn(name = "calendar_id", referencedColumnName = "id")
    private Calendar calendar;

    public Long getDoctorId() {
        return idDoctor;
    }

    public void setDoctorId(Long doctorId) {
        this.idDoctor= doctorId;
    }

}