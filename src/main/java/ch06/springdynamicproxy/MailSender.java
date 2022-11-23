package ch06.springdynamicproxy;

public interface MailSender {
	void send(SimpleMailMessage simpleMailMessage) throws MailException;
	void send(SimpleMailMessage[] simpleMailMessage) throws MailException;
}
