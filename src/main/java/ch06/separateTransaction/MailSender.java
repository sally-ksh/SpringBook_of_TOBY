package ch06.separateTransaction;

public interface MailSender {
	void send(SimpleMailMessage simpleMailMessage) throws MailException;
	void send(SimpleMailMessage[] simpleMailMessage) throws MailException;
}
