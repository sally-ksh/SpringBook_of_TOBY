package ch03.templatecallback;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.net.URI;

public class CalcSumTest {
	private Calculator calculator;
	private URI filepath;

	@BeforeEach
	void setUp() throws IOException {
		this.calculator = new Calculator();
		ClassPathResource resource = new ClassPathResource("numbers.txt");
		this.filepath = resource.getURI();
	}

	@Test
	void sumOfNumber() throws IOException {
		int sum = calculator.calcSum(filepath);
		assertThat(sum).isEqualTo(10);
	}

	@Test
	void multiplyOfNumbers() throws IOException {
		int actual = calculator.calcMultiply(this.filepath);
		assertThat(actual).isEqualTo(24);
	}

	@Test
	void concatenateString() throws IOException {
		assertThat(calculator.concatenate(this.filepath)).isEqualTo("1234");
	}
}
