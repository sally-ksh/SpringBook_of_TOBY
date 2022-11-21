package ch06.dynamicproxy;

public class Message {
	private final String text;

	public Message(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public static Message newMessage(String text) {
		return new Message(text);
	}
}
