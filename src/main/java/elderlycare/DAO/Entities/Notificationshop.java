package elderlycare.DAO.Entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Notificationshop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "elderly_id")
    Elderly elderly;

    @ManyToOne
    @JoinColumn(name = "product_id")
    Product product;
}
