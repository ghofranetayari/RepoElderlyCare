package elderlycare.DAO.Entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
<<<<<<< HEAD
public class Product {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
=======
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
>>>>>>> a91cccbc16c00c02dfa62d7def9d2a41298a99ae
    long productId;

    String productName;
    String prodDesc;
<<<<<<< HEAD
    float price;
    Double prodCapacity;
    String ArchProd ;
    String imageUrl;
    boolean discounted; // Flag to track if the product has been discounted

=======
    Double price;
    Double prodCapacity;
    String ArchProd;

    @ManyToMany(mappedBy = "products")
    List <Cart> carts;
>>>>>>> a91cccbc16c00c02dfa62d7def9d2a41298a99ae
}
