package com.service;

import com.domain.entity.RecycleRequestEntity;
import com.domain.entity.VoucherEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    private static final String SENDER = "recycleitapplication@gmail.com";

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public void sendConfirmationMail(RecycleRequestEntity requestEntity) {
        try {

            String template = loadTemplate("src/main/resources/requestConfirmedEmailTemplate.html");
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

    public void sendThanksMail(RecycleRequestEntity requestEntity) {
        try {

            String template = loadTemplate("src/main/resources/thanksEmailTemplate.html");
            if (template != null) {

                template = template.replace("{0}", requestEntity.getClient().getFirstname());
                template = template.replace("{1}", requestEntity.getQuantity().toString());
                template = template.replace("{2}", requestEntity.getType().toString());
                template = template.replace("{3}", requestEntity.getDate().format(DATE_FORMAT));

                MimeMessage mailMessage = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true);

                helper.setFrom(SENDER);
                helper.setTo(requestEntity.getClient().getEmail());
                helper.setText(template, true);
                helper.setSubject("Thanks from RecycleIT!");

                javaMailSender.send(mailMessage);
            } else {
                throw new Exception("Error while loading mail template!");
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public void sendUsedVoucherMail(VoucherEntity voucherEntity) {
        try {

            String template = loadTemplate("src/main/resources/usedVoucherEmailTemplate.html");
            if (template != null) {

                template = template.replace("{0}", voucherEntity.getClient().getFirstname());
                template = template.replace("{1}", voucherEntity.getCode());
                template = template.replace("{2}", voucherEntity.getVaildUntil().toLocalDate().toString());

                MimeMessage mailMessage = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true);

                helper.setFrom(SENDER);
                helper.setTo(voucherEntity.getClient().getEmail());
                helper.setText(template, true);
                helper.setSubject("Your voucher");

                javaMailSender.send(mailMessage);
            } else {
                throw new Exception("Error while loading mail template!");
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private String loadTemplate(String path) {
        String content;
        try {
            content = new Scanner(new File(path)).useDelimiter("\\Z").next();
            System.out.println(content);
            return content;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}