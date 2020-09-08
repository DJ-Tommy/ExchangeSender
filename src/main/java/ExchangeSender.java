import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

/*
javax.mail.AuthenticationFailedException: 535 5.7.3 Authentication unsuccessful

If you see this exception for the office365 (Exchange)
we must go to https://admin.microsoft.com/ and Turn this setting on:
Users -> Active users -> User -> Mail (Tab) -> Manage email apps -> Authenticated SMTP

And sometimes there can be another problem: enabled Azure security mechanism
For solve you should switch a parameter to No
Azure Active Directory -> Properties -> Manage Security defaults -> (NO)
*/

public class ExchangeSender {

    Properties properties = new Properties();
    String emailFromName = "email@company.onmicrosoft.com";
    String emailFromPassword = "password";
    String emailToName = "email@domain";

    public static void main(String[] args) {
        ExchangeSender exchangeSender = new ExchangeSender();
        try {
            exchangeSender.sendEmail();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initOffice365Properties() {
        properties.put("mail.smtp.host", "smtp.office365.com");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.debug", "true");
    }

    public void sendEmail() throws Exception {
        initOffice365Properties();

        Session session = Session.getDefaultInstance(properties,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(emailFromName, emailFromPassword);
                    }
                });

        Message message = new MimeMessage(session);
        message.setSentDate(new Date());
        message.setFrom(new InternetAddress(emailFromName));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(emailToName));
        message.setSubject("Email Title");
        message.setText("Message Text");
        Transport.send(message, message.getAllRecipients());
    }
}