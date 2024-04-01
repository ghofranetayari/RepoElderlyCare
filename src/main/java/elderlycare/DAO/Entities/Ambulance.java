package elderlycare.DAO.Entities;

<<<<<<< HEAD
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
=======
>>>>>>> a91cccbc16c00c02dfa62d7def9d2a41298a99ae
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
<<<<<<< HEAD
@Data
=======
>>>>>>> a91cccbc16c00c02dfa62d7def9d2a41298a99ae
public class Ambulance implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ambulanceID;
<<<<<<< HEAD

    private String  location;
    private String status;
    private String imageAmbul;
    private boolean archive;
    private double latitude; // New field for latitude
    private double longitude; // New field for longitude

=======
    private String  Location;
    @Enumerated(EnumType.ORDINAL)
    private AmbulanceStatus status;
    private String imageAmbul;
    private boolean archive;
>>>>>>> a91cccbc16c00c02dfa62d7def9d2a41298a99ae
    @ManyToOne
    AmbulanceOwner ambulanceowner;

    @OneToOne
<<<<<<< HEAD
    @JsonIgnoreProperties("ambulance") // Exclure cette propriété de la sérialisation JSON
=======
>>>>>>> a91cccbc16c00c02dfa62d7def9d2a41298a99ae
    AmbulanceDriver ambulancedriver;

    @ManyToMany(mappedBy = "ambulances")
    List<Elderly> elderlies;

}
