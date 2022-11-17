package ch05.transaction.email;

public interface MailSender {
	void send(SimpleMailMessage simpleMailMessage) throws MailException;
	void send(SimpleMailMessage[] simpleMailMessage) throws MailException;
}
