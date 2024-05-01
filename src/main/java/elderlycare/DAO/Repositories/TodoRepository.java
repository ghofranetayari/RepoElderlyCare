package elderlycare.DAO.Repositories;

import elderlycare.DAO.Entities.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findByElderlyId(Long elderlyId);
    Page<Todo> findByElderlyId(Long elderlyId, Pageable pageable);

}
