package elderlycare.DAO.Entities;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorCalendarDTO {
    private Long calendarId;
    private List<Appointment> appointments;

}
