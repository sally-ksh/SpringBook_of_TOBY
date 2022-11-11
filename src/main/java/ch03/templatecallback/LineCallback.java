package ch03.templatecallback;

public interface LineCallback<T> {
	T doSomethingWithLines(String line, T value);
}
