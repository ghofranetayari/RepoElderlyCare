package elderlycare.Services;

import elderlycare.DAO.Entities.Event;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


public interface IEventService {

        public List<Event> getAllEvents();
        public Event addEvent(Event event, MultipartFile file) throws IOException ;
        public Event updateEvent(Long eventId, Event updatedEvent, MultipartFile file) throws IOException ;
        public Optional<Event> getEventById(Long id) ;
        public boolean decrementEventCapacity(Long eventId) ;
        public boolean participateInEventWithPayment(Long elderlyId, Long eventId) ;

        public List<Event> getEventsForElderly(Long elderlyId) ;

        public Event toggleEventArchive(Long eventId) ;


        }
