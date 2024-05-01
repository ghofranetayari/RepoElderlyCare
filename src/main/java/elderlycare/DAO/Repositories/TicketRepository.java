package elderlycare.DAO.Repositories;

import elderlycare.DAO.Entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TicketRepository extends JpaRepository<Ticket,Long> {
}
