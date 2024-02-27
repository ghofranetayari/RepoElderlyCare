package elderlycare.DAO.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;


import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class MedicalFolder implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Id
    private long idFolder ;
    private String familyMedicalHistory;
    private String allergies;
    private String typeOfBlood;
    private String mentalHealthDisorders;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Medication> medications;
    @ManyToOne
    Doctor doctor;
    @OneToOne
    private Elderly elderlyMedF;
}