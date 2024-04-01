package elderlycare.Services;

import elderlycare.DAO.Entities.Appointment;

import java.util.List;

public interface IAppointmentService {

    public List<Appointment> getAllAppointments() ;
    public List<Appointment> getTodaysAppointments() ;
        public Appointment getAppointmentById(long id);
    public Appointment createAppointment(Appointment appointment, Long elderlyId, long calendarId) ;
    public void cancelAppointment(long id) ;
    public Appointment updateAppointment(long id, Appointment updatedAppointment) ;
 //   public List<Appointment> getPendingAppointments(Long doctorId) ;
 public Appointment markAppointmentDone(Long id) ;
    public List<Appointment> getElderlyAppointments(Long elderlyId) ;


    public List<Appointment> getPendingAppointmentsByCalendarId(Long calendarId) ;


    List<Appointment> getAppointmentsByCalendarId(Long calendarId);

    List<Appointment> getPendingAppointmentsByElderlyId(Long elderlyId);

    List<Appointment> getCompletedAppointmentsByElderlyId(Long elderlyId);
    List<Appointment> getRejectedAppointmentsByElderlyId(Long elderlyId);

    public List<Appointment> getCompletedAppointmentsByCalendarId(Long calendarId) ;

    public List<Appointment> getApprovedAppointmentsByCalendarId(Long calendarId) ;

    public void markAppointmentRejected(long id) ;














}
