package elderlycare.DAO.Repositories;

import elderlycare.DAO.Entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface EventRepository extends JpaRepository<Event,Long> {
    List<Event> findAllByTicketpriceLessThanAndDateAfter(double ticketPrice, LocalDate date);



    @Query("SELECT MAX(e.ticketprice) FROM Event e")
    double findMaxTicketPrice();

    @Query("SELECT MAX(e.capacity) FROM Event e")
    int findMaxEventCapacity();
}
