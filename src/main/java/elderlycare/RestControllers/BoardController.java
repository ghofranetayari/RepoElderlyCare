package elderlycare.RestControllers;

import elderlycare.DAO.Entities.*;
import elderlycare.DAO.Repositories.TodoRepository;
import elderlycare.Services.IBoardService;
import elderlycare.Services.ICalendarService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = {"http://localhost:4200"} , allowedHeaders = {"Content-Type"})

@AllArgsConstructor

@RestController

public class BoardController {
    @Autowired
    private TodoRepository todoRepository;
    @Autowired
    private ICalendarService icalendarService;
    @Autowired
    private IBoardService boardService;
    @GetMapping("/boards")
    public ResponseEntity<List<Board>> getAllBoards() {
        List<Board> boards = boardService.getAllBoards();
        return new ResponseEntity<>(boards, HttpStatus.OK);
    }

    // Get a specific board by ID
    @GetMapping("/{elderlyId}/board")
    public ResponseEntity<Board> getElderlyBoard(@PathVariable Long elderlyId) {
        Elderly elderly = icalendarService.getElderlyById(elderlyId);

        if (elderly == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Elderly not found
        }

        Board board = elderly.getBoard(); // Get the board associated with this elderly
        if (board != null) {
            return new ResponseEntity<>(board, HttpStatus.OK); // Return the board
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // No board associated
        }
    }

    @PostMapping("/boards")
    public ResponseEntity<Board> createBoard(@RequestBody Board requestBoard) {
        List<ListE> initialLists = requestBoard.getLists();
        Board newBoard = boardService.createBoard(requestBoard.getName(), initialLists);
        return new ResponseEntity<>(newBoard, HttpStatus.CREATED);
    }

    // Delete a board
    @DeleteMapping("/boards/{boardId}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long boardId) {
        try {
            boardService.deleteBoard(boardId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Add a list to a board
    @PostMapping("/boards/{boardId}/lists")
    public ResponseEntity<ListE> addListToBoard(@PathVariable Long boardId, @RequestBody ListE list) {
        try {
            ListE newList = boardService.addListToBoard(boardId, list.getName());
            return new ResponseEntity<>(newList, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Update the name of a list
    @PutMapping("/lists/{listId}")
    public ResponseEntity<Void> updateListName(@PathVariable Long listId, @RequestBody ListE list) {
        try {
            boardService.updateListName(listId, list.getName());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete a list
    @DeleteMapping("/lists/{listId}")
    public ResponseEntity<Void> deleteList(@PathVariable Long listId) {
        try {
            boardService.deleteList(listId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/{listId}/cards")
    public ResponseEntity<Card> addCardToList(@PathVariable Long listId, @RequestBody Card newCard) {
        ListE list = boardService.getListById(listId);
        if (list == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // List not found
        }

        // Associate the new card with the specified list
        newCard.setList(list);

        // Save the card to the repository
        Card createdCard = boardService.saveCard(newCard);

        // Add the card to the list's card collection
        list.getCards().add(createdCard);
        boardService.saveList(list); // Save the list to ensure persistence

        return new ResponseEntity<>(createdCard, HttpStatus.CREATED); // Return the created card
    }

    // Update the status of a card (drag-and-drop)
    @PutMapping("/cards/{cardId}/list/{listId}")
    public ResponseEntity<Void> updateCardStatus(@PathVariable Long cardId, @PathVariable Long listId) {
        try {
            boardService.updateCardStatus(cardId, listId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/todos/{todoId}/{status}/{listId}")
    public ResponseEntity<Void> updateCardStatus(@PathVariable("todoId") Long todoId, @PathVariable("status") String status, @PathVariable("listId") Long listId) {
        Optional<Todo> optionalTodo = todoRepository.findById(todoId);
        if (optionalTodo.isPresent()) {
            Todo todo = optionalTodo.get();
            TodoStatus todoStatus = TodoStatus.valueOf(status.toUpperCase());
            if (listId == 1) {
                todo.setStatus(TodoStatus.PENDING);
            } else if (listId == 2) {
                todo.setStatus(TodoStatus.IN_PROGRESS);
            } else if (listId == 3) {
                todo.setStatus(TodoStatus.COMPLETED);
            } else {
                throw new IllegalArgumentException("Invalid list ID");
            }
            todoRepository.save(todo);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Update the content of a card
    @PutMapping("/cards/{cardId}/content")
    public ResponseEntity<Void> updateCardContent(@PathVariable Long cardId, @RequestBody Card card) {
        try {
            boardService.updateCardContent(cardId, card.getContent());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete a card
    @DeleteMapping("/cards/{cardId}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long cardId) {
        try {
            boardService.deleteCard(cardId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{todoId}/status")
    public ResponseEntity<Todo> updateTodoStatus(@PathVariable Long todoId, @RequestBody Map<String, String> statusJson) {
        TodoStatus status = TodoStatus.valueOf(statusJson.get("status").toUpperCase());
        Todo updatedToDo = boardService.updateStatus(todoId, status);
        return ResponseEntity.ok(updatedToDo);
    }
}

