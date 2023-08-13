package com.personalProject.reddit.Repository;

import com.personalProject.reddit.model.Comment;
import com.personalProject.reddit.model.Post;
import com.personalProject.reddit.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<List<Comment>> findAllCommentsByPost(Post postById);

    Optional<List<Comment>> findAllCommentsByUser(User user);

    Optional<List<Comment>> findByPost(Post post);
}
