package com.personalProject.reddit.controller;

import com.personalProject.reddit.dto.SubRedditDto;
import com.personalProject.reddit.model.SubReddit;
import com.personalProject.reddit.service.SubRedditService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/subreddit")
@Slf4j
@AllArgsConstructor
public class SubRedditController {

    private final SubRedditService subRedditService;

    @PostMapping
    public ResponseEntity<SubRedditDto> createSubReddit(@RequestBody SubRedditDto subRedditDto){

        SubRedditDto subRedditFromService = subRedditService.save(subRedditDto);

        return  ResponseEntity.status(HttpStatus.CREATED)
                .body(subRedditFromService);
    }

    @GetMapping
    public ResponseEntity<List<SubRedditDto>> getAll(){
        List<SubRedditDto> allSubReddits = subRedditService.getAll();

        return ResponseEntity.status(HttpStatus.OK)
                .body(allSubReddits);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubRedditDto> getOneSubReddit(@PathVariable Long id){
        SubRedditDto subRedditById = subRedditService.getSubRedditById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(subRedditById);
    }


}
