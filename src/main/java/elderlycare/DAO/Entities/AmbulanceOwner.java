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
public class AmbulanceOwner implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long AmbulanceOwnerID;

    private String password;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate dateOfBirth;
    private String address;
    private String gender;

    private String role;

    private String yearsofexperience;

    @OneToOne
    @JoinColumn(name = "user_id") // Assurez-vous que le nom correspond à la colonne appropriée dans votre table
    private OurUsers user;

    @OneToMany(mappedBy = "ambulanceowner")
    List<Ambulance> ambulanceList;

    @OneToMany
    List<Complaint> complaintList;

    @OneToMany
    List<Message> messages;

    public Long getAmbulanceOwnerID() {
        return AmbulanceOwnerID;
    }
}
