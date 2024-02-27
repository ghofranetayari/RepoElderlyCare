package elderlycare.DAO.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
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
    private long Yearsofexperience;
    @OneToMany(mappedBy = "ambulanceowner")
    List<Ambulance> ambulanceList;

    @OneToMany
    List<Complaint> complaintList;

    @OneToMany
    List<Message> messages;
}
