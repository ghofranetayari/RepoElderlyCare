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
<<<<<<< HEAD
=======
@FieldDefaults(level = AccessLevel.PRIVATE)
>>>>>>> a91cccbc16c00c02dfa62d7def9d2a41298a99ae
public class Nurse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long nurseID;
<<<<<<< HEAD

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
=======
    String username;
    String password;
    String phoneNumber;
    String firstName;
    String lastName;
    String email;
    LocalDate dateOfBirth;
    String address;
    String responsibilities;

    @OneToMany(cascade = CascadeType.ALL, mappedBy="nurse")
    private List<Elderly> Elderlys;

>>>>>>> a91cccbc16c00c02dfa62d7def9d2a41298a99ae

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Complaint> Complaints;

<<<<<<< HEAD

    public Long getNurseID() {
        return nurseID;
    }
=======
    @OneToMany(cascade = CascadeType.ALL)
    private List<Message> Messages;
>>>>>>> a91cccbc16c00c02dfa62d7def9d2a41298a99ae


}
