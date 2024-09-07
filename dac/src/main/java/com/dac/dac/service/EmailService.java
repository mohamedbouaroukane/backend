package com.dac.dac.service;

import com.dac.dac.utils.CustomDate;
import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.util.ByteArrayDataSource;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;

@Service
public class EmailService {

    Logger log = LoggerFactory.getLogger(EmailService.class);
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String emailSender;

    private void sendSimpleEmail(String to, String subject, String text){
        try {
            MimeMessagePreparator preparator = mimeMessage -> {
                mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
                mimeMessage.setFrom(new InternetAddress(emailSender));
                mimeMessage.setSubject(subject);
                try{
                    MimeMessageHelper helper= new MimeMessageHelper(mimeMessage,true);
                    helper.setText(text,true);

                } catch (Exception ex){
                    ex.printStackTrace();
                }

            };
            javaMailSender.send(preparator);


        }catch (MailException exception){
            exception.printStackTrace();
        }
    }


    private void sendAttachmentsEmail(String to, byte[] content,String body)  {
        log.info(to);
        try {

            MimeMessagePreparator preparator = mimeMessage -> {
                mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
                mimeMessage.setFrom(new InternetAddress(emailSender));
                mimeMessage.setSubject("Swift Box Parcel Label");
                try{

                    ByteArrayDataSource attachment = new ByteArrayDataSource(IOUtils.toByteArray(new ByteArrayInputStream(content)), "application/pdf");
                        MimeMessageHelper helper= new MimeMessageHelper(mimeMessage,true);
                        helper.addAttachment("Parcel Label"+ CustomDate.getCurrentDate(), attachment);
                        helper.setText(body,true);

                } catch (Exception e){
                    e.printStackTrace();
                }

            };
            javaMailSender.send(preparator);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void sendParcelLabelAttachmentsEmail(String to, String client, byte[] content){
        sendAttachmentsEmail(to,content,generateParcelLabelBodyMail(client));
    }
    public void sendManifestToCourier(String to,byte[] content,String body){
        sendAttachmentsEmail(to,content,"");

    }



    public void sendMailConfirmation(String to,String client, String link){
            sendSimpleEmail(to,"Confirm your email",generateMailConfirmationBody(client,link));
    }
    private String generateParcelLabelBodyMail(String client){

            return "<html>" +
                    "<body>" +
                    "<p>Dear " + client + ",</p>" +
                    "<p>We hope this email finds you well.</p>" +
                    "<p>Your parcel label is attached to this email. Please follow the instructions below to ensure a smooth delivery process:</p>" +
                    "<ol>" +
                    "<li><b>Download the Attached Parcel Label:</b> Open the attachment in this email and download the parcel label.</li>" +
                    "<li><b>Print the Parcel Label:</b> Print the downloaded label on a standard A4 sheet of paper.</li>" +
                    "<li><b>Attach the Label:</b> Carefully attach the printed label to your parcel, ensuring it is securely fastened and clearly visible.</li>" +
                    "<li><b>Scan the QR Code:</b> At the bottom of the label, you will find a QR code. Scan this code  by using the parcel locker. This step is essential for depositing your parcel into the locker.</li>" +
                    "</ol>" +
                    "<p>Thank you for choosing our service. If you have any questions or need further assistance, please do not hesitate to contact our support team.</p>" +
                    "<p>Best regards,</p>" +
                    "<p>Swift Box<br>" +
                    "contact@swiftbox.dz<br>" +
                    "+21333665512</p>" +
                    "</body>" +
                    "</html>";
        }
    private String generateMailConfirmationBody(String client,String link){
            return
                    "<html>" +
                    "<body>" +
                    "<div>" +
                    "<h1>Email Confirmation</h1>" +
                    "<p>Hi "+client +"</p>" +
                    "<p>Please click the button below within 15 minutes to confirm your email address:</p>" +
                    "<a href=\""+link+"\">Confirm Email</a>" +
                    "<p>If you didn't request this, you can safely ignore this email.</p>" +
                    "</div>" +
                    "</body>" +
                    "</html>";

    }
}
