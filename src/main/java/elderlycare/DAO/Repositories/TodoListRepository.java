package elderlycare.DAO.Repositories;

import elderlycare.DAO.Entities.TodoList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoListRepository extends JpaRepository <TodoList,Long> {
}
