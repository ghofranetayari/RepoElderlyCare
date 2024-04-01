package elderlycare.DAO.Entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Data
public class Ambulance implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ambulanceID;

    private String  location;
    private String status;
    private String imageAmbul;
    private boolean archive;
    private double latitude; // New field for latitude
    private double longitude; // New field for longitude

    @ManyToOne
    AmbulanceOwner ambulanceowner;

    @OneToOne
    @JsonIgnoreProperties("ambulance") // Exclure cette propriété de la sérialisation JSON
    AmbulanceDriver ambulancedriver;

    @ManyToMany(mappedBy = "ambulances")
    List<Elderly> elderlies;

}
