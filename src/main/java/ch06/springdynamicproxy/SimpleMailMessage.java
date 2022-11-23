package ch06.springdynamicproxy;

public interface SimpleMailMessage {
	void setTo(String to);
	void setFrom(String from);
	void setSubject(String subject);
	void setText(String text);

	String getTo();
}
