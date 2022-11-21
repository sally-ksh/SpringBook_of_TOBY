package ch06.dynamicproxy;

import org.springframework.beans.factory.FactoryBean;

public class MessageFactoryBean implements FactoryBean<Message> {
	private final String text;

	public MessageFactoryBean(String text) {
		this.text = text;
	}

	@Override
	public Message getObject() throws Exception {
		return Message.newMessage(this.text);
	}

	@Override
	public Class<?> getObjectType() {
		return Message.class;
	}

	@Override
	public boolean isSingleton() {
		return false;  // 요청마다 매번 새로운 오브젝트 생성 -> 만들어진 빈 오브젝트는 스프링이 싱글톤으로 관리
	}
}
