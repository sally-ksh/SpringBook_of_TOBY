package ch06.separateTransaction;

public class SimpleMailMessageImpl implements SimpleMailMessage {
	private String to;
	private String from;
	private String subject;
	private String text;
	@Override
	public void setTo(String to) {
		this.to = to;
	}

	@Override
	public void setFrom(String from) {
		this.from = from;
	}

	@Override
	public void setSubject(String subject) {
		this.subject = subject;
	}

	@Override
	public void setText(String text) {
		this.text = text;
	}

	public String getTo() {
		return to;
	}

	public String getFrom() {
		return from;
	}

	public String getSubject() {
		return subject;
	}

	public String getText() {
		return text;
	}
}
