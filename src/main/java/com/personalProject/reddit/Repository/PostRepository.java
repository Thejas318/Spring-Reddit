package com.personalProject.reddit.Repository;

import com.personalProject.reddit.model.Post;
import com.personalProject.reddit.model.SubReddit;
import com.personalProject.reddit.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<List<Post>> findByUser(User byUserName);

    List<Post> findAllBySubreddit(SubReddit subRedditById);
}
