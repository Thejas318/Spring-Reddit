package com.personalProject.reddit.controller;

import com.personalProject.reddit.dto.PostRequest;
import com.personalProject.reddit.dto.PostResponse;
import com.personalProject.reddit.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/posts")
@AllArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<Void> createPost(@RequestBody PostRequest postRequest){
        postService.save(postRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public  ResponseEntity<PostResponse> getPost(@PathVariable Long id){
        return new ResponseEntity(postService.getPostById(id), HttpStatus.OK);       //This is one way of return the status and body using Response Entity
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts(){
        return status(HttpStatus.OK).body(postService.getAllPosts());                //This is another way of returning the status and body using Response Entity
    }

    @GetMapping("/by-subReddit/{id}")
    public ResponseEntity<List<PostResponse>> getPostsBySubreddit(@PathVariable Long id){
        return status(HttpStatus.OK).body(postService.getPostsBySubreddit(id));
    }

    @GetMapping("/by-user")
    public ResponseEntity<List<PostResponse>> getPostsByUserName(@RequestParam String username){
        return status(HttpStatus.OK).body(postService.getPostsByUserName(username));
    }
}
