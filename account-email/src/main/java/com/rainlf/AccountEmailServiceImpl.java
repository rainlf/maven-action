package com.rainlf;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Slf4j
@Data
public class AccountEmailServiceImpl implements AccountEmailService {

    private JavaMailSender javaMailSender;

    private String systemEmail;

    @Override
    public void sendMail(String to, String subject, String htmlText) {
        try {
            MimeMessage msg = javaMailSender.createMimeMessage();
            MimeMessageHelper msgHelper = new MimeMessageHelper(msg);

            msgHelper.setFrom(systemEmail);
            msgHelper.setTo(to);
            msgHelper.setSubject(subject);
            msgHelper.setText(htmlText);

            log.info("Send email: {}", msg.getSubject());
//            javaMailSender.send(msg);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
