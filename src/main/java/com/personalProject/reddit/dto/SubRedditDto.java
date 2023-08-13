package com.personalProject.reddit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SubRedditDto {
    private Long id;
    private String name;
    private String description;
    private Integer numberOfPosts;

}
