package ConfHandler.Admin;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


import java.util.Map;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;


    public void sendEmailWithTemplate(String toEmail, String subject, Map<String, Object> templateModel) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        Context context = new Context();
        context.setVariables(templateModel);
        String htmlBody = templateEngine.process("emailTemplate", context);

        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
        helper.setFrom("dsa2024@info.p.lodz.pl");

        mailSender.send(message);
    }
}
