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
			
			System.out.println(meetup.getDateAndTime());
			
			String emailBody = "<div style=\"background-color: lightgray; color: black; width:40rem; margin: 0 auto; height: 30vh;\">\r\n" + 
				          "<h5 style=\"font-size: 1.83em; text-align: center; padding-top: 5px; margin-bottom: 2px; \">Meetup System</h5>\r\n" + 
					      "<p style=\"font-family: 'Times New Roman', Times, serif; font-size: 1.2em;\">Hello, "+user.getFullname()+"</p>\r\n" + 
					      "<p style=\"font-size: 1.2em; font-family: 'Times New Roman', Times, serif;\">This is to let you know that there is a new meetup scheduled on : <strong>"+meetup.getDateAndTime()+"</strong>\r\n" + 
					      "<br />in your group <strong>"+groupName+"</strong>.<br/> Please click the button below to confirm your attendance.\r\n" + 
					      "</p>\r\n" + 
					      "<a href=\"http://localhost:3000/login\" style=\"text-decoration: none; font-size: 1.2em; background-color: darkgreen; color: white; margin: 5px; padding: 1.5% 2%;\">Confirm</a>\r\n" + 
					      "</div>";
			
			
//			String emailBody =""
//					+ "<div style='width: 20rem; background-color: light-gray'>"
//					+ "<h5 style='font-size: 1.83em; margin-top: -9px;'>Meetup System</h5>"
//					+ "<h3 style='font-size: 20px;'>Hello, "+user.getFullname()+".</h3>"
//			        + "<p>This is to let you know that "
//			        + "there is a new meetup scheduled on : <strong>"+meetup.getDateAndTime()+"</strong>"
//			        + " <p>in your group <strong>"+groupName+"</strong> </p>"
//			        + "<p>Please <a href=\"http://localhost:3000/login\" style=\"text-decoration: none; font-size: 20; color: blue \">click here</a>"
//			        + " to let us know if your are attending</p>"
//			        + "</div>";
					
			helper.setTo(user.getEmail());
			helper.setSubject("New Meetup Scheduled");
			helper.setText(emailBody, true);
			
			mailSender.send(message);
			
			return "email sent successfully";
			
		} catch (Exception e) {
			throw new RuntimeException("Could not send email : "+e.getMessage());
		}
		
	}
	
	
//public String sendEmailToMany(String emailBody, String subject, String[] ccEmail) throws Exception {
//		
//		try {
//			SimpleMailMessage message = new SimpleMailMessage();
//			int i = 0;
//			for(i=0; i<ccEmail.length; i++) {
//				
//				message.setTo(ccEmail[i]);
//				message.setSubject(subject);
//				message.setText(emailBody);
//				mailSender.send(message);
//			}
//			
//			
//			return "email sent successfully";
//			
//		} catch (Exception e) {
//			throw new RuntimeException("Could not send email : "+e.getMessage());
//		}
//		
//	}
}
