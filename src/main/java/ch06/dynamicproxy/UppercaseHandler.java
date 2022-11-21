package ch06.dynamicproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import ch06.proxyz.Hello;

public class UppercaseHandler implements InvocationHandler {
	private final Object target;

	public UppercaseHandler(Object target) {
		this.target = target;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws
		InvocationTargetException,
		IllegalAccessException {
		String result = (String)method.invoke(target, args);
		if (result instanceof String && method.getName().startsWith("say")) {
			return ((String)result).toUpperCase();
		}
		return result;
	}
}
