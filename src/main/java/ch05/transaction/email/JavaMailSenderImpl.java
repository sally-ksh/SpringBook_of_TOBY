package ch05.transaction.email;

public class JavaMailSenderImpl implements MailSender {
	@Override
	public void send(SimpleMailMessage simpleMailMessage) throws MailException {
		System.out.println("send email");
	}

	@Override
	public void send(SimpleMailMessage[] simpleMailMessage) throws MailException {
		System.out.println("send emails");
	}
}
