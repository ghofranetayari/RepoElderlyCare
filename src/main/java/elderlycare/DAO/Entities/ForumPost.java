package elderlycare.DAO.Entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ForumPost {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    int forumpostid ;
    Date TimestampFpost ;
    String topic ;
    String ContentFpost ;
    @OneToMany(mappedBy = "forumPost", cascade = CascadeType.ALL)
    List<ForumComment> comments;
    @ManyToOne // Many posts can be made by one elderly
    @JoinColumn(name = "elderly_id")
    Elderly elderlyf;
}
