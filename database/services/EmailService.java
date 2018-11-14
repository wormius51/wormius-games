package wormius.games.wormiusgames.database.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService implements IEmailService {

	@Autowired
	private JavaMailSender mailSender;
	
	@Async
	@Override
	public void sendMail(SimpleMailMessage email) {
		mailSender.send(email);
	}

}
