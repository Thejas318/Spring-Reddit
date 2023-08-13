package com.personalProject.reddit.Repository;

import com.personalProject.reddit.model.Post;
import com.personalProject.reddit.model.User;
import com.personalProject.reddit.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);
}
