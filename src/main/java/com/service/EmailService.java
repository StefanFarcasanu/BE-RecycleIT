package com.service;

import com.domain.entity.RequestEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    private static final String SENDER = "recycleitapplication@gmail.com";

    public void sendSimpleMail(RequestEntity requestEntity) {
        try {

            String template = loadTemplate();
            if (template != null) {

                template = template.replace("{0}", requestEntity.getStatus().toString());
                template = template.replace("{1}", requestEntity.getClient().getFirstname());
                template = template.replace("{2}", requestEntity.getQuantity().toString());
                template = template.replace("{3}", requestEntity.getType().toString());

                MimeMessage mailMessage = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true);

                helper.setFrom(SENDER);
                helper.setTo(requestEntity.getClient().getEmail());
                helper.setText(template, true);
                helper.setSubject("Recycling request " + requestEntity.getStatus().toString());

                javaMailSender.send(mailMessage);
            } else {
                throw new Exception("Error while loading mail template!");
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private String loadTemplate() {
        String content;
        try {
            content = new Scanner(new File("src/main/resources/requestConfirmedEmailTemplate.html")).useDelimiter("\\Z").next();
            System.out.println(content);
            return content;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}