package elderlycare.DAO.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
<<<<<<< HEAD
import java.time.LocalDate;
=======
>>>>>>> a91cccbc16c00c02dfa62d7def9d2a41298a99ae
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
<<<<<<< HEAD

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

=======
    private Boolean onDuty;
    private long drivingExperienceYears;
   @OneToOne(mappedBy = "ambulancedriver")
    Ambulance ambulance;

   @OneToMany
    List<Message> messageList;
>>>>>>> a91cccbc16c00c02dfa62d7def9d2a41298a99ae
}
