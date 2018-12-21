package com.perspecta.hybrid.mail;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Mail {

	Properties emailProperties;
	Session mailSession;
	MimeMessage emailMessage;
	String emailPort;
	String emailHost;
	
	
	public void setMailServerProperties() {

		emailPort = "25"; //"587"or "25";//gmail's smtp port
		emailHost =  "smtp.gmail.com";   //"smtp-mail.outlook.com";      //"smtp.gmail.com";
		emailProperties = System.getProperties();
		emailProperties.put("mail.smtp.port", emailPort);
		emailProperties.put("mail.smtp.auth", "true");
		emailProperties.put("mail.smtp.starttls.enable", "true");
    
	}

	public void createEmailMessage(
			String emailSubject,
			String emailBody,
			String attachmentPath,
			String attachmentName,
			String toEmails[]
			) throws AddressException,
			MessagingException {
		
		//String emailSubject = "Java Email";
		//String emailBody = "Please find the reports in attachment.";

		mailSession = Session.getDefaultInstance(emailProperties, null);
		emailMessage = new MimeMessage(mailSession);

		for (int i = 0; i < toEmails.length; i++) {
			emailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmails[i]));
		}

		emailMessage.setSubject(emailSubject);
		DataSource source = new FileDataSource(attachmentPath);
		emailMessage.setDataHandler(new DataHandler(source));
		emailMessage.setFileName(attachmentName);
  //      emailMessage.setText(emailBody);// for a text email

	}

	public void sendEmail(String fromUser,String fromUserEmailPassword) throws AddressException, MessagingException {

		 
		

		Transport transport = mailSession.getTransport("smtp");

		transport.connect(emailHost, fromUser, fromUserEmailPassword);
		transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
		transport.close();
		System.out.println("Email sent successfully.");
	}

}