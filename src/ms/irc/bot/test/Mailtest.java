package ms.irc.bot.test;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Mailtest {

	public static void main(String[] args) {

		String mailSmtpHost = "mail.mailinator.com";

		String mailTo = "schlegelaushohensonne@yahoo.de";
		String mailCc = "";
		String mailFrom = "komischerjavatest@ubukubu.com";
		String mailSubject = "Email from Java";
		String mailText = "This is an email from Java";

		sendEmail(mailTo, mailCc, mailFrom, mailSubject, mailText, mailSmtpHost);
	}

	public static void sendEmail(String to, String cc, String from, String subject, String text, String smtpHost) {
		try {
			Properties properties = new Properties();
			properties.put("mail.smtp.host", smtpHost);
//	        properties.put("mail.smtp.port", port);
//	        properties.put("mail.smtp.auth", "true");
//	        properties.put("mail.smtp.starttls.enable", "true");
//	        properties.put("mail.user", "schlegelaushohensonne@yahoo.de");
//	        properties.put("mail.password", "5950945611101992");
//	        Authenticator auth = new SMTPAuthenticator("schlegelaushohensonne@yahoo.de", "5950945611101992");
			Session emailSession = Session.getInstance(properties);

			Message emailMessage = new MimeMessage(emailSession);
			emailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
//			emailMessage.addRecipient(Message.RecipientType.CC, new InternetAddress(cc));
			emailMessage.setFrom(new InternetAddress(from));
			emailMessage.setSubject(subject);
			emailMessage.setText(text);

			emailSession.setDebug(true);

			Transport.send(emailMessage);
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}


final class SMTPAuthenticator extends javax.mail.Authenticator {
	private String userName;
	private String password;

	public SMTPAuthenticator(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}

	public PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(userName, password);
	}
}