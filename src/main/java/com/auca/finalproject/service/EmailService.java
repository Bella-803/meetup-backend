package com.auca.finalproject.service;



import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.auca.finalproject.entity.Meetup;
import com.auca.finalproject.entity.UserAccount;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;
	public String sendEmail(String emailBody, String subject, String to) throws Exception {
		
		try {
			//SimpleMailMessage message = new SimpleMailMessage();
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message);
					
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(emailBody, true);
			
			mailSender.send(message);
			
			return "email sent successfully";
			
		} catch (Exception e) {
			throw new RuntimeException("Could not send email : "+e.getMessage());
		}
		
	}
	
	public String sendMeetupScheduledEmail(UserAccount user, Meetup meetup, String groupName) throws Exception {
		
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message);
			String link = "http://localhost:8080/api/categories/image/1_categoryPic.jpg";
			String img = "<img src=\""+link+"\" width=\"100\" height=\"100\" />";
			
			String emailBody = img + "<h3>Hello, "+user.getFullname()+".</h3>"
			        + "<p>This is to let you know that "
			        + "there is a new meetup scheduled on : <strong>"+meetup.getDateAndTime()+"</strong>"
			        + " <p>in your group <strong>"+groupName+"</strong> </p>"
			        + "<p>Please <a href=\"http://localhost:3000/login\">click here</a>"
			        + " to let us know if your are attending</p>";
					
			helper.setTo(user.getEmail());
			helper.setSubject("New Meetup Scheduled");
			helper.setText(emailBody, true);
			
			mailSender.send(message);
			
			return "email sent successfully";
			
		} catch (Exception e) {
			throw new RuntimeException("Could not send email : "+e.getMessage());
		}
		
	}
	
	
public String sendEmailToMany(String emailBody, String subject, String[] ccEmail) throws Exception {
		
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			int i = 0;
			for(i=0; i<ccEmail.length; i++) {
				
				message.setTo(ccEmail[i]);
				message.setSubject(subject);
				message.setText(emailBody);
				mailSender.send(message);
			}
			
			
			return "email sent successfully";
			
		} catch (Exception e) {
			throw new RuntimeException("Could not send email : "+e.getMessage());
		}
		
	}
}
