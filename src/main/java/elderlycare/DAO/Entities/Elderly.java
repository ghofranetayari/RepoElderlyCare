package elderlycare.DAO.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Elderly {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long elderlyID;

    private String password;
    private long phoneNumber;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate dateOfBirth;
    private String address;

    private String preferences;
    private String healthRecord;



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


    //oumaima
    @OneToOne(mappedBy="elderlyMedF")
    private MedicalFolder medicalfolder;
    @OneToMany(mappedBy = "patient")
    List<Appointment> appointments;

    @OneToOne
    @JoinColumn(name = "cart_id") // Assuming the name of the foreign key column
    Cart cart;

    // nadhiir
    @ManyToMany(cascade = CascadeType.ALL)
    private List<Ambulance> ambulances;
    //hazem
    @OneToOne
    Cart carts;


    @OneToOne(mappedBy = "elderlyt")
    TodoList toDoList;
    @OneToMany(mappedBy = "elderlyf")
    List<ForumPost> forumPosts; // An elderly can make

}
