package ch06.proxyz;

public class HelloTarget implements Hello{
	@Override
	public String sayHello(String name) {
		return String.format("Hello %s", name);
	}

	@Override
	public String sayHi(String name) {
		return String.format("Hi %s", name);
	}

	@Override
	public String sayThankYou(String name) {
		return String.format("Thank You %s", name);
	}
}
