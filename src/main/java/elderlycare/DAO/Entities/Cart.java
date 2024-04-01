package elderlycare.DAO.Entities;

<<<<<<< HEAD
import com.fasterxml.jackson.annotation.JsonIgnore;
=======
>>>>>>> a91cccbc16c00c02dfa62d7def9d2a41298a99ae
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

<<<<<<< HEAD
import java.util.HashSet;
=======
>>>>>>> a91cccbc16c00c02dfa62d7def9d2a41298a99ae
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
<<<<<<< HEAD

=======
@FieldDefaults(level = AccessLevel.PRIVATE)
>>>>>>> a91cccbc16c00c02dfa62d7def9d2a41298a99ae
public class Cart {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    long cartId;

<<<<<<< HEAD
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy="cartss")
    Set<Orderr> orders = new HashSet<>();
=======

    @ManyToMany
    List<Product> products;
    @OneToMany(mappedBy = "cart")
    List<Order> orders;
>>>>>>> a91cccbc16c00c02dfa62d7def9d2a41298a99ae

}



<<<<<<< HEAD

=======
>>>>>>> a91cccbc16c00c02dfa62d7def9d2a41298a99ae
