package elderlycare.DAO.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private boolean receiveNotifications;

    private String password;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate dateOfBirth;
    private String address;
    private String gender;
    private double latitude;
    private double longitude;

    private String preferences;
    private String healthRecord;
    private String role;
    private String tracking;


    @OneToOne
    @JoinColumn(name = "user_id") // Assurez-vous que le nom correspond à la colonne appropriée dans votre table
    private OurUsers user;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @ManyToOne
    @JsonIgnore
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
    @OneToMany(cascade = CascadeType.ALL)
    List<Appointment> appointments;

    @OneToOne
    @JoinColumn(name = "cart_id") // Assuming the name of the foreign key column
    Cart cart;


    @ManyToMany(cascade = CascadeType.ALL)
    private List<Ambulance> ambulances;

    @JsonIgnore
    @OneToOne
    Cart carts;

    @OneToOne(mappedBy = "elderlyt")
    TodoList toDoList;
    @OneToMany(mappedBy = "elderlyf")
    List<ForumPost> forumPosts; // An elderly can make
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviewsE;



    public Long getElderlyID() {
        return elderlyID;
    }

    public void setElderlyId(Long elderlyId) {
        this.elderlyID = elderlyId;
    }


}
