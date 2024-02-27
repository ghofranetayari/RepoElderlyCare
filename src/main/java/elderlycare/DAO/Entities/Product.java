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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long productId;

    private String productName;
    private  String prodDesc;
    private Double price;
    private Double prodCapacity;
    private String ArchProd;
    @Lob
    byte[] image;


}
