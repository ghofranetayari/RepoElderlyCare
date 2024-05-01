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
public class Relative implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Id
    private long idRelative;
    private String relationship;

    private String password;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate dateOfBirth;
    private String address;
    private String gender;
    private  Long etats;
    private String role;
    @OneToOne
    @JoinColumn(name = "user_id") // Assurez-vous que le nom correspond à la colonne appropriée dans votre table
    private OurUsers user;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Message> Messages;

    @ManyToOne
    Elderly elderly;
    public Long getRelativeID() {
        return idRelative;
    }

    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Complaint> Complaints;
}
