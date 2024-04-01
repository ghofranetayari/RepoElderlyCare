package elderlycare.DAO.Entities;

<<<<<<< HEAD
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;
=======
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
>>>>>>> a91cccbc16c00c02dfa62d7def9d2a41298a99ae

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
<<<<<<< HEAD
=======
@FieldDefaults(level = AccessLevel.PRIVATE)
>>>>>>> a91cccbc16c00c02dfa62d7def9d2a41298a99ae
public class Elderly {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long elderlyID;
<<<<<<< HEAD
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


=======
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

>>>>>>> a91cccbc16c00c02dfa62d7def9d2a41298a99ae
    @ManyToMany(mappedBy="elderlys", cascade = CascadeType.ALL)
    private List<Event> events;

    // @ManyToMany(cascade = CascadeType.ALL)
    // private List<Complaint> complaints;


    @OneToMany(cascade = CascadeType.ALL)
    private List<Complaint> Complaints;


<<<<<<< HEAD

=======
    @OneToMany(cascade = CascadeType.ALL)
    private List<Message> Messages;
>>>>>>> a91cccbc16c00c02dfa62d7def9d2a41298a99ae



    @OneToOne(mappedBy="elderlyMedF")
    private MedicalFolder medicalfolder;
<<<<<<< HEAD
    @OneToMany(cascade = CascadeType.ALL)
    List<Appointment> appointments;

=======
>>>>>>> a91cccbc16c00c02dfa62d7def9d2a41298a99ae
    @OneToOne
    @JoinColumn(name = "cart_id") // Assuming the name of the foreign key column
    Cart cart;

<<<<<<< HEAD

    @ManyToMany(cascade = CascadeType.ALL)
    private List<Ambulance> ambulances;

    @JsonIgnore
    @OneToOne
    Cart carts;
=======
    // nadhiir
    @ManyToMany(cascade = CascadeType.ALL)
    private List<Ambulance> ambulances;
    //hazem
    @OneToMany(mappedBy = "elderlyo")
    List<Order> orders;
>>>>>>> a91cccbc16c00c02dfa62d7def9d2a41298a99ae

    @OneToOne(mappedBy = "elderlyt")
    TodoList toDoList;
    @OneToMany(mappedBy = "elderlyf")
    List<ForumPost> forumPosts; // An elderly can make
<<<<<<< HEAD
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviewsE;



    public Long getElderlyID() {
        return elderlyID;
    }

    public void setElderlyId(Long elderlyId) {
        this.elderlyID = elderlyId;
    }
=======

>>>>>>> a91cccbc16c00c02dfa62d7def9d2a41298a99ae
}
