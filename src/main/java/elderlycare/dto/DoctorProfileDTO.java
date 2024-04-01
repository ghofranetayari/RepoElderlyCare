package elderlycare.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DoctorProfileDTO {
    private Long id;
    private String doctorType;
    private String specialization;
    private String schedule;
    private String username;
    private String firstName;
    private String lastName;
    private String address;
    private String phoneNumber;
    private String favoriteOfTheMonth;
    private String language;

}