package elderlycare.DAO.Entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TimeSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonManagedReference

    @ManyToOne
    @JoinColumn(name = "calendar_id")
    private Calendar calendar;

    private String startTime;

    private String endTime;

    private boolean isAvailable;

    @ManyToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

}
