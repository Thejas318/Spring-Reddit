package com.personalProject.reddit.service;

import com.personalProject.reddit.exceptionHandler.SpringRedditException;
import com.personalProject.reddit.model.NotificationEmail;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class MailService {

    private final MailContentBuilder mailContentBuilder;

    private final JavaMailSender mailSender;

    @Async
    void sendMail(NotificationEmail notificationEmail){
        MimeMessagePreparator messagePreparator = (mimeMessage) -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("springreddit@email.com");
            messageHelper.setTo(notificationEmail.getRecipient());
            messageHelper.setSubject(notificationEmail.getSubject());
            messageHelper.setText(mailContentBuilder.build(notificationEmail.getBody()));
        };

        try{
            mailSender.send(messagePreparator);
            log.info("Activation email has been sent to the user: " + notificationEmail.getRecipient());
        }
        catch (MailException ex) {
            throw new SpringRedditException("Exception occured when sending mail to user: " + notificationEmail.getRecipient());
        }

    }

    @Async
    void sendMailForNewCommnets(NotificationEmail notificationEmail){
        MimeMessagePreparator messagePreparator = (mimeMessage) -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("springreddit@email.com");
            messageHelper.setTo(notificationEmail.getRecipient());
            messageHelper.setSubject(notificationEmail.getSubject());
            messageHelper.setText(notificationEmail.getBody());
        };

        try{
            mailSender.send(messagePreparator);
            log.info("Notification email has been sent to the owner of the post: " + notificationEmail.getRecipient());
        }
        catch (MailException ex) {
            throw new SpringRedditException("Exception occured when sending mail to author: " + notificationEmail.getRecipient());
        }

    }
}
