package elderlycare.DAO.Entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Nurse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long nurseID;

    private String password;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate dateOfBirth;
    private String address;
    private String responsibilities;
    private String gender;
    private String role;


    @OneToMany(cascade = CascadeType.ALL, mappedBy="nurse")
    private List<Elderly> Elderlys;
    @OneToOne
    @JoinColumn(name = "user_id") // Assurez-vous que le nom correspond à la colonne appropriée dans votre table
    private OurUsers user;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Complaint> Complaints;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Message> Messages;
    public Long getNurseID() {
        return nurseID;
    }


}
