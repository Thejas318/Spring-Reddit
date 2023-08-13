package com.personalProject.reddit.service;

import com.personalProject.reddit.Repository.SubRedditRepository;
import com.personalProject.reddit.dto.SubRedditDto;
import com.personalProject.reddit.exceptionHandler.SpringRedditException;
import com.personalProject.reddit.model.SubReddit;
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
public class SubRedditService {

    private final SubRedditRepository subRedditRepository;

    @Transactional
    public SubRedditDto save(SubRedditDto subRedditDto){
        SubReddit subReddit = mapToSubReddit(subRedditDto);         //building the SubReddit object to save in db
        SubReddit save = subRedditRepository.save(subReddit);        // saving in db and storing the Subreddit for local use.
        subRedditDto.setId(save.getId());

        return subRedditDto;
    }

    private SubReddit mapToSubReddit(SubRedditDto subRedditDto) {

       return SubReddit.builder()
                .name(subRedditDto.getName())
                .description(subRedditDto.getDescription())
                .build();
    }
    @Transactional(readOnly = true)
    public List<SubRedditDto> getAll() {
        List<SubRedditDto> subRedditsInDb = subRedditRepository.findAll()
                .stream().map(subReddit -> mapToDto(subReddit))
                .collect(Collectors.toList());

        return subRedditsInDb;
    }

    private SubRedditDto mapToDto(SubReddit subReddit) {
      return  SubRedditDto.builder()
                .name(subReddit.getName())
                .id(subReddit.getId())
                .description(subReddit.getDescription())
                .numberOfPosts(subReddit.getPosts().size())
                .build();
    }

    public SubRedditDto getSubRedditById(Long id) {
        try {
            Optional<SubReddit> subRedditById =  subRedditRepository.findById(id);
            SubRedditDto subRedditDto = mapToDto(subRedditById.get());

            return subRedditDto;
        }
        catch(Exception ex){
            log.error("Invalid ID - {} ", id);
            throw new SpringRedditException("Cannot find subreddit with ID: Invalid ID");
        }
    }
}
