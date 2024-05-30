package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.SubscriptionDetails;
import com.mycompany.myapp.repository.SubscriptionDetailsRepository;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

import java.util.List;
import java.util.Properties;

@Service
public class NotificationService {

    private final JavaMailSender emailService;
    private final Velocitytemp templateGenerator;

    @Autowired
    SubscriptionDetailsRepository subscriptionDetailsRepository;

    public NotificationService(JavaMailSender emailService, Velocitytemp templateGenerator, SubscriptionDetailsRepository subscriptionDetailsRepository) {
        this.emailService = emailService;
        this.templateGenerator = templateGenerator;
        this.subscriptionDetailsRepository = subscriptionDetailsRepository;
    }

    public List<SubscriptionDetails> getExpiringSubscriptions() {
        LocalDate currentDate = LocalDate.now();
        return subscriptionDetailsRepository.findExpiringSubscriptionsWithDays();
    }

//    @Scheduled(cron = "0 0 10 * * ?") // Executes at 10 PM every day
   /* @Scheduled(cron = "0 1 0 * * ?") // Executes at 10 PM every day
    public void sendScheduledEmail() throws Exception {
        String from = "prathamesh.kadam@redberyltech.com";
        String to = "kadam2992000@gmail.com";
        String cc = "cc@example.com";
        String bcc = "bcc@example.com";
        String subject = "Test Email";

        // Call generateTemplate method to get the email template
        String message = templateGenerator.generateTemplate();

        // Send email using the generated template
        sendEmail(from, to, cc, bcc, subject, message);
    }*/

    @Scheduled(cron = "0 32 15 * * * ") // Executes at 10 PM every day
    public void sendScheduledEmail() throws Exception {
        List<SubscriptionDetails> subscriptionDetailsList = getExpiringSubscriptions();
        for(SubscriptionDetails subscriptionDetails:subscriptionDetailsList) {
            String from = "hr@redberyltech.com";
            String to = subscriptionDetails.getNotificationTo();
            String cc = subscriptionDetails.getNotificationCc();
            String bcc = subscriptionDetails.getNotificationBcc();
            String subject = "Test Email";

            // Call generateTemplate method to get the email template
            String message = templateGenerator.generateTemplate(subscriptionDetails);

            // Send email using the generated template
            sendEmail(from, to, cc, bcc, subject, message);
        }
    }

    public boolean sendEmail(String from,String to, String cc, String bcc, String subject, String message) {
        {
            boolean f = false;
            Properties properties = new Properties();
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.port", "465");
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.auth", "true");
            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    String Email = "prathamesh.kadam@redberyltech.com";
                    String secretKey = "ifbf qqrb jqkb mvph";
                    // or fetch it from globalEmail.getEmail() if such a method exist
                    return new PasswordAuthentication(Email, secretKey);
                }
            });
            session.setDebug(true);
            //compose msg[text,media]
            MimeMessage mimeMessage = new MimeMessage(session);


            try {
                mimeMessage.setFrom("prathamesh.kadam@redberyltech.com");
                String[] toAddresses = to.split(",");
                for (String address : toAddresses) {
                   // mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
                    mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(address.trim()));
                }
                if (cc != null && !cc.isEmpty()) {
                    mimeMessage.addRecipient(Message.RecipientType.CC, new InternetAddress(cc));
                }
                // Adding BCC recipients if provided
                if (bcc != null && !bcc.isEmpty()) {
                    mimeMessage.addRecipient(Message.RecipientType.BCC, new InternetAddress(bcc));
                }

                mimeMessage.setSubject(subject);
                // mimeMessage.setText(message);
                //send html and text
                mimeMessage.setContent(message, "text/html");
                Transport.send(mimeMessage);
                f = true;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return f;
        }
    }
}











