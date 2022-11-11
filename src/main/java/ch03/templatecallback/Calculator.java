package ch03.templatecallback;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;

public class Calculator {
	public int calcSum(URI filepath) throws IOException {
		// BufferedReaderCallback sumCallback = new BufferedReaderCallback() {
		// 	@Override
		// 	public int doSomethingWitReader(BufferedReader bufferedReader) throws IOException {
		// 		int sum = 0;
		// 		String line = null;
		// 		while ((line = bufferedReader.readLine()) != null) {
		// 			sum += Integer.valueOf(line);
		// 		}
		// 		return sum;
		// 	}
		// };
		BufferedReaderCallback sumCallback = (BufferedReader bufferedReader) -> {
			int sum = 0;
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				sum += Integer.valueOf(line);
			}
			return sum;
		};
		return fileReadTemplate(filepath, sumCallback);
	}

	public int calcMultiply(URI filepath) throws IOException {
		BufferedReaderCallback sumCallback = (BufferedReader bufferedReader) -> {
			int result = 1;
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				result *= Integer.valueOf(line);
			}
			return result;
		};
		return fileReadTemplate(filepath, sumCallback);
	}

	public int fileReadTemplate(URI filepath, BufferedReaderCallback callback) throws IOException {
		BufferedReader br = null;

		try {
			br = new BufferedReader(new FileReader(Paths.get(filepath).toFile()));
			int result = callback.doSomethingWitReader(br);
			return result;
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
