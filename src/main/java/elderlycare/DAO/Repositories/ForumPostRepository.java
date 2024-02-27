package elderlycare.DAO.Repositories;

import elderlycare.DAO.Entities.ForumPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForumPostRepository extends JpaRepository <ForumPost,Long> {
}
