package ch03.templatecallback;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.net.URI;

public class CalcSumTest {
	@Test
	void sumOfNumber() throws IOException {
		Calculator calculator = new Calculator();
		ClassPathResource resource = new ClassPathResource("numbers.txt");
		int sum = calculator.calcSum(resource.getURI());

		assertThat(sum).isEqualTo(10);
	}
}
