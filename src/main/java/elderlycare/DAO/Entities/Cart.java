package elderlycare.DAO.Entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

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
    private long cartId;
    private String quantite ;

    @OneToMany(cascade = CascadeType.ALL, mappedBy="cartss")
    Set<Orderr> orders;

}



