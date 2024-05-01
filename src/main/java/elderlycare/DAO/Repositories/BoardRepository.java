package elderlycare.DAO.Repositories;

import elderlycare.DAO.Entities.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BoardRepository extends JpaRepository <Board, Long>{
    @Query("SELECT b FROM Board b LEFT JOIN FETCH b.lists WHERE b.id = :boardId")
    Optional<Board> findBoardWithLists(@Param("boardId") Long boardId);
}
