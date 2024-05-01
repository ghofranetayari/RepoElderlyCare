package elderlycare.DAO.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Complaint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long complaintID;
    String description;
    LocalDate creationDate;
    String type;
    String category;
    private String priority;
    private String notes;

    // Champs spécifiques aux administrateurs
    private String assigneA;
    private LocalDate closingDate;
    private String internalNotes;
    private boolean archived;


    // Associations avec les autres entités
    @ManyToOne
    @JoinColumn(name = "elderly_id")
    @JsonIgnore // Ignorer la propriété lors de la sérialisation en JSON
    private Elderly elderly;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    @JsonIgnore // Ignorer la propriété lors de la sérialisation en JSON
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "nurse_id")
    @JsonIgnore // Ignorer la propriété lors de la sérialisation en JSON
    private Nurse nurse;



    @ManyToOne
    @JoinColumn(name = "ambulance_driver_id")
    @JsonIgnore // Ignorer la propriété lors de la sérialisation en JSON
    private AmbulanceDriver ambulanceDriver;

    @ManyToOne
    @JoinColumn(name = "ambulance_owner_id")
    @JsonIgnore // Ignorer la propriété lors de la sérialisation en JSON
    private AmbulanceOwner ambulanceOwner;

    @ManyToOne
    @JoinColumn(name = "relative_id")
    @JsonIgnore // Ignorer la propriété lors de la sérialisation en JSON
    private Relative relative;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id") // Nom de la colonne dans la table des plaintes faisant référence à l'utilisateur
    private OurUsers user; // Champ pour stocker l'utilisateur associé à la plainte

    // Constructeurs, getters et setters

    public void setUser(OurUsers user) {
        this.user = user;
    }
    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }


    // Setter method for relativeId

    // Vérification null avant d'accéder à la méthode getIdDoctor()

}
