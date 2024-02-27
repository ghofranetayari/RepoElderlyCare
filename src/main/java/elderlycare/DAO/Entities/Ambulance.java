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
public class Ambulance implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ambulanceID;
    private String  Location;
    @Enumerated(EnumType.ORDINAL)
    private AmbulanceStatus status;
    @ManyToOne
    AmbulanceOwner ambulanceowner;

    @OneToOne
    AmbulanceDriver ambulancedriver;

    @ManyToMany(mappedBy = "ambulances")
    List<Elderly> elderlies;

}
