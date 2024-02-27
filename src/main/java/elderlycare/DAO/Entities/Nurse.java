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
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Nurse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long nurseID;
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


    @OneToMany(cascade = CascadeType.ALL)
    private Set<Complaint> Complaints;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Message> Messages;


}
