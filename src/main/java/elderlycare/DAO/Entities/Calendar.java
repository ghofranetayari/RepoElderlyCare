package elderlycare.DAO.Entities;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;


import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Calendar implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL)
    private List<Appointment> appointments;
    @OneToOne( cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Doctor doctor;

    @Temporal(TemporalType.DATE)
    private Date date;

    @JsonIgnore
    @OneToMany(mappedBy = "calendar", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TimeSlot> timeSlots;


}
