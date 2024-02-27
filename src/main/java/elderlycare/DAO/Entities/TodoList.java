package elderlycare.DAO.Entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TodoList {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    int idtodolist;
    String task ;
    Boolean StatusTodolist ;


    @OneToOne
    @JoinColumn(name = "elderly_id")
    Elderly elderlyt;


}
