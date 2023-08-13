package com.personalProject.reddit.service;

import com.personalProject.reddit.Repository.PostRepository;
import com.personalProject.reddit.Repository.VoteRepository;
import com.personalProject.reddit.dto.VoteDto;
import com.personalProject.reddit.exceptionHandler.PostNotFoundException;
import com.personalProject.reddit.exceptionHandler.SpringRedditException;
import com.personalProject.reddit.model.Post;
import com.personalProject.reddit.model.User;
import com.personalProject.reddit.model.Vote;
import com.personalProject.reddit.model.VoteType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private AuthService authService;

    private PostMapper postMapper;

    @Transactional
    public void vote(VoteDto voteDto) {

        User currentUser = authService.getCurrentUser();

        Post post = postRepository.findById(voteDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Post with postId: " + voteDto.getPostId().toString() + " not found, Invalid Post ID"));
        Optional<Vote> voteByPostAndUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, currentUser);

        if( voteByPostAndUser.isPresent() &&
                    voteByPostAndUser.get().getVoteType()
                            .equals(voteDto.getVoteType())){
            throw new SpringRedditException("You have already " + voteDto.getVoteType().toString() + "'d for this post");
        }

        if(VoteType.UPVOTE.equals(voteDto.getVoteType())){
            post.setVoteCount(post.getVoteCount() + 1);
        } else {
            post.setVoteCount(post.getVoteCount() - 1);
        }

        Vote vote = postMapper.voteDtoToVote(voteDto, post, currentUser);
        voteRepository.save(vote);


    }
}
