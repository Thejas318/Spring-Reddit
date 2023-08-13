package com.personalProject.reddit.service;

import com.personalProject.reddit.Repository.PostRepository;
import com.personalProject.reddit.Repository.SubRedditRepository;
import com.personalProject.reddit.Repository.UserRepository;
import com.personalProject.reddit.dto.PostRequest;
import com.personalProject.reddit.dto.PostResponse;
import com.personalProject.reddit.exceptionHandler.PostNotFoundException;
import com.personalProject.reddit.exceptionHandler.SubRedditNotFoundException;
import com.personalProject.reddit.exceptionHandler.UserNotFoundException;
import com.personalProject.reddit.model.Post;
import com.personalProject.reddit.model.SubReddit;
import com.personalProject.reddit.model.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final SubRedditRepository subRedditRepository;
    private final AuthService authService;
    private UserRepository userRepository;
    private final PostMapper postMapper;

    public Post save(PostRequest postRequest) {

        SubReddit subReddit = subRedditRepository.findByName(postRequest.getSubredditName())
                .orElseThrow(() -> new SubRedditNotFoundException(postRequest.getSubredditName()));

        User currentUser = authService.getCurrentUser();

        Post post = postMapper.postRequestToPostMapper(postRequest, subReddit, currentUser);

        Post postSaved = postRepository.save(post);

        return postSaved;

    }

    @Transactional(readOnly = true)
    public PostResponse getPostById(Long id) {

        Post postById = postRepository.findById(id)
                .orElseThrow( () -> new PostNotFoundException(id.toString()));

        PostResponse postResponse = postMapper.postToPostResponse(postById);
        return postResponse;
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts() {

        List<PostResponse> postResponses = postRepository.findAll()
                .stream()
                .map((post) -> postMapper.postToPostResponse(post))
                .collect(Collectors.toList());
        return postResponses;
    }
    @Transactional(readOnly = true)
    public List<PostResponse> getPostsBySubreddit(Long id) {

        SubReddit subRedditById = subRedditRepository.findById(id)
                .orElseThrow(() -> new SubRedditNotFoundException(id.toString()));

        List<Post> postsBySubreddit = postRepository.findAllBySubreddit(subRedditById);

        List<PostResponse> postResponses =  postsBySubreddit.stream()
                .map(post -> postMapper.postToPostResponse(post))
                .collect(Collectors.toList());

        return postResponses;
    }
    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByUserName(String username) {
        User userByUserName = userRepository.findByUserName(username)
                .orElseThrow(() -> new UserNotFoundException(" User is not found in database: Invalid username"));

       List<Post>  postsByUser = postRepository.findByUser(userByUserName)
                .orElseThrow(() -> new PostNotFoundException("The user with username " + username + " does not have any Posts"));

        List<PostResponse> postResponses = postsByUser.stream()
                .map(post -> postMapper.postToPostResponse(post))
                .collect(Collectors.toList());

        return postResponses;
    }
}
