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
public class Product {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    long productId;

    String productName;
    String prodDesc;
    float price;
    Double prodCapacity;
    String ArchProd ;
    String imageUrl;
    boolean discounted; // Flag to track if the product has been discounted

}
