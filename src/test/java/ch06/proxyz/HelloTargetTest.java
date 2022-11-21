package ch06.proxyz;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class HelloTargetTest {

	@Test
	void simpleProxy() {
		Hello target = new HelloTarget();
		String testName = "Sally";

		assertThat(target.sayHello(testName)).isEqualTo("Hello Sally");
		assertThat(target.sayHi(testName)).isEqualTo("Hi Sally");
		assertThat(target.sayThankYou(testName)).isEqualTo("Thank You Sally");
	}

	@Test
	void helloUpperProxy() {
		Hello target = new HelloUppercaseProxy(new HelloTarget());
		String testName = "Sally";

		assertThat(target.sayHello(testName)).isEqualTo("HELLO SALLY");
		assertThat(target.sayHi(testName)).isEqualTo("HI SALLY");
		assertThat(target.sayThankYou(testName)).isEqualTo("THANK YOU SALLY");
	}
}
