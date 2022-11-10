package ch03.templatecallback;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;

public class Calculator {
	public int calcSum(URI filepath) throws IOException {
		BufferedReader br = null;

		try {
			br = new BufferedReader(new FileReader(Paths.get(filepath).toFile()));
			int sum = 0;
			String line = null;
			while ((line = br.readLine()) != null) {
				sum += Integer.valueOf(line);
			}

			br.close();
			return sum;
		} catch (IOException exception) {
			System.out.println(exception.getMessage());
			throw exception;
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException exception) {
					System.out.println(exception.getMessage());
				}
			}
		}

	}
}
