package wormius.games.wormiusgames.database.services;

import org.springframework.mail.SimpleMailMessage;

public interface IEmailService {
	public void sendMail(SimpleMailMessage email);
}
