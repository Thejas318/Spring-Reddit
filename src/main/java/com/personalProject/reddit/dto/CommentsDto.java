package com.personalProject.reddit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CommentsDto {

    private Long id;
    private Long postId;
    private Instant createDate;
    private String text;
    private String userName;

}
