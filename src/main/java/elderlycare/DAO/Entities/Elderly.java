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
    private double compte;

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

@JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "elderly_event",
            joinColumns = @JoinColumn(name = "elderly_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    private List<Event> events;

    // @ManyToMany(cascade = CascadeType.ALL)
    // private List<Complaint> complaints;


    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Complaint> Complaints;


    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Message> Messages;



    @OneToOne(mappedBy="elderlyMedF")
    @JsonIgnore
    private MedicalFolder medicalfolder;
    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
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
    @JsonIgnore
    TodoList toDoList;
    @OneToMany(mappedBy = "elderlyf")
    @JsonIgnore
    List<ForumPost> forumPosts; // An elderly can make
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Review> reviewsE;



    public Long getElderlyID() {
        return elderlyID;
    }

  /*  public void setElderlyId(Long elderlyId) {
        this.elderlyID = elderlyId;
    }*/
  @OneToOne
  @JoinColumn(name = "relative_id")
  private Relative relative;

    public Relative getRelative() {
        return this.relative;
    }



}
