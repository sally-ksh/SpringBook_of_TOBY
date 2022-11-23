package ch06.proxyz;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

import java.lang.reflect.Proxy;

import ch06.dynamicproxy.UppercaseHandler;

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

	@Test
	void helloUpperProxy_proxyFactoryBean() {
		Hello target = (Hello)Proxy.newProxyInstance(
			getClass().getClassLoader(),
			new Class[] {Hello.class},
			new UppercaseHandler(new HelloTarget())
		);
		String testName = "Sally";

		assertThat(target.sayHello(testName)).isEqualTo("HELLO SALLY");
		assertThat(target.sayHi(testName)).isEqualTo("HI SALLY");
		assertThat(target.sayThankYou(testName)).isEqualTo("THANK YOU SALLY");
	}

	@Test
	void proxyFactoryBean() {
		ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
		proxyFactoryBean.setTarget(new HelloTarget());
		proxyFactoryBean.addAdvice(new UppercaseAdvice());

		Hello proxiedHello = (Hello)proxyFactoryBean.getObject();
		String testName = "Sally";

		assertThat(proxiedHello.sayHello(testName)).isEqualTo("HELLO SALLY");
		assertThat(proxiedHello.sayHi(testName)).isEqualTo("HI SALLY");
		assertThat(proxiedHello.sayThankYou(testName)).isEqualTo("THANK YOU SALLY");
	}

	static class UppercaseAdvice implements MethodInterceptor {
		@Override
		public Object invoke(MethodInvocation invocation) throws Throwable {
			String result = (String)invocation.proceed();
			return result.toUpperCase();
		}
	}

	@Test
	void pointcutAdvisor() {
		ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
		proxyFactoryBean.setTarget(new HelloTarget());

		NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
		pointcut.setMappedName("sayH*"); // sayThankYou() 메소드는 제외

		proxyFactoryBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));

		Hello proxiedHello = (Hello)proxyFactoryBean.getObject();

		String testName = "Sally";

		assertThat(proxiedHello.sayHello(testName)).isEqualTo("HELLO SALLY");
		assertThat(proxiedHello.sayHi(testName)).isEqualTo("HI SALLY");
		assertThat(proxiedHello.sayThankYou(testName)).isEqualTo("Thank You Sally");
	}
}
