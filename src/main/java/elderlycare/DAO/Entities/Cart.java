package elderlycare.DAO.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Cart {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    long cartId;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy="cartss")
    Set<Orderr> orders = new HashSet<>();

}




