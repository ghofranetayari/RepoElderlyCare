package elderlycare.DAO.Entities;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorFilterCriteria {
    private String gender;
    private String specialization;
    private String language;
    private String city;

}
