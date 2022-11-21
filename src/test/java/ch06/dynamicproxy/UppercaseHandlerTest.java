package ch06.dynamicproxy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

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

}
