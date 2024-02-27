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
    private long phoneNumber;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate dateOfBirth;
    private String address;

    private boolean onDuty;
    private long drivingExperienceYears;



    @OneToOne(mappedBy = "ambulancedriver")
    Ambulance ambulance;

    @OneToMany
    List<Message> messageList;
}
