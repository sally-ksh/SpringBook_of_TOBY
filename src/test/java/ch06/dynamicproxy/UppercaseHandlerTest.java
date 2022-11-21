package ch06.dynamicproxy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import static ch06.dynamicproxy.FactoryBeanTestConfig.TEXT_MESSAGE;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.lang.reflect.Proxy;

import ch06.proxyz.Hello;
import ch06.proxyz.HelloTarget;

class UppercaseHandlerTest {

	@Test
	void proxyHandler() {
		Hello proxiedHello = (Hello)Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] {Hello.class},
			new UppercaseHandler(new HelloTarget()));

		assertThat(proxiedHello.sayHello("sally")).isEqualTo("HELLO SALLY");
	}

	@Test
	void getMessageFormatFactoryBean() {
		ApplicationContext context = new AnnotationConfigApplicationContext(FactoryBeanTestConfig.class);
		Object message = context.getBean("message");

		assertThat(message).isInstanceOf(Message.class);
		assertThat(((Message)message).getText()).isEqualTo(TEXT_MESSAGE);
	}

	@Test
	void getFactoryBean() {
		ApplicationContext context = new AnnotationConfigApplicationContext(FactoryBeanTestConfig.class);
		Object factory = context.getBean("&message");
		assertThat(factory).isInstanceOf(MessageFactoryBean.class);
	}
}
