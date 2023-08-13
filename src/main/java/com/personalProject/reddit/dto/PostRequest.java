package com.personalProject.reddit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PostRequest {

    private Long postId;
    private String subredditName;
    private String postName;
    private String url;
    private String description;

}
