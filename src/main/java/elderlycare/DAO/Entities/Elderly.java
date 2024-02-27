package elderlycare.DAO.Entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Elderly {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long elderlyID;
    String username;
    String password;
    String phoneNumber;
    String firstName;
    String lastName;
    String email;
    LocalDate dateOfBirth;
    String address;
    String preferences;
    String healthRecord;



    @ManyToOne
    Nurse nurse;

    @ManyToMany(mappedBy="elderlys", cascade = CascadeType.ALL)
    private List<Event> events;

    // @ManyToMany(cascade = CascadeType.ALL)
    // private List<Complaint> complaints;


    @OneToMany(cascade = CascadeType.ALL)
    private List<Complaint> Complaints;


    @OneToMany(cascade = CascadeType.ALL)
    private List<Message> Messages;



    @OneToOne(mappedBy="elderlyMedF")
    private MedicalFolder medicalfolder;
    @OneToOne
    @JoinColumn(name = "cart_id") // Assuming the name of the foreign key column
    Cart cart;

    // nadhiir
    @ManyToMany(cascade = CascadeType.ALL)
    private List<Ambulance> ambulances;
    //hazem
    @OneToMany(mappedBy = "elderlyo")
    List<Order> orders;

    @OneToOne(mappedBy = "elderlyt")
    TodoList toDoList;
    @OneToMany(mappedBy = "elderlyf")
    List<ForumPost> forumPosts; // An elderly can make

}
