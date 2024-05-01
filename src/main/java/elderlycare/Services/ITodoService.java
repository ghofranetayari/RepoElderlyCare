package elderlycare.Services;

import elderlycare.DAO.Entities.Elderly;
import elderlycare.DAO.Entities.Relative;
import elderlycare.DAO.Entities.Todo;

import java.util.List;

public interface ITodoService {
    public Todo createTodo(Long elderlyId, Todo todo) ;
    public List<Relative> getRelativesByElderlyId(Long elderlyId) ;
    List<Todo> getTodosByElderlyId(Long elderlyId);
    public List<Todo> getTodosByRelativeId(Long relativeId) ;
    public Long getElderlyIdByRelativeId(Long relativeId);
    public Todo markTodoAsCompleted(Long todoId) ;
    public Elderly getElderlyById(Long elderlyId);
    public void removeTodo(Long todoId) ;
    public List<Todo> getPaginatedTodosByElderlyId(Long elderlyId, int page, int pageSize);

    }
