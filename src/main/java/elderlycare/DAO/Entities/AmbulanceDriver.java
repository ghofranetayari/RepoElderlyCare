package elderlycare.DAO.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AmbulanceDriver implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long AmbulanceDriverID;

    private String password;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate dateOfBirth;
    private String address;
    private String gender;
    private String role;

    private boolean onDuty;
    private String drivingExperienceYears;


    public boolean getOnDuty() {
        return onDuty;
    }

    @OneToOne
    @JoinColumn(name = "user_id") // Assurez-vous que le nom correspond à la colonne appropriée dans votre table
    private OurUsers user;


    @OneToOne(mappedBy = "ambulancedriver")
    Ambulance ambulance;


    public Long getAmbulanceDriverID() {
        return AmbulanceDriverID;
    }

}
