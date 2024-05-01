package elderlycare.DAO.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Tracking implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double latitudeInitial;
    private Double longitudeInitial;
    private Double latitudeDest;
    private Double longitudeDest;
    private String initial;
    private String destination;



    @OneToOne
    @JoinColumn(name = "elderly_id")
    @JsonIgnore // Exclude from JSON serialization to avoid circular references
    private Elderly elderly;



    public Long getId() {
        return id;
    }


}
