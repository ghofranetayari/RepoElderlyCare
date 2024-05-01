package elderlycare.RestControllers;

import elderlycare.DAO.Entities.AssigneeType;
import elderlycare.DAO.Entities.Elderly;
import elderlycare.DAO.Entities.Relative;
import elderlycare.DAO.Entities.Todo;
import elderlycare.Services.ITodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")

public class TodoController {
    @Autowired
private ITodoService ItodoService;

    @PostMapping("/{elderlyId}")
    public ResponseEntity<Todo> createTodo(
            @PathVariable Long elderlyId,
            @RequestBody Todo todo
    ) {
        if (todo.getAssignees() == null || todo.getAssignees().isEmpty()) {
            todo.setAssignee(AssigneeType.SELF); // Set assignee to SELF if no relatives assigned
        } else {
            todo.setAssignee(AssigneeType.RELATIVE); // Set assignee to RELATIVE if at least one relative assigned
        }

        Todo createdTodo = ItodoService.createTodo(elderlyId, todo);
        return new ResponseEntity<>(createdTodo, HttpStatus.CREATED);
    }


    @GetMapping("/elderly/{elderlyId}")
    public List<Relative> getRelativesByElderlyId(@PathVariable Long elderlyId) {
        return ItodoService.getRelativesByElderlyId(elderlyId);
    }

    @GetMapping("/all/{elderlyId}")
    public List<Todo> getTodosByElderlyId(@PathVariable Long elderlyId) {
        return ItodoService.getTodosByElderlyId(elderlyId);
    }
    @GetMapping("/relative/{relativeId}")
    public ResponseEntity<List<Todo>> getTodosByRelativeId(@PathVariable Long relativeId) {
        List<Todo> todos = ItodoService.getTodosByRelativeId(relativeId);
        return new ResponseEntity<>(todos, HttpStatus.OK);
    }
    @GetMapping("/{relativeId}/elderlyId")
    public Long getElderlyIdByRelativeId(@PathVariable Long relativeId) {
        return ItodoService.getElderlyIdByRelativeId(relativeId);
    }
    @PutMapping("/{todoId}/complete")
    public ResponseEntity<Todo> markTodoAsCompleted(@PathVariable Long todoId) {
        Todo updatedTodo = ItodoService.markTodoAsCompleted(todoId);
        return ResponseEntity.ok(updatedTodo);
    }
    @GetMapping("/{elderlyId}")
    public ResponseEntity<Elderly> getElderlyById(@PathVariable Long elderlyId) {
        Elderly elderly = ItodoService.getElderlyById(elderlyId);
        return ResponseEntity.ok(elderly);
    }
    @DeleteMapping("/delete/{todoId}")
    public ResponseEntity<Void> removeTodo(@PathVariable Long todoId) {
        ItodoService.removeTodo(todoId);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{elderlyId}/page")
    public ResponseEntity<List<Todo>> getPaginatedTodosByElderlyId(
            @PathVariable Long elderlyId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        List<Todo> paginatedTodos = ItodoService.getPaginatedTodosByElderlyId(elderlyId, page, pageSize);
        return new ResponseEntity<>(paginatedTodos, HttpStatus.OK);
    }
}
