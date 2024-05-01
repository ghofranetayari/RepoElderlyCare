package elderlycare.Services;

import elderlycare.DAO.Entities.Elderly;
import elderlycare.DAO.Entities.Relative;
import elderlycare.DAO.Entities.Todo;
import elderlycare.DAO.Entities.TodoStatus;
import elderlycare.DAO.Repositories.ElderlyRepository;
import elderlycare.DAO.Repositories.RelativeRepository;
import elderlycare.DAO.Repositories.TodoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class TodoService implements ITodoService{
    @Autowired
    private TodoRepository todoRepository;
    @Autowired
    private RelativeRepository relativeRepository;
    @Autowired
    private ElderlyRepository elderlyRepository;

    @Override
    public Todo createTodo(Long elderlyId, Todo todo) {
        todo.setElderlyId(elderlyId);
        todo.setCreatedAt(LocalDateTime.now()); // Set current timestamp
        return todoRepository.save(todo);
    }
    @Override

    public List<Relative> getRelativesByElderlyId(Long elderlyId) {
        return relativeRepository.findByElderlyId(elderlyId);
    }

    @Override
    public List<Todo> getTodosByElderlyId(Long elderlyId) {
        return todoRepository.findByElderlyId(elderlyId);
    }

    @Override
    public List<Todo> getTodosByRelativeId(Long relativeId) {
        Long elderlyId = relativeRepository.getElderlyIdByRelativeId(relativeId);

        if (elderlyId != null) {
            // If elderly ID is found, fetch todos by elderly ID
            return todoRepository.findByElderlyId(elderlyId);
        } else {
            // Handle case where elderly ID is not found
            throw new IllegalArgumentException("No elderly found for the given relative ID");
        }    }

    public Long getElderlyIdByRelativeId(Long relativeId) {
        Long elderlyId = relativeRepository.getElderlyIdByRelativeId(relativeId);

        return elderlyId; // Return null if elderly ID is not found or if the relationship is not established
    }
    public Todo markTodoAsCompleted(Long todoId) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new RuntimeException("Todo not found"));

        todo.setStatus(TodoStatus.COMPLETED);
        return todoRepository.save(todo);
    }
@Override

    public Elderly getElderlyById(Long elderlyId) {
        return elderlyRepository.findById(elderlyId)
                .orElseThrow(() -> new RuntimeException("Elderly not found with ID: " + elderlyId));
    }
    @Override
    public void removeTodo(Long todoId) {
         if (todoRepository.existsById(todoId)) {
            todoRepository.deleteById(todoId);
        } else {
            throw new RuntimeException("Todo not found with ID: " + todoId);
        }
    }

    @Override
    public List<Todo> getPaginatedTodosByElderlyId(Long elderlyId, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize); // PageRequest is zero-based
        Page<Todo> pageResult = todoRepository.findByElderlyId(elderlyId, pageable);
        return pageResult.getContent(); // Extract content from the page
    }
}
