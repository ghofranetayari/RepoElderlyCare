package elderlycare.Services;

import elderlycare.DAO.Entities.*;

import java.util.List;
import java.util.Optional;

public interface IBoardService {
    public Todo updateStatus(Long todoId, TodoStatus status) ;
    public Card saveCard(Card card);
    public ListE getListById(Long listId);
    public ListE saveList(ListE list);
    List<Board> getAllBoards();
    Optional<Board> getBoardById(Long boardId);
    public Board createBoard(String boardName, List<ListE> initialLists);
    void deleteBoard(Long boardId);
    ListE addListToBoard(Long boardId, String listName);
    void updateListName(Long listId, String newName);
    void deleteList(Long listId);
    Card addCardToList(Long listId, String cardTitle, String cardContent, String priority);
    void updateCardStatus(Long cardId, Long newListId);
    void updateCardContent(Long cardId, String newContent);
    void deleteCard(Long cardId);
    }
