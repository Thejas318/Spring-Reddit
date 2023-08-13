package com.personalProject.reddit.Repository;

import com.personalProject.reddit.model.SubReddit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubRedditRepository extends JpaRepository<SubReddit, Long> {

    Optional<SubReddit> findByName(String subredditname);
}
