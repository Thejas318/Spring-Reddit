package com.personalProject.reddit.controller;

import com.personalProject.reddit.dto.CommentsDto;
import com.personalProject.reddit.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@AllArgsConstructor
public class CommentsController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Void> createComments(@RequestBody CommentsDto commentsDto){
        commentService.create(commentsDto);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/by-postId/{postId}")
    public ResponseEntity<List<CommentsDto>> getAllCommentsByPostID(@PathVariable Long postId){
        List<CommentsDto> allCommentsForPost = commentService.getCommentsForPost(postId);
        return ResponseEntity.status(HttpStatus.OK).body(allCommentsForPost);
    }

    @GetMapping("/by-userName/{userName}")
    public ResponseEntity<List<CommentsDto>> getCommentsByUsername(@PathVariable String userName){
        List<CommentsDto> allCommentsForUser = commentService.getAllCommentsForUser(userName);

        return ResponseEntity.status(HttpStatus.OK).body(allCommentsForUser);
    }
}
