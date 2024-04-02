package elderlycare.DAO.Entities;

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
public class Order {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    long orderId;

    Date orderDate;
    String orderStatus;
    float price;

    @ManyToOne
    Cart cart;
    @ManyToOne
    @JoinColumn(name = "elderly_id")
    Elderly elderlyo ;

}
