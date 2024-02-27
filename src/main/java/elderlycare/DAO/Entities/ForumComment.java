package elderlycare.DAO.Entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ForumComment {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    int forumcommentid ;
    Date TimeStampcomment ;
    String content ;
    String Reaction ;
    @ManyToOne
    ForumPost forumPost;
    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL)
    List<ForumComment> descomments;

    @ManyToOne
    @JoinColumn(name = "parent_comment_id")
    ForumComment parentComment;
}
