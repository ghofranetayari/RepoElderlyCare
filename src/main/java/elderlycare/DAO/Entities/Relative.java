package elderlycare.DAO.Entities;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;


import java.io.Serializable;
<<<<<<< HEAD
import java.time.LocalDate;
=======
>>>>>>> a91cccbc16c00c02dfa62d7def9d2a41298a99ae
import java.util.HashSet;
import java.util.Set;
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Relative implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Id
    private long idRelative;
    private String relationship;
<<<<<<< HEAD

    private String password;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate dateOfBirth;
    private String address;
    private String gender;
    private long etats;

    @OneToOne
    @JoinColumn(name = "user_id") // Assurez-vous que le nom correspond à la colonne appropriée dans votre table
    private OurUsers user;



    @ManyToOne
    Elderly elderly;
    private String role;

    public Long getRelativeID() {
        return idRelative;
    }

=======
    private String username;
    private String password;
    private String phoneNumber;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Message> Messages;

    @ManyToOne
    Elderly elderly;
>>>>>>> a91cccbc16c00c02dfa62d7def9d2a41298a99ae
}
