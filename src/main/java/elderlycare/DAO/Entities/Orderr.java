package elderlycare.DAO.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Orderr {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    long orderId;

    Date orderDate;
    String orderStatus;
    float price;
    private double totalPrice;
    int quantite; // Add quantity field
    private String paymentIntentId;

    @JsonIgnore
    @ManyToOne
    Cart cartss;


    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    Product product;
    String productName;



}