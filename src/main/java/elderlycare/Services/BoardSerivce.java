package elderlycare.Services;

import elderlycare.DAO.Entities.*;
import elderlycare.DAO.Repositories.BoardRepository;
import elderlycare.DAO.Repositories.CardRepository;
import elderlycare.DAO.Repositories.ListERepository;
import elderlycare.DAO.Repositories.TodoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class BoardSerivce implements  IBoardService {
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    ListERepository listRepository;
    @Autowired
    TodoRepository todoRepository;
    public List<Board> getAllBoards() {
        return boardRepository.findAll();
    }




    @Override
    public Optional<Board> getBoardById(Long boardId) {
        return boardRepository.findBoardWithLists(boardId); // Use the custom query
    }
    @Override

    public Board createBoard(String boardName, List<ListE> initialLists) {
        Board board = new Board();
        board.setName(boardName);

        if (initialLists != null) {
            for (ListE list : initialLists) {
                list.setBoard(board); // Set board reference for each list
                board.getLists().add(list); // Add to board's lists collection
            }
        }

        return boardRepository.save(board); // Save the board with lists
    }
    @Override
    public void deleteBoard(Long boardId) {
        boardRepository.deleteById(boardId);
    }

    @Override
    public ListE addListToBoard(Long boardId, String listName) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("Board not found"));

        ListE list = new ListE();
        list.setName(listName);
        list.setBoard(board);

        return listRepository.save(list);
    }

    @Override
    public void updateListName(Long listId, String newName) {
        ListE list = listRepository.findById(listId)
                .orElseThrow(() -> new EntityNotFoundException("List not found"));

        list.setName(newName);
        listRepository.save(list);
    }

    @Override
    public void deleteList(Long listId) {
        listRepository.deleteById(listId);
    }

    @Override
    public Card addCardToList(Long listId, String cardTitle, String cardContent, String priority) {
        ListE list = listRepository.findById(listId)
                .orElseThrow(() -> new EntityNotFoundException("List not found"));

        Card card = new Card();
        card.setTitle(cardTitle);
        card.setContent(cardContent);
        card.setPriority(priority);
        card.setList(list);

        return cardRepository.save(card);
    }

    @Override
    public void updateCardStatus(Long cardId, Long newListId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new EntityNotFoundException("Card not found"));

        ListE newList = listRepository.findById(newListId)
                .orElseThrow(() -> new EntityNotFoundException("List not found"));

        card.setList(newList);
        cardRepository.save(card);
    }

    @Override
    public void updateCardContent(Long cardId, String newContent) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new EntityNotFoundException("Card not found"));

        card.setContent(newContent);
        cardRepository.save(card);
    }

    @Override
    public void deleteCard(Long cardId) {
        cardRepository.deleteById(cardId);
    }
    @Override

    public ListE getListById(Long listId) {
        return listRepository.findById(listId).orElse(null);
    }
    @Override

    public ListE saveList(ListE list) {
        return listRepository.save(list); // Save the list with associated cards
    }




    @Override

    public Card saveCard(Card card) {
        return cardRepository.save(card); // Save the card to the repository
    }









@Override
    public Todo updateStatus(Long todoId, TodoStatus status) {
        Todo toDo = todoRepository.findById(todoId) .orElseThrow(() -> new EntityNotFoundException("todo not found"));
        toDo.setStatus(status);
        return todoRepository.save(toDo);
    }
}