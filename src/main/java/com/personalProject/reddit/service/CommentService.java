package com.personalProject.reddit.service;

import com.personalProject.reddit.Repository.CommentRepository;
import com.personalProject.reddit.Repository.PostRepository;
import com.personalProject.reddit.Repository.UserRepository;
import com.personalProject.reddit.dto.CommentsDto;
import com.personalProject.reddit.exceptionHandler.PostNotFoundException;
import com.personalProject.reddit.exceptionHandler.SpringRedditException;
import com.personalProject.reddit.exceptionHandler.SubRedditNotFoundException;
import com.personalProject.reddit.exceptionHandler.UserNotFoundException;
import com.personalProject.reddit.model.Comment;
import com.personalProject.reddit.model.NotificationEmail;
import com.personalProject.reddit.model.Post;
import com.personalProject.reddit.model.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
@AllArgsConstructor
@Transactional
public class CommentService {

    //ToDo: Construct a POST URL
    private static final String POST_URL = "";

    private final PostMapper postMapper;
    private final PostRepository postRepository;
    private final AuthService authService;
    private final CommentRepository commentRepository;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;
    private final UserRepository userRepository;

    public Comment create(CommentsDto commentsDto) {

        Post post = postRepository.findById(commentsDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException("post with POstId: " + commentsDto.getPostId().toString() + " is not found in DB, Invalid PostID"));

        User currentUser = authService.getCurrentUser();

        Comment comment = postMapper.commentDtoToComment(commentsDto, post, currentUser);

        Comment savedComment = commentRepository.save(comment);

        String commentMailMessage = mailContentBuilder.build(post.getUser().getUserName()
                + "posted a comment on your post" + POST_URL);

        sendNotificationMessage(commentMailMessage, post.getUser());

        return savedComment;
    }


    public void sendNotificationMessage(String commentMailMessage, User user){
        NotificationEmail notificationEmail = new NotificationEmail();
        notificationEmail.setSubject(user.getUserName() + " commented on your post");
        notificationEmail.setBody(commentMailMessage);
        notificationEmail.setRecipient(user.getEmail());

        mailService.sendMailForNewCommnets(notificationEmail);
    }

    public List<CommentsDto> getCommentsForPost(Long postId) {

        Post postById = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post with postId: " + postId.toString() + "is not found, Invalid postId"));

        List<Comment> comments = commentRepository.findAllCommentsByPost(postById)
                .orElseThrow(() -> new SubRedditNotFoundException("Comments not found for the post"));

        List<CommentsDto> commentsDto = comments.stream()
                .map(comment -> postMapper.commentToCommentDto(comment))
                .collect(toList());

        return commentsDto;
    }

    public List<CommentsDto> getAllCommentsForUser(String userName) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UserNotFoundException("User with name: " + userName + " is not found. Invalid userName"));

        List<Comment> allCommentsByUser = commentRepository.findAllCommentsByUser(user)
                .orElseThrow(() -> new SpringRedditException("comments not found for the user: " + userName));

        List<CommentsDto> commentsDto = allCommentsByUser.stream()
                .map(comment -> postMapper.commentToCommentDto(comment))
                .collect(toList());

        return commentsDto;
    }
}
