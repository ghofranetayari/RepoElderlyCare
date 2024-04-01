package elderlycare.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import elderlycare.DAO.Entities.OurUsers;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReqRes {
 private int id;
 private int statusCode;
 private String error;
 private String message;
 private String token;
 private String refreshToken;
 private String expirationTime;
 private String name;
 private String email;
 private String role;
 private String password;
 private OurUsers ourUsers;
 private String doctorType;
 private String specialization; // Cardiology, Oncology, etc.
 private String schedule; // "Monday, Wednesday, Friday: 9am-5pm"
 private String username;
 private String phoneNumber;
 private String relationship;
 private String firstName;
 private String lastName;
 private LocalDate dateOfBirth;
 private String address;
 private String  responsibilities;
 private long etats;

 private String preferences;
 private String healthRecord;
 private String gender;
 private  String yearsofexperience;
 private String drivingExperienceYears;
 private boolean onDuty;


 private double longitude;
 private double latitude;

 private Long roleId; // Déclaration de la variable roleId
 // Méthode setter pour roleId
 public void setRoleId(Long roleId) {
  this.roleId = roleId;
 }

 // Méthode getter pour roleId (si nécessaire)
 public Long getRoleId() {
  return roleId;
 }

 private Long specificId;
 private String language;

 public void setSpecificId(Long specificId) {
  this.specificId = specificId;
 }

 @JsonIgnoreProperties
 private List<Long> elderlyIds; // Ajoutez cette propriété pour stocker les IDs des personnes âgées associées
 private List<String> elderlyEmails; // Ajouter cette nouvelle liste pour stocker les emails des personnes âgées

 private String elderlyEmail;

 public String getElderlyEmail() {
  return elderlyEmail;
 }
 public void setElderlyEmail(String elderlyEmail) {
  this.elderlyEmail = elderlyEmail;
 }

}
