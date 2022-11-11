package ch03.templatecallback;

import java.io.BufferedReader;
import java.io.IOException;

public interface BufferedReaderCallback {
	int doSomethingWitReader(BufferedReader bufferedReader) throws IOException;
}
