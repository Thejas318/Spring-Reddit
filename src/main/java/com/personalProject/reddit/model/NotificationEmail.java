package com.personalProject.reddit.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class NotificationEmail {

    private String subject;
    private String recipient;
    private String body;
}
