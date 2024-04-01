package elderlycare.DAO.Entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)

public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     Long id;

     String senderId;
     String recipientId;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
     byte[] audioContent;

     String textContent;

     LocalDateTime timestamp;
     boolean seen;


}
