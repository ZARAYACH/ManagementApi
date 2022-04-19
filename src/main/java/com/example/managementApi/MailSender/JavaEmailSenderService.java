package com.example.managementApi.MailSender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class JavaEmailSenderService {

    @Autowired
    private JavaMailSender javaMailSender;

    public ResponseEntity<?> sendSimpleEmail(String to,
                                             String from,
                                             String subject,
                                             String text) {
        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(to);
            mail.setFrom(from);
            mail.setSubject(subject);
            mail.setText(text);
            javaMailSender.send(mail);
            System.out.println("email sent");
            return ResponseEntity.ok().body("email was sent by success");
        } catch (MailException e) {
            System.out.println(e.toString());
            e.getStackTrace();
            return ResponseEntity.badRequest().body("this email was not send");
        }
    }

    public ResponseEntity<?> SendHtmlEmail(String to,
                                           String from,
                                           String subject,
                                           String html) {
        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(html, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            javaMailSender.send(mimeMessage);
            return ResponseEntity.ok().body("email was sent by success");
        } catch (MailException e) {
            System.out.println(e.toString());
            e.getStackTrace();
            return ResponseEntity.badRequest().body("this email was not send");
        } catch (MessagingException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("this email was not send");
        }
    }

//    public ResponseEntity<?> sendEmailWithAttachments(String to,
//                                                      String from,
//                                                      String subject,
//                                                      String text,
//                                                      String attachment) {
//
//        MimeMessagePreparator preparator = new MimeMessagePreparator() {
//            @Override
//            public void prepare(MimeMessage mimeMessage) throws Exception {
//                mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
//                mimeMessage.setFrom(from);
//                mimeMessage.setSubject(subject);
//                mimeMessage.setText(text);
//                FileSystemResource file = new FileSystemResource(new File(attachment));
//                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
//                helper.addAttachment("Screen Shot 2022-01-11 at 9.18.17 PM.png", file);
//            }
//        };
//        try {
//                javaMailSender.send(preparator);
//                return ResponseEntity.ok().body("sent successfully");
//        }catch (MailException e){
//            e.getStackTrace();
//            return  ResponseEntity.badRequest().body("was not sent");
//        }
//    }


}
