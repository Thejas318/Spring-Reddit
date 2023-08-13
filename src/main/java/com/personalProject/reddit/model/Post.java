package com.personalProject.reddit.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.Instant;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long postId;
    @NotBlank(message = "Post Name cannot be empty")
    private String postName;
    @Nullable
    private String url;
    @Nullable
    @Lob
    private String description;
    private Integer voteCount;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="userId", referencedColumnName = "userId")
    private User user;
    private Instant createdDate;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "id", referencedColumnName = "id")
    private SubReddit subreddit;

}
