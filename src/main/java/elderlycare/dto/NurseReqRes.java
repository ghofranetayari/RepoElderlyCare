package elderlycare.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import elderlycare.DAO.Entities.Nurse;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NurseReqRes {
    private String name;
    private String email;
    private String password;
    private String token;
    private String refreshToken;
     private long phoneNumber;
    private String firstName;
    private String lastName;
     private LocalDate dateOfBirth;
    private String address;

    private String responsibilities;
    private Nurse nurse;

}