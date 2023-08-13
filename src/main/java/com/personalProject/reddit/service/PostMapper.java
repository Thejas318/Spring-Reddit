package com.personalProject.reddit.service;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.personalProject.reddit.Repository.CommentRepository;
import com.personalProject.reddit.Repository.VoteRepository;
import com.personalProject.reddit.dto.CommentsDto;
import com.personalProject.reddit.dto.PostRequest;
import com.personalProject.reddit.dto.PostResponse;
import com.personalProject.reddit.dto.VoteDto;
import com.personalProject.reddit.model.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.Instant;

@AllArgsConstructor
@Data
@Component
public class PostMapper {

    private CommentRepository commentRepository;

    private VoteRepository voteRepository;

    private AuthService authService;

    public Post postRequestToPostMapper(PostRequest postRequest, SubReddit subReddit, User user){

       return Post.builder()
                .createdDate(Instant.now())
                .subreddit(subReddit)
                .user(user)
                .postName(postRequest.getPostName())
                .description(postRequest.getDescription())
                .url(postRequest.getUrl())
                .voteCount(0)
                .build();
    }

    public PostResponse postToPostResponse(Post post){

        return PostResponse.builder()
                .id(post.getPostId())
                .postName(post.getPostName())
                .description(post.getDescription())
                .url(post.getUrl())
                .subredditName(post.getSubreddit().getName())
                .subredditId(post.getSubreddit().getId())
                .userName(post.getUser().getUserName())
                .commentCount(commentRepository.findByPost(post).get().size())
                .duration(TimeAgo.using(post.getCreatedDate().toEpochMilli()))
                .build();
    }

    public Comment commentDtoToComment(CommentsDto commentsDto, Post post, User user){

        return Comment.builder()
                .text(commentsDto.getText())
                .post(post)
                .user(user)
                .createdDate(Instant.now())
                .build();
    }

    public CommentsDto commentToCommentDto(Comment comment){

        return CommentsDto.builder()
                .id(comment.getId())
                .postId(comment.getPost().getPostId())
                .createDate(comment.getCreatedDate())
                .text(comment.getText())
                .userName(comment.getUser().getUserName())
                .build();
    }

    public Vote voteDtoToVote(VoteDto voteDto, Post post, User user){

        return Vote.builder()
                .voteType(voteDto.getVoteType())
                .post(post)
                .user(user)
                .build();
    }

}
