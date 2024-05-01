package elderlycare.DAO.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import elderlycare.DAO.Entities.Elderly;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
    String ImageUrl ;
    String place ;
    double ticketprice ;
    double Longitude  ;
    double Latitude ;

    String archiveevent ;





    @JsonIgnore

    @ManyToMany(mappedBy = "events")
    private List<Elderly> elderlys;


}