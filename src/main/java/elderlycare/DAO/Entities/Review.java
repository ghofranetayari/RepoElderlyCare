package elderlycare.DAO.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Review implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String comment;
    private int rating;

    @Column(name = "elderly_id") // This column will store the elderly's ID
    private Long elderlyId;

    @Column(name = "doctor_id") // This column will store the doctor's ID
    private Long doctorId;

    private String elderlyUsername;
    @Column(name = "creation_time") // Column to store creation time
    private LocalDateTime creationTime;
}
