package elderlycare.DAO.Repositories;

import elderlycare.DAO.Entities.ForumComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForumCommentRepository extends JpaRepository <ForumComment,Long> {
}
