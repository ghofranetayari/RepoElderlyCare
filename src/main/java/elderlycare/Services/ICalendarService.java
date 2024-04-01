package elderlycare.Services;

import elderlycare.DAO.Entities.Calendar;
import elderlycare.DAO.Entities.Elderly;

import java.util.Optional;

public interface ICalendarService {
    public Calendar getCalendarSettingsById(long id) ;
    public Calendar createOrUpdateCalendarSettings(Calendar calendar) ;
    public void deleteCalendarSettingsById(long id) ;
    public Long getCalendarIdByDoctorId(Long doctorId) ;
    public Elderly getElderlyById(Long ElderlyId) ;

    public Optional<String> getElderlyNameByAppointmentId(Long appointmentId);




}