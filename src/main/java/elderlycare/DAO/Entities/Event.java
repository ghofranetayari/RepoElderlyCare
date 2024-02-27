package elderlycare.DAO.Entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)

public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long eventID;
    String name;
    LocalDate date;
    String description;
    int capacity;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<Elderly> elderlys;


}
